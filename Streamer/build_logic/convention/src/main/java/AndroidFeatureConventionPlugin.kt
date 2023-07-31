import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("streamer.android.library")
                apply("streamer.android.hilt")
            }
//            extensions.configure<LibraryExtension> {
//                defaultConfig {
//                    testInstrumentationRunner =
//                        "com.khalidtouch.streamer.core.testing.StreamerTestRunner"
//                }
//            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("testImplementation", kotlin("test"))
                add("androidTestImplementation", kotlin("test"))

                add("implementation", libs.findLibrary("brotli").get())
                add("implementation", libs.findLibrary("exoplayer").get())
                add("implementation", libs.findLibrary("kotlin.coroutines").get())
                add("implementation", libs.findLibrary("ktor.client.core").get())
                add("implementation", libs.findLibrary("ktor.client.cio").get())
                add("implementation", libs.findLibrary("ktor.client.content.negotiation").get())
                add("implementation", libs.findLibrary("ktor.client.encoding").get())
                add("implementation", libs.findLibrary("ktor.client.serialization").get())
                add("implementation", libs.findLibrary("ktor.serialization.json").get())
                add("implementation", libs.findLibrary("palette").get())
            }
        }
    }
}