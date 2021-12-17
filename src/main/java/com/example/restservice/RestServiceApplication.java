package com.example.restservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestServiceApplication {

    private static Log logger = LogFactory.getLog(RestServiceApplication.class.getName());

    public static void main(String[] args) {  
        logger.debug("In main()");      
        SpringApplication.run(RestServiceApplication.class, args);
    }


}
