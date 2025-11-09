package espe.edu.tienda_ropa.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public String test() {
        return "API funcionando correctamente";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
