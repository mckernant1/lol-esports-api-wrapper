import name.remal.gradle_plugins.dsl.extensions.convention
import name.remal.gradle_plugins.dsl.extensions.get
import name.remal.gradle_plugins.dsl.extensions.implementation
import name.remal.gradle_plugins.dsl.extensions.testImplementation
import name.remal.gradle_plugins.plugins.publish.ossrh.RepositoryHandlerOssrhExtension

plugins {
    `maven-publish`
    `java-library`
    signing
    jacoco
    kotlin("jvm") version "1.3.72"
    id("org.jetbrains.dokka") version "0.10.1"
    id("name.remal.maven-publish-ossrh") version "1.0.192"
}

group = "com.github.mckernant1"
version = "0.1.14"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.apache.httpcomponents:httpclient-cache:4.5.12")
    implementation("com.beust:klaxon:5.0.1")
    testImplementation("junit:junit:4.12")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    dokka {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.isEnabled = true
        csv.isEnabled = true
        html.destination = file("${buildDir}/jacocoHtml")
    }
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            include("**/*Client*")
        }
    )
}

jacoco {
    toolVersion = "0.8.5"
    reportsDir = file("$buildDir/customJacocoReportDir")
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    from(tasks.dokka)
}


publishing.repositories.convention[RepositoryHandlerOssrhExtension::class.java].ossrh {
    credentials.username = System.getenv("MAVEN_USERNAME")
    credentials.password = System.getenv("MAVEN_PASSWORD")
}



publishing {
    publications {
        create<MavenPublication>("default") {
            artifactId = "lol-esports-api-wrapper"
            from(components["java"])
            val sourcesJar by tasks.creating(Jar::class) {
                val sourceSets: SourceSetContainer by project
                from(sourceSets["main"].allSource)
                classifier = "sources"
            }
            artifact(dokkaJar)
            artifact(sourcesJar)
            pom {
                name.set("Lol Esports Api Wrapper")
                description.set("A wrapper for the lol esports API")
                url.set("https://github.com/mckernant1/LolEsportsApiWrapper")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("mckernant1")
                        name.set("Tom McKernan")
                        email.set("tmeaglei@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/mckernant1/LolEsportsApiWrapper.git")
                    developerConnection.set("scm:git:ssh://github.com/mckernant1/LolEsportsApiWrapper.git")
                    url.set("https://github.com/mckernant1/LolEsportsApiWrapper")
                }
            }
        }
    }
}
