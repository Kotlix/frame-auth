import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
    id("org.springframework.boot") apply false
    id("org.jlleitschuh.gradle.ktlint") apply false
    id("io.spring.dependency-management")
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.springframework.boot")
        plugin( "io.spring.dependency-management")
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    val springBootVersion: String by project
    val springCloudVersion: String by project

    val groupId: String by project
    val versionIdNumber: String by project
    val versionIdStatus: String by project

    group = groupId
    val versionId: String = if (versionIdStatus.isEmpty()) versionIdNumber else "$versionIdNumber-$versionIdStatus"
    version = versionId

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}