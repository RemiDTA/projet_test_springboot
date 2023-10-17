package com.example.demo.util;

import com.example.demo.model.User;

/**
 * Utilitaire pour les utilisateurs
 *
 * @author ferre
 *
 */
public class UserUtil {

	private static final String TEMPLATE_EMAIL = "%s-%s@gmail.com";

	public static final String EMAIL_ADMIN = "admin@gmail.com";

	public static final String MDP_PAR_DEFAUT = "CHANGE_MOI";

	/**
	 * Génère une addresse mail à partir du nom et prenom d'un user (utilisé pour la génération de données)
	 *
	 * @param user
	 */
	public static void genererEmailDonneeTest(final User user) {
		user.setEmail(String.format(TEMPLATE_EMAIL, user.getNom().replace(' ', '_').toLowerCase(), user.getPrenom().replace(' ', '_').toLowerCase()));
	}

	public static void appliquerMdpParDefaut(final User user) {
		user.setMotPasse(MDP_PAR_DEFAUT);
	}

}
