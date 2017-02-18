FROM tomcat:8-jre8
VOLUME /tmp
ADD build/libs/mitosis-spring5-app-1.0.0-alpha.0.jar build/libs/app.jar
RUN sh -c 'touch build/libs/app.jar'
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "build/libs/app.jar" ]
