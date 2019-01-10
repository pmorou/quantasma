package quantasma.integrations.data.provider.oanda;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;
import com.oanda.v20.primitives.DateTime;
import lombok.extern.slf4j.Slf4j;
import quantasma.core.Quote;
import quantasma.integrations.data.provider.LiveDataProvider;
import quantasma.integrations.event.Event;
import quantasma.integrations.event.EventPublisher;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class OandaLiveDataProvider implements LiveDataProvider {

    private final String url;
    private final String token;
    private final String accountId;
    private final EventPublisher eventPublisher;

    private boolean isRunning = false;
    private CompletableFuture<Void> fetchingData;

    public OandaLiveDataProvider(String url, String token, String accountId, EventPublisher eventPublisher) {
        this.url = url;
        this.token = token;
        this.accountId = accountId;
        this.eventPublisher = eventPublisher;
    }

    private void fetchData() {
        final Context ctx = new ContextBuilder(url)
                .setToken(token)
                .setApplication("PricePolling")
                .build();

        final List<String> instruments = new ArrayList<>(
                Arrays.asList("EUR_USD")
        );

        try {
            final PricingGetRequest request = new PricingGetRequest(new AccountID(accountId), instruments);

            DateTime since = null;

            while (true) {
                if (since != null) {
                    log.info("Oanda API: Polling data since " + since);
                    request.setSince(since);
                }

                final PricingGetResponse resp = ctx.pricing.get(request);

                for (ClientPrice price : resp.getPrices()) {
                    log.info("Oanda API: {}", price);

                    eventPublisher.publish(Event.quote(
                            Quote.bidAsk(
                                    price.getInstrument().toString().replace("_", ""),
                                    ZonedDateTime.parse(price.getTime()),
                                    price.getBids().get(0).getPrice().doubleValue(),
                                    price.getAsks().get(0).getPrice().doubleValue()))
                    );
                }
                since = resp.getTime();

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("Oanda API: Exception - {}", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        fetchingData = CompletableFuture.runAsync(this::fetchData);
        isRunning = true;
    }

    @Override
    public void stop() {
        isRunning = !fetchingData.cancel(true);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

}
