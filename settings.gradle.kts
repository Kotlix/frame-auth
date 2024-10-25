rootProject.name = "frame-auth"

pluginManagement {
    plugins {
        val jvmPluginVersion: String by settings
        val springBootVersion: String by settings
        val springDependencyManagementVersion: String by settings

        kotlin("jvm") version jvmPluginVersion
        kotlin("plugin.spring") version jvmPluginVersion
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
    }

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include("frame-auth-api")
include("frame-auth-client")
include("frame-auth-client-starter")
include("frame-auth-server")