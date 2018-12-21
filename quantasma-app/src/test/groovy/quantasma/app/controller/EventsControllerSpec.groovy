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
import quantasma.integrations.event.AccountState
import quantasma.integrations.event.Direction
import quantasma.integrations.event.Event
import quantasma.integrations.event.OpenedPosition
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

    def "should return 3 unique account states"() {
        given:
        List<MappedAccountState> expectedResult = []
        service.accountState() >> Flux.just(1, 2, 3)
                .zipWith(Flux.interval(Duration.ofSeconds(1)))
                .map({ tuple -> Event.accountState((expectedResult << createAccountState(tuple)).last() as AccountState)
        })

        when:
        def result = get("/api/events/accountState")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()

        then:
        noExceptionThrown()
        with(result) {
            expectStatus().isOk()
            def body = expectBodyList(MappedAccountState).returnResult().getResponseBody()
            body.eachWithIndex {
                entry, i -> entry == expectedResult[i]
            }
            body.size() == 3
        }
    }

    private static AccountState createAccountState(Tuple2<Integer, Long> tuple) {
        new AccountState(tuple.getT1(), tuple.getT1(), tuple.getT1(), tuple.getT1(), tuple.getT1(), "currency", tuple.getT1())
    }

    def "should return 3 unique opened positions"() {
        given:
        List<List<MappedOpenedPosition>> expectedResult = []
        service.openedPositions() >> Flux.just(1, 2, 3)
                .zipWith(Flux.interval(Duration.ofSeconds(1)))
                .map({ tuple -> Event.openedPositions((expectedResult << createOpenedPosition(tuple)).last() as List<OpenedPosition>)
        })

        when:
        def result = get("/api/events/openedPositions")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()

        then:
        noExceptionThrown()
        with(result) {
            expectStatus().isOk()
            def body = expectBodyList(MappedOpenedPosition[]).returnResult().getResponseBody()
            body.eachWithIndex {
                entry, i -> entry == expectedResult[i]
            }
            body.size() == 3
        }
    }

    private static List<OpenedPosition> createOpenedPosition(Tuple2<Integer, Long> tuple) {
        [new OpenedPosition("symbol", Direction.LONG, tuple.getT1(), tuple.getT1(), tuple.getT1(), tuple.getT1(), tuple.getT1(), tuple.getT1())]
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

    static class MappedAccountState extends AccountState {

        @JsonCreator
        MappedAccountState(@JsonProperty("entity") double equity,
                           @JsonProperty("balance") double balance,
                           @JsonProperty("positionsProfitLoss") double positionsProfitLoss,
                           @JsonProperty("positionsAmount") double positionsAmount,
                           @JsonProperty("usedMargin") double usedMargin,
                           @JsonProperty("currency") String currency,
                           @JsonProperty("leverage") double leverage) {
            super(equity, balance, positionsProfitLoss, positionsAmount, usedMargin, currency, leverage)
        }
    }

    static class MappedOpenedPosition extends OpenedPosition {

        @JsonCreator
        MappedOpenedPosition(@JsonProperty("symbol") String symbol,
                             @JsonProperty("direction") Direction direction,
                             @JsonProperty("amount") double amount,
                             @JsonProperty("price") double price,
                             @JsonProperty("stopLoss") double stopLoss,
                             @JsonProperty("takeProfit") double takeProfit,
                             @JsonProperty("profitLossPips") double profitLossPips,
                             @JsonProperty("profitLoss") double profitLoss) {
            super(symbol, direction, amount, price, stopLoss, takeProfit, profitLossPips, profitLoss)
        }
    }
}
