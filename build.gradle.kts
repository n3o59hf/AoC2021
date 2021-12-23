plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("org.graalvm.buildtools.native") version "0.9.9"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}

application {
    mainClass.set("lv.n3o.aoc2021.Main")
}

tasks.named("run").configure {
    outputs.upToDateWhen { false }
}

if (System.getProperty("java.vm.vendor").contains("Graal")) {
    tasks.named("nativeCompile").configure {
        graalvmNative {
            binaries {
                named("main") {
                    agent {
                        enabled.set(true)
                    }
                }
            }
        }
    }
}