package com.example.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.User;
import com.example.demo.util.UserUtil;

/**
 * Classe de sécurité permettant de définir où aller récupérer le login/mdp des utilisateurs
 *
 * @author ferre
 *
 */
public class CustomUserDetails implements UserDetails {

	private User user;

	public CustomUserDetails(final User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));
		// Il ne s'agit que d'un test, evidement pour de la PROD il faudrait de vrai groupe de sécurité, voir un serveur d'authentification externe (LDAP)
		// L'idée étant que cette méthode questionne le serveur d'authentification
		if (this.user.getEmail().equalsIgnoreCase(UserUtil.EMAIL_ADMIN))
			authorities.add(new SimpleGrantedAuthority(Role.ROLE_ADMIN.toString()));
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.user.getMotPasse();
	}

	@Override
	public String getUsername() {
		return this.user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
