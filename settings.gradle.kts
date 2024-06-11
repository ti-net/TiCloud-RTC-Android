pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://mvnrepo.jiagouyun.com/repository/maven-releases")
        }
    }
}
rootProject.name = "TiCloud-RTC-Android"
include(":public_sdk")
