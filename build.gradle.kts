import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.10"
    id("com.github.ben-manes.versions") version "0.20.0"
}

group = "es.uji.ei1039"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    // Kotlin Standard Library
    implementation(kotlin("stdlib-jdk8"))

    // Dependency Injection
    implementation("org.koin:koin-core:1.0.2")
    implementation("org.koin:koin-core-ext:1.0.2")
    implementation("org.koin:koin-logger-slf4j:1.0.2")

    // User Interface
    implementation("no.tornado:tornadofx:1.7.18-SNAPSHOT")
    implementation("org.controlsfx:controlsfx:8.40.14")

    // Icons
    implementation("org.kordamp.ikonli:ikonli-javafx:2.4.0")
    implementation("org.kordamp.ikonli:ikonli-fontawesome-pack:2.4.0")

    // Validation
    implementation("com.googlecode.libphonenumber:libphonenumber:8.10.1")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha4")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

application {
    mainClassName = "es.uji.ei1039.agenda.App"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        apiVersion = "1.3"
        languageVersion = "1.3"
        freeCompilerArgs = listOf("-Xjvm-default=enable", "-progressive")
    }
}
