import com.google.protobuf.gradle.id
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.23"
	id("com.google.protobuf") version "0.9.4"
	kotlin("plugin.spring") version "1.9.23"
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.yifeistudio"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	all {
		exclude("org.springframework.boot", "spring-boot-starter-tomcat")
	}
}

repositories {
	mavenCentral()
}

val mybatisVersion = "3.5.5"
val spaceVersion = "2.0.1-RELEASE"

dependencies {

	implementation("com.yifeistudio:space-unit:${spaceVersion}")
	implementation("com.baomidou:mybatis-plus-boot-starter:${mybatisVersion}") {
		exclude("org.mybatis", "mybatis-spring")
	}
	implementation("org.mybatis:mybatis-spring:3.0.3")

	// grpc
	implementation("io.grpc:grpc-kotlin-stub:1.4.1")
	implementation("io.grpc:grpc-protobuf:1.63.0")

	// grpc-server
	implementation("net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE")

	// grpc-client
	implementation("net.devh:grpc-client-spring-boot-starter:3.1.0.RELEASE")

	// nacos-discovery
	implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:2023.0.1.0")
	// nacos-config
	implementation("com.alibaba.boot:nacos-config-spring-boot-starter:0.3.0-RC")

	// springboot.
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	// kotlin.
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	// runtime.
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// Kotlin 编译配置
tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

// 测试配置
tasks.withType<Test> {
	useJUnitPlatform()
}

// 忽略测试
tasks.named<Test>("test") {
	enabled = false
}

sourceSets {
	main {
		proto {
			srcDir("src/proto")
		}
	}
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:3.6.1"
	}
	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.63.0"
		}
		id("grpc-kt") {
			artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.1:jdk8@jar"
		}
	}
	generateProtoTasks {
		ofSourceSet("main").forEach {
			it.plugins {
				id("grpc-kt") {
					option("lite")
				}
				id("grpc") { }
			}
		}
	}
}

///~