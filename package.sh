mvn clean package
#java -jar target/posts-1.0-SNAPSHOT.jar
docker build -t cleptes/users:0.02 .
docker stop users
docker rm users
docker run -d --name users -p 8090:8090 cleptes/users:0.02

#docker run -d --name postgres-users -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=users -p 5440:5432 postgres:latest