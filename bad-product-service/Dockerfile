# ---------- build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# 先复制 pom 用于依赖缓存
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# 再复制源码并打包
COPY src ./src
RUN mvn -q -DskipTests package

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# 拷贝可执行 jar（Spring Boot repackage 后通常在 target/*.jar）
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]