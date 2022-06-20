val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val resilience4jVersion: String by project
val mockk_version: String by project
val open_api_version: String by project
val snake_yaml_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.kotlinx.kover") version "0.5.0"
}

group = "com.thales.atwin.session.rtc"
version = "0.0.1"
application {
    mainClass.set("com.thales.atwin.session.rtc.ApplicationKt")
}
tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    //implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
    //implementation("io.micrometer:micrometer-registry-prometheus:latest.release")
    implementation("io.ktor:ktor-serialization:$ktor_version")
   // implementation("com.github.papsign:Ktor-OpenAPI-Generator:$open_api_version")
    //implementation("io.ktor:ktor-serialization:$ktor_version")
    //implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-mock:$ktor_version")
    implementation("io.ktor:ktor-client-gson:$ktor_version")
    implementation("io.ktor:ktor-client-logging-jvm:$ktor_version")

    //implementation("io.ktor:ktor-gson:$ktor_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.github.resilience4j:resilience4j-kotlin:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-retry:$resilience4jVersion")
    implementation("org.yaml:snakeyaml:$snake_yaml_version")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("io.mockk:mockk:$mockk_version")
    testImplementation(platform("org.junit:junit-bom:5.8.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}