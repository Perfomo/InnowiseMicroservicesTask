FROM openjdk:21

VOLUME /tmp

ARG JAR_FILE=target/config-service-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} app.jar

ENV GATEWAY_SERVICE_PORT = 8081 \
    USER_SERVICE_PORT = 8082 \
    PRODUCT_SERVICE_PORT = 8083 \
    INVENTORY_SERVICE_PORT = 8084 \
    ORDER_SERVICE_PORT = 8085 \
    CONFIG_SERVICE_PORT = 8086 \
    EUREKA_SERVICE_PORT = 8181

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
