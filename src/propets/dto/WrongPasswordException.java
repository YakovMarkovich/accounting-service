package propets.dto;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class WrongPasswordException extends Exception {

	private static final long serialVersionUID = -2654213378325613475L;

	public WrongPasswordException(String password) {
		super(password);
	}

}
