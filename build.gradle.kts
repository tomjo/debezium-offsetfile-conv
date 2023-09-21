plugins {
	java
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.jib)
	alias(libs.plugins.owasp)
	`maven-publish`
}

val projectVersion: String by project

group = "net.tomjo"
version = projectVersion

tasks.wrapper {
	distributionType = Wrapper.DistributionType.ALL
	gradleVersion = "8.3"
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(platform(libs.spring.boot.dependencies))
	annotationProcessor(platform(libs.spring.boot.dependencies))
	implementation(libs.spring.boot.starter.actuator)
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.webflux)
	implementation(libs.spring.boot.starter.security)
	implementation(libs.spring.boot.starter.oauth2.client)
	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)
	testImplementation(libs.spring.boot.starter.test)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

dependencyCheck {
	format = org.owasp.dependencycheck.reporting.ReportGenerator.Format.ALL.toString()
}


springBoot {
	buildInfo {
		properties {
			artifact.set(tasks.named<Jar>("bootJar").get().archiveFileName.get())
		}
	}
}


publishing {
	publications {
		create<MavenPublication>(project.name) {
			from(components["java"])
			artifactId = project.name
			artifact((project.tasks.named("bootJar").get() as AbstractArchiveTask).archiveFile.get())
		}
	}

	repositories {
		maven {
			val s3MavenRepository: String by project
			url = uri(s3MavenRepository)
			credentials(AwsCredentials::class) {
				if (project.hasProperty("s3AccessKey")) {
					val s3AccessKey: String by project
					accessKey = s3AccessKey
				}
				if (project.hasProperty("s3SecretKey")) {
					val s3SecretKey: String by project
					secretKey = s3SecretKey
				}
			}
		}
	}
}

jib {
	to {
		image = project.property("container.image") as String
		tags = setOf(project.version.toString())
	}
}

val configureS3Endpoint by tasks.registering {
	group = "publishing"
	doFirst {
		val s3EndPoint = System.getProperty("org.gradle.s3.endpoint")
		if (s3EndPoint == null) {
			System.setProperty("org.gradle.s3.endpoint", project.property("s3EndPoint") as String)
		}
	}
}

tasks.withType(PublishToMavenRepository::class) {
	dependsOn(tasks.named("bootJar").get())
	dependsOn(configureS3Endpoint)
}
