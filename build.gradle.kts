import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins

plugins {
    kotlin("jvm") version "1.4.10"
    java
    application
    idea
    id("com.google.protobuf") version "0.8.15"
}

group = "org.example"
version = "1.0-SNAPSHOT"
val protobuVersion = "3.6.1"
val grpcVersion = "1.36.0"

application {
    mainClass.set("com.snow.rpc.MainKt")
}

idea {
    module{
        sourceDirs.plusAssign(file("${projectDir}/build/generated/source/proto/main/java"))
        sourceDirs.plusAssign(file("${projectDir}/build/generated/source/proto/main/grpc"))
    }
}

sourceSets {
    main {
        java {
            srcDirs.plusAssign(file("build/generated/source/proto/main/grpc"))
            srcDirs.plusAssign(file("build/generated/source/proto/main/java"))
        }
    }
}

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.15")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.api.grpc:proto-google-common-protos:2.1.0")
    implementation("io.grpc:grpc-netty:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
}

tasks.withType<KotlinCompile>{
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget= "11"
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobuVersion"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.36.0"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}