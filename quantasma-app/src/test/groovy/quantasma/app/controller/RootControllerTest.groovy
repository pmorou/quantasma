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
@WebMvcTest(RootController)
class RootControllerTest {

    @Autowired
    private MockMvc mockMvc

    @Test
    void rootOk() throws Exception {
        mockMvc.perform(get("test"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"))
    }
}