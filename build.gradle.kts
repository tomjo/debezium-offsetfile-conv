plugins {
	java
	alias(libs.plugins.quarkus)
	alias(libs.plugins.owasp)
}

val projectVersion: String by project

group = "net.tomjo"
version = projectVersion

tasks.wrapper {
	distributionType = Wrapper.DistributionType.ALL
	gradleVersion = "8.4"
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
	annotationProcessor(enforcedPlatform(libs.quarkus.bom))
	implementation(enforcedPlatform(libs.quarkus.bom))
    implementation(libs.quarkus.container.image.docker)
	implementation(libs.quarkus.arc)
	implementation(libs.quarkus.picocli)
	implementation(libs.kafka.connect.runtime){
		isTransitive = false
	}
	testImplementation(libs.quarkus.junit5)
	testImplementation(libs.bundles.test.implementation)
}

tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
	options.compilerArgs.add("-parameters")
}

dependencyCheck {
	format = org.owasp.dependencycheck.reporting.ReportGenerator.Format.ALL.toString()
}

tasks.withType<AbstractArchiveTask>().configureEach {
	isPreserveFileTimestamps = false
	isReproducibleFileOrder = true
}
