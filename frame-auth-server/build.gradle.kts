dependencies {
    api(project(":frame-auth-api"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.springframework:spring-context-support")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.liquibase:liquibase-core")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    runtimeOnly("org.postgresql:postgresql")
}