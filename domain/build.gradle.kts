import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.spring)
}

// versions
val h2Version = "2.2.224"
val h2ReactiveDriverVersion = "1.0.0.RELEASE"

dependencies{
    implementation("org.springframework.boot:spring-boot-starter-rsocket")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // DB
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("com.h2database:h2:$h2Version")
    implementation("io.r2dbc:r2dbc-h2:$h2ReactiveDriverVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}
