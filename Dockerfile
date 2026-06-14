#usa Java 21 como base
FROM eclipse-temurin:21-jdk-alpine

#carpeta de trabajo dentro del contenedor
WORKDIR /app

#copia el jar generado por Maven al contenedor
COPY target/usuario-0.0.1-SNAPSHOT.jar app.jar

#comando para ejecutar la aplicacion
ENTRYPOINT ["java", "-jar", "app.jar"]