package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.model.Team;
import com.example.demo.model.User;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TeamService;
import com.example.demo.util.UserUtil;

/**
 * Classe de test permettant de vérifier si les API de Team fonctionnent correctement.
 * 2 possibilités :
 * Passer par mockito (MockMvc) pour simuler des appels qui ne seront dans la réalité pas fait :
 * => permet de tester des parties bien précises en faisant abstraction du reste
 * Passer par TestRestTemplate qui va réellement lancer le server et executer les requêtes afin de vérifier la cohérence globale des traitements
 *
 * Ici le but est de réaliser réellement des requêtes et d'appeler le controller qui va appeler le service, ...
 *
 * @author ferre
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class TeamControllerTest {

	private static final String URL_TEAM = "/team";

	private static final String URL_TEAM_ID = URL_TEAM + "/%d";

	private static final String NOM_UTILISATEUR_TEST_2_INITIAL = "RenoTeam";

	private static final String PRENOM_UTILISATEUR_TEST_1_INITIAL = "BarbaraTeam";

	private static final String PRENOM_UTILISATEUR_TEST_2_INITIAL = "JeanTeam";

	private static final String NOM_UTILISATEUR_TEST_1_INITIAL = "PalvinTeam";

	private Team equipeTest = null;

	private User utilisateurTest = null;

	private Team equipeTestDelete = null;

	private User utilisateurTestDelete = null;

	/**
	 * On stock en cache l'équipe que l'on test afin de la supprimer une fois les test fini
	 */
	private Team equipeTestCreerTeam = null;

	/**
	 * Permet de typer le type de réponse que l'on s'attend à recevoir via les requêtes
	 */
	private final ParameterizedTypeReference<List<Team>> responseTypeListeUtilisateur = new ParameterizedTypeReference<List<Team>>() {
	};

	/**
	 * Permet de réaliser des requêtes
	 */
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private TeamRepository tr;

	@Autowired
	private TeamService ts;

	@Autowired
	private UserRepository ur;

	/**
	 * Création des données utilisés pour les JUNIT
	 */
	@BeforeAll
	public void creationDonnees() {

		final Team equipeTest = new Team();
		equipeTest.setDescription("Equipe Test");
		equipeTest.setEmplacement("ICI");

		final Team equipeTestDelete = new Team();
		equipeTestDelete.setDescription("Equipe Test Delete");
		equipeTestDelete.setEmplacement("LA");

		this.tr.save(equipeTest);
		this.tr.save(equipeTestDelete);

		final User utilisateurTest = new User();
		utilisateurTest.setNom(NOM_UTILISATEUR_TEST_1_INITIAL);
		utilisateurTest.setPrenom(PRENOM_UTILISATEUR_TEST_1_INITIAL);
		UserUtil.genererEmailDonneeTest(utilisateurTest);
		UserUtil.appliquerMdpParDefaut(utilisateurTest);

		final User utilisateurTestDelete = new User();
		utilisateurTestDelete.setNom(NOM_UTILISATEUR_TEST_2_INITIAL);
		utilisateurTestDelete.setPrenom(PRENOM_UTILISATEUR_TEST_2_INITIAL);
		UserUtil.genererEmailDonneeTest(utilisateurTestDelete);
		UserUtil.appliquerMdpParDefaut(utilisateurTestDelete);

		this.utilisateurTest = utilisateurTest;
		this.utilisateurTestDelete = utilisateurTestDelete;
		this.equipeTest = equipeTest;
		this.equipeTestDelete = equipeTestDelete;
		this.utilisateurTest = utilisateurTest;

		this.ur.save(utilisateurTest);
		this.ur.save(utilisateurTestDelete);

		// Interdépendance des données, pour associée une équipe à l'utilisateur test, il faut que celle-ci ait été persisté
		// S'agissant d'une relation bidirectionnelle on associe le User et la Team mais également la Team et le User
		equipeTest.setChefEquipe(utilisateurTest);
		final ArrayList<User> users = new ArrayList<>();
		users.add(utilisateurTest);
		equipeTest.setUsers(users);
		utilisateurTest.setEquipe(equipeTest);
		this.tr.save(equipeTest);
		this.ur.save(utilisateurTest);

	}

	@Test
	public void testCreerEquipe() {
		final Team equipe = new Team();
		// equipe.setChefEquipe(this.utilisateurTest);
		equipe.setDescription("TEST");
		equipe.setEmplacement("TEST_EMPL");
		equipe.setEmplacement("TEST_EMPL");
		final List<User> membreEquipe = new ArrayList<>();
		membreEquipe.add(this.utilisateurTest);
		equipe.setUsers(null);

		this.equipeTestCreerTeam = equipe;

		final ResponseEntity<Void> response = this.restTemplate.postForEntity(URL_TEAM, equipe, Void.class);

		assertTrue(response.getStatusCode() == HttpStatus.OK);
	}

	@Test
	public void testListerEquipes() {
		final long nombreEquipe = this.tr.count();

		final ResponseEntity<List<Team>> response = this.restTemplate.exchange(URL_TEAM, HttpMethod.GET, null, this.responseTypeListeUtilisateur);

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		assertTrue(response.getBody().size() == nombreEquipe);
	}

	@Test
	public void testRecupererEquipeParId() {
		final ResponseEntity<Team> response = this.restTemplate.getForEntity(String.format(URL_TEAM_ID, this.equipeTest.getId()), Team.class);

		final Team equipe = response.getBody();
		assertTrue(equipe.getId() == this.equipeTest.getId());
		final User chefEquipe = equipe.getChefEquipe();
		if (chefEquipe != null)
			assertTrue(chefEquipe.getId() == this.equipeTest.getChefEquipe().getId());
		assertTrue(equipe.getDescription().equals(this.equipeTest.getDescription()));
		assertTrue(equipe.getEmplacement().equals(this.equipeTest.getEmplacement()));
		final List<User> membreEquipe = equipe.getUsers();
		if (membreEquipe != null)
			assertTrue(membreEquipe.size() == this.equipeTest.getUsers().size());
	}

	@Test
	public void testSupprimerEquipe() {
		this.restTemplate.delete(String.format(URL_TEAM_ID, this.equipeTestDelete.getId()));
		assertFalse(this.tr.findById(this.equipeTestDelete.getId()).isPresent());

		// Suppression d'une équipe associée à un utilisateur => ne doit pas fonctionner
		this.restTemplate.delete(String.format(URL_TEAM_ID, this.equipeTest.getId()));
		assertTrue(this.tr.findById(this.equipeTest.getId()).isPresent());
	}

	/**
	 * Purge des données, la contrainte étrangère est portée par les utilisateurs (qui sont associées à une équipe)
	 * Donc on supprime l'équipe avant les utilisateurs
	 */
	@AfterAll
	public void purgerDonnees() {
		// On vide les interdépendances
		this.equipeTest.setUsers(null);
		this.equipeTest.setChefEquipe(null);
		this.utilisateurTest.setEquipe(null);
		this.ur.save(this.utilisateurTest);
		this.tr.save(this.equipeTest);

		this.ur.delete(this.utilisateurTest);
		this.ur.delete(this.utilisateurTestDelete);

		this.tr.delete(this.equipeTest);
		this.tr.delete(this.equipeTestDelete);
		// TODO lorsque les requêtes retourneront ce qu'elles modifient, il faudra supprimer le user creer par testCreerEquipe()
	}

}
