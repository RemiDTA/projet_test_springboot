package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Team;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository ur;

	public void creerUser(final User utilisateur) {
		// En théorie faudrait 2 messages
		final String telephone = utilisateur.getTelephone();
		if (this.recupererUserParTel(telephone) != null)
			throw new IllegalArgumentException("Telephone déjà existant " + telephone);
		this.ur.save(utilisateur);
	}

	public void majUser(final User utilisateur) {
		if (utilisateur.getId() == null)
			throw new IllegalArgumentException("ID obligatoire");
		this.ur.save(utilisateur);
	}

	public Optional<User> recupererUserParId(final Long id) {
		return this.ur.findById(id);
	}

	public User recupererUserParTel(final String telephone) {
		final List<User> listeUtilisateurTrouver = this.ur.findByTelephone(telephone);
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
		this.ur.deleteById(id);
	}

	public void supprimerTousUtilisateurs() {
		this.ur.deleteAll();
	}

	public List<User> recupererTousUser() {
		return this.ur.findAll();
	}

	/**
	 * Ajoute l'utilisateur à une équipe
	 *
	 * @param equipe
	 * @param id
	 */
	public void ajouterUtilisateur(final Team equipe, final long id) {
		final Optional<User> optionalUtilisateur = this.ur.findById(id);
		if (optionalUtilisateur.isEmpty())
			throw new IllegalArgumentException("Utilisateur non trouvée");
		final User utilisateur = optionalUtilisateur.get();

		utilisateur.setEquipe(equipe);
		this.majUser(utilisateur);
	}

}
