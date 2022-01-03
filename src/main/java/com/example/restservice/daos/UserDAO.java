package com.example.restservice.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.restservice.model.User;
import com.example.restservice.util.ConnectionUtil;
import com.example.restservice.util.DbUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setHashpass(rs.getString("hashpass"));
        return user;
    }
    
}

@Component
public class UserDAO /*extends DaoItf*/ {

    public static final String USERS_TABLE = "users";

    private static Log logger = LogFactory.getLog(UserDAO.class.getName());

    @Autowired ConnectionUtil connUtil;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //@Autowired
    //private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbUtil dbUtil;
    
    public void createTable() {
        // try-with-resources Statement  Any object that implements java.lang.AutoCloseable can be used as a resource
        // it will automatically do a finally { resource.close(); }
        //try (Connection con = ConnectionUtil.getConnection()) {
        try (Connection con = connUtil.getConnection()) {
            String createQuery = "CREATE TABLE IF NOT EXISTS users (" +
            "    id INTEGER PRIMARY KEY NOT NULL," +
            "    username           TEXT    NOT NULL," +
            "   hashpass            TEXT    NOT NULL" +
            ")";
            PreparedStatement pstmt = con.prepareStatement(createQuery);

            pstmt.execute();
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
        // finally resource.close() not needed

    }

    public User add(User user) {
        User newUser = new User(user.getUsername(), user.getHashpass());
        if (user.getId() <= 0) {
            long newId = dbUtil.get_new_id(UserDAO.USERS_TABLE);
            newUser.setId(newId);
        } else {
            newUser.setId(user.getId());
        }
        try (Connection con = connUtil.getConnection()) {
            String insertQuery = "INSERT INTO users(id, username, hashpass) VALUES(?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(insertQuery);
            pstmt.setLong(1, newUser.getId());
            pstmt.setString(2, newUser.getUsername());
            pstmt.setString(3, newUser.getHashpass());
            pstmt.executeUpdate();
        } catch (SQLException exc) {
            logger.error(exc.getMessage());            
            return newUser;
        }
        return newUser;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection con = connUtil.getConnection()) {
            String query = "SELECT * FROM users";
            PreparedStatement pstmt = con.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                users.add(user);
            }
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }

        return users;
    }

    public User findUserByName(String username) {
        User user = null;
        try (Connection con = connUtil.getConnection()) {
            String query = "SELECT id, username, hashpass FROM users WHERE username = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setHashpass(rs.getString("hashpass"));
            }
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
        return user;
    }

    public User findUserByNameWithParamsAndMapper(String username) {
        User user = null;
        try (Connection con = connUtil.getConnection()) {
            String query = "SELECT id, username, hashpass FROM users WHERE username = :username";
            Map<String, String> parameters = new HashMap<>();
            parameters.put("username", username);
            RowMapper<User> myRowMapper = new UserMapper();
            user = (User) namedParameterJdbcTemplate.queryForObject(query, parameters, myRowMapper);
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
        return user;
    }

    // Third way with the BeanPropertyRowMapper
    public User findUserByNameWithBeanPropertyRowMapper(String username) {
        User user = null;
        try (Connection con = connUtil.getConnection()) {
            String query = "SELECT id, username, hashpass FROM users WHERE username = :username";
            BeanPropertyRowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class);
            Map<String, String> parameters = new HashMap<>();
            parameters.put("username", username);
            user = (User) namedParameterJdbcTemplate.queryForObject(query, parameters, rowMapper);            
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
        return user;
    }


}
