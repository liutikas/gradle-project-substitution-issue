plugins {
    id("java")
    id("maven-publish")
}

group = "org.example"
version = "1.0.0"

dependencies {
    implementation(project(":javaLibA"))
}

publishing {
    repositories {
        maven {
            url = uri(File(rootDir, "repo"))
        }
    }
    publications {
        register<MavenPublication>("release") {
            groupId = "com.example"
            artifactId = "javaLibB"
            version = "1.0.0"

            from(components["java"])

        }
    }
}