dependencies {
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
    implementation("io.sentry:sentry-logback:${property("sentryVersion")}")
}
