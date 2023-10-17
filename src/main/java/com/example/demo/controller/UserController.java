package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.configuration.ProprieteLimite;
import com.example.demo.model.Projet;
import com.example.demo.model.Team;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.UserUtil;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService us;

	@Autowired
	ProprieteLimite propLimite;

	@PostMapping
	public User creerUtilisateur(@RequestBody final User utilisateur) {
		if (utilisateur.getMotPasse() == null || utilisateur.getMotPasse().isEmpty())
			UserUtil.appliquerMdpParDefaut(utilisateur);
		return this.us.creerUtilisateur(utilisateur);
	}

	/**
	 * @param utilisateur
	 * @param id          PathVariable pour respecter les bonnes pratiques REST, néanmoins l'ID étant présent dans le body et n'est pas utilisé par le service
	 * @return
	 */
	@PatchMapping("/{id}")
	public User majUtilisateur(@RequestBody final User utilisateur, @PathVariable final long id) {
		return this.us.majUtilisateur(utilisateur);
	}

	@GetMapping
	public List<User> listerUtilisateurs(@RequestParam(name = "pageDebut", required = false) final Integer pageDebut) {
		// Si non spécifié on retourne tout
		if (pageDebut == null)
			return this.us.recupererTousUtilisateurs();
		// Si spécifié on fait de la pagination
		return this.us.recupererTousUtilisateursAvecPagination(pageDebut, this.propLimite.getTaillePagination());
	}

	@GetMapping("/{id}")
	public User recupererUtilisateurParId(@PathVariable final Long id) {
		return this.us.recupererUtilisateurParId(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> supprimerUtilisateur(@PathVariable final Long id) {
		this.us.supprimerUtilisateur(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("associer_equipe/{id}")
	public User associerEquipe(@RequestBody final Team equipe, @PathVariable final long id) {
		return this.us.associerEquipe(equipe, id);
	}

	@GetMapping("/telephone/06")
	public List<User> recupererUtilisateurEn06() {
		return this.us.recupererUtilisateurEn06();
	}

	@GetMapping("/telephone/07")
	public List<User> recupererUtilisateurEn07() {
		return this.us.recupererUtilisateurEn07();
	}

	@GetMapping("{id}/projets")
	public List<Projet> recupererProjetsUtilisateur(@PathVariable final Long id) {
		return this.us.recupererProjetsUtilisateur(id);
	}
}
