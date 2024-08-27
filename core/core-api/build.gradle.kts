import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":web"))
    implementation(project(":core:core-domain"))
    implementation(project(":support:monitoring"))
    implementation(project(":support:logging"))
    implementation(project(":storage:db-core"))
    implementation(project(":clients:client-kakao"))
    implementation(project(":clients:client-mqtt"))
    implementation(project(":clients:client-apple"))

    testImplementation(project(":tests:api-docs"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.named<BootJar>("bootJar") {
    enabled = true
    dependsOn("copyApiDocument")

    from("src/main/resources/static/docs") {
        into("static/docs")
    }
}

tasks.getByName("jar") {
    enabled = false
}
