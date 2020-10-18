package propets.security;

public class AuthResponse {
    private String login;
    private String userName;
    private String avatar;

    public AuthResponse() {
    }

	public AuthResponse(String login, String userName, String avatar) {
		super();
		this.login = login;
		this.userName = userName;
		this.avatar = avatar;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}