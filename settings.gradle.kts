pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.google" && requested.id.name == "osdetector") {
                useModule ("com.google.gradle:osdetector-gradle-plugin:1.6.0")
            }
        }
    }
}

rootProject.name = "agenda"
