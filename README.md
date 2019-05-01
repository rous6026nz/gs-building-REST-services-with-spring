# gs-building-REST-services-with-spring

A small learning by coding project based off the Spring.io tutorial [Building REST services with Spring](https://spring.io/guides/tutorials/rest/).

## Introduction

This project contains 2 implementations to help demonstrate the key differences between an [RPC (Remote Procedure Call)](https://en.wikipedia.org/wiki/Remote_procedure_call) `nonrest` - and a RESTful service `restful`.

The RESTful implementation also contains a feature branch `feature/restful--with-resource-assembler` which illustrates the process of implementing an assembler class and refactoring the current implementation of a RESTful service to use a resource assembler. This branch also demonstrates how to upgrade a RESTful service, while maintaining backwards compatibility with all clients.

>I am getting frustrated by the number of people calling any HTTP-based interface a REST API. Today’s example is the SocialSite REST API. That is RPC. It screams RPC. There is so much coupling on display that it should be given an X rating.
>
>What needs to be done to make the REST architectural style clear on the notion that hypertext is a constraint? In other words, if the engine of application state (and hence the API) is not being driven by hypertext, then it cannot be RESTful and cannot be a REST API. Period. Is there some broken manual somewhere that needs to be fixed?
>_~Roy Fielding_

## Development Environment

This project uses Java, the Spring Framework and Maven as the build tool.

## Interrogating The Service

To explore the services simply `cd` into the service folder you wish to interrogate `nonrest` or `rest`, then run the application `mvn clean spring-boot:run`.

To interrogate the services, you can use an API development tool such as [Postman](https://www.getpostman.com/) and explore the service by navigating to `http://localhost:8080/employees`.

Alternatively, you can use a command line tool called [CURL](https://curl.haxx.se/) to interact with the services. The following commands can be used to make GET, POST, PUT and DELETE HTTP operations with our services:

- GET
  - Get all records: `curl -v localhost:8080/employees`
  - Get record by Id: `curl -v localhost:8080/employees/{id}`

- POST
  - `curl -X POST localhost:8080/employees -H 'Content-type:application/json' -d '{"name": "Joe Blog", "role": "Bus Driver"}'`

- PUT
  - `curl -X PUT localhost:8080/employees/{id} -H 'Content-type:application/json' -d '{"name": "Joe Blog", "role": "Bus Inspector"}'`

- DELETE
  - `curl -X DELETE localhost:8080/employees/{id}`