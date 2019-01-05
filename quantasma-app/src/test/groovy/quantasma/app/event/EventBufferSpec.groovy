package quantasma.app.event

import groovy.transform.TupleConstructor
import lombok.Data
import org.reactivestreams.Publisher
import org.reactivestreams.tck.PublisherVerification
import org.reactivestreams.tck.TestEnvironment
import quantasma.integrations.event.Event

//class EventBufferSpec extends PublisherVerification<TestEvent> {
class EventBufferSpec {

    EventBufferSpec() {
//        super(new TestEnvironment())
    }

//    @Override
    Publisher<TestEvent> createPublisher(long elements) {
        def eventBuffer = EventBuffer.instance(TestEvent)


        Runnable run = { ->
            for (int i = 0; i < elements + 1000; i++) {
                eventBuffer.setNext(new TestEvent(data: i))
            }
        }
        new Thread(run).start()

        return eventBuffer.publisher()
    }

//    @Override
    Publisher<TestEvent> createFailedPublisher() {
        return null
    }

    @TupleConstructor
    @Data
    class TestEvent implements Event<Integer> {
        private String id
        private String name
        private Integer data

        @Override
        String id() {
            return id
        }

        @Override
        String name() {
            return name
        }

        @Override
        Integer data() {
            return data
        }
    }
}