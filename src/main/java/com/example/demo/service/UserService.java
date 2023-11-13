package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.model.Projet;
import com.example.demo.model.Team;
import com.example.demo.model.User;
import com.example.demo.repository.ProjetRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.EncodeurConfig;

@Service
public class UserService {

	@Autowired
	private UserRepository ur;

	@Autowired
	private ProjetRepository pr;

	@Autowired
	EncodeurConfig encodeur;

	public User creerUtilisateur(final User utilisateur) {
		final String telephone = utilisateur.getTelephone();
		Optional.ofNullable(this.recupererUtilisateurParTel(telephone)).ifPresent(user -> {
			throw new IllegalArgumentException("Telephone déjà existant");
		});
		utilisateur.setMotPasse(this.encodeur.getEncodeur().encode(utilisateur.getMotPasse()));
		return this.ur.save(utilisateur);
	}

	/**
	 * Permet de merger les informations de l'utilisateur appelant avec les données en base
	 *
	 * @param utilisateur
	 * @return
	 */
	public User majUtilisateur(final User utilisateur) {

		Optional.ofNullable(utilisateur.getId()).orElseThrow(() -> new IllegalArgumentException("ID obligatoire"));
		final User utilisateurBdd = this.recupererUtilisateurParId(utilisateur.getId());

		Optional.ofNullable(utilisateur.getMotPasse()).ifPresent(mdp -> utilisateurBdd.setMotPasse(this.encodeur.getEncodeur().encode(mdp)));
		Optional.ofNullable(utilisateur.getEmail()).ifPresent(email -> utilisateurBdd.setEmail(email));
		Optional.ofNullable(utilisateur.getNom()).ifPresent(nom -> utilisateurBdd.setNom(nom));
		Optional.ofNullable(utilisateur.getPrenom()).ifPresent(prenom -> utilisateurBdd.setPrenom(prenom));
		Optional.ofNullable(utilisateur.getTelephone()).ifPresent(telephone -> utilisateurBdd.setTelephone(telephone));
		return this.ur.save(utilisateurBdd);
	}

	public User recupererUtilisateurParId(final Long id) {
		return Optional.ofNullable(this.ur.findById(id)).orElseThrow(() -> new IllegalArgumentException(String.format("L'id %d ne correspond à aucun utilisateur", id))).get();
	}

	public User recupererUtilisateurParTel(final String telephone) {
		return this.ur.findByTelephone(telephone);
	}

	/**
	 * On pourrait se contenter d'utiliser le deleteById, cependant si l'utilisateur n'existe pas, le plantage serait silencieux
	 * La suppression est possible sous les conditions suivantes :
	 * L'utilisateur n'est chef d'aucune équipe
	 *
	 * @param utilisateur
	 */
	public void supprimerUtilisateur(final Long id) {
		// Le code suivant n'est pas utile car la contrainte de clef étrangère tel que défini au niveau du modèle vérifie déjà ce cas
		// final List<Team> equipeResponsable = utilisateur.getEquipeResponsable();
		// if (equipeResponsable != null && !equipeResponsable.isEmpty())
		// throw new IllegalArgumentException("Un utilisateur qui est chef d'équipe ne peux être supprimer");
		final User utilisateur = this.recupererUtilisateurParId(id);
		this.ur.delete(utilisateur);
	}

	public void supprimerTousUtilisateurs() {
		this.ur.deleteAll();
	}

	public List<User> recupererTousUtilisateurs() {
		return this.ur.findAll();
	}

	/**
	 * Idem que recupererTousUtilisateurs mais avec gestion de paginsation
	 *
	 * @param debut
	 * @param taille
	 * @return
	 */
	public List<User> recupererTousUtilisateursAvecPagination(final int debut, final int taille) {
		final Pageable pageable = PageRequest.of(debut, taille, Sort.by("id"));
		final Page<User> retour = this.ur.findAll(pageable);
		return retour.getContent();
	}

	/**
	 * Ajoute l'utilisateur à une équipe
	 *
	 * @param equipe
	 * @param id
	 * @return
	 */
	public User associerEquipe(final Team equipe, final long id) {
		// Lambda : Si findById retourne null je throw dans le orElseThrow sinon je retourne la valeur du Optional (le User)
		// La map permet de réaliser pour la valeur du Optional le traitement désiré, s'il n'y a pas de valeur, retourne un empty Optional (qui sera traité par le orElseThrow)
		return this.ur.findById(id).map(utilisateur -> {
			utilisateur.setEquipe(equipe);
			return this.majUtilisateur(utilisateur);
		}).orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvée"));
	}

	public List<User> recupererUtilisateurEn07() {
		return this.ur.utilisateurDontTelephoneCommencePar("07");
	}

	public List<User> recupererUtilisateurEn06() {
		return this.ur.findByTelephoneStartingWith("06");
	}

	public List<Projet> recupererProjetsUtilisateur(final long idUtilisateur) {
		return this.pr.findByCollaborateurs(this.recupererUtilisateurParId(idUtilisateur));
	}

	/**
	 * Permet de récupérer l'équipe de l'utilisateur dont l'id est passé en paramètre
	 *
	 * @param idUtilisateur
	 * @return
	 */
	public Team recupererEquipeUtilisateur(final Long idUtilisateur) {
		final User utilisateur = this.recupererUtilisateurParId(idUtilisateur);
		return utilisateur.getEquipe();
	}

}
