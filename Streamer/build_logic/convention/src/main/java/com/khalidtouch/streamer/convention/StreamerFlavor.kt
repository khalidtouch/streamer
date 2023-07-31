package com.khalidtouch.streamer.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import org.gradle.api.Project

@Suppress("EnumEntryName")
enum class FlavorDimension { contentType }


enum class StreamerFlavor(
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null
) {
    demo(FlavorDimension.contentType, applicationIdSuffix = ".demo"),
    prod(FlavorDimension.contentType)
}

fun Project.configureFlavors(
    commonExtension: CommonExtension<*,*,*,*>,
    flavorConfigurationBlock: ProductFlavor.(flavor: StreamerFlavor) -> Unit = {}
) {
    commonExtension.apply {
        flavorDimensions += FlavorDimension.contentType.name
        productFlavors {
            StreamerFlavor.values().forEach {
                create(it.name) {
                    dimension = it.dimension.name
                    flavorConfigurationBlock(this, it)
                    if(this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if(it.applicationIdSuffix != null) {
                            this.applicationIdSuffix = it.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}