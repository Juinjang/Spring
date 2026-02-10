plugins {
    id("org.jetbrains.kotlin.plugin.jpa")
}

dependencies {
    // Modules
    implementation(project(":supports:monitoring"))
    implementation(project(":supports:logging"))
    implementation(project(":modules:jpa"))
    implementation(project(":modules:redis"))
    testImplementation(testFixtures(project(":modules:jpa")))

    // Lombok (코드 마이그레이션 전까지 유지)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Spring Cloud
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // Spring Retry
    implementation("org.springframework.retry:spring-retry")

    // QueryDSL (이 모듈 자체 엔티티용 APT)
    kapt("com.querydsl:querydsl-apt:${project.properties["queryDslVersion"]}:jakarta")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:${project.properties["jjwtVersion"]}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${project.properties["jjwtVersion"]}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${project.properties["jjwtVersion"]}")

    // AWS S3
    implementation("org.springframework.cloud:spring-cloud-starter-aws:${project.properties["springCloudAwsVersion"]}")

    // Google Cloud Vision
    implementation("com.google.cloud:google-cloud-vision:${project.properties["googleCloudVisionVersion"]}")

    // Apple StoreKit
    implementation("com.apple.itunes.storekit:app-store-server-library:${project.properties["appleStoreKitVersion"]}")

    // Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${project.properties["springDocOpenApiVersion"]}")

    // Database
    runtimeOnly("com.h2database:h2")

    // Test
    testImplementation("org.springframework.security:spring-security-test")
}
