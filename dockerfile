# Usa la imagen oficial de Java 21
FROM openjdk:21-jdk

# Copia el jar compilado al contenedor
COPY target/assistants-0.0.1-SNAPSHOT.jar app.jar

# Comando para ejecutar el jar
ENTRYPOINT ["java", "-jar", "app.jar"]