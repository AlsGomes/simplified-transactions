# Simplified Transactions

## About the project
A backend project, made with Java using Spring Framework, that represents simple transactions that can be done from user to user.

The transactions before completed, are retrieving a mocked json from [https://designer.mocky.io/](https://designer.mocky.io/), as an example of an external service that authorizes the transaction.

Also, when a transaction is authorized, the user that received the money is being notified by E-mail and SMS. Both notifications are asynchronous, and will be triggered after 10 seconds.

API and Integration Tests were done and you can run all of them with 
>mvnw test

### UML Model
The model can be found at [https://snipboard.io/rf9cwO.jpg](https://snipboard.io/rf9cwO.jpg)

## How to run the Project
### With IDEA
Using an IDEA, you can just import the project as an Existing Maven Project and be sure to be running an instance of MySQL Database (installed or in a container). MySQL version to run the project correctly is 8.0.

Verify the username and password of the application.properties file. By default it's set to
* Username: root
* Password: \<empty\>

You can change it to run the project as it's configured in your environment

### With Docker
If you want to use docker, there's a docker-compose.yml file in the repository that will do the work, you just need to have two images
* [mysql:8.0](https://hub.docker.com/layers/mysql/library/mysql/8.0/images/sha256-930e762c3f185d55d96a65e0d6cb2f724ffc16a87270f85283ae099d75e92ad0?context=explore)
* [alsgomes/simplified-transactions:latest](https://hub.docker.com/r/alsgomes/simplified-transactions-api)

After pulling the images, re-tag the [alsgomes/simplified-transactions:latest](https://hub.docker.com/r/alsgomes/simplified-transactions-api) to just [simplified-transactions:latest](https://hub.docker.com/r/alsgomes/simplified-transactions-api)

Run the docker-compose file and it will be deployed by default at port 8080

## Documentation
The docs will be found at http://localhost:8080/swagger-ui/index.html