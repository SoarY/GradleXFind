pluginManagement {
    repositories {
        //maven("./GradleFind/build/maven-repo")
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/public")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/public")
        google()
        mavenCentral()
    }
}

rootProject.name = "GradleXFind"
include(":app")
include(":GradleFind")
