package com.artsgard.sociobank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @author artsgard
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.artsgard.sociobank")
public class SocioBankApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocioBankApplication.class, args);
    }
}
