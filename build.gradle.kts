import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
}

repositories {
    mavenCentral()
}

group = "com.github.ngnhub.rsocket_ui"
version = "1.0"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
    }

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "21"
        }
    }

    // disables creation of plain jar
    tasks.getByName<Jar>("jar") {
        enabled = false
    }

    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            events("passed", "failed")
        }
    }
}
