package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Team;
import com.example.demo.repository.TeamRepository;

@Service
public class TeamService {

	@Autowired
	TeamRepository tr;

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

	public Team majEquipe(final Team equipe) {
		return this.tr.save(equipe);
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
	public void supprimerEquipe(final long id) {
		final Team equipe = this.recupererEquipeParId(id);
		this.tr.delete(equipe);
	}

}
