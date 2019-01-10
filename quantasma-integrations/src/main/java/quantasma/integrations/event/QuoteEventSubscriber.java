package quantasma.integrations.event;

import lombok.extern.slf4j.Slf4j;
import quantasma.core.Quote;
import quantasma.core.TradeEngine;

import java.util.concurrent.Flow;

@Slf4j
public class QuoteEventSubscriber implements Flow.Subscriber<Event> {

    private final TradeEngine tradeEngine;

    private Flow.Subscription subscription;

    public QuoteEventSubscriber(TradeEngine tradeEngine) {
        this.tradeEngine = tradeEngine;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        log.info("Subscribed to events");
        this.subscription = subscription;
        requestNext();
    }

    @Override
    public void onNext(Event item) {
        if (item instanceof QuoteEvent) {
            tradeEngine.process((Quote) item.data());
        }
        requestNext();
    }

    private void requestNext() {
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Exception occurred", throwable);
    }

    @Override
    public void onComplete() {
        log.info("Event stream completed");
    }
}
