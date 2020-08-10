package ninja.spring.pcfscheduler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("")
    private String getText() {
        return "demo";
    }
}
