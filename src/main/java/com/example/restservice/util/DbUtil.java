package com.example.restservice.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DbUtil {

    private static Log logger = LogFactory.getLog(DbUtil.class.getName());

    @Autowired
    ConnectionUtil connUtil;

    public Long get_new_id(String tableName) {
        Long res = -1l;

        try (Connection con = connUtil.getConnection()) {
            String query = "SELECT IFNULL(max(id)+1, 0) as id FROM " + tableName;
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                res = rs.getLong("id");
            }
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }

        return res;
    }
}
