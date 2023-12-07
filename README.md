# URL-Shortener with Kotlin and Spring Boot
Basic URL shortener application written in Kotlin, Spring Boot 3.2 and Maven. 

## Requirements to run the project
1. Kotlin 1.9.2
2. Maven
3. Docker
4. Docker-Compose

## How to run the project
1. The first option is to open the project in IntelliJ (or your favourite IDE) and click on the Run button. The project has `spring-boot-docker-compose` as a dependency and will run the PostgreSQL DB in the background
2. The second option is to run `docker compose up -d`. If you have Docker and Docker Compose installed, everything will be built and started automatically

# API Usage
1. Create a shortened URL by calling the POST http://localhost:8080/url/v1/shorten Endpoint with the following JSON payload: 

```json
{
    "url": "https://google.com"
}
```

You will get a response looking like this: 

```json
{
    "shortenedUrl": "c5cd9fc1"
}
```

2. You can then call the following GET endpoint in order to retrieve the original URL https://localhost:8080/url/v1/resolve/c5cd9fc1:

```json
{
    "originalUrl": "https://google.com"
}
```

Please keep in mind that the DB will be recreated on each application start. This means that the short URLs won't be persisted on application restart

## Assumptions for the URL Shortener
The URL Shortener uses the MurmurHash3 32-bit Hashing Algorithm. Since it hashes the original URL, it returns the same short URL for the same original URL every time. The output in this case is an 8 character long hexadecimal string. 

There are multiple ways to do URL shortening based on different requirements. In this case, there is no requirement for the number of short URLs that have to be stored and if the URLs should be different for different users. 
Also, there is no requirement for the length of the short URL, the time for which it is valid, click tracking, etc. This application has a basic functionality for link expiration after 30 days. 

Also, a NoSQL DB like MongoDB can also be used instead of PostreSQL in this case.