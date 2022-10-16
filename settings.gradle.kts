rootProject.name = "intellij-sarif"
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            // Gradle has to map a plugin dependency to Maven coordinates - '{groupId}:{artifactId}:{version}'.
            // It tries to do use '{plugin.id}:{plugin.id}.gradle.plugin:version'.
            // This doesn't work for jsonschema2pojo, so we provide some help
            if (requested.id.id == "jsonschema2pojo") {
                useModule("org.jsonschema2pojo:jsonschema2pojo-gradle-plugin:${requested.version}")
            }
        }
    }
}