FROM eclipse-temurin:19.0.1_10-jdk-ubi9-minimal as build
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src src
RUN ./mvnw -B package
RUN ./mvnw install dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/lib

FROM eclipse-temurin:19.0.1_10-jdk-ubi9-minimal
RUN mkdir /opt/app
COPY --from=build target/microhttprealworld-1.0-SNAPSHOT.jar /
RUN mkdir /libs
COPY --from=build target/libs/* /libs/
CMD ["java", "--class-path", "/opt/libs/:/opt/app/microhttprealworld-1.0-SNAPSHOT.jar",  "dev.mccue.realworld.Main"]