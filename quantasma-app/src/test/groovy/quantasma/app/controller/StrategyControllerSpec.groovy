package quantasma.app.controller

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import quantasma.app.service.StrategyService
import quantasma.core.StrategyDescription
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(StrategyController)
class StrategyControllerSpec extends Specification {

    @SpringBean
    private StrategyService service = Mock()

    @Autowired
    private MockMvc mockMvc

    def 'status returns OK'() {
        given:
        service.all() >> []

        when:
        mockMvc.perform(get("/api/strategy/all"))
                .andExpect(status().isOk())

        then:
        1 * service.all()
        noExceptionThrown()
    }

    def 'should return 1 strategy'() {
        given:
        service.all() >> [new StrategyDescription(1L, "name", true, [new StrategyDescription.Parameter("name", "clazz", "value")])]

        when:
        mockMvc.perform(get("/api/strategy/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("\$[0].id").value(1))
                .andExpect(jsonPath("\$[0].name").value("name"))
                .andExpect(jsonPath("\$[0].active").value(true))
                .andExpect(jsonPath("\$[0].parameters[0].name").value("name"))
                .andExpect(jsonPath("\$[0].parameters[0].clazz").value("clazz"))
                .andExpect(jsonPath("\$[0].parameters[0].value").value("value"))

        then:
        noExceptionThrown()
    }

    def "should call activation method"() {
        when:
        mockMvc.perform(patch("/api/strategy/activate/1"))
            .andExpect(status().isOk())

        then:
        noExceptionThrown()
        1 * service.activate(1)
        0 * service._
    }

}
