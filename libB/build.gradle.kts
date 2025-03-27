import com.android.utils.childrenIterator
import com.android.utils.forEach

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.example.libb"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri(File(rootDir, "repo"))
        }
    }
    publications {
        register<MavenPublication>("release") {
            groupId = "com.external"
            artifactId = "libB"
            version = "1.0"

            afterEvaluate {
                from(components["release"])
            }
            pom.withXml {
                fun org.w3c.dom.Node.find(predicate: (org.w3c.dom.Node) -> Boolean): org.w3c.dom.Node? {
                    val iterator = childrenIterator()
                    while (iterator.hasNext()) {
                        val node = iterator.next()
                        if (predicate(node)) {
                            return node
                        }
                    }
                    return null
                }

                fun org.w3c.dom.Node.appendElement(
                    tagName: String,
                    textValue: String? = null
                ): org.w3c.dom.Element {
                    val element = ownerDocument.createElement(tagName)
                    appendChild(element)

                    if (textValue != null) {
                        val textNode = ownerDocument.createTextNode(textValue)
                        element.appendChild(textNode)
                    }

                    return element
                }

                val xmlElement = this.asElement()
                val dependencies = xmlElement.find { it.nodeName == "dependencies" } as? org.w3c.dom.Element
                dependencies?.getElementsByTagName("dependency")?.forEach { dependency ->
                    val groupId =
                        dependency.find { it.nodeName == "groupId" }?.textContent
                            ?: throw IllegalArgumentException("Failed to locate groupId node")
                    val artifactId =
                        dependency.find { it.nodeName == "artifactId" }?.textContent
                            ?: throw IllegalArgumentException("Failed to locate artifactId node")
                    if ("$groupId:$artifactId" == "com.example:libA") {
                        dependency.appendElement("type", "aar")
                    }
                }
            }
        }
    }
}

tasks.withType<GenerateModuleMetadata>() {
    enabled = false
}

dependencies {
    api(project(":libA"))
}