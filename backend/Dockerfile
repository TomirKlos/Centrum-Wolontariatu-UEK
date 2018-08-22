# Start with a base image containing Java runtime
FROM openjdk:10

# Add Maintainer Info
LABEL maintainer="callicoder@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/centrumWolontariatu-1.0.jar

# Add the application's jar to the container
ADD ${JAR_FILE} websocket-demo.jar

# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/websocket-demo.jar"]
