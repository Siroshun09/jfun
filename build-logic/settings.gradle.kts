plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.9.0")
}

rootProject.name = "jfun-build-logic"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        register("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
