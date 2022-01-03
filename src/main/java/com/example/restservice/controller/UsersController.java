package com.example.restservice.controller;

import java.util.List;

import com.example.restservice.model.User;
import com.example.restservice.model.UserFromDB;
import com.example.restservice.service.UserService;
import com.example.restservice.singleton.UsersSingleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
@RequestMapping("/api")
public class UsersController {

	private Object mutex = new Object();

	@Autowired
	UsersSingleton us;

	@Autowired
	UserService userService;

	@GetMapping("/users")
	public List<UserFromDB> getUsers() {	  
	  return us.users;
	}

	@PostMapping("/users")
	@ResponseStatus(code = HttpStatus.CREATED)	
	public User addUser(@RequestBody User user)
	{
		// Make sure one and only one request can mutate the data at a time (sqlite)
		synchronized (mutex) {
			return userService.addUser(user);
		}
	}

}
