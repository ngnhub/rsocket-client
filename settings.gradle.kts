include("web")

rootProject.name = "rsocket_client"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            plugin("kotlin", "org.jetbrains.kotlin.jvm").version("1.9.22")
            plugin("spring-boot", "org.springframework.boot").version("3.2.2")
            plugin("spring-dependency", "io.spring.dependency-management").version("1.1.4")
            plugin("kotlin-spring", "org.jetbrains.kotlin.plugin.spring").version("1.9.22")
        }
    }
}