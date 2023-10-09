package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Projet;
import com.example.demo.model.User;

public interface ProjetRepository extends JpaRepository<Projet, Long> {
	/**
	 * Rechercher n,n en partant de l'utilisateur
	 *
	 * @param projet
	 * @return
	 */
	List<Projet> findByCollaborateurs(User user);
}
