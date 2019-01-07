package quantasma.integrations.event;

import quantasma.core.Quote;
import quantasma.core.TradeEngine;

import java.util.concurrent.Flow;

public class QuoteEventSubscriber implements Flow.Subscriber<Event> {

    private final TradeEngine tradeEngine;

    public QuoteEventSubscriber(TradeEngine tradeEngine) {
        this.tradeEngine = tradeEngine;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        // ignore
    }

    @Override
    public void onNext(Event item) {
        if (item instanceof QuoteEvent) {
            tradeEngine.process((Quote) item.data());
        }
    }

    @Override
    public void onError(Throwable throwable) {
        // ignore
    }

    @Override
    public void onComplete() {
        // ignore
    }
}
