plugins {
    id("java")
}

allprojects {
    group = "ru.boomearo"
    version = "1.5.19"

    apply(plugin = "java")
    apply(plugin = "java-library")

    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    val javaVersion = 17

    java {
        sourceCompatibility = JavaVersion.toVersion(javaVersion)
        targetCompatibility = JavaVersion.toVersion(javaVersion)
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion))
        }
    }

    repositories {
        maven(url = "https://repo.codemc.org/repository/nms/")
        maven(url = "https://libraries.minecraft.net/")
        mavenCentral()
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.42")
        annotationProcessor("org.projectlombok:lombok:1.18.42")
    }
}