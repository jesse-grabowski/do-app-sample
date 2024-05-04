FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

COPY . /workspace/app

RUN --mount=type=cache,target=/root/.m2 ./mvnw install -DskipTests -P production
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
RUN addgroup -S demo && adduser -S demo -G demo
USER demo
ENTRYPOINT ["java","-cp","app:app/lib/*","com.grabowj.dosampleapp.DoSampleAppApplication"]