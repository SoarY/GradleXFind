plugins {
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm")

//    id("maven-publish")
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


//group = "com.soarsy.gradlefind"
//version = "1.2.0"
//
//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = project.group.toString()
//            artifactId = "dependencies"
//            version = project.version.toString()
//
//            from(components["java"])
//        }
//    }
//    repositories {
//        maven {
//            url = uri(layout.buildDirectory.dir("maven-repo"))
//        }
//    }
//}

