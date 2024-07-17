dependencies {
    implementation(project(":core:core-domain"))
    implementation("org.springframework:spring-context")
    implementation("com.amazonaws:aws-iot-device-sdk-java:1.3.9")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}
