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
        add("implementation", "org.springframework.grpc:spring-grpc-spring-boot-starter")
        add("implementation", "io.grpc:grpc-services")
    }

    tasks.withType<Test> { useJUnitPlatform() }
}

subprojects {
    val isAgentProject = project.path.startsWith(":agents:")
    if (isAgentProject) {
        apply(plugin = "com.google.cloud.tools.jib")
        dependencies {
        }
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
                    "org.opencontainers.image.revision" to "dev"
                )
            }
        }
    }
}