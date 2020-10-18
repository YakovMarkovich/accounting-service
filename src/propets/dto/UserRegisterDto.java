package propets.dto;

public class UserRegisterDto {
	private String userLogin;
	private String userName;
	private String password;
	
	private UserRegisterDto() {}

	public UserRegisterDto(String userLogin, String userName, String password) {
		super();
		this.userLogin = userLogin;
		this.userName = userName;
		this.password = password;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
