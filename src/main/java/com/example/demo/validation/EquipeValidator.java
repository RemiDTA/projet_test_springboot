package com.example.demo.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.demo.model.Team;
import com.example.demo.model.User;

public class EquipeValidator implements ConstraintValidator<EquipeValide, Team> {

	@Override
	public boolean isValid(final Team equipe, final ConstraintValidatorContext context) {
		final boolean estChefEquipeValide = this.validationChefEquipe(equipe);
		if (!estChefEquipeValide) {
			// On modifie le message d'erreur par défaut de l'annotation EquipeValide
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(String.format("Le chef d'équipe %s ne fait pas partie de l'équipe %s", equipe.getChefEquipe().getNom(), equipe.getDescription()))
					.addConstraintViolation();
		}
		return estChefEquipeValide;
	}

	/**
	 * Permet de s'assurer que le chef d'équipe est un membre de l'équipe
	 *
	 * @param equipe
	 * @return
	 */
	private boolean validationChefEquipe(final Team equipe) {
		final List<User> listeMembreEquipe = equipe.getUsers();
		final User chefEquipe = equipe.getChefEquipe();
		if (chefEquipe == null)
			return true;
		if (listeMembreEquipe == null)
			return false; // Si on a un chef d'équipe mais aucun membre c'est KO
		return listeMembreEquipe.stream().anyMatch(membreEquipe -> chefEquipe.getId() == membreEquipe.getId());
	}

}
