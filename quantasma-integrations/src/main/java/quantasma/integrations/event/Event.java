package quantasma.integrations.event;

import quantasma.core.Quote;

public interface Event<D> {
    String id();

    String name();

    D data();

    static QuoteEvent quote(Quote data) {
        return new QuoteEvent(data);
    }

    static AccountStateEvent accountState(AccountState data) {
        return new AccountStateEvent(data);
    }
}
