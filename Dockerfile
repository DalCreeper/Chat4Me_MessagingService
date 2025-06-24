# Buildo l'app con JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build

#Creo la directory principale
WORKDIR /app

# Copio i file di configurazione Maven e scarico le dipendenze prima del codice
COPY pom.xml .
COPY src ./src

# Compilo l'applicazione e genero il jar skippando i test
RUN mvn clean package -DskipTests

# Specifico come avviare l'app
FROM eclipse-temurin:21-jre

#Creo la directory principale
WORKDIR /app

# Copio solo il jar generato
COPY --from=build /app/target/Chat4Me_Messaging_Service-0.0.1-SNAPSHOT.jar app.jar

# Espongo la porta usata dal mio servizio
EXPOSE 8082

# Avvio l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]