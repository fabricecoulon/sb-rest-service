package com.example.restservice.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.restservice.model.Role;
import com.example.restservice.model.User;
import com.example.restservice.util.ConnectionUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleDAO {
    public static final String USER_ROLES_TABLE = "user_roles";

    private static Log logger = LogFactory.getLog(RoleDAO.class.getName());

    @Autowired ConnectionUtil connUtil;

    public void createTable() {
        try (Connection con = connUtil.getConnection()) {
            String createQuery = "CREATE TABLE IF NOT EXISTS user_roles (id INTEGER PRIMARY KEY NOT NULL, " +
                " user_id INT NOT NULL, " +
                " role INT NOT NULL)";
            PreparedStatement pstmt = con.prepareStatement(createQuery);
            pstmt.execute();            
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
    }

    public Role add(Role role) {
        Role newRole = new Role(1, 0);
        return newRole;
    }

}
