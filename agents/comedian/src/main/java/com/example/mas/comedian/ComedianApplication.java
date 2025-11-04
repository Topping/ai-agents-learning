package com.example.mas.comedian;

import com.google.adk.web.AdkWebServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AdkWebServer.class)
public class ComedianApplication {
    static void main(String[] args) {
        System.setProperty("adk.agents.loader", "none");
        SpringApplication.run(ComedianApplication.class, args);
    }
}
