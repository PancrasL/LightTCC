package github.pancras.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.pancras.business.service.BusinessService;

@RestController
public class BussinessController {
    @Autowired
    private BusinessService service;

    @RequestMapping("/test")
    public String test() {
        service.createBussiness(null);
        return "Hello, Spring Boot 2!";
    }
}
