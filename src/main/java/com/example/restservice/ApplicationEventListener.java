package com.example.restservice;

import com.example.restservice.daos.RoleDAO;
import com.example.restservice.daos.UserDAO;
import com.example.restservice.model.User;
import com.example.restservice.util.DbUtil;

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
    @Autowired DbUtil dbUtil;

    @Autowired UserDAO userDao;
    @Autowired RoleDAO roleDao;

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
            /*jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS ... */

            // Do one time configuration
            jdbcTemplate.execute("PRAGMA journal_mode=WAL");

            // Try the new way with the DataSource class:
            userDao.createTable();
            long id = dbUtil.get_new_id("users");
            if (id <= 0) {  // rowId star
                User myUser = new User();
                myUser.setId(1);
                myUser.setUsername("fabrice");
                myUser.setHashpass("$2a$10$Rz0aFUvNc9DmX9Bxh75MUu9VCFJz4EzmyN/IRINKix9qw0IF/b/OW");  //1234
                userDao.add(myUser);     
            }            

            roleDao.createTable();
            
        }

        logger.info("####  onApplicationReadyEvent END");
    }   
}
