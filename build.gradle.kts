import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val koinVersion: String by project
val ikonliVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.3.11"
    id("com.github.ben-manes.versions") version "0.20.0"
    id("com.github.johnrengelman.shadow") version "4.0.3"
}

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
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-core-ext:$koinVersion")
    implementation("org.koin:koin-logger-slf4j:$koinVersion")

    // Collections
    implementation("com.github.ben-manes.caffeine:caffeine:2.6.2")
    implementation("com.google.guava:guava:27.0.1-jre")

    // User Interface
    implementation("no.tornado:tornadofx:1.7.19-SNAPSHOT")
    implementation("no.tornado:tornadofx-controlsfx:0.1.1")
    implementation("org.controlsfx:controlsfx:8.40.14")

    // Icons
    implementation("org.kordamp.ikonli:ikonli-javafx:$ikonliVersion")
    implementation("org.kordamp.ikonli:ikonli-material-pack:$ikonliVersion")

    // Databases
    implementation("org.jetbrains.exposed:exposed:0.11.2")
    implementation("org.xerial:sqlite-jdbc:3.25.2")
    implementation("org.postgresql:postgresql:42.2.5")

    // Data Export
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
    implementation("com.googlecode.ez-vcard:ez-vcard:0.10.5")

    // Directories
    implementation("io.github.soc:directories:10")

    // Validation
    implementation("commons-validator:commons-validator:1.6")
    implementation("com.googlecode.libphonenumber:libphonenumber:8.10.2")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha4")
    implementation("io.github.microutils:kotlin-logging:1.6.22")

    implementation("org.apache.commons:commons-text:1.5")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

application {
    mainClassName = "agenda.Agenda"
}

tasks.withType<ShadowJar> {
    baseName = "Agenda"
    classifier = ""
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        apiVersion = "1.3"
        languageVersion = "1.3"
        freeCompilerArgs = listOf("-Xjvm-default=enable", "-progressive")
    }
}
