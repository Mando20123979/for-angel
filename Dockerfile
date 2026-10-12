FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY Main.java index.html ./
CMD ["java", "Main.java"]
