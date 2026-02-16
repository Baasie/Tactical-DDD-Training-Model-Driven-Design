plugins {
    kotlin("jvm")
    id("io.ktor.plugin") version "3.1.2"
}

application {
    mainClass.set("org.weaveit.seatingplacesuggestions.api.ApplicationKt")
}

dependencies {
    implementation(project(":ScreeningSeatingArrangementSuggestions"))
    implementation(project(":ExternalDependencies"))

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-jackson")
}
