## Messaging app

This is an application for learning how to work with `Kafka` and `Vaadin` gluing them with `Spring Boot`. 
It consists of two modules - front-end and back-end.

### Setting up and starting the application locally

To run the application execute
```bash
docker-compose up
```
This will start the `PostgreSQL` database and `Zookeeper` with `Kafka`. 
To set up the `message` table an SQL script `./sql-scripts/createMessagesTable.sql` can be used.

Both modules can be started with the `com.axinalis.messages.App.main()` method.

The alternative option of running the `front` part is with help of `maven`
```bash
cd ./messages-front/
mvn spring-boot:run
```

### Building the application

To build it execute
```bash
mvn clean package -Pproduction
```

The desired jars will be in the `./module-name/target` directory.