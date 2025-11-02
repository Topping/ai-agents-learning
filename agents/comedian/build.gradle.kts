plugins {
    application
    alias(libs.plugins.shadow) // optional: fat jar for local runs
    alias(libs.plugins.graalvm) apply true
    // Jib is applied from the root convention; you can also apply here explicitly if you prefer
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.adk.core)
    // optional dev UI/server scaffolding:
    implementation(libs.adk.dev)

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("com.example.mas.comedian.ComedianApplication")
}

tasks.named<Jar>("jar") {
    manifest { attributes["Main-Class"] = application.mainClass.get() }
}

tasks.shadowJar {
    isZip64 = true
}

// ---- Per-agent Jib overrides (optional) ----
// extensions.configure<com.google.cloud.tools.jib.gradle.JibExtension>("jib") {
//     to {
//         // Give the agent a distinct repository and tag set
//         image = "ghcr.io/your-org/agent-researcher"
//         // Example tagging strategy
//         val sha = System.getenv("GITHUB_SHA") ?: "local"
//         tags = setOf(
//             project.version.toString(),
//             "sha-$sha",
//             "latest"
//         )
//     }
//     container {
//         // If your agent serves HTTP, keep port 8080; if CLI, you can omit ports.
//         // Pass defaults via env:
//         environment = mapOf(
//             "LOG_LEVEL" to "info",
//             "AGENT_NAME" to "researcher"
//         )
//         // If you want a custom entrypoint/args:
//         // mainClass is already from application.mainClass
//         // args = listOf("--profile=prod")
//     }
// }
