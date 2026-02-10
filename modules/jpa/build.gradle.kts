plugins {
    id("org.jetbrains.kotlin.plugin.jpa")
    `java-test-fixtures`
}

dependencies {
    // JPA
    api("org.springframework.boot:spring-boot-starter-data-jpa")

    // QueryDSL
    api("com.querydsl:querydsl-jpa:${project.properties["queryDslVersion"]}:jakarta")
    kapt("com.querydsl:querydsl-apt:${project.properties["queryDslVersion"]}:jakarta")

    // Lombok (코드 마이그레이션 전까지 유지)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // JDBC (버전은 gradle.properties의 mysql.version으로 관리)
    runtimeOnly("com.mysql:mysql-connector-j")

    // testFixtures
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testFixturesImplementation("org.testcontainers:mysql")
    testFixturesImplementation("org.testcontainers:junit-jupiter")
}
