package propets.dto;
import java.util.List;
import java.util.Map;


public class UserDto {
	private String userLogin;
	private String userName;
	private String avatar;
	private String phone;
	private String[] roles;
	private Map<String, List<Long>> activities;
	
	
	public UserDto() {
	}
	
	
	public UserDto(String userLogin, String userName, String avatar, String phone, String[] roles) {
		super();
		this.userLogin = userLogin;
		this.userName = userName;
		this.avatar = avatar;
		this.phone = phone;
		this.roles = roles;
	}
	
	
	public Map<String, List<Long>> getActivities() {
		return activities;
	}


	public void setActivities(Map<String, List<Long>> activities) {
		this.activities = activities;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	
	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	

}
