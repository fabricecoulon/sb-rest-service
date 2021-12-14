package com.example.restservice.controller;

import java.util.List;

import com.example.restservice.model.UserFromDB;
import com.example.restservice.singleton.UsersSingleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
@RequestMapping("/api")
public class UsersController {

	@Autowired
	UsersSingleton us;

	@GetMapping("/users")
	public List<UserFromDB> getUsers() {	  
	  return us.users;
	}
}
