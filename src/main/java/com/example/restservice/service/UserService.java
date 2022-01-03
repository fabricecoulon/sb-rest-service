package com.example.restservice.service;

import org.springframework.stereotype.Service;

import com.example.restservice.daos.UserDAO;
import com.example.restservice.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

	@Autowired
	UserDAO userDAO;

	@Autowired
	PasswordEncoder passwordEncoder;

    public User addUser(User user) {
        // We test receiving a user with just username and password, generate the hashpass
        user.setHashpass(passwordEncoder.encode(user.getPassword()));
        User newUser = userDAO.add(user);
        return newUser;
    }
}
