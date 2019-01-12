plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":common"))

    // Dependency Injection
    koin("core", "core-ext", "logger-slf4j", "ktor")

    // Web Framework
    ktor("server-core", "server-cio", "auth", "auth-jwt", "gson")

    // Database
    implementation("org.postgresql:postgresql:42.2.5")
    implementation("com.zaxxer:HikariCP:3.3.0")
}
