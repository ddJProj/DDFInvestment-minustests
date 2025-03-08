package com.ddfinv;

import org.springframework.boot.SpringApplication;

import com.ddfinv.core.DdfInvestmentApplication;

public class TestDdfInvestmentApplication {

    public static void main(String[] args) {
        SpringApplication.from(DdfInvestmentApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
