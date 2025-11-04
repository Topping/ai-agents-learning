plugins {
    alias(libs.plugins.springBoot) apply true
    alias(libs.plugins.protobuf) apply true
}

dependencies {
    implementation(libs.adk.core)              // only if shared needs ADK types
    api("io.github.a2asdk:a2a-java-sdk-reference-common:0.3.1.Final")
    api("io.github.a2asdk:a2a-java-sdk-client:0.3.1.Final")
    implementation("io.github.a2asdk:a2a-java-sdk-common:0.3.1.Final")
    implementation("io.github.a2asdk:a2a-java-sdk-transport-grpc:0.3.1.Final")
    implementation("io.github.a2asdk:a2a-java-sdk-server-common:0.3.1.Final")

    implementation("com.google.api.grpc:proto-google-common-protos:2.62.0")
    implementation("com.google.protobuf:protobuf-java:4.33.0")
    implementation("io.grpc:grpc-protobuf:1.76.0")
    implementation("io.grpc:grpc-stub:1.76.0")

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.grpc:spring-grpc-test:${libs.versions.spring.grpc.get()}")
}

// Disable Spring Boot application tasks for library module
tasks.named("resolveMainClassName") {
    enabled = false
}
tasks.named("bootJar") {
    enabled = false
}