package quantasma.integrations.event

import org.reactivestreams.FlowAdapters
import org.reactivestreams.Publisher
import org.reactivestreams.tck.PublisherVerification
import org.reactivestreams.tck.TestEnvironment
import org.testng.annotations.Test

import java.util.concurrent.Flow

class EventPublisherSpec extends PublisherVerification<TestEvent> {

    EventPublisherSpec() {
        super(new TestEnvironment())
    }

    @Test(enabled = false, description = "test takes too long to finish")
    @Override
    void required_spec317_mustNotSignalOnErrorWhenPendingAboveLongMaxValue() throws Throwable {
        super.required_spec317_mustNotSignalOnErrorWhenPendingAboveLongMaxValue()
    }

    @Override
    Publisher<TestEvent> createPublisher(long elements) {
        FlowAdapters.toPublisher(
                new LazyEventPublisher(
                        publisher: EventPublisher.instance(),
                        demand: elements))
    }

    @Override
    Publisher<TestEvent> createFailedPublisher() {
        null
    }

    static class LazyEventPublisher implements EventPublisher {
        private EventPublisher publisher
        private long demand

        @Override
        void publish(Event event) {
            publisher.publish(event)
        }

        @Override
        void close() {
            publisher.close()
        }

        @Override
        void subscribe(Flow.Subscriber<? super Event> subscriber) {
            publisher.subscribe(subscriber)
            publishFiniteEvents()
        }

        private publishFiniteEvents() {
            demand.times {
                publisher.publish(new TestEvent(data: it))
            }
            close()
        }
    }

    static class TestEvent implements Event<Integer> {
        private String id
        private String name
        private Integer data

        @Override
        String id() {
            id
        }

        @Override
        String name() {
            name
        }

        @Override
        Integer data() {
            data
        }
    }

}
