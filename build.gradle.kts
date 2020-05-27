plugins {
    `maven-publish`
    kotlin("jvm") version "1.3.72"
    id("org.jetbrains.dokka") version "0.10.1"
}

group = "com.lolapi"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.12")
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

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    from(tasks.dokka)
}




publishing {
    publications {
	create<MavenPublication>("default") {
	    from(components["java"])	
            artifact(dokkaJar)	
	}
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/mckernant1/LolEsportsApiWrapper")
            credentials.username = System.getenv("GPR_USER")
            credentials.password = System.getenv("GPR_API_KEY")
        }
    }
}
