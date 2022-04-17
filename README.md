# Video rental store

## Installation requirements
Java JVM 7 and later .
It runs on its own embedded server with Jetty, no servlet container like Tomcat is needed

## API overview
- Generate rental: POST /rentals/save 
- Return a rental: GET /rentals/return 
- port is 8080  

## Project structure

It is implemented as a multi-module maven project.

## Frameworks used
- Dropwizard framework
- Shade maven plugin
- Jetty embedded server 
- Slf4j
- Java EE: JAX-RS, JSR 349 with JBoss Hybernate validator, Servlet filters
- Java SE 8
- Lombok
- Jackson for XML 
- Maven & Git
- Junit
- SonarQube and PMD

## Application design

![Alt text](https://raw.githubusercontent.com/atdance/moviesrental/master/res/MOVIE_api.jpg)

Dropwizard framework is used and the application is built as an only jar file with an embedded server Jetty.
The application was built with Eclipse , Maven and Java 7+ with the following command:
mvn package -DskipTests
Actors and user cases were identified before proceeding to design
<br/>
<br/>

![alt text](https://github.com/atdance/moviesrental/blob/master/res/movie_sequence3.jpg?raw=true)

This is a sequence diagram for the two API calls:
- Generate rental: POST /rentals/save 
- Return a rental: GET /rentals/return 

## Assumptions and limitations
- The rental basket and the returned basket consist of the same films.
- The duration of rental is the same for all movies in the basket.
- The Cart has as a fixed maximum number of titles in place.
- The Rental has a fixed maximum number of days in place.
- Errors are exposed to users if this values are exceeded.
- The maximum length of a rental is 1000 days. In the case the user presents a such return an 
error warning is emitted. No other actions like automatic warning to the user or alarm to 
administrator is previously emitted .
- Elapsed Days are represented as integers.

## Database
- The database is implemented in Ram memory and is already filled with some movies when
the application starts.
- Each rental has a unique identifier.
- With the return of a rental this identifier is not removed but is set to not recyclable.

## Exceptions
- ExceptionMapper is used to handle exceptions
- SystemException is used for weird situations that should never happen and that should be 
known by administrators and has its own mapper.
- ApiException is used mostly when a rental is created or a return is submitted and it has properties set to not allowed ranges. This exception has its own mapper.
- GlobalException mapper is used on all other cases and gives an internal server error code. 

## Security
- The API is rate limited.
- Input data is validated.
- Logged input validation failures.
- Strong typing: incoming data is strogly typed as quickly as possible.

## How to run

### If you fork from Github
The  API module is configured to create a single JAR containing the byte-code of the project
 and all of the byte-code from the dependencies.
- To create this assembly, run the package goal of the Maven Shade plugin from the API module directory:
<br> cd api
<br> mvn package
 
- Start the application the localhost/port 8080 from API directory on  with:
<br> java -jar target\API-1.0-SNAPSHOT.jar server 
- Execute the simulated workload against the application with:
<br> java -jar target\API-1.0-SNAPSHOT.jar workflow

### From zip file
Application is in a jar file in the target folder of the attached zipped file. 
<br> Run all the steps as listed in previous paragraph except the first one .