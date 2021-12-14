package com.example.restservice.service;

import java.util.ArrayList;

import com.example.restservice.singleton.UsersSingleton;
import com.example.restservice.model.UserFromDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	UsersSingleton us;

	@Override
	public UserDetails loadUserByUsername(String aUsername) throws UsernameNotFoundException {
		UserFromDB userFromDb = us.users.stream().filter(u -> u.getUsername().equals(aUsername)).findFirst().orElse(null);  // model.User
		/*if ("javainuse".equals(aUsername)) {
			// org.springframework.security.core.userdetails.User
			return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + aUsername);
		}*/
		UserDetails newUser = null;
		if (userFromDb != null) {
			newUser = new User(userFromDb.getUsername(), userFromDb.getHashpass(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + aUsername);
		}
		return newUser;
	}

}