package propets.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetails;

import propets.dto.UserChanges;
import propets.dto.UserDto;
import propets.dto.UserRegisterDto;
import propets.dto.WrongPasswordException;

public interface IAccountService {
	UserDto addUser(UserRegisterDto userRegisterDto) throws WrongPasswordException;
	UserDto getUserData(UserDetails userDetails, String login, HttpServletResponse response) throws IOException;
	UserDto getUserDataWithActivities(UserDetails userDetails, String login, HttpServletResponse response) throws IOException;
	UserDto editUserData(UserDetails userDetails, String login, UserChanges userChanges, HttpServletResponse response) throws IOException;
	UserDto editUserData(UserDetails userDetails, String login, String serviceName, long postId, HttpServletResponse response) throws IOException;
	UserDto removeUserActivity(UserDetails userDetails, String login, String serviceName, long postId);
	String[] addUserRole(String login, String role);
	String[] deleteUserRole(String login, String role);
	UserDto removeUser(String login);
	String blockUser(String login, Boolean status);

}
