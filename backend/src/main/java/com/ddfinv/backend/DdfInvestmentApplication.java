package com.ddfinv.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EntityScan("com.ddinv.core.entity")
@EnableJpaRepositories("com.ddinv.core.repository")
@ComponentScan({"com.ddinv.core","com.ddinv.backend"})
public class DdfInvestmentApplication {

    /*
     * Runs the application
     * @param args The arguments of the program.
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DdfInvestmentApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restBuilder){
        return restBuilder.build();
    }

}
