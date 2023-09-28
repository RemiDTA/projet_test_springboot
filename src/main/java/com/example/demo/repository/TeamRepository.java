package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	List<Team> findByEmplacement(String emplacement);

}
