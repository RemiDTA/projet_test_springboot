package com.example.demo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.example.demo.limite") // Correspond au fichier application.properties
public class ProprieteLimite {

	private int taillePagination;

	public int getTaillePagination() {
		return this.taillePagination;
	}

	public void setTaillePagination(final int tailleGet) {
		this.taillePagination = tailleGet;
	}

}
