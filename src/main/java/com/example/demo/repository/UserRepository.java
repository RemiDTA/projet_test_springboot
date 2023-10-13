package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Projet;
import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByEmail(String email);

	List<User> findByTelephone(String numeroTelephone);

	List<User> findByTelephoneStartingWith(String prefixe);

	@Query(value = "SELECT * FROM user WHERE telephone LIKE :debutTel%", nativeQuery = true)
	List<User> utilisateurDontTelephoneCommencePar(@Param("debutTel") String prefixe);

	/**
	 * Gère la pagination
	 */
	@Override
	Page<User> findAll(Pageable pageable);

	/**
	 * Rechercher n,n en partant du projet
	 *
	 * @param projet
	 * @return
	 */
	List<User> findByProjets(Projet projet);
}
