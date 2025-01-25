import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    api(project(":frame-auth-api"))

    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
