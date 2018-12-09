package quantasma.integrations.event;

import quantasma.core.Quote;

public interface Event<D> {
    String id();

    String name();

    D data();

    static QuoteEvent quote(Quote data) {
        return new QuoteEvent(data);
    }

    static AccountEvent accountInfo(AccountInfo data) {
        return new AccountEvent(data);
    }
}
