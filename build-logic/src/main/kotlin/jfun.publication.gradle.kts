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
    id("jfun.common-conventions")
    `maven-publish`
    signing
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            java {
                withJavadocJar()
                withSourcesJar()
            }

            groupId = project.group.toString()
            artifactId = project.name

            from(components["java"])

            pom {
                name.set(project.name)
                url.set("https://github.com/Siroshun09/jfun")
                description.set("A library to assist functional programming in Java.")

                licenses {
                    license {
                        name.set("APACHE LICENSE, VERSION 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }

                developers {
                    developer {
                        name.set("Siroshun09")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/Siroshun09/jfun.git")
                    developerConnection.set("scm:git@github.com:Siroshun09/jfun.git")
                    url.set("https://github.com/Siroshun09/jfun")
                }

                issueManagement {
                    system.set("GitHub Issues")
                    url.set("https://github.com/Siroshun09/jfun/issues")
                }

                ciManagement {
                    system.set("GitHub Actions")
                    url.set("https://github.com/Siroshun09/jfun/runs")
                }
            }
        }

        repositories {
            maven {
                url = uri(rootProject.layout.buildDirectory.dir("staging"))
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])
}
