package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.model.Team;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository ur;

	public User creerUtilisateur(final User utilisateur) {
		final String telephone = utilisateur.getTelephone();
		if (this.recupererUtilisateurParTel(telephone) != null)
			throw new IllegalArgumentException("Telephone déjà existant " + telephone);
		return this.ur.save(utilisateur);
	}

	public User majUtilisateur(final User utilisateur) {
		if (utilisateur.getId() == null)
			throw new IllegalArgumentException("ID obligatoire");
		return this.ur.save(utilisateur);
	}

	public User recupererUtilisateurParId(final Long id) {
		final Optional<User> optionalUtilisateur = this.ur.findById(id);
		if (optionalUtilisateur.isEmpty())
			throw new IllegalArgumentException(String.format("L'id %d ne correspond à aucun utilisateur", id));

		return optionalUtilisateur.get();
	}

	public User recupererUtilisateurParTel(final String telephone) {
		final List<User> listeUtilisateurTrouver = this.ur.findByTelephone(telephone);
		if (!listeUtilisateurTrouver.isEmpty())
			return listeUtilisateurTrouver.get(0);

		return null;
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
		final Optional<User> optionalUtilisateur = this.ur.findById(id);
		if (optionalUtilisateur.isEmpty())
			throw new IllegalArgumentException("Utilisateur non trouvée");
		final User utilisateur = optionalUtilisateur.get();

		utilisateur.setEquipe(equipe);
		return this.majUtilisateur(utilisateur);
	}

	public List<User> recupererUtilisateurEn07() {
		return this.ur.utilisateurDontTelephoneCommencePar("07");
	}

	public List<User> recupererUtilisateurEn06() {
		return this.ur.findByTelephoneStartingWith("06");
	}

}
