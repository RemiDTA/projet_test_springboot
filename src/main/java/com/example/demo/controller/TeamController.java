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

import com.example.demo.model.Team;
import com.example.demo.service.TeamService;

@RestController
@RequestMapping("/team")
public class TeamController {

	@Autowired
	TeamService ts;

	@PostMapping
	public void creerEquipe(@RequestBody final Team equipe) {
		this.ts.creerEquipe(equipe);
	}

	@PatchMapping
	public void majEquipe(@RequestBody final Team equipe) {
		this.ts.majEquipe(equipe);
	}

	@GetMapping
	public List<Team> listerEquipes() {
		return this.ts.recupererToutesEquipes();
	}

	@GetMapping("/{id}")
	public Optional<Team> getTeamById(@PathVariable final Long id) {
		return this.ts.recupererTeamParId(id);
	}

}
