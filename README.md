# examcomposerservice

Part of a exam platform. 
Developed by: Milo tolboom, Yarince Martis, Arthur Baars and Robin Schulling.

Api service Exam Composer Service (ECS) is a Kotlin service that communicates between a database with exam files and two user interfaces. 
The two interfaces are seperate projects not hosted here. These are Teacher Environment (TE) and Student Environment (SE). 

All communication is via API Rest endpoints. The api is accessible via the swagger api specification. 
When you run the program you can find and test it at ```localhost:8082/ecs/swagger-ui.html```.

The responsibilities of the exam comoser Serivce are:

* (SE) Generating practice exams for students.
* (TE) Managing exams (CRUD)
* (TE) Managing exam questions (CRUD).
* (TE) Managing answers to an exam question (CRUD).
* (TE) Retrieving course information (CRUD)

The ECS is developed in Kotlin with small additions in Java. 
The service is developed with the Spring Boot framework to add basic commercial of the shelf functionality.
For database connections JDBC is used. 

