dependencies {
    compileOnly(project(":common"))
    compileOnly("org.spigotmc:spigot:1.21.3-R0.1-SNAPSHOT") {
        exclude(group = "com.mojang", module = "*")
    }
}