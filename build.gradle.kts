plugins {
    kotlin("jvm") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "7.1.1"
    id("maven-publish")
}

group = "me.oddlyoko"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly("cn.powernukkitx:powernukkitx:1.20.40-r1")

    // For yaml files
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.0")

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.mockito:mockito-core:4.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:4.11.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://repo1.maven.org/maven2/")
    maven("https://jitpack.io")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.oddlyoko.tycoon"
            artifactId = "tycoon"
            version = "1.0"

            from(components["java"])
        }
    }
}

tasks.named("shadowJar", com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    mergeServiceFiles()
}

tasks.test {
    useJUnitPlatform()
}
