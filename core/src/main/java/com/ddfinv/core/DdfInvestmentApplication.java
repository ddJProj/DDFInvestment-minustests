package com.ddfinv.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class DdfInvestmentApplication {

    /*
     * Runs the application
     * @param args The arguments of the program.
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DdfInvestmentApplication.class, args);
    }

}
