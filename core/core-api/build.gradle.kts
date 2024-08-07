import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":core:core-domain"))
    implementation(project(":support:monitoring"))
    implementation(project(":support:logging"))
    implementation(project(":storage:db-core"))
    implementation(project(":clients:client-kakao"))
    implementation(project(":clients:client-mqtt"))

    testImplementation(project(":tests:api-docs"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
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
