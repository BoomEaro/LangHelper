plugins {
    id("com.gradleup.shadow") version "9.3.1"
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
    implementation(project(":common"))
}

tasks.processResources {
    val props = mapOf("version" to version)

    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("plugin.yml") {
        expand(props)
    }
}

val shadedModules = listOf(
    ":nms:1_12_R1",
    ":nms:1_13_R2",
    ":nms:1_14_R1",
    ":nms:1_15_R1",
    ":nms:1_16_R3",
    ":nms:1_17_R1",
    ":nms:1_18_R2",
    ":nms:1_19_R3",
    ":nms:1_20_R1",
    ":nms:1_20_R2",
    ":nms:1_20_R3",
    ":nms:1_20_R4",
    ":nms:1_21_R1",
    ":nms:1_21_R2",
    ":nms:1_21_R3",
    ":nms:1_21_R4",
    ":nms:1_21_R5",
    ":nms:1_21_R6",
    ":nms:1_21_R7"
)

tasks.shadowJar {
    shadedModules.forEach { path ->
        val jarTaskProvider = project(path).tasks.jar

        dependsOn(jarTaskProvider)

        from(jarTaskProvider.flatMap { jarTask -> jarTask.archiveFile.map { zipTree(it) } })
    }

    archiveBaseName.set("LangHelper")
    archiveVersion.set("")
    archiveClassifier.set("")
}

tasks.build {
    dependsOn("shadowJar")
}