FROM tomcat:8-jre8
VOLUME /tmp
ADD build/libs/workspace.jar build/libs/app.jar
RUN sh -c 'touch build/libs/app.jar'
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "build/libs/app.jar" ]
