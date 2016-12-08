# SpringRestBatch
### Spring Boot-Batch project to read Movies from [TheMovieDB] REST API.

### Tech

SpringRestBatch uses a number of open source projects:

* [Spring Boot] - Takes an opinionated view of building production-ready Spring applications. Spring Boot favors convention over configuration and is designed to get you up and running as quickly as possible.
* [Spring Data JPA] - Spring Data JPA, part of the larger Spring Data family, makes it easy to easily implement JPA based repositories.
* [Spring Batch] - A lightweight, comprehensive batch framework designed to enable the development of robust batch applications vital for the daily operations of enterprise systems.


### Installation

1. Clone this repository by running below command:
```sh
$ git clone https://github.com/RawSanj/SpringRestBatch.git
```
2. Register @ https://www.themoviedb.org/documentation/api and get your API key. Update the `REST_API_URL_WITH_KEY` key in `src/main/resources/application.properties` file with your obtained key.

3. Start your local PostgreSQL database, configure the database properties in `src/main/resources/application.properties` file.
4. Run this application using embedded Tomcat and PostgreSql DB Server: 

```sh
mvn spring-boot:run
```


### Tools

The following tools are used to create this project :

* IntelliJ IDEA
* Postman
* Maven
* Google Chrome
* Git

### License
----

The MIT License (MIT)

Copyright (c) 2016. Sanjay Rawat

[//]: #

   [Spring Boot]: <http://projects.spring.io/spring-boot/>
   [Spring Data JPA]: <http://projects.spring.io/spring-data-jpa/>
   [Spring Batch]: <http://projects.spring.io/spring-batch/>
   [TheMovieDB]: https://developers.themoviedb.org/3
