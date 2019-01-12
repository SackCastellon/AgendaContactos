@file:Suppress("unused")

import org.gradle.api.artifacts.dsl.DependencyHandler

const val ktorVersion: String = "1.1.1"
const val koinVersion: String = "1.0.2"
const val ikonliVersion: String = "2.4.0"
const val logbackVersion: String = "1.2.1"

fun DependencyHandler.ktor(vararg artifacts: String) {
    artifacts.forEach { add("implementation", "io.ktor:ktor-$it:$ktorVersion") }
}

fun DependencyHandler.koin(vararg artifacts: String) {
    artifacts.forEach { add("implementation", "org.koin:koin-$it:$koinVersion") }
}
