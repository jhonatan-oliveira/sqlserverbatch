package br.com.jhonatan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * A sample Spring Boot application that starts the Camel routes.
 */
@SpringBootApplication
@PropertySource("classpath:application.properties")
public class SqlServerBatchApplication {

    /**
     * A main method to start this application.
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
    	SpringApplication.run(SqlServerBatchApplication.class, args);
    }

}