package com.ddfinv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.ddfinv.backend.DdfInvestmentApplication;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class DdfInvestmentApplicationTests {
    public static void main(String[] args) {
        SpringApplication.from(DdfInvestmentApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
