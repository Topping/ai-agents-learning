import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.springBoot) apply true
    alias(libs.plugins.protobuf) apply true
}

dependencies {
    //api("org.springframework.boot:spring-boot-starter:${libs.versions.spring.boot.get()}")
    implementation(libs.adk.core)              // only if shared needs ADK types

    api("io.github.a2asdk:a2a-java-sdk-reference-common:0.3.1.Final")
    implementation("io.github.a2asdk:a2a-java-sdk-client:0.3.1.Final")
    implementation("io.github.a2asdk:a2a-java-sdk-common:0.3.1.Final")
    implementation("io.github.a2asdk:a2a-java-sdk-transport-grpc:0.3.1.Final")
    implementation("io.github.a2asdk:a2a-java-sdk-server-common:0.3.1.Final")

    implementation("com.google.protobuf:protobuf-java:4.33.0")
    implementation("io.grpc:grpc-protobuf:1.76.0")
    implementation("io.grpc:grpc-stub:1.76.0")

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.grpc:spring-grpc-test:${libs.versions.spring.grpc.get()}")
//    protobuf("com.google.api.grpc:proto-google-common-protos:2.62.0")
}

//protobuf {
//    protoc {
//        artifact = "com.google.protobuf:protoc"
//    }
//    plugins {
//        id("grpc") {
//            artifact = "io.grpc:protoc-gen-grpc-java"
//        }
//    }
//    generateProtoTasks {
//        all().forEach {
//            it.plugins {
//                id("grpc") {
//                    option("@generated=omit")
//                }
//            }
//        }
//    }
//}

//protobuf {
//    protoc {
//        artifact = "com.google.protobuf:protoc:4.33.0"
//    }
//
//    // Configure compileProtoPath to exclude Spring Boot dependencies
//    configurations {
//        getByName("compileProtoPath") {
//            exclude(group = "org.springframework.boot", module = "spring-boot-starter")
//        }
//        getByName("testCompileProtoPath") {
//            exclude(group = "org.springframework.grpc", module = "spring-grpc-test")
//        }
//    }
//}

// Disable Spring Boot application tasks for library module
tasks.named("resolveMainClassName") {
    enabled = false
}
tasks.named("bootJar") {
    enabled = false
}