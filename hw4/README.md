### steps

0. start infrastructure
```
docker-compose up -d
```

1. create user and database
```mysql-sql
docker exec -i searchads_mysql_1 mysql --password=mypass < ./src/main/resources/script/setup.sql
```

2. mvn build
```
mvn clean install
```

3. run FakeCrawler to publish ads data into rabbitmq
```
mvn exec:java -Dexec.mainClass=demo.FakeCrawler
```

4. run IndexServer
```
mvn exec:java -Dexec.mainClass=demo.IndexServer
```

5. run Jetty and AdServer
```
mvn jetty:run
```

6. search: http://127.0.0.1:8080/SearchAds?q=razor

7. stop infrastructure
```
docker-compose stop
docker-compose rm
```
