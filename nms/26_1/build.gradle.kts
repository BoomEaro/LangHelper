dependencies {
    compileOnly(project(":common"))
    compileOnly("org.spigotmc:spigot:26.1-R0.1-SNAPSHOT") {
        exclude(group = "com.mojang", module = "*")
    }
}

val javaVersion = 25

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}