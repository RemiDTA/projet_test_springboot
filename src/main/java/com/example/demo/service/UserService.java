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
		if (this.getByTel(telephone) != null)
			throw new IllegalArgumentException("Telephone déjà existant " + telephone);
		this.userRepo.save(utilisateur);
	}

	public void majUser(final User utilisateur) {
		if (utilisateur.getId() == null)
			throw new IllegalArgumentException("ID obligatoire");
		this.userRepo.save(utilisateur);
	}

	public Optional<User> getUserById(final Long id) {
		return this.userRepo.findById(id);
	}

	public User getByTel(final String telephone) {
		final List<User> listeUtilisateurTrouver = this.userRepo.findByTelephone(telephone);
		if (!listeUtilisateurTrouver.isEmpty())
			return listeUtilisateurTrouver.get(0);

		return null;
	}

	public void deleteUser(final User utilisateur) {
		this.userRepo.delete(utilisateur);
	}

	public void supprimerTousUtilisateurs() {
		this.userRepo.deleteAll();
	}

	public List<User> findAll() {
		return this.userRepo.findAll();
	}

}
