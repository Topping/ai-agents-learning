plugins {
    application
    alias(libs.plugins.springBoot) apply true
    alias(libs.plugins.shadow) // optional: fat jar for local runs
    alias(libs.plugins.graalvm) apply true
    // Jib is applied from the root convention; you can also apply here explicitly if you prefer
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.adk.core)
    implementation(libs.adk.dev)
    implementation(libs.adk.lc4j)
    implementation(libs.lc4j.base)
    implementation(libs.lc4j.core)
    implementation(libs.lc4j.oai)
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("com.example.mas.comedian.ComedianApplication")
}

System.setProperty("java.io.tmpdir", "D:/temp")

tasks.named<Jar>("jar") {
    manifest { attributes["Main-Class"] = application.mainClass.get() }
}

tasks.shadowJar {
    isZip64 = true
}

graalvmNative {
    binaries {
        named("main") {
            buildArgs.add("--initialize-at-build-time=com.google.protobuf.RuntimeVersion\$RuntimeDomain")
        }
    }
}
