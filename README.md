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

To explore the services make sure you are in the right branch - `master` for `nonrest` or `feature/restful` for `restful`. Then `cd` into the service folder you wish to interrogate `nonrest` or `restful`, and run the application `mvn clean spring-boot:run`.

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

## API Response Model

The following documents how the expected responses for each service call should look. I have broken this section into Non-RESTful and RESTful to help illustrate the key differences between the two.

### Non-RESTful Service

GET: Non-RESTful response retreiving all Employee records `http://localhost:8080/employees`

_HTTP Response_: 200

```javascript
[
  {
    "id": 1,
    "name": "Bilbo Baggins",
    "role": "burglar"
  },
  {
    "id": 2,
    "name": "Frodo Baggins",
    "role": "thief"
  }
]
```

GET: Non-RESTful response retreiving an Employee record by ID `http://localhost:8080/employees/1`

_HTTP Response_: 200

```javascript
{
  "id": 1,
  "name": "Bilbo Baggins",
  "role": "burglar"
}
```

POST: Non-RESTful response adding a new Employee record `curl -X POST localhost:8080/employees -H 'Content-type:application/json' -d '{"name": "Zaine Kingi", "role": "Space Ranger"}'`

_HTTP Response_: 200

```javascript
{
  "id":3,
  "name":"Zaine Kingi",
  "role":"Space Ranger"
}
```

PUT: Non-RESTful response replacing an Employee record `curl -X PUT localhost:8080/employees/3 -H 'Content-type:application/json' -d '{"name": "Buzz Lightyear", "role": "Space Ranger"}'`

_HTTP Response_: 200

```javascript
{
  "id":3,
  "name":"Buzz Lightyear",
  "role":"Space Ranger"
}
```

DELETE: Non-RESTful response deleting an Employee record `curl -X DELETE localhost:8080/employees/3`

_HTTP Response_: 200

```javascript

```

---

### RESTful Service

GET: RESTful response retreiving all Employee records `http://localhost:8080/employees`

_HTTP Response_: 201

```javascript
{
  "_embedded": {
    "employeeList": [
      {
        "id": 1,
        "name": "Bilbo Baggins",
        "role": "burglar",
        "_links": {
          "self": {
            "href": "http://localhost:8080/employees/1"
          },
          "employees": {
            "href": "http://localhost:8080/employees"
          }
        }
      },
      {
        "id": 2,
        "name": "Frodo Baggins",
        "role": "thief",
        "_links": {
          "self": {
            "href": "http://localhost:8080/employees/2"
          },
          "employees": {
            "href": "http://localhost:8080/employees"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/employees"
    }
  }
}
```

GET: RESTful response retreiving an Employee record by ID `http://localhost:8080/employees/1`

_HTTP Response_: 201

```javascript
{
  "id": 1,
  "name": "Bilbo Baggins",
  "role": "burglar",
  "_links": {
    "self": {
      "href": "http://localhost:8080/employees/1"
    },
    "employees": {
      "href": "http://localhost:8080/employees"
    }
  }
}
```

POST: RESTful response adding a new Employee record `curl -X POST localhost:8080/employees -H 'Content-type:application/json' -d '{"name": "Zaine Kingi", "role": "Space Ranger"}'`

_HTTP Response_: 201

```javascript
{
  "id": 3,
  "name": "Zaine Kingi",
  "role": "Space Ranger",
  "_links": {
    "self": {
      "href": "http://localhost:8080/employees/3"
    },
    "employees": {
      "href": "http://localhost:8080/employees"
    }
  }
}
```

PUT: RESTful response replacing an Employee record `curl -X PUT localhost:8080/employees/3 -H 'Content-type:application/json' -d '{"name": "Buzz Lightyear", "role": "Space Ranger"}'`

_HTTP Response_: 201

```javascript
{
  "id": 3,
  "name": "Buzz Lightyear",
  "role": "Space Ranger",
  "_links": {
    "self": {
      "href": "http://localhost:8080/employees/3"
    },
    "employees": {
      "href": "http://localhost:8080/employees"
    }
  }
}
```

DELETE: RESTful response deleting an Employee record `curl -X DELETE localhost:8080/employees/3`

_HTTP Response_: 204

```javascript

```