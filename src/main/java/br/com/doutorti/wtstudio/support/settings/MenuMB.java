package br.com.doutorti.wtstudio.support.settings;

import java.io.Serializable;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "menuMB")
public class MenuMB implements Serializable {

	private static final long serialVersionUID = 201405150723L;

	private String sessionRule;

	public MenuMB() {
	}

	public String getSessionRule() {
		if (sessionRule == null) {
			sessionRule = SecurityContextHolder.getContext()
					.getAuthentication().getAuthorities().iterator().next()
					.getAuthority();
		}
		return sessionRule;
	}

	public boolean isAdmin() {
		return getSessionRule().equals("ROLE_ADMIN");
	}

}