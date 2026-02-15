plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":ExternalDependencies"))

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")
}
