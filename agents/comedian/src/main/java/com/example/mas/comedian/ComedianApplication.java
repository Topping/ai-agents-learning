package com.example.mas.comedian;


import com.example.mas.shared.CustomGrpcThing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@CustomGrpcThing
public class ComedianApplication {
    public static void main(String[] args) {
        SpringApplication.run(ComedianApplication.class, args);
    }
}
