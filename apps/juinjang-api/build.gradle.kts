plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.jlleitschuh.gradle.ktlint")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveBaseName.set("juinjang-api")
}

tasks.named<Jar>("jar") { enabled = false }

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // Spring Cloud
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // Spring Retry
    implementation("org.springframework.retry:spring-retry")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // Database
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.mockito:mockito-core:5.14.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("org.instancio:instancio-junit:5.0.2")
}
