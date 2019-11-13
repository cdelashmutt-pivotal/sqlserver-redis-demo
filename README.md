# Microsoft SQL Server with Redis in Spring Boot
This is an example project to show how to use a set of SQL Server credentials stored in CredHub and a bound Redis service instance from a Redis Service Broker in a Spring Boot Application.

## Overview of the Example
This example uses simple Spring Boot project using Spring Data JDBC and Spring Data Redis along with the Java CF Env library to ease configuration of connections to Redis and a JDBC Data source backed by Microsoft SQL Server.

Refer to the `build.gradle` file for the dependencies needed.  Specifically, the `java-cfenv-boot` library used to translate the data from the bound services in Cloud Foundry into the Spring Boot properties for automatically creating beans for working with SQL Server and Redis.

Also, this project includes an application configuration file that is activated under the "local" Spring profile.  You can include configuration to connect to an SQL Server and Redis server from your local machine.  Then you can then set the profile by directly invoking the built Spring Boot JAR by calling `java -jar build/libs/sqlserver-redis-demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=local` or by passing the arguments through the Gradle `bootRun` task via the following command `./gradlew bootRun -Pargs="--spring.profiles.active=local".

## Building
From the command line in the root of the project, execute the following:

```./gradlew assemble```

## Creating services
This project relies on two services to be created.  One service that represents a Redis server instance (called `redis`) and one respresenting an SQL Server (called `sql-server`).  These services must be present before pushing with those names, or you will need to modify the `manifest.yml` file to rename those references to the names you are using for your services.

### Creating a Redis Service
If you are using Pivotal Redis, refer to https://docs.pivotal.io/redis/2-2/using.html#create.  If you are using Redis Labs, refer to https://docs.pivotal.io/partners/redis-labs-enterprise-pack/using.html#using-topic-1.  Refer to your service provider on the process for creating instances of Redis.

### Creating a Microsoft SQL Server Service
There are a variety of methods to create an Microsoft SQL Server service instance.

#### User Provided Service
Execute the following to create an SQL Server service instance using a User Provided Service, replacing the values surrounded by the greater-than and lesser-than symbols (`<` and `>`).

---

**NOTE**

Using a User Provided Service exposes sensitive credentials to anyone who has rights to view your application environment via the Cloud Foundry Cloud Controller.  Only use this method for local testing with disposable credentials.  See a better method below using CredHub for secure credential storage and transmission to the application.

---

##### With Powershell
```
cf create-user-provided-service sql-server -p '{\"jdbcUrl\":\"jdbc:sqlserver://<sql-server-host>:<sql-server-port>;database=<database-name>\",\"username\":\"sa\",\"password\":\"password\"}'
```

#### CredHub Service Broker
Execute the following to create an SQL Server service instance using a CredHub Service Broker, replacing the values surrounded by the greater-than and lesser-than symbols (`<` and `>`).

##### With Powershell
```
cf create-service credhub default sql-server -c '{\"jdbcUrl\":\"jdbc:sqlserver://<sql-server-host>:<sql-server-port>;database=<database-name>\",\"username\":\"sa\",\"password\":\"password\"}'
```

## Deploying to Cloud Foundry
To deploy to Cloud Foundry, execute the following from the root of the project.

```
cf push
```

## Testing Connectivity
You can make a request to the root context of the application to get a list of Redis client connections and a list of user tables from the SQL Server database specified in the service binding.