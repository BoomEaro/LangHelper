dependencies {
    compileOnly(project(":common"))
    compileOnly("org.spigotmc:spigot:1.21.1-R0.1-SNAPSHOT")
}

val javaVersion = 21

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}