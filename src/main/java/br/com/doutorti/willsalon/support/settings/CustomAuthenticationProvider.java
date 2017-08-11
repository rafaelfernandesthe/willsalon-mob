package br.com.doutorti.willsalon.support.settings;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.doutorti.willsalon.model.UserEntity;
import br.com.doutorti.willsalon.model.repositories.IUserRepository;

//@Primary
//@Component("customAuthenticationProvider")
@Named( "customAuthenticationProvider" )
public class CustomAuthenticationProvider implements AuthenticationProvider {

	Logger logger = Logger.getLogger( CustomAuthenticationProvider.class );

	@Inject
	private IUserRepository userRepository;

	public CustomAuthenticationProvider() {
		super();
		logger.info( "ping" );
	}

	@Override
	public Authentication authenticate( final Authentication authentication ) throws AuthenticationException {
		logger.info( "ping authenticate() with user " + authentication.getName() );
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		UserEntity user = this.userRepository.findByUsernameAndPasswordAndActive( username, password, true );

		if ( user != null ) {
			List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

			grantedAuthorities.add( new SimpleGrantedAuthority( user.getRole() ) );

			UserDetails userDetails = new User( username, password, grantedAuthorities );
			return new UsernamePasswordAuthenticationToken( userDetails, password, grantedAuthorities );
		} else {
			return null;
		}
	}

	@Override
	public boolean supports( final Class<?> authentication ) {
		return authentication.equals( UsernamePasswordAuthenticationToken.class );
	}

}
