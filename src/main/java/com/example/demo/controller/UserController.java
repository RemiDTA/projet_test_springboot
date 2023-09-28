package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userServ;

	@PostMapping
	public void creerUser(@RequestBody User utilisateur) {
		userServ.creerUser(utilisateur);
	}

	@PatchMapping
	public void majUser(@RequestBody User utilisateur) {
		userServ.majUser(utilisateur);
	}

	@GetMapping
	public List<User> listerUsers() {
		return userServ.findAll();
	}

	@GetMapping("/{id}")
	public Optional<User> getUserById(@PathVariable Long id) {
		return userServ.getUserById(id);
	}
}
