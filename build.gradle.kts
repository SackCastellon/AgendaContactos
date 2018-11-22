plugins {
    java
    application
    id("com.github.ben-manes.versions") version "0.20.0"
}

group = "es.uji.ei1039"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.controlsfx:controlsfx:8.40.14")
    
    implementation("org.kordamp.ikonli:ikonli-javafx:2.4.0")
    implementation("org.kordamp.ikonli:ikonli-fontawesome-pack:2.4.0")

    implementation("ch.qos.logback:logback-classic:1.3.0-alpha4")

    implementation("org.jetbrains:annotations:16.0.3")
    implementation("org.projectlombok:lombok:1.18.4")
    annotationProcessor("org.projectlombok:lombok:1.18.4")

    testImplementation("junit:junit:4.12")
}

application {
    mainClassName = "es.uji.ei1039.agenda.App"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "8"
    targetCompatibility = "8"
}
