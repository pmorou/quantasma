package quantasma.integrations.event;

@FunctionalInterface
public interface EventPipe<E extends Event<D>, D> {

    void handle(E event);
}
