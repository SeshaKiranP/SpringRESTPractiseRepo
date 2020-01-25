package com.sbrapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.sbrapi.model.User;
import com.sbrapi.service.UserService;
import com.sbrapi.util.CustomError;

@RestController
@RequestMapping("/api")
public class RestApiController
{
	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	UserService userService;

	//@RequestMapping(value = "/user/", method = RequestMethod.GET)
	@GetMapping("/user/")
	public ResponseEntity<List<User>> listAllUsers()
	{
		List<User> users = userService.findAllUsers();
		if (users.isEmpty())
		{
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable("id") long id)
	{
		User user = userService.findById(id);
		if (user == null)
		{
			return new ResponseEntity(new CustomError("User with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody User user)
	{
		if (userService.isUserExist(user))
		{
			return new ResponseEntity(new CustomError("User with name " + user.getName() + " already exist."),HttpStatus.CONFLICT);
		}
		userService.saveUser(user);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user)
	{
		User currentUser = userService.findById(id);
		if (currentUser == null)
		{
			return new ResponseEntity(new CustomError("Unable to upate. User with id " + id + " not found."),HttpStatus.NOT_FOUND);
		}
		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setSalary(user.getSalary());
		userService.updateUser(currentUser);
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id)
	{
		User user = userService.findById(id);
		if (user == null)
		{
			return new ResponseEntity(new CustomError("Unable to delete. User with id " + id + " not found."),HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(id);
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/user/", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers()
	{
		userService.deleteAllUsers();
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
}