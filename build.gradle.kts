plugins {
    id("java-library")
    id("maven-publish")
    id("com.gradleup.shadow") version "8.3.9"
}

allprojects {
    group = "ru.boomearo"
    version = "1.5.18"
}

subprojects {
    apply(plugin = "java")

    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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

publishing {
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

dependencies {
    implementation(project(":plugin"))
    implementation(project(":common"))
    implementation(project(":nms:1_12_R1"))
    implementation(project(":nms:1_13_R2"))
    implementation(project(":nms:1_14_R1"))
    implementation(project(":nms:1_15_R1"))
    implementation(project(":nms:1_16_R3"))
    implementation(project(":nms:1_17_R1"))
    implementation(project(":nms:1_18_R2"))
    implementation(project(":nms:1_19_R3"))
    implementation(project(":nms:1_20_R1"))
    implementation(project(":nms:1_20_R2"))
    implementation(project(":nms:1_20_R3"))
    implementation(project(":nms:1_20_R4"))
    implementation(project(":nms:1_21_R1"))
    implementation(project(":nms:1_21_R2"))
    implementation(project(":nms:1_21_R3"))
    implementation(project(":nms:1_21_R4"))
    implementation(project(":nms:1_21_R5"))
    implementation(project(":nms:1_21_R6"))
}

tasks.shadowJar {
    archiveBaseName.set("LangHelper")
    archiveClassifier.set("")
}

tasks.build {
    dependsOn("shadowJar")
}