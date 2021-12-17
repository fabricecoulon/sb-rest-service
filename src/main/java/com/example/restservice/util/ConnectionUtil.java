package com.example.restservice.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sqlite.SQLiteDataSource;

@Component
public class ConnectionUtil {
    private static Log logger = LogFactory.getLog(ConnectionUtil.class.getName());

    @Value("${url}")
    private String sqliteUrl;

    private static SQLiteDataSource datasource = null;

    public Connection getConnection() {
        if (datasource == null) {
            datasource = new SQLiteDataSource();
            datasource.setUrl(sqliteUrl);            
        }
        try {
            if (datasource != null) {
              return datasource.getConnection();
            }
        }
        catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
        return null;
    }

    public SQLiteDataSource getDataSource() {
        if (datasource == null) {
            datasource = new SQLiteDataSource();
            datasource.setUrl(sqliteUrl);            
        }
        return datasource;
    }
}
