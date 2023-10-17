package com.example.demo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

/**
 * Classe permettant de retourner une instance de {@link #CustomUserDetailsService()}
 * Est utilisé par la configuration de Spring Security
 *
 * @author ferre
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository ur;

	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		final List<User> listeUtilisateur = this.ur.findByEmail(email);
		if (listeUtilisateur == null || listeUtilisateur.isEmpty()) {
			throw new IllegalArgumentException(String.format("Utilisateur %s non trouvé", email));
		}
		final User user = listeUtilisateur.get(0);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return new CustomUserDetails(user);
	}

}
