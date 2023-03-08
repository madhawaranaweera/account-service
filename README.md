## Wholesale Account Service Application

This application contains get apis to provide account details and account transaction details to users.

## Api specification

```
/openapi/account_service.swagger.yml
```

## Used technologies

```
java 11
springboot
gradle
flyway
docker compose
postgres database
```

## Running the application locally

The below command can be used to bring up the local test environment.
This will bring up postgres database using docker compose.

```
./gradlew startLE or ./gradlew startLocalEnvironment
```

The below command can be used to run the application locally.
This command will bring up the application using dev profile.

```
./gradlew bootRunDev
```

Application health check url - http://localhost:8080/anz/wholesale/health

The below command can be used to bring down the local test environment

```
./gradlew stopLE or ./gradlew stopLocalEnvironment
```

## Database

Application dev profile database connection details.

```
Database: wholesale
Host: localhost
Port: 5432
Username: admin
Password: admin
```

## Data

Data will be migrated to the database in application start up using flyway scripts.

```
resources/db/migration/V1_0__create_schemas.sql
resources/db/migration/V1_1__migrate_accounts.sql
resources/db/migration/V1_2__migrate_transactions.sql
```

## Endpoints

To retrieve accounts for given user - http://localhost:8080/api/accounts/{customerId}

```
eg. http://localhost:8080/anz/wholesale/users/{user-id}/accounts
```

To retrieve account transactions for given account - http://localhost:8080/api/accounts/{accountId}/transactions

```
eg. http://localhost:8080/anz/wholesale/users/{user-id}/accounts/{account-id}/transactions?page=0&size=50
```

## Test

The below command can be used to run automated tests locally
```
./gradlew clean build test
```
To view the test reports - /build/reports/index.html

Swagger ui can be used to perform manual api testings

```
http://localhost:8080/anz/wholesale/swagger-ui
```



