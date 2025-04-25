plugins {
    id("java")
    id("maven-publish")
}

group = "org.example"
version = "1.0.0"

dependencies {
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
            artifactId = "javaLibA"
            version = "1.0.0"

            from(components["java"])

        }
    }
}

tasks.withType(Jar::class.java).configureEach {
    //this.archiveExtension.set("foo")
}