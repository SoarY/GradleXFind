package com.soarsy.gradlefind

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logging
import java.nio.file.Files
import java.util.jar.JarFile

/**
 * NAME：YONG_
 * Created at: 2025/9/3 17
 * Describe:
 */
class DependenciesPlugin: Plugin<Project> {

    private val TAG=DependenciesPlugin::class.java.simpleName

    private val logger = Logging.getLogger(TAG)

    override fun apply(project: Project) {

        val extension = project.extensions.create(DependenciesPluginExtension.NAME_SPACE, DependenciesPluginExtension::class.java)

        project.afterEvaluate {

            if (extension.printDependencies) {
                logger.lifecycle("$TAG Dependencies printing>>>>>>>>")
                doPrintDependencies(project)
            }else{
                logger.lifecycle("$TAG Dependencies Close printing>>>>>>>>")
            }

            if (extension.analysisSo){
                logger.lifecycle("$TAG so print>>>>>>>>")
                doAnalysisSo(project)
            }

            if (!extension.permissionsToRemove.isEmpty()){
                logger.lifecycle("$TAG permissionsToRemove>>>>>>>>")
                permissionsToRemove(project, extension.permissionsToRemove)
            }
        }

    }

    private fun doPrintDependencies(project: Project) {
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        appExtension.applicationVariants.all {
            logger.lifecycle("applicationVariants=${it.name}")

            val androidLibs: ArrayList<String> = ArrayList()
            val otherLibs: ArrayList<String> = ArrayList()

            val configuration = project.configurations.getByName(it.name + "CompileClasspath")
            configuration.resolvedConfiguration.lenientConfiguration.allModuleDependencies.forEach {it1->
                val identifier = it1.module.id
                if (identifier.getGroup().contains("androidx") || identifier.getGroup()
                        .contains("com.google") || identifier.getGroup().contains("org.jetbrains")
                ) {
                    androidLibs.add(identifier.getGroup() + ":" + identifier.getName() + ":" + identifier.getVersion());
                } else {
                    otherLibs.add(identifier.getGroup() + ":" + identifier.getName() + ":" + identifier.getVersion());
                }
            }

            logger.lifecycle("--------------官方库 start--------------")
            androidLibs.forEach {
                logger.lifecycle(it)
            }
            logger.lifecycle("--------------官方库 end--------------")

            logger.lifecycle("--------------三方库 start--------------")
            otherLibs.forEach {
                logger.lifecycle(it)
            }
            logger.lifecycle("--------------三方库 end--------------")
        }
    }

    private fun doAnalysisSo(project: Project) {
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        appExtension.applicationVariants.all {applicationVariant->
            logger.lifecycle(TAG + "applicationVariant.getName() = " + applicationVariant.getName())

            val configuration = project.configurations.getByName(applicationVariant.name + "CompileClasspath")
            
            configuration.forEach {
                val fineName = it.name
                logger.lifecycle(TAG + "fine name = " + fineName)

                if (fineName.endsWith(".jar") || fineName.endsWith(".aar")) {

                    val result = runCatching {
                        JarFile(it)
                    }.onSuccess { response ->
                    }.onFailure { exception ->
                        exception.printStackTrace()
                        return@all
                    }
                    val jarFile = result.getOrNull()
                    for (entry in jarFile!!.entries()) {
                        if (entry.getName().endsWith(".so")) {
                            logger.lifecycle(TAG + "----- so name = " + entry.getName())
                        }
                    }
                }
            }
        }
    }

    private fun permissionsToRemove(project: Project, permissionsToRemove: List<String>) {
        if (permissionsToRemove.isEmpty())
            return
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        appExtension.applicationVariants.all {
            it.outputs.all {output->
                output.processManifest.doLast {
                    runCatching {
                        val manifestFile = output.processResourcesProvider.get().manifestFile
                        var manifestContent = Files.readString(manifestFile.toPath())
                        permissionsToRemove.forEach {item->
                            logger.lifecycle(TAG + "permission = " + item)
                            manifestContent=manifestContent.replace(item,"android.permission.NULL_PERMISSION")
                        }
                        Files.writeString(manifestFile.toPath(),manifestContent)
                    }.onSuccess { response ->
                    }.onFailure { exception ->
                        exception.printStackTrace()
                        return@doLast
                    }
                }
            }
        }

    }

}