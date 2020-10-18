package propets.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import io.jsonwebtoken.SignatureException;
import propets.dto.Post;
import propets.dto.UserChanges;
import propets.dto.UserDto;
import propets.dto.UserRegisterDto;
import propets.dto.WrongPasswordException;
import propets.entities.GeneralPost;
import propets.security.AuthRequest;
import propets.security.AuthResponse;
import propets.security.JWTUtil;
import propets.service.IAccountService;

@RestController
@RequestMapping(value = AuthenticationController.REST_URL)

public class AuthenticationController {

	// static final String REST_URL = "/propets-app.herokuapp.com/account";
	static final String REST_URL = "/account/en/v1";
	Authentication authentication;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtil jwtTokenUtil;

	@Autowired
	IAccountService accountService;

	@Transactional
	@PostMapping("/register")
	ResponseEntity<UserDto> register(@RequestBody UserRegisterDto userRegisterDto, HttpServletResponse response) {
		UserDto user;
		try {
			user = accountService.addUser(userRegisterDto);
		} catch (WrongPasswordException e) {
			user = null;
			e.printStackTrace();
		}

		if (user != null) {
			AuthRequest request = new AuthRequest();
			System.out.println("request" + request);
			request.setName(userRegisterDto.getUserLogin());
			request.setPassword(userRegisterDto.getPassword());
			ResponseEntity<?> res = createAuthenticationToken(request, response);
			System.out.println("result" + res);
			return ResponseEntity.ok().headers(res.getHeaders()).body(user);
		}
		return null;
	}

