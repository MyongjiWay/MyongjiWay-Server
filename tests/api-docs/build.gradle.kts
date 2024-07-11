dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api")
    compileOnly("org.springframework.boot:spring-boot-starter-test")
    api("org.springframework.restdocs:spring-restdocs-mockmvc")
    api("org.springframework.restdocs:spring-restdocs-restassured")
    api("io.rest-assured:spring-mock-mvc")
}

tasks.register<Test>("restDocsTest") {
    group = "verification"
    useJUnitPlatform {
        includeTags("restdocs")
    }
    outputs.dir(file("build/generated-snippets"))
}
