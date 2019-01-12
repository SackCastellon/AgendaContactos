plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    // Kotlin Standard Library
    api(kotlin("stdlib-jdk8"))

    // Database
    api("org.jetbrains.exposed:exposed:0.11.2")

    // Logging
    api("ch.qos.logback:logback-classic:1.3.0-alpha4")
}
