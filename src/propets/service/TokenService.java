package propets.service;

import java.util.Base64;

import org.springframework.security.core.userdetails.UserDetails;


import propets.entities.User;

public interface TokenService {
	
	String createToken(UserDetails user);

	boolean validateToken(String token, UserDetails userDetails);


}
