package com.example.restservice.service;

import java.util.ArrayList;

import com.example.restservice.singleton.UsersSingleton;
import com.example.restservice.daos.UserDAO;
import com.example.restservice.model.UserFromDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	UsersSingleton us;

	@Value("${usesqlite}")
	Boolean usesqlite;

	@Autowired
	UserDAO userDAO;

	@Override
	public UserDetails loadUserByUsername(String aUsername) throws UsernameNotFoundException {
		UserFromDB userFromDB = null;
		com.example.restservice.model.User user = null;
		UserDetails newUser = null;

		if (!usesqlite) {
			userFromDB = us.users.stream().filter(u -> u.getUsername().equals(aUsername)).findFirst().orElse(null);
		} else {
			//user = userDAO.findUserByName(aUsername);
			//user = userDAO.findUserByNameWithParamsAndMapper(aUsername);
			user = userDAO.findUserByNameWithBeanPropertyRowMapper(aUsername);
			
		}

		if (!usesqlite) {
			if (userFromDB != null) {
				newUser = new User(userFromDB.getUsername(), userFromDB.getHashpass(), new ArrayList<>());
			} else {
				throw new UsernameNotFoundException("User not found with username: " + aUsername);
			}
		} else {
			if (user != null) {
				newUser = new User(user.getUsername(), user.getHashpass(), new ArrayList<>());
			} else {
				throw new UsernameNotFoundException("User not found with username: " + aUsername);
			}
		}
		return newUser;
	}

}