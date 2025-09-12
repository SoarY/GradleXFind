package com.soarsy.gradlefind


/**
 * GitHub : https://github.com/yechaoa
 * CSDN : http://blog.csdn.net/yechaoa
 *
 *
 * Created by yechao on 2023/10/7.
 * Describe : Plugin Extension Params
 */
open class DependenciesPluginExtension {
    /**
     * 是否打印输出所有依赖并区分二方三方，默认关闭
     */
    var printDependencies: Boolean = false

    /**
     * 是否打印输出so和依赖的关系，默认关闭
     */
    var analysisSo: Boolean = false

    /**
     * snapshot版本检查，默认关闭
     */
    var checkSnapshot: Boolean = false

    /**
     * snapshot版本检查 如有，打断编译，默认关闭
     */
    var blockSnapshot: Boolean = false

    /**
     * 需要移除的权限
     */
    var permissionsToRemove: List<String> = ArrayList()

    companion object {
        var NAME_SPACE: String = "GradleXFind"
    }
}