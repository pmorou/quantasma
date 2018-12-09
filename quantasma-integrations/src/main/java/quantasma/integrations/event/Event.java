package quantasma.integrations.event;

public interface Event<D> {
    String id();

    String name();

    D data();

    static QuoteEvent quote(Quote data) {
        return new QuoteEvent(data);
    }
}
