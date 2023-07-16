import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.xcore"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.4.1"
val junitJupiterVersion = "5.9.1"

val mainVerticleName = "com.xcore.MainVerticle"
val launcherClassName = "com.xcore.XLauncher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-core")
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-health-check")
  implementation("io.vertx:vertx-web-openapi")
  implementation("io.vertx:vertx-auth-otp")
  implementation("io.vertx:vertx-grpc-server")
  implementation("io.vertx:vertx-hazelcast")
  implementation("io.vertx:vertx-service-discovery")
  implementation("io.vertx:vertx-camel-bridge")
  implementation("io.vertx:vertx-auth-oauth2")
  implementation("io.vertx:vertx-stomp")
  implementation("io.vertx:vertx-tcp-eventbus-bridge")
  implementation("io.vertx:vertx-reactive-streams")
  implementation("io.vertx:vertx-grpc-client")
  implementation("io.vertx:vertx-auth-shiro")
  implementation("io.vertx:vertx-service-factory")
  implementation("io.vertx:vertx-pg-client")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
  implementation("io.vertx:vertx-web-sstore-redis")
  implementation("io.vertx:vertx-mongo-client")
  implementation("io.vertx:vertx-web-validation")
  implementation("io.vertx:vertx-auth-ldap")
  implementation("io.vertx:vertx-web-templ-thymeleaf")
  implementation("io.vertx:vertx-auth-jwt")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-http-service-factory")
  implementation("io.vertx:vertx-json-schema")
  implementation("io.vertx:vertx-micrometer-metrics")
  implementation("io.vertx:vertx-shell")
  implementation("io.vertx:vertx-web-api-contract")
  implementation("io.vertx:vertx-uri-template")
  implementation("io.vertx:vertx-rx-java3")
  implementation("io.vertx:vertx-redis-client")
  implementation("io.vertx:vertx-mqtt")
  implementation("io.vertx:vertx-config")
  implementation("io.vertx:vertx-web-graphql")
  implementation("io.vertx:vertx-cassandra-client")
  implementation("io.vertx:vertx-circuit-breaker")
  implementation("io.vertx:vertx-mail-client")
  implementation("io.vertx:vertx-rabbitmq-client")
  implementation("io.vertx:vertx-kafka-client")
  implementation("com.ongres.scram:client:2.1")
  implementation("eu.infomas:annotation-detector:3.0.5")
  // https://mvnrepository.com/artifact/io.vertx/vertx-auth-jwt
  implementation("io.vertx:vertx-auth-jwt:4.4.1")
  implementation("io.netty:netty-resolver-dns-native-macos:4.1.74.Final")

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  compileOnly ("org.projectlombok:lombok:1.18.26")
  annotationProcessor ("org.projectlombok:lombok:1.18.26")
  testCompileOnly ("org.projectlombok:lombok:1.18.26")
  testAnnotationProcessor ("org.projectlombok:lombok:1.18.26")
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
