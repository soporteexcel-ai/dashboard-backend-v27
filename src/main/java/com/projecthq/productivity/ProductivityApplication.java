package com.projecthq.productivity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación Spring Boot Principal.
 * V-018: Punto de entrada para el Microservicio de Productividad.
 */
@SpringBootApplication
public class ProductivityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductivityApplication.class, args);
    }
}
