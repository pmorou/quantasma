package quantasma.app.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import quantasma.app.service.EventsService
import quantasma.core.Quote
import quantasma.integrations.event.Event
import reactor.core.publisher.Flux
import reactor.util.function.Tuple2
import spock.lang.Specification

import java.time.Duration
import java.time.ZonedDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventsControllerSpec extends Specification {

    private static final ZonedDateTime TIME = ZonedDateTime.now()

    @Autowired
    private WebTestClient webTestClient

    @SpringBean
    private EventsService service = Mock()

    def "should return 3 unique quotes"() {
        given:
        List<MappedQuote> expectedResult = []
        service.quote() >> Flux.just(1, 2, 3)
                .zipWith(Flux.interval(Duration.ofSeconds(1)))
                .map({ tuple -> Event.quote((expectedResult << createQuote(tuple)).last() as Quote)
        })

        when:
        def result = get("/api/events/quote")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()

        then:
        noExceptionThrown()
        with(result) {
            expectStatus().isOk()
            def body = expectBodyList(MappedQuote).returnResult().getResponseBody()
            body.eachWithIndex {
                entry, i -> entry == expectedResult[i]
            }
            body.size() == 3
        }
    }

    private static Quote createQuote(Tuple2<Integer, Long> tuple) {
        new Quote("symbol", TIME, tuple.getT1(), tuple.getT1())
    }

    private WebTestClient.RequestBodyUriSpec get(String uri) {
        return webTestClient.get().uri(uri) as WebTestClient.RequestBodyUriSpec
    }

    static class MappedQuote extends Quote {

        @JsonCreator
        MappedQuote(@JsonProperty("symbol") String symbol,
                    @JsonProperty("time") ZonedDateTime time,
                    @JsonProperty("bid") double bid,
                    @JsonProperty("ask") double ask) {
            super(symbol, time, bid, ask)
        }
    }
}
