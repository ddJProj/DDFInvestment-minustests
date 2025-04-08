package com.ddfinv.backend;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.ddfinv.backend.DdfInvestmentApplication;

//@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Disabled
class DdfInvestmentApplicationTests {

    // public static void main(String[] args) {

    // }
    @Test
    void contextLoading(){
        // test working?
        assertTrue(true, "Test always passes, is system configured correctly?");
    }
    // public static void main(String[] args) {
    //     SpringApplication.from(DdfInvestmentApplication::main)
    //             .with(TestcontainersConfiguration.class)
    //             .run(args);
    // }
} 
    // @BeforeEach
    // void setUp() {
    // }

    // @AfterEach
    // void tearDown() {
    // }

    // @Test
    // void main() {
    // }

    // @Test
    // void restTemplate() {
    // }
