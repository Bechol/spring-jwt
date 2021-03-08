# Spring Secuity JWT token demo

### Stack

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.1/maven-plugin/reference/html/)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#using-boot-devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#boot-features-security)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Flyway Migration](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#howto-execute-flyway-database-migrations-on-startup)

### Docker support

For building ONLY application image run in cmd or bash:

    docker build -f ./docker/Dockerfile -t bechol/spring-jwt-demo .
    
For building whole project create .env file with environment variables and then run in cmd or bash:

    docker-compose -f ./docker/docker-compose.yml --env-file ./docker/env-config.env up


