package quantasma.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
@Slf4j
public class RootController {

    @RequestMapping
    public String root() {
        log.info("Root accessed");
        return "OK";
    }

}
