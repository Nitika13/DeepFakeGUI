FROM openjdk:17

WORKDIR /app

COPY DeepfakeDetectorGUI.java /app/

RUN javac DeepfakeDetectorGUI.java

CMD ["java", "DeepfakeDetectorGUI"]
