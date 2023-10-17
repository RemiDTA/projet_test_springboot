package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.test.annotation.Rollback;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.UserUtil;

/**
 * Classe de test permettant de vérifier si les API de User fonctionnent correctement.
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
public class UserControllerTest {

	private static final String NOM_UTILISATEUR_TEST_2_INITIAL = "Reno";

	private static final String PRENOM_UTILISATEUR_TEST_1_INITIAL = "Barbara";

	private static final String PRENOM_UTILISATEUR_TEST_2_INITIAL = "Jean";

	private static final String NOM_UTILISATEUR_TEST_1_INITIAL = "Palvin";

	private static final String URL_USER = "/user";

	private static final String URL_USER_ID = URL_USER + "/%d";

	private static final String TEL_TROP_PETIT = "06999999";

	private static final String TEL_TROP_GRAND = "06999999999";

	private static final String TEL_MAUVAIS_DEBUT = "0899999999";

	private static final String TEL_CORRECT_1 = "0611111111";

	private static final String TEL_CORRECT_2 = "0622222222";

	private static final String TEL_CORRECT_3 = "0633333333";

	private User utilisateurTest1 = null;

	private User utilisateurTest2 = null;

	private User utilisateurTestDelete = null;

	/**
	 * On stock en cache l'utilisateur que l'on test afin de le supprimer une fois les test fini
	 */
	private User utilisateurTestCreerUser = null;

	/**
	 * Permet de typer le type de réponse que l'on s'attend à recevoir via les requêtes
	 */
	private final ParameterizedTypeReference<List<User>> responseTypeListeUtilisateur = new ParameterizedTypeReference<List<User>>() {
	};

	/**
	 * Permet de réaliser des requêtes
	 */
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserRepository ur;

	@Autowired
	private UserService us;

	/**
	 * Il s'agit de restTemplate avec les informations de connexions de l'admin
	 */
	private TestRestTemplate restTemplateBasicAuth;

	/**
	 * Création des données utilisés pour les JUNIT
	 */
	@BeforeAll
	public void creationDonnees() {
		final User utilisateurTest = new User();
		utilisateurTest.setNom(NOM_UTILISATEUR_TEST_1_INITIAL);
		utilisateurTest.setPrenom(PRENOM_UTILISATEUR_TEST_1_INITIAL);
		utilisateurTest.setTelephone(TEL_CORRECT_3);
		UserUtil.genererEmailDonneeTest(utilisateurTest);
		UserUtil.appliquerMdpParDefaut(utilisateurTest);

		final User utilisateurTest2 = new User();
		utilisateurTest2.setNom(NOM_UTILISATEUR_TEST_2_INITIAL);
		utilisateurTest2.setPrenom(PRENOM_UTILISATEUR_TEST_2_INITIAL);
		utilisateurTest2.setTelephone(TEL_CORRECT_2);
		UserUtil.genererEmailDonneeTest(utilisateurTest2);
		UserUtil.appliquerMdpParDefaut(utilisateurTest2);

		final User utilisateurTestDelete = new User();
		utilisateurTestDelete.setNom("nom");
		utilisateurTestDelete.setPrenom("prenom");
		UserUtil.genererEmailDonneeTest(utilisateurTestDelete);
		UserUtil.appliquerMdpParDefaut(utilisateurTestDelete);

		this.ur.save(utilisateurTest);
		this.ur.save(utilisateurTestDelete);
		this.ur.save(utilisateurTest2);
		this.utilisateurTest1 = utilisateurTest;
		this.utilisateurTest2 = utilisateurTest2;
		this.utilisateurTestDelete = utilisateurTestDelete;

		// Créez une entité HTTP avec les en-têtes
		this.restTemplateBasicAuth = this.restTemplate.withBasicAuth(UserUtil.EMAIL_ADMIN, UserUtil.MDP_PAR_DEFAUT);
	}

	@Test
	// @Rollback Devait permettre de rollback l'utilisateur créer durant ce test, ne fonctionne pas
	// peut-être dû au fait que le rollback dépend d'un contexte et que le test fait un appel et est sur une autre transaction
	public void testCreerUser() {
		final User utilisateurTest = new User();
		utilisateurTest.setNom("Tartempion");
		utilisateurTest.setPrenom("Jean");
		utilisateurTest.setTelephone(TEL_TROP_PETIT);
		UserUtil.genererEmailDonneeTest(utilisateurTest);
		UserUtil.appliquerMdpParDefaut(utilisateurTest);

		// TODO remplacer Void.class par User.class lorsque les requêtes retourneront ce qu'elles modifient
		ResponseEntity<Void> response = this.restTemplateBasicAuth.postForEntity(URL_USER, utilisateurTest, Void.class);

		// TODO à modifier lorsque les numéro d'erreur HTTP seront correctement gérer
		assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

		utilisateurTest.setTelephone(TEL_TROP_GRAND);
		response = this.restTemplateBasicAuth.postForEntity(URL_USER, utilisateurTest, Void.class);
		assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

		utilisateurTest.setTelephone(TEL_MAUVAIS_DEBUT);
		response = this.restTemplateBasicAuth.postForEntity(URL_USER, utilisateurTest, Void.class);
		assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

		utilisateurTest.setTelephone(TEL_CORRECT_1);
		response = this.restTemplateBasicAuth.postForEntity(URL_USER, utilisateurTest, Void.class);
		// TODO this.utilisateurTestCreerUser = response.getBody();
		assertTrue(response.getStatusCode() == HttpStatus.OK);

		final User utilisateurDoublonTest = new User();
		utilisateurDoublonTest.setNom("Tartempionne");
		utilisateurDoublonTest.setPrenom("Jeanne");
		utilisateurDoublonTest.setTelephone(TEL_CORRECT_1);
		UserUtil.genererEmailDonneeTest(utilisateurDoublonTest);
		UserUtil.appliquerMdpParDefaut(utilisateurDoublonTest);

		response = this.restTemplateBasicAuth.postForEntity(URL_USER, utilisateurTest, Void.class);
		assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * TODO Ne fonctionne pas, il y a un bug que je ne comprends pas lors du patch
	 */
	// @Test
	// public void testMajUser() {
	// final User utilisateurMaj = this.us.recupererUserParTel(this.utilisateurTest1.getTelephone());
	// utilisateurMaj.setNom("NOM");
	// utilisateurMaj.setPrenom("PRENOM");
	// utilisateurMaj.setTelephone(TEL_CORRECT_3);
	//
	// final ResponseEntity<Void> response = this.restTemplate.exchange(String.format(URL_USER_ID, utilisateurMaj.getId()), HttpMethod.PATCH, null, Void.class);
	//
	// assert response.getStatusCode() == HttpStatus.OK;
	// }

	@Test
	public void testListerUsers() {
		final long nombreUtilisateur = this.ur.count();

		final ResponseEntity<List<User>> response = this.restTemplateBasicAuth.exchange(URL_USER, HttpMethod.GET, null, this.responseTypeListeUtilisateur);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		assertTrue(response.getBody().size() == nombreUtilisateur);

	}

	@Test
	public void testRecupererUserById() {
		final ResponseEntity<User> response = this.restTemplateBasicAuth.getForEntity(String.format(URL_USER_ID, this.utilisateurTest2.getId()), User.class);

		final User utilisateur = response.getBody();
		// Je ne teste pas directement utilisateur.equals(utilisateurTest2) puisqu'il devrait s'agir de 2 instances différents
		assertTrue(utilisateur.getId() == this.utilisateurTest2.getId());
		assertTrue(utilisateur.getNom().equals(this.utilisateurTest2.getNom()));
		assertTrue(utilisateur.getPrenom().equals(this.utilisateurTest2.getPrenom()));
		assertTrue(utilisateur.getTelephone().equals(this.utilisateurTest2.getTelephone()));
	}

	@Test
	@Rollback
	public void testSupprimerUser() {
		this.restTemplateBasicAuth.delete(String.format(URL_USER_ID, this.utilisateurTestDelete.getId()));
		assertFalse(this.ur.findById(this.utilisateurTestDelete.getId()).isPresent());
	}

	@AfterAll
	public void purgerDonnees() {
		this.ur.delete(this.utilisateurTest1);
		this.ur.delete(this.utilisateurTest2);
		this.ur.delete(this.utilisateurTestDelete);
		// TODO lorsque les requêtes retourneront ce qu'elles modifient, il faudra supprimer le user creer par testCreerUser()
	}

}
