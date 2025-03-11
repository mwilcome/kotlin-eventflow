plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    // Uncomment to add Kafka client:
    // implementation("org.apache.kafka:kafka-clients:3.6.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.test {
    useJUnitPlatform()
}