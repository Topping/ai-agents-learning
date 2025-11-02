import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    alias(libs.plugins.jib) apply false
    alias(libs.plugins.springManagement) apply false
}

val springBootVersion = libs.versions.spring.boot.get()
val springGrpcVersion = libs.versions.spring.grpc.get()
subprojects {
    if (project.path.startsWith(":agents:")) {
        apply(plugin = "java")
    } else {
        apply(plugin = "java-library")
    }

    apply(plugin = "io.spring.dependency-management")
    repositories { mavenCentral() }

    configure<JavaPluginExtension> {
        toolchain { 
            languageVersion.set(JavaLanguageVersion.of(25)) 
            vendor.set(JvmVendorSpec.GRAAL_VM)
        }
    }

    configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${springBootVersion}")
            mavenBom("org.springframework.grpc:spring-grpc-dependencies:${springGrpcVersion}")
        }
    }

    dependencies {
        add("implementation","org.slf4j:slf4j-api:2.0.17")
    }

    tasks.withType<Test> { useJUnitPlatform() }
}

// Apply Jib plugin and configure for agent subprojects only
subprojects {
    if (project.path.startsWith(":agents:")) {
        apply(plugin = "com.google.cloud.tools.jib")

        configure<com.google.cloud.tools.jib.gradle.JibExtension> {
            from {
                image = "gcr.io/distroless/java25-debian13"
                platforms {
                    platform {
                        architecture = "amd64"
                        os = "linux"
                    }
                    platform {
                        architecture = "arm64"
                        os = "linux"
                    }
                }
            }
            
            container {
                jvmFlags = listOf(
                    "-XX:MaxRAMPercentage=75.0",
                    "-Dfile.encoding=UTF-8"
                )
                ports = listOf("8080")
                
                labels = mapOf(
                    "org.opencontainers.image.source" to "https://github.com/your-org/adk-monorepo",
                    "org.opencontainers.image.revision" to (System.getenv("GITHUB_SHA") ?: "dev")
                )
            }
        }
    }
}