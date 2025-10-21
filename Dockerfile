FROM eclipse-temurin:21-jdk AS builder
WORKDIR /build
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jdk AS runner
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh
EXPOSE 8080
ENTRYPOINT ["./wait-for-it.sh", "db:5432", "--", "java", "-jar", "/app/app.jar"]
