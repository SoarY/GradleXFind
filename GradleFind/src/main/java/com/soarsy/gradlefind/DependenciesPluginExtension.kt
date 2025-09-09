package com.soarsy.gradlefind
import org.gradle.api.provider.Property
/**
 * NAME：YONG_
 * Created at: 2025/9/5 15
 * Describe:
 */
interface DependenciesPluginExtension {
    fun getEnable():Property<Boolean>
}