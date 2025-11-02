package com.example.mas.shared;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: Define interface properly, so agent services can add this annotation to their SpringBootApplication class
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(GrpcServiceConfiguration.class)
public @interface CustomGrpcThing {
}
