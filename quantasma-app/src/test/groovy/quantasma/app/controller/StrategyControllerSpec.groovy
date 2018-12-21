package quantasma.app.controller

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import quantasma.app.service.StrategyService
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

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
}
