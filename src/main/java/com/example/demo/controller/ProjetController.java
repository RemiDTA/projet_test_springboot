package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Projet;
import com.example.demo.model.User;
import com.example.demo.service.ProjetService;

@RestController
@RequestMapping("/projet")
public class ProjetController {

	@Autowired
	ProjetService ps;

	@GetMapping
	public List<Projet> listerProjets() {
		return this.ps.listerProjets();
	}

	@GetMapping("/{id}")
	public Projet recupererProjetParId(@PathVariable final Long id) {
		return this.ps.recupererProjetParId(id);
	}

	@PostMapping("/{id}/ajouterCollaborateurs")
	public Projet ajouterCollaborateurs(@PathVariable final Long id, @RequestBody final List<User> listeUtilisateurs) {
		return this.ps.ajouterCollaborateurs(id, listeUtilisateurs);
	}

	@PostMapping("/{idProjet}/ajouterCollaborateursEquipe/{idEquipe}")
	public Projet ajouterCollaborateursEquipe(@PathVariable final Long idProjet, @PathVariable final Long idEquipe) {
		return this.ps.ajouterCollaborateursEquipe(idProjet, idEquipe);
	}

	@PostMapping
	public Projet creerProjet(@RequestBody final Projet projet) {
		return this.ps.creerProjet(projet);
	}

	@GetMapping("/{id}/collaborateurs")
	public List<User> recupererCollaborateurProjet(@PathVariable final long id) {
		return this.ps.recupererCollaborateurProjet(id);
	}
}
