plugins {
    id("java")
}

group = "org.example"
version = "1.0.0"

dependencies {
    implementation("com.example:javaLibB:1.0.0")
}
tasks.register("resolve") {
    val files = configurations.runtimeClasspath.get().incoming.files
    doFirst {
        files.forEach { println(it.absolutePath) }
    }
}

val projectModules = mapOf(
    "com.example:javaLibA" to ":javaLibA"
)

configurations.configureEach {
    resolutionStrategy.dependencySubstitution.apply {
        all {
            val requested = requested
            if (requested is ModuleComponentSelector) {
                val module = requested.group + ":" + requested.module
                if (projectModules.containsKey(module)) {
                    artifactSelection {
                        if (hasSelectors())
                            println("HAS SELECTORS $module ${requestedSelectors.joinToString(", ") { it.type } }")
                    }
                    artifactSelection {
                        withoutArtifactSelectors()
                    }
                    useTarget(project(projectModules[module]!!))
                }
            }
        }
    }
}


