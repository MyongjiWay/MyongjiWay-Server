dependencies {
    implementation(project(":core:core-domain"))
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    api("org.springframework.security:spring-security-oauth2-client")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-security")
}
