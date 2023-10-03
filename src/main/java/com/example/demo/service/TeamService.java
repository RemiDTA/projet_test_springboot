package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Team;
import com.example.demo.repository.TeamRepository;

@Service
public class TeamService {

	@Autowired
	TeamRepository tr;

	@Autowired
	UserService us;

	public Optional<Team> recupererEquipeParId(final long id) {
		return this.tr.findById(id);
	}

	public List<Team> recupererEquipeParEmplacement(final String emplacement) {
		return this.tr.findByEmplacement(emplacement);
	}

	public void creerEquipe(final Team equipe) {
		this.tr.save(equipe);
	}

	public void majEquipe(final Team equipe) {
		this.tr.save(equipe);
	}

	public List<Team> recupererToutesEquipes() {
		return this.tr.findAll();
	}

	public void supprimerToutesEquipes() {
		this.tr.deleteAll();
	}

	public void supprimerEquipe(final long id) {
		this.tr.deleteById(id);
	}

}
