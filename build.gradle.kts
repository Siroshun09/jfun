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
    alias(libs.plugins.jcommon)
    alias(libs.plugins.aggregated.javadoc)
    alias(libs.plugins.mavenPublication)
    alias(libs.plugins.mavenCentralPortal)
}

jcommon {
    javaVersion = JavaVersion.VERSION_21

    setupJUnit(libs.junit.bom)

    commonDependencies {
        compileOnlyApi(libs.annotations)
    }
}

aggregatedJavadoc {
    modules = listOf("org.jetbrains.annotations", "org.junit.jupiter.api")
}

mavenPublication {
    localRepository(mavenCentralPortal.stagingDirectory)
    description("A library to assist functional programming in Java.")
    apacheLicense()
    developer("Siroshun09")
    github("Siroshun09/jfun")
}
