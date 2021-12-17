package com.example.restservice;

import com.example.restservice.daos.UserDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventListener {
 
    private static Log logger = LogFactory.getLog(RestServiceApplication.class.getName());

    public static int counter;

    @Autowired JdbcTemplate jdbcTemplate;

    @Autowired UserDAO userDao;

    @Value("${usesqlite}")
    Boolean usesqlite;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyEvent() {
        logger.info("#### onApplicationReadyEvent BEGIN");
        counter++;
        
        if (usesqlite) {
            logger.info("#### Using Sqlite");
            /*
            there are 2 ways to connect to database thru JDBC: with DriverManager class (old, not recommended) and with DataSource class.
            */
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (" +
            "    id INT PRIMARY KEY     NOT NULL," +
            "    username           TEXT    NOT NULL," +
            "    hashpass TEXT    NOT NULL" +
            ")");

            // Try the new way with the DataSource class:
            userDao.createTable();
        }

        logger.info("####  onApplicationReadyEvent END");
    }   
}
