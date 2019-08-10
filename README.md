## Cox Automotive Programming Challenge

Using the provided APIs, this program retrieves a datasetID, retrieves all vehicles and dealers for that dataset, and successfully posts to the answer endpoint. The output will be a response structure that describes status and total elapsed time and outputs this response.

## Instructions to build, run and test the Application 

### Pre-requisites
* JDK 1.8 should be installed with the PATH set
* Latest Maven version should be installed with the PATH set

### Technology Stack used
* Development - ***Java 8, Spring boot, Junit 4, Mockito*** 

### Instructions to build and unit test the code

* ***cd to the folder where there is a pom.xml file ***

* ***Run the below-mentioned Maven command to build the code and run JUnits ***
***JUnit test results will be visible in the bash / cmd window where this command is run and a code coverage report (index.html) would be available at target/site/jacoco location*** 

$ mvn clean install

* ***cd to the "target" directory and run the below-mentioned command to start the Spring boot app***

$ java -jar cox-program-challenge-1.0.0-SNAPSHOT.jar

* This will start the Spring boot app on an embedded Tomcat server  - (Wait until you see a message similar to this - Started CoxProgrammingChallengeApplication in 9.858 seconds (JVM running for 12.968)

## Testing the App (This app will run on port 9000)

* ***Use a tool like a Browser or Postman or soapUI or CURL cli to test / run the following end points -*** 

***GET*** http://http://localhost:9000/coxchallenge/startProcess
