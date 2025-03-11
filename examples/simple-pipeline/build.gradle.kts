plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(project(":core"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation(project(":adapters:in-memory"))
}

application {
    mainClass.set("com.example.eventflow.examples.SimplePipelineKt")
}