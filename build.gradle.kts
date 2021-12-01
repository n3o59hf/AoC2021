
plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
}

application {
    mainClass.set("lv.n3o.aoc2021.Main")
}