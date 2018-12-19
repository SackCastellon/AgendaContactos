plugins {
    kotlin("jvm") version "1.3.11" apply false
    id("com.github.ben-manes.versions") version "0.20.0"
}

allprojects {
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
    }
}
