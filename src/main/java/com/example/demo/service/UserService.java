package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	public void creerUser(final User utilisateur) {
		// En théorie faudrait 2 messages
		final String telephone = utilisateur.getTelephone();
		if (this.recupererUserParTel(telephone) != null)
			throw new IllegalArgumentException("Telephone déjà existant " + telephone);
		this.userRepo.save(utilisateur);
	}

	public void majUser(final User utilisateur) {
		if (utilisateur.getId() == null)
			throw new IllegalArgumentException("ID obligatoire");
		this.userRepo.save(utilisateur);
	}

	public Optional<User> recupererUserParId(final Long id) {
		return this.userRepo.findById(id);
	}

	public User recupererUserParTel(final String telephone) {
		final List<User> listeUtilisateurTrouver = this.userRepo.findByTelephone(telephone);
		if (!listeUtilisateurTrouver.isEmpty())
			return listeUtilisateurTrouver.get(0);

		return null;
	}

	/**
	 * La suppression est possible sous les conditions suivantes :
	 * L'utilisateur n'est chef d'aucune équipe
	 *
	 * @param utilisateur
	 */
	public void supprimerUser(final Long id) {
		// Le code suivant n'est pas utile car la contrainte de clef étrangère tel que défini au niveau du modèle vérifie déjà ce cas
		// final List<Team> equipeResponsable = utilisateur.getEquipeResponsable();
		// if (equipeResponsable != null && !equipeResponsable.isEmpty())
		// throw new IllegalArgumentException("Un utilisateur qui est chef d'équipe ne peux être supprimer");
		this.userRepo.deleteById(id);
	}

	public void supprimerTousUtilisateurs() {
		this.userRepo.deleteAll();
	}

	public List<User> recupererTousUser() {
		return this.userRepo.findAll();
	}

}
