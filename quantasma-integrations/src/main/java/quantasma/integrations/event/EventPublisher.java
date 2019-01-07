package quantasma.integrations.event;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public interface EventPublisher extends Flow.Publisher<Event> {

    void publish(Event event);

    void close();

    static EventPublisher instance() {
        return new EventPublisherImpl();
    }

    final class EventPublisherImpl extends SubmissionPublisher<Event> implements EventPublisher {

        @Override
        public void publish(Event event) {
            submit(event);
        }

    }
}
