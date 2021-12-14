package com.example.restservice.controller;

import java.util.List;

import com.example.restservice.model.Entry;
import com.example.restservice.singleton.EntriesSingleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
@RequestMapping("/api")
public class EntriesController {

	@Autowired
	EntriesSingleton es;

	@GetMapping("/entries")
	public List<Entry> getEntries() {	  
	  return es.entries;
	}
}
