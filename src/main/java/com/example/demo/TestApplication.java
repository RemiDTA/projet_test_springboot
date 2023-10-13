package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Projet;
import com.example.demo.model.Team;
import com.example.demo.model.User;
import com.example.demo.service.ProjetService;
import com.example.demo.service.TeamService;
import com.example.demo.service.UserService;
import com.example.demo.util.UserUtil;

@SpringBootApplication
public class TestApplication implements CommandLineRunner {

	private static final String TEL_BIDON = "060101010";

	@Autowired
	UserService us;

	@Autowired
	TeamService ts;

	@Autowired
	ProjetService ps;

	public static void main(final String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

	/**
	 * Création des données de tests
	 * Transactional : Permet de faire en sorte que toute la méthode soit transactionnelle,
	 * càd que l'on execute le commit qu'à la fin de la méthode pas à chaque save
	 */
	@Override
	@Transactional(timeout = 45, propagation = Propagation.REQUIRED) // Timeout et propagation pas utile mais pour l'exemple (propagation concerne la méthode courante)
	public void run(final String... args) throws Exception {

		final List<Team> listeEquipe = this.ts.recupererToutesEquipes();
		if (listeEquipe != null) {
			for (final Team equipe : listeEquipe) {
				equipe.setChefEquipe(null);
				this.ts.majEquipe(equipe);
			}
		}
		final List<Projet> listeProjet = this.ps.listerProjets();
		if (listeProjet != null) {
			for (final Projet projet : listeProjet) {
				projet.setCollaborateurs(null);
				this.ps.creerProjet(projet);
			}
		}
		this.us.supprimerTousUtilisateurs();
		this.ts.supprimerToutesEquipes();
		this.ps.supprimerTousProjets();

		// Création de l'utilisateur admin (tester les droits)
		final User admin = new User();
		admin.setNom("admin");
		admin.setPrenom("admin");
		admin.setEmail(UserUtil.EMAIL_ADMIN);
		UserUtil.appliquerMdpParDefaut(admin);
		this.us.creerUtilisateur(admin);

		final Team equipePair = new Team();
		equipePair.setDescription("Equipe de Paris");
		equipePair.setEmplacement("PARIS");

		final Team equipeImpair = new Team();
		equipeImpair.setDescription("Equipe de Lille");
		equipeImpair.setEmplacement("LILLE");

		// Les équipes doivent au préalable être créée avant d'y associer un sous objet
		this.ts.creerEquipe(equipePair);
		this.ts.creerEquipe(equipeImpair);

		final List<User> listeUtilisateurPair = new ArrayList<>();
		final List<User> listeUtilisateurImpair = new ArrayList<>();
		// On split des users entre les 2 équipes
		for (int i = 0; i < 5; i++) {
			final User utilisateur = new User();
			utilisateur.setNom("Smith");
			utilisateur.setPrenom("John " + i);
			utilisateur.setTelephone(TEL_BIDON + i);
			UserUtil.genererEmailDonneeTest(utilisateur);
			UserUtil.appliquerMdpParDefaut(utilisateur);
			this.us.creerUtilisateur(utilisateur);

			// Le 1er des utilisateurs pair devient chef de l'équipe pair (idem impaire)
			if (i % 2 == 0) {
				if (equipePair.getChefEquipe() == null)
					equipePair.setChefEquipe(utilisateur);
				listeUtilisateurPair.add(utilisateur);
				utilisateur.setEquipe(equipePair);
			} else {
				if (equipeImpair.getChefEquipe() == null)
					equipeImpair.setChefEquipe(utilisateur);
				utilisateur.setEquipe(equipeImpair);
				listeUtilisateurImpair.add(utilisateur);
			}
		}

		// On assigne ces utilisateurs aux équipes
		equipePair.setUsers(listeUtilisateurPair);
		equipeImpair.setUsers(listeUtilisateurImpair);

		this.ts.majEquipe(equipePair);
		this.ts.majEquipe(equipeImpair);

		final Projet projet = new Projet();
		projet.setEntreprise("WE+");
		projet.setNomProjet("WENEO");
		this.ps.creerProjet(projet);

		this.ps.ajouterCollaborateursEquipe(projet, equipePair);

	}
}
