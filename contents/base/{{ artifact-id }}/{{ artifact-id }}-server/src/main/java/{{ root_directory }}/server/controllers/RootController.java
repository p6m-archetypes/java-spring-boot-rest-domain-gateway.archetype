package {{ root_package }}.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @RequestMapping("/")
    public String root() {
        return "{{ project-title }}";
    }
}
