# Estágio 1: Build com Maven e Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copia o pom.xml e baixa as dependências (cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e gera o .jar
COPY src ./src
RUN mvn clean package -DskipTests -Dfile.encoding=UTF-8

# Estágio 2: Runtime com Java 21 (JRE leve)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia apenas o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]