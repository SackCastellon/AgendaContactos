plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":common"))

    // Dependency Injection
    koin("core", "core-ext", "logger-slf4j")

    // User Interface
    implementation("no.tornado:tornadofx:1.7.18")
    implementation("org.controlsfx:controlsfx:8.40.14")

    // Database
    implementation("org.xerial:sqlite-jdbc:3.25.2")
}
