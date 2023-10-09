package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Projet;
import com.example.demo.model.Team;
import com.example.demo.model.User;
import com.example.demo.repository.ProjetRepository;

@Service
public class ProjetService {

	@Autowired
	ProjetRepository pr;

	@Autowired
	TeamService ts;

	public Projet recupererProjetParId(final long id) {
		final Optional<Projet> optionalProjet = this.pr.findById(id);
		if (optionalProjet.isEmpty())
			throw new IllegalArgumentException("Le projet n'existe pas " + id);

		return optionalProjet.get();
	}

	public List<Projet> listerProjets() {
		return this.pr.findAll();
	}

	/**
	 * Permet d'ajouter un ensemble de collaborateurs à un projet
	 *
	 * @param id
	 * @param listeNouveauxCollaborateurs
	 * @return
	 */
	public Projet ajouterCollaborateurs(final Long id, final List<User> listeNouveauxCollaborateurs) {

		final Projet projet = this.recupererProjetParId(id);
		return this.ajouterCollaborateurs(projet, listeNouveauxCollaborateurs);
	}

	/**
	 * Permet d'ajouter un ensemble de collaborateurs à un projet
	 *
	 * @param id
	 * @param listeNouveauxCollaborateurs
	 * @return
	 */
	public Projet ajouterCollaborateurs(final Projet projet, final List<User> listeNouveauxCollaborateurs) {
		final List<User> utilisateursActuels = projet.getCollaborateurs();
		if (utilisateursActuels != null) {
			utilisateursActuels.addAll(listeNouveauxCollaborateurs);
		} else {
			projet.setCollaborateurs(listeNouveauxCollaborateurs);
		}

		return this.pr.save(projet);
	}

	public Projet creerProjet(final Projet projet) {
		return this.pr.save(projet);
	}

	/**
	 * Ajoute tous les membres d'une équipes dans un projet
	 *
	 * @param idProjet
	 * @param idEquipe
	 * @return
	 */
	public Projet ajouterCollaborateursEquipe(final Long idProjet, final Long idEquipe) {
		final Team equipe = this.ts.recupererEquipeParId(idEquipe);
		return this.ajouterCollaborateurs(this.recupererProjetParId(idProjet), equipe.getUsers());
	}

	/**
	 * Ajoute tous les membres d'une équipes dans un projet
	 *
	 * @param idProjet
	 * @param idEquipe
	 * @return
	 */
	public Projet ajouterCollaborateursEquipe(final Projet projet, final Team equipe) {
		return this.ajouterCollaborateurs(projet, equipe.getUsers());
	}

}
