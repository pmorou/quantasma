package quantasma.integrations.event;

import quantasma.core.Quote;

import java.util.List;

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

    static OpenedPositionsEvent openedPositions(List<OpenedPosition> data) {
        return new OpenedPositionsEvent(data);
    }
}
