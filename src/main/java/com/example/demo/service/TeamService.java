package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Team;
import com.example.demo.repository.TeamRepository;

@Service
public class TeamService {

	@Autowired
	TeamRepository tr;

	@Autowired
	UserService us;

	@Autowired
	private EntityManager entityManager;

	public Team recupererEquipeParId(final long id) {
		return this.tr.findById(id).orElseThrow(() -> new IllegalArgumentException(String.format("L'id %d n'existe pas", id)));
	}

	public List<Team> recupererEquipeParEmplacement(final String emplacement) {
		return this.tr.findByEmplacement(emplacement);
	}

	/**
	 * Le retour de tr.save(equipe) n'est que la serialisation de equipe, or equipe est le body envoyé par l'appelant,
	 * si celui-ci a un json incomplet, je veux que l'on retourne l'objet Team au complet (l'id quant à lui est remplit au moment du save)
	 *
	 * @param equipe
	 * @return
	 */
	public Team creerEquipe(final Team equipe) {
		// Règle métier sur l'initialisation de la date de création
		equipe.setDateCreation(LocalDate.now());
		this.tr.save(equipe);

		// Globalement à éviter mais je le conserve au cas où
		// Le problème c'est que lorsque l'on execute le save() suivi d'un findById(), un cache est présent
		// ce cache retourne la même instance que la variable equipe, il ne rerécupère pas les données au niveau de la base
		// le this.entityManager.detach(equipe) permet justement de repartir dans la base mais normalement il n'est pas nécessaire de gérer manuellement ce genre de chose
		this.entityManager.detach(equipe);

		return this.tr.findById(equipe.getId()).orElse(null);
	}

	/**
	 * Permet de merger les informations de l'équipe appelant avec les données en base
	 *
	 * @param equipe
	 * @return
	 */
	public Team majEquipe(final Team equipe) {
		final Team equipeBdd = this.recupererEquipeParId(equipe.getId());
		Optional.ofNullable(equipe.getDescription()).ifPresent(description -> equipeBdd.setDescription(description));
		Optional.ofNullable(equipe.getEmplacement()).ifPresent(emplacement -> equipeBdd.setEmplacement(emplacement));
		Optional.ofNullable(equipe.getChefEquipe()).ifPresent(chefEquipe -> equipeBdd.setChefEquipe(chefEquipe));

		return this.tr.save(equipeBdd);
	}

	public List<Team> recupererToutesEquipes() {
		return this.tr.findAll();
	}

	public void supprimerToutesEquipes() {
		this.tr.deleteAll();
	}

	/**
	 * On pourrait se contenter d'utiliser le deleteById, cependant si l'équipe n'existe pas, le plantage serait silencieux
	 *
	 * @param id
	 */
	@Transactional
	public void supprimerEquipe(final long id) {
		final Team equipe = this.recupererEquipeParId(id);
		// Si l'équipe contient des utilisateurs, il faut d'abord retirer l'équipe de chacun des utilisateurs avant de la supprimer
		equipe.getUsers().stream().forEach(user -> this.us.associerEquipe(null, user.getId()));
		this.tr.delete(equipe);
	}

}
