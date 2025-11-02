rootProject.name = "adk-monorepo"

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":shared")
include(":agents:comedian")
