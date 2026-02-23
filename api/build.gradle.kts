plugins {
    id("maven-publish")
}

dependencies {
    compileOnlyApi("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.javadoc {
    options.apply {
        encoding = "UTF-8"
    }
}

publishing {
    repositories {
        maven {
            name = "BoomEaroRepo"
            url = uri("https://repo.boomearo.net/repository/langhelper-release/")
            credentials {
                username = project.findProperty("boomearoMavenRepoUser") as String?
                password = project.findProperty("boomearoMavenRepoPass") as String?
            }
        }
    }
    publications {
        register<MavenPublication>("maven") {
            groupId = "ru.boomearo"
            artifactId = "langhelper-api"
            from(components["java"])
        }
    }
}