package quantasma.app.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(StatusController)
class StatusControllerSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    def 'status returns OK'() {
        when:
        mockMvc.perform(get("status"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"))

        then:
        noExceptionThrown()
    }
}