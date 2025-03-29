/*
 *    Copyright 2025 Siroshun09
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

plugins {
    id("jfun.aggregate-javadoc")
}

tasks {
    register<Delete>("clean") {
        group = "build"
        layout.buildDirectory.get().asFile.deleteRecursively()
    }
    register("createArtifactZipForMavenCentral") {
        dependsOn(
            subprojects.mapNotNull { it.tasks.findByName("publish") }
        )
        group = "publishing"

        val staging = layout.buildDirectory.get().asFile.resolve("staging")

        if (!staging.exists()) {
            error("staging directory not found")
        }

        val outputFile = layout.buildDirectory.get().asFile.resolve("artifacts-${project.name}-${project.version}.zip")
        if (outputFile.exists()) {
            outputFile.delete()
        }

        val stagingPath = staging.toPath()
        java.util.zip.ZipOutputStream(outputFile.outputStream()).use { zipOut ->
            staging.walk().forEach { file ->
                if (!file.isDirectory) {
                    val zipEntry = java.util.zip.ZipEntry(stagingPath.relativize(file.toPath()).toString())
                    zipOut.putNextEntry(zipEntry)
                    file.inputStream().use { it.copyTo(zipOut) }
                    zipOut.closeEntry()
                }
            }
        }
    }
}
