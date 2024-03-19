plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.spring)
}

// versions
val h2Version = "2.2.224"
val h2ReactiveDriverVersion = "1.0.0.RELEASE"
val mockitoKotlinVersion = "5.2.1"
val kotlinCoroutinesTestVersion = "1.8.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation(project(":domain-spring-boot-starter"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesTestVersion")
}

// disables creation of plain jar
tasks.getByName<Jar>("jar") {
    enabled = false
}
