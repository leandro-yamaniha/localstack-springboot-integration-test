FROM openjdk:11
ARG var_port=9080
ENV PORT=$var_port
ADD target/*.jar app
ENTRYPOINT ["java", "-jar","app"]
EXPOSE $PORT