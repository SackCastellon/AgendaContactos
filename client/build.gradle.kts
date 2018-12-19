import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

repositories {
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation(project(":common"))

    // Kotlin Standard Library
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.0.1")

    // Ktor Framework
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-gson:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    // Dependency Injection
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-core-ext:$koinVersion")
    implementation("org.koin:koin-logger-slf4j:$koinVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Database
    implementation("org.jetbrains.exposed:exposed:0.11.2")
    implementation("org.xerial:sqlite-jdbc:3.25.2")

    // User Interface
    implementation("no.tornado:tornadofx:1.7.17")
    implementation("no.tornado:tornadofx-controlsfx:0.1.1")
    implementation("org.controlsfx:controlsfx:8.40.14")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.11")

    // Icons
    implementation("org.kordamp.ikonli:ikonli-javafx:$ikonliVersion")
    implementation("org.kordamp.ikonli:ikonli-fontawesome-pack:$ikonliVersion")

    // Validation
    implementation("com.googlecode.libphonenumber:libphonenumber:8.10.2")

    // Collections
    implementation("com.google.guava:guava:27.0.1-jre")
    implementation("com.github.ben-manes.caffeine:caffeine:2.6.2")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=enable", "-progressive")
    }
}
