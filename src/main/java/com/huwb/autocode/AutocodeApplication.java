package com.huwb.autocode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class AutocodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutocodeApplication.class, args);
    }

}