	@PostMapping("/authenticate")
	public ResponseEntity<UserDto> createAuthenticationToken(@RequestBody AuthRequest authRequest,
			HttpServletResponse response) {
		System.out.println("authRequesy in autthenticate" + authRequest.getName() + authRequest.getPassword());
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));
			System.out.println("authetication " + authentication);
			System.out.println("I m before catch");
		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User name or password not correct", e);
		}

		try {
			System.out.println("I generate jwt");
			final String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
			return ResponseEntity.ok().header("X-Token", jwt).body(accountService
					.getUserData((UserDetails) authentication.getPrincipal(), authRequest.getName(), response));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}

	@GetMapping("/{login}/info")
	public ResponseEntity<UserDto> getUserInfo(@PathVariable("login") String login, HttpServletResponse response)
			throws IOException {
		UserDto user = accountService.getUserData((UserDetails) authentication.getPrincipal(), login, response);
		System.out.println("user in get User " + user);
		System.out.println(response);
		if (user != null) {
			System.out.println("I begin generate");
			System.out.println(authentication);
			System.out.println(authentication.getPrincipal() + " principal");
			final String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
			// final String jwt = jwtTokenUtil.generateToken( user.getUserLogin());
			System.out.println("finished generate");
			return ResponseEntity.ok().header("X-Token", jwt).body(user);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/token/validation")
	ResponseEntity<?> tokenValidation(@RequestHeader("X-Token") String token, HttpServletResponse response)
			throws IOException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String login = userDetails.getUsername();
		UserDto user = accountService.getUserData((UserDetails) authentication.getPrincipal(), login, response);
		String userName = user.getUserName();
		String avatar = user.getAvatar();
		if (jwtTokenUtil.validateToken(token, userDetails)) {
			System.out.println("I begin generate");
			final String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
			// final String jwt = jwtTokenUtil.generateToken( user.getUserLogin());
			System.out.println("finished generate");
			return ResponseEntity.ok().header("X-Token", jwt).body(new AuthResponse(login, userName, avatar));
		}
		return ResponseEntity.status(401).build();
	}

	@PutMapping("/{login}/activity/{postId}")
	ResponseEntity<UserDto> addUserActivity(@PathVariable String login, @PathVariable long postId,
			@RequestHeader("X-ServiceName") String serviceName, HttpServletResponse response) throws IOException {
		System.out.println("I'm in addUserActiv " + login + " " + postId + " " + serviceName);
		UserDto userDto = accountService.editUserData((UserDetails) authentication.getPrincipal(), login, serviceName,
				postId, response);
		System.out.println("userDto " + userDto);
		if (userDto != null) {
			final String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
			return ResponseEntity.ok().header("X-Token", jwt).body(userDto);
		} else
			return ResponseEntity.notFound().build();

	}

	@DeleteMapping("/{login}/activity/{id}")
	ResponseEntity<UserDto> removeUserActivity(@PathVariable String login, @PathVariable long id,
			@RequestHeader("X-ServiceName") String serviceName) {
		System.out.println("I'm in deleteUserActiv " + login + " " + id + " " + serviceName);
		UserDto userDto = accountService.removeUserActivity((UserDetails) authentication.getPrincipal(), login,
				serviceName, id);
		System.out.println(userDto + " userDto after removing");
		if (userDto != null) {
			final String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
			return ResponseEntity.ok().header("X-Token", jwt).body(userDto);
		} else
			return ResponseEntity.notFound().build();

	}

	@PutMapping("/1{login}")
	ResponseEntity<UserDto> editUserProfile(@PathVariable String login, @RequestBody UserChanges userChanges,
			HttpServletResponse response) throws IOException {
		System.out.println("login in controller " + login);
		UserDto userDto = accountService.editUserData((UserDetails) authentication.getPrincipal(), login, userChanges,
				response);
		if (userDto != null) {
			final String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
			return ResponseEntity.ok().header("X-Token", jwt).body(userDto);
		} else
			return ResponseEntity.notFound().build();
	}

	@PutMapping("/{login}/role/{role}")
	ResponseEntity<String[]> addUserRole(@PathVariable String login, @PathVariable String role) {
		String[] roles = accountService.addUserRole(login, role);
		if (roles != null) {
			final String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
			return ResponseEntity.ok().header("X-Token", jwt).body(roles);
		} else
			return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{login}/role/{role}")
	ResponseEntity<String[]> deleteUserRole(@PathVariable String login, @PathVariable String role) {
		String[] roles = accountService.deleteUserRole(login, role);
		if (roles != null) {
			final String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
			return ResponseEntity.ok().header("X-Token", jwt).body(roles);
		} else
			return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{login}")
	ResponseEntity<UserDto> removeUser(@PathVariable String login, @RequestHeader("X-Token") String token,
			HttpServletResponse response) throws IOException {
		UserDto user = accountService.getUserDataWithActivities((UserDetails) authentication.getPrincipal(), login,
				response);
		List<Long> posts = user.getActivities().get("message");
		if (posts != null) {
			for (long post : posts) {
				try {
					HttpHeaders responseHeaders = new HttpHeaders();
					responseHeaders.set("X-Token", token);
					/*RequestEntity<?> requestEntity = new RequestEntity<String>(responseHeaders, HttpMethod.DELETE,
							new URI("http://localhost:8090/propets-app.herokuapp.com/message/" + post));*/
					RequestEntity<?> requestEntity = new RequestEntity<String>(responseHeaders, HttpMethod.DELETE,
							new URI("https://message-service.herokuapp.com/message/en/v1/" + post));
					System.out.println("request Entity message " + requestEntity);
					RestTemplate restTemplate = new RestTemplate();
					ResponseEntity<Post> responseEntity = restTemplate.exchange(requestEntity, Post.class);
				} catch (RestClientException e) {
					e.getMessage();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
		UserDto userDto = accountService.removeUser(login);
		if (userDto != null) {
			final String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
			return ResponseEntity.ok().header("X-Token", jwt).body(userDto);
		} else
			return ResponseEntity.noContent().build();
	}

	@PutMapping("/{login}/block/{status}")
	ResponseEntity<String> blockUserAccount(@PathVariable String login, @PathVariable Boolean status) {
		System.out.println(login);
		String result = accountService.blockUser(login, status);
		System.out.println(result);
		if (result != null) {
			final String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
			return ResponseEntity.ok().header("X-Token", jwt).body(result);
		} else
			return ResponseEntity.notFound().build();
	}
}