FROM maven:3.8.1-openjdk-17-slim AS builder
WORKDIR /app
RUN apt update -y && apt install git unzip -y
ADD "https://api.github.com/repos/Weebs-Kingdom/New-Yuki-Sora/commits?per_page=1" latest_commit
RUN curl -sL "https://github.com/Weebs-Kingdom/New-Yuki-Sora/archive/main.zip" -o yuki.zip && unzip yuki.zip
WORKDIR ./New-Yuki-Sora-main
RUN mvn dependency:resolve
RUN mvn clean compile package -DskipTests
RUN mv ./target/YukiSora*.jar ./target/bot.jar

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/New-Yuki-Sora-main/target/bot.jar /app/bot.jar
COPY --from=builder /app/New-Yuki-Sora-main/pom.xml /app/pom.xml
ENTRYPOINT ["java", "-jar", "/app/bot.jar", "start", "docker"]
