package github.pancras.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import github.pancras.spring.annotation.RpcScan;

@SpringBootApplication
@RpcScan(basePackage = {""})
public class BusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }
}
