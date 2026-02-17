plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":ExternalDependencies"))
    implementation(project(":ScreeningSeatingArrangementSuggestions"))
    implementation(project(":ScreeningSeatingArrangementSuggestionsApi"))

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")
    testImplementation("org.assertj:assertj-core:3.27.6")
}
