package com.krushimitra.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * KrushiMitra AI - Main Application Entry Point
 * Intelligent Government Scheme Assistant for Farmers
 */
@SpringBootApplication
@EnableJpaAuditing
public class KrushiMitraApplication {

    public static void main(String[] args) {
        SpringApplication.run(KrushiMitraApplication.class, args);
    }
}
