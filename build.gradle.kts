import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    val kotlin = "2.1.0"
    val springBoot = "3.4.0"
    val springDependencyManagement = "1.1.6"

    kotlin("jvm") version kotlin
    kotlin("plugin.serialization") version kotlin
    kotlin("plugin.spring") version kotlin
    id("org.springframework.boot") version springBoot
    id("io.spring.dependency-management") version springDependencyManagement
}

repositories {
    mavenCentral()
}

group = "ru.jetlabs"

dependencies {
    val exposed = "0.56.0"

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposed")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed")
    runtimeOnly("org.postgresql:postgresql")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
    jvmToolchain(21)
}

dependencyManagement {
    val springCloud = "2024.0.0"

    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloud")
    }
}