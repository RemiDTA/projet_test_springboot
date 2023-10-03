package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	/**
	 * Retourne les équipes en filtrant par emplacement si spécifié, sinon retourne
	 * l'ensemble des équipes
	 *
	 * exemple : localhost:8080/team?emplacement=PARIS
	 *
	 * @param emplacement
	 * @return
	 */
	@GetMapping
	public List<Team> listerEquipes(@RequestParam(name = "emplacement", required = false) final String emplacement) {
		// Si on a un paramètre emplacement de renseigner, on retourne ces equipes
		if (emplacement != null)
			return this.ts.recupererEquipeParEmplacement(emplacement);
		// Sinon par défaut on les retourne toutes
		return this.ts.recupererToutesEquipes();
	}

	@GetMapping("/{id}")
	public Optional<Team> recupererEquipeParId(@PathVariable final Long id) {
		return this.ts.recupererEquipeParId(id);
	}

	@DeleteMapping("/{id}")
	public void supprimerEquipe(@PathVariable final Long id) {
		this.ts.supprimerEquipe(id);
	}

}
