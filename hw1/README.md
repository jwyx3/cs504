# cs504-hw-1

### steps for mysql docker
1. docker run -d --name my-db -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mypass mysql:5.6.37
2. docker exec -i my-db mysql --password=mypass < ./src/main/resources/setup.sql

### build and start server
1. mvn clean install
2. java -jar target/running-information-analysis-service-1.0.0.BUILD-SNAPSHOT.jar 

### FAQ
1. If you hit Access Denied issue. You may need to modify setup.sql to have correct host.
