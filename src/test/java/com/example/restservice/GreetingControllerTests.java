/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.restservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

class TestUser {
	public String username;
	public String password;
}

@SpringBootTest
@AutoConfigureMockMvc
public class GreetingControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private String token;

	private void setAuthorizationToken() throws Exception {
		TestUser userData = new TestUser();
		userData.username = "fabrice";
		userData.password = "1234";
		ObjectMapper mapper = new ObjectMapper();
		String userDataAsString = mapper.writeValueAsString(userData);
		// Use andReturn to block until the response is received:
		MvcResult result = this.mockMvc.perform(post("/api/authenticate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(userDataAsString))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();
		String content = result.getResponse().getContentAsString();
		JsonNode jsonNode = mapper.readTree(content);		
		this.token = jsonNode.get("token").asText();
	}

	@Test
	public void noParamGreetingShouldReturnDefaultMessage() throws Exception {
		
		this.setAuthorizationToken();

		this.mockMvc.perform( get("/api/greeting").header("authorization", "Bearer " + this.token) )
			//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("Hello, World!"))
            .andReturn();
	}

	@Test
	public void paramGreetingShouldReturnTailoredMessage() throws Exception {
		this.setAuthorizationToken();
		this.mockMvc.perform( get("/api/greeting").param("name", "Spring Community").header("authorization", "Bearer " + this.token) )				
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value("Hello, Spring Community!"));
	}

	@Test
	public void apiCallWithoutValidTokenShouldReturnWithUnauthorized() throws Exception {
		this.mockMvc.perform( get("/api/greeting") )
			.andDo(print())
			.andExpect(status().is(401)).andReturn();
	}

}
