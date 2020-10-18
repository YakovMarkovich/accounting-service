package propets.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TokenNotCorrespondsLoginException extends RuntimeException {
	
	public TokenNotCorrespondsLoginException(String reason) {
		super(reason);
	}

}
