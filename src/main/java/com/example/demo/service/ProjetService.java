package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Projet;
import com.example.demo.model.Team;
import com.example.demo.model.User;
import com.example.demo.repository.ProjetRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ProjetService {

	@Autowired
	ProjetRepository pr;

	@Autowired
	TeamService ts;

	@Autowired
	UserRepository ur;

	public Projet recupererProjetParId(final long id) {
		return this.pr.findById(id).orElseThrow(() -> new IllegalArgumentException("Le projet n'existe pas " + id));
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
		Optional.ofNullable(utilisateursActuels).ifPresentOrElse(
				listeUtilisateurCourant -> {
					listeUtilisateurCourant.addAll(listeNouveauxCollaborateurs);
				},
				() -> projet.setCollaborateurs(listeNouveauxCollaborateurs));

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
		final Projet projet = this.recupererProjetParId(idProjet);

		final List<User> listeMembreEquipeMerger = this.recupererMembreEquipeNonPresentProjet(projet.getCollaborateurs(), equipe.getUsers());
		return this.ajouterCollaborateurs(projet, listeMembreEquipeMerger);
	}

	/**
	 * Permet de récuperer les utilisateurs présent dans la listeMembreEquipe qui n'est pas présente dans la listeCollaborateurProjet
	 *
	 * @param listeCollaborateurProjet
	 * @param listeMembreEquipe
	 * @return
	 */
	private List<User> recupererMembreEquipeNonPresentProjet(final List<User> listeCollaborateurProjet, final List<User> listeMembreEquipe) {
		final List<User> resultat = new ArrayList<User>();
		listeMembreEquipe.stream().forEach(membreEquipe -> {
			if (!listeCollaborateurProjet.stream().anyMatch(collaborateur -> collaborateur.getId() == membreEquipe.getId())) {
				resultat.add(membreEquipe);
			}
		});
		return resultat;
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

	public List<User> recupererCollaborateurProjet(final long id) {
		return this.ur.findByProjets(this.recupererProjetParId(id));
	}

	public void supprimerTousProjets() {
		this.pr.deleteAll();
	}

	/**
	 * Suppression d'un projet
	 *
	 * @param id
	 */
	public void supprimerProjet(final Long id) {
		this.pr.deleteById(id);
	}

}
