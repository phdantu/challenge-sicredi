Technical Challenge
=========

This is my sollution for the technical challenge. Using Java and SpringBoot, for develop an API for Vote Session. The goal for this code is to be as clean as possible, based on my knowlegde for the moment, but always have space for improvements.

---
## Instalation

- To run the application you will need docker-compose to run the RabbitMQ and the Database.
- Java 11 installed on your machine.
- You will need to enter in [this address](localhost:15672) login with `guest/guest` credentials and create a Queue named `votingResult` before starting the application for the first time. 

>Informations about database connectivity will be on the default `application.yml`.


## Documentation

- http://server:port/api/challenge-service/swagger-ui.html

## Versioning

- The actual version of the API will be on the context-path, that will be changed only when is a big change that changes some features or the entire lifecycle for the API.
- I will use the Semantic Versioning, where will be, after release 1.0. The Major and Minor version. Major changing as it was said above. And minor for new features or bugfixes and improvements, that not change the actual state of the API.


## Repository
- This repository will follow the best of the GitFlow Model to organize this repository in the clearer e simpler way possible.


## Exceptions

- For the time beign will be used the default exceptions handler and response from springboot framework because the application in the actual state doesn't need big customizations on that matter.