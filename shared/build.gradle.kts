plugins {
    alias(libs.plugins.springBoot) apply true
    alias(libs.plugins.graalvm) apply true
    alias(libs.plugins.protobuf) apply true
}

dependencies {
    api("org.springframework.boot:spring-boot-starter:${libs.versions.spring.boot.get()}")
    implementation(libs.adk.core)              // only if shared needs ADK types
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.grpc:spring-grpc-test:${libs.versions.spring.grpc.get()}")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.3"
    }
    
    // Configure compileProtoPath to exclude Spring Boot dependencies
    configurations {
//        getByName("compileProtoPath") {
//            exclude(group = "org.springframework.boot", module = "spring-boot-starter")
//        }
//        getByName("testCompileProtoPath") {
//            exclude(group = "org.springframework.grpc", module = "spring-grpc-test")
//        }
    }
}

// Disable Spring Boot application tasks for library module
tasks.named("resolveMainClassName") {
    enabled = false
}
tasks.named("processAot") {
    enabled = false
}
tasks.named("bootJar") {
    enabled = false
}