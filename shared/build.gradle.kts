plugins {
    alias(libs.plugins.spring) apply true
    alias(libs.plugins.springBoot) apply true
    alias(libs.plugins.graalvm) apply true
    alias(libs.plugins.protobuf) apply true
}



dependencies {
    api("org.springframework.boot:spring-boot-starter")
    implementation(libs.adk.core)              // only if shared needs ADK types
    implementation("io.grpc:grpc-services")
    implementation("org.springframework.grpc:spring-grpc-spring-boot-starter")
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.grpc:spring-grpc-test")
}
