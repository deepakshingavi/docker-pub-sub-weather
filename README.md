# docker-pub-sub-weather

Setup a docker environment to 
1. confluentinc/cp-zookeeper:5.5.1 for Zookeeper
2. confluentinc/cp-kafka:5.5.1 for Kafka
3. mysql:latest for RDBS database
4. weather-publisher (Custom Kafka producer to read JSON file and push it to Kafka using Kafka producer APIs to weather topic) 
5. weather-subscriber (Custom Kafka consumer to read from weather topic and validate and parse it to Java object and insert into DB)

## Pre-requisite
1. Docker 20+
2. Docker-Composer 2.2+
3. MySQL Client compatible with Mysql 8+
4. Maven 3+

## Steps

### 1. Clone and build the project
Run the command below to clone the project:
```
git clone https://github.com/DipakPravin87/docker-pub-sub-weather.git
cd docker-pub-sub-weather
mvn clean package -DskipTests
```
### 2. Build the Kafka Producer (weather-publisher) and Kafka Consumer (weather-subscriber) Docker Images
#### weather-publisher
```
cd docker-pub-sub-weather/weather-publisher
mvn clean package docker:build -DskipTests
```
#### weather-subscriber
```
cd docker-pub-sub-weather/weather-subscriber
mvn clean package docker:build -DskipTests
```

List the Docker images and check the new images by executing:
```
docker images
```
Output should look like
```console
REPOSITORY                                     TAG                 IMAGE ID            CREATED      SIZE
com.dp.practice/weather-subscriber              1              414b3dee141f   39 minutes ago      168MB
com.dp.practice/weather-subscriber              latest         414b3dee141f   39 minutes ago      168MB
com.dp.practice/weather-publisher               1              5d4683c8bd16   4 hours ago         150MB
com.dp.practice/weather-publisher               latest         5d4683c8bd16   4 hours ago         150MB
```
### 3. Create the containers
Cmd for initialising the Docker image vai Docker composer
```
docker-compose up -d
```
Cmd for checking the Docker image init logs
```sql
docker-compose logs -f
```
cmd to check docker image statuses
```
docker-compose ps
```
### 4. Verify data in Database

Connect to Mysql docker container with password 
```shell
mysql -h 127.0.0.1 -P 3306 --protocol=tcp -u root -p weather_db
```
Verify inserted weather record count
```sql
select count(1) from weather;
#17153
#1 record skipped for incorrect timestamp format
```
Verify `currently_preciptype` data
```sql
select distinct(currently_preciptype) from weather;
```

### 5. Stopping the docker containers
Stop the docker container

To stop all containers execute:
```
docker-compose down
```

### 6. Assumptions
1. Consumer (`weather-subscriber`) will always read latest data from kafka topic. 
So due to race condition `weather-subscriber` consumer starts before `weather-pubisher` pushes the data 
hence there might be a need to re-run `weather-pubisher` explicitly to push new data.
```shell
# cmd to restart weather-publisher container 
docker restart weather-publisher
```
2. `weather-publisher` would ideally read the JSON file from some external file storage but for this POC the source JSON file is packed in the image.
3. No primary key added to weather table as I thought it was not needed particularly for this POC

### Limitations
1. Unit test cases were only written to cover the business use cases.
2. More unit test cases need to be covered
   1. Database batch insert code
   2. Kafka consumer code 
   3. Kafka producer code