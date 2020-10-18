package propets.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import propets.dao.UserRepository;
import propets.dto.UserChanges;
import propets.dto.UserDto;
import propets.dto.UserExistsException;
import propets.dto.UserRegisterDto;
import propets.dto.WrongPasswordException;
import propets.entities.User;
import propets.security.TokenNotCorrespondsLoginException;

@Service
public class AccountService implements IAccountService {

	@Value("${password_length:5}")
	private int passwordLength;

	@Autowired
	UserRepository repository;

	@Autowired
	PasswordEncoder encoder;

	@Override
	@Transactional
	public UserDto addUser(UserRegisterDto userRegisterDto) throws WrongPasswordException {
		System.out.println(userRegisterDto);
		if (repository.existsById(userRegisterDto.getUserLogin())) {
			throw new UserExistsException(userRegisterDto.getUserLogin());
		}
		if (!isPasswordValid(userRegisterDto.getPassword())) {
			throw new WrongPasswordException(userRegisterDto.getPassword());
		}
		String roles[] = new String[1];
		roles[0] = "USER";
		User userAccount = new User(userRegisterDto.getUserLogin(), userRegisterDto.getUserName(),
				"https://www.gravatar.com/avatar/0?d=mp", roles, null, encoder.encode(userRegisterDto.getPassword()));
		repository.save(userAccount);
		return new UserDto(userAccount.getUserLogin(), userAccount.getUserName(), userAccount.getAvatar(),
				userAccount.getPhone(), userAccount.getRoles());
	}

	private boolean isPasswordValid(String password) {

		return password.length() >= passwordLength;
	}

	@Override
	public UserDto getUserData(UserDetails userDetails, String login, HttpServletResponse response) throws IOException {
		if (!login.equals(userDetails.getUsername())) {
			System.out.println("userDetails " + userDetails.getUsername());
			response.sendError(400, "Token not corresponds to login");
		}
		User user = repository.findById(login).orElse(null);
		if (user != null) {
			return new UserDto(user.getUserLogin(), user.getUserName(), user.getAvatar(), user.getPhone(),
					user.getRoles());
		} else
			return null;
	}
	
	@Override
	public UserDto getUserDataWithActivities(UserDetails userDetails, String login, HttpServletResponse response) throws IOException {
		if (!login.equals(userDetails.getUsername())) {
			System.out.println("userDetails " + userDetails.getUsername());
			throw new TokenNotCorrespondsLoginException("Token not corresponds to login");
		}
		User user = repository.findById(login).orElse(null);
		if (user != null) {
			UserDto userDto = new UserDto(user.getUserLogin(), user.getUserName(), user.getAvatar(), user.getPhone(),
					user.getRoles());
			userDto.setActivities(user.getActivities());
			return userDto;
		} else
			return null;
	}

	@Override
	public UserDto editUserData(UserDetails userDetails, String login, UserChanges userChanges, HttpServletResponse response) throws IOException {
		System.out.println("login " + login);
		if (!login.equals(userDetails.getUsername())) {
			System.out.println("userDetails " + userDetails.getUsername());
			response.sendError(400, "Token not corresponds to login");
		}
		User user = repository.findById(login).orElse(null);
		System.out.println("user " + user);
		if (user != null) {
			user.setAvatar(userChanges.getAvatar());
			user.setUserName(userChanges.getUserName());
			user.setPhone(userChanges.getPhone());
		}
		repository.save(user);
		return new UserDto(user.getUserLogin(), user.getUserName(), user.getAvatar(), user.getPhone(), user.getRoles());
	}

	@Override
	public UserDto editUserData(UserDetails userDetails, String login, String serviceName, long postId, HttpServletResponse response) throws IOException  {
		if (!login.equals(userDetails.getUsername())) {
			System.out.println("userDetails " + userDetails.getUsername());
			response.sendError(400, "Token not corresponds to login");
		}
		User user = repository.findById(login).orElse(null);
		if (user != null) {
			user.setActivities(serviceName, postId);
		}
		repository.save(user);
		return new UserDto(user.getUserLogin(), user.getUserName(), user.getAvatar(), user.getPhone(), user.getRoles());
	}

	@Override
	public UserDto removeUserActivity(UserDetails userDetails, String login, String serviceName, long postId) {
		if (!login.equals(userDetails.getUsername())) {
			System.out.println("userDetails " + userDetails.getUsername());
			return null;
		}
		User user = repository.findById(login).orElse(null);
		if (user != null) {
			user.deleteActivities(serviceName, postId);
		}
		user.getActivities().forEach((k, v) -> System.out.println(k + " " + v));
		repository.save(user);
		return new UserDto(user.getUserLogin(), user.getUserName(), user.getAvatar(), user.getPhone(), user.getRoles());
	}

	@Override
	public String[] addUserRole(String login, String role) {
		User user = repository.findById(login).orElse(null);
		if (user != null) {
			String roles[] = new String[2];
			roles[0] = "USER";
			roles[1] = role;
			user.setRoles(roles);
			repository.save(user);
			return user.getRoles();
		}
		return null;
	}

	@Override
	public String[] deleteUserRole(String login, String role) {
		User user = repository.findById(login).orElse(null);
		if (user != null) {
			String roles[] = new String[1];
			roles[0] = "USER";
			user.setRoles(roles);
			repository.save(user);
			return user.getRoles();
		}
		return null;
	}

	@Override
	public UserDto removeUser(String login) {
		User userAccount = repository.findById(login).orElse(null);
		if (userAccount != null) {
			repository.delete(userAccount);
			return new UserDto(userAccount.getUserLogin(), userAccount.getUserName(), userAccount.getAvatar(),
					userAccount.getPhone(), userAccount.getRoles());
		}
		return null;
	}

	@Override
	public String blockUser(String login, Boolean status) {
		User user = repository.findByUserLogin(login);
		System.out.println(user + " user in block");
		if(user!=null) {
			user.setBlocked(status);
			System.out.println("user after blocking " + user.getBlocked());
			repository.save(user);
			return status.toString();
		}
		return null;
	}

}
