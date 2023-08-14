plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("org.springframework.boot:spring-boot-starter-parent:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.1.4")
    implementation("org.springframework.security:spring-security-crypto:6.1.0")
    implementation("org.springframework.boot:spring-boot-starter-security:3.1.0")
    testImplementation("org.springframework.security:spring-security-test:6.1.0")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    compileOnly("org.projectlombok:lombok:1.18.26")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
    implementation("org.modelmapper:modelmapper:3.1.1")
    implementation("javax.validation:validation-api:2.0.1.Final")
}

tasks.test {
    useJUnitPlatform()
}