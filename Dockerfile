# Usa imagen base oficial de Java 21
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR compilado al contenedor
COPY target/assistants-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto 8080 (Render usa este puerto por defecto)
EXPOSE 8080

# Comando de entrada
ENTRYPOINT ["java", "-jar", "app.jar"]
