plugins {
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm")
}

gradlePlugin {
    plugins {
        create("DependenciesPlugin") {
            id = "com.soarsy.gradlefind.dependencies"
            implementationClass = "com.soarsy.gradlefind.DependenciesPlugin"
        }
    }
}

dependencies {
    // 版本跟AGP版本对应
    implementation("com.android.tools.build:gradle:8.8.0")
}