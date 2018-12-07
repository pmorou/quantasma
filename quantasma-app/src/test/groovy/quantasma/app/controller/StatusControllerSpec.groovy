package quantasma.app.controller

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner.class)
@WebMvcTest(StatusController)
class StatusControllerSpec {

    @Autowired
    private MockMvc mockMvc

    @Test
    void statusOk() throws Exception {
        mockMvc.perform(get("status"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"))
    }
}