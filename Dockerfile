FROM openjdk:17
COPY ./target/demo-capstone-project.jar demo-capstone-project.jar
CMD ["java","-jar","demo-capstone-project.jar"]