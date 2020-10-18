package propets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import propets.dto.UserDto;
import propets.service.AccountService;
import propets.service.IAccountService;

@RestController
public class HelloController {

	@GetMapping("/")
	public String hello() {
		return "Hello";
	}

	@GetMapping("/user")
	public String user() {
		return "User";
	}

	@GetMapping("/admin")
	public String admin() {
		return "Admin";
	}

}
