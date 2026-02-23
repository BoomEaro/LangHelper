plugins {
    id("maven-publish")
}

dependencies {
    compileOnlyApi("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = "ru.boomearo"
            artifactId = "langhelper-api"
            from(components["java"])
        }
    }
}
