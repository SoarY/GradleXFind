package com.soarsy.gradlefind

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.internal.file.impl.DefaultFileMetadata.file
import java.util.jar.JarFile




/**
 * NAME：YONG_
 * Created at: 2025/9/3 17
 * Describe:
 */
class DependenciesPlugin: Plugin<Project> {

    private val TAG="DependenciesPlugin"

    override fun apply(project: Project) {
         val logger = project.getLogger()

        val extension = project.extensions.create("printDependencies", DependenciesPluginExtension::class.java)
        extension.getEnable().convention(false)

        project.afterEvaluate {
            logger.quiet("$TAG so print>>>>>>>>")
            doAnalysisSo(project)

            logger.quiet("$TAG>>>>>>>>")
            if (!extension.getEnable().get())
                return@afterEvaluate

            val appExtension = project.extensions.getByType(AppExtension::class.java)
            appExtension.applicationVariants.all {itExtension->
                val androidLibs: ArrayList<String> = ArrayList()
                val otherLibs: ArrayList<String> = ArrayList()
                processVariant(logger, itExtension, project, androidLibs, otherLibs)

                logger.quiet("--------------官方库 start--------------")
                androidLibs.forEach {
                    logger.quiet(it)
                }
                logger.quiet("--------------官方库 end--------------")

                logger.quiet("--------------三方库 start--------------")
                otherLibs.forEach {
                    logger.quiet(it)
                }
                logger.quiet("--------------三方库 end--------------")
            }

        }

    }

    private fun doAnalysisSo(project: Project) {
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        appExtension.applicationVariants.all {applicationVariant->
            System.out.println(TAG + "applicationVariant.getName() = " + applicationVariant.getName())

            val configuration = project.configurations.getByName(applicationVariant.name + "CompileClasspath")
            configuration.forEach {
                val fineName = it.name
                System.out.println(TAG + "fine name = " + fineName)

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
                            System.out.println(TAG + "----- so name = " + entry.getName())
                        }
                    }
                }
            }
        }
    }

    private fun processVariant(
        logger: Logger,
        it: ApplicationVariant,
        project: Project,
        androidLibs: ArrayList<String>,
        otherLibs: ArrayList<String>
    ) {
        logger.quiet("applicationVariants=${it.name}")

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
    }
}