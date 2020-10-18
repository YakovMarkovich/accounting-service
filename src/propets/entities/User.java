package propets.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
public class User {
	@Id
	private String userLogin;
	private String userName;
	private String avatar;
	private String[] roles;
	private Boolean blocked = false;
	private Map<String, List<Long>> activities;

	{
		activities = new HashMap<>();
		activities.put("message", new ArrayList<Long>());
		activities.put("lostFound", new ArrayList<Long>());
		activities.put("hotels", new ArrayList<Long>());
	}

	public Map<String, List<Long>> getActivities() {
		return activities;
	}

	public void setActivities(String key, long value) {
		activities.get(key).add(value);
	}

	public void deleteActivities(String key, long value) {
		activities.get(key).removeIf(t -> t.equals(value));
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User(String userName, String userLogin, String password) {
		super();
		this.userLogin = userLogin;
		this.userName = userName;
		this.password = password;
	}

	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setActivities(Map<String, List<Long>> activities) {
		this.activities = activities;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String password;

	public User() {
	}

	public User(String userLogin, String userName, String avatar, String[] roles, String phone, String password) {
		super();
		this.userLogin = userLogin;
		this.userName = userName;
		this.avatar = avatar;
		this.roles = roles;
		this.phone = phone;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	
	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activities == null) ? 0 : activities.hashCode());
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + Arrays.hashCode(roles);
		result = prime * result + ((userLogin == null) ? 0 : userLogin.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (!Arrays.equals(roles, other.roles))
			return false;
		if (userLogin == null) {
			if (other.userLogin != null)
				return false;
		} else if (!userLogin.equals(other.userLogin))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	

	@Override
	public String toString() {
		return "User [userLogin=" + userLogin + ", userName=" + userName + ", avatar=" + avatar + ", roles="
				+ Arrays.toString(roles) + ", activities=" + activities + ", phone=" + phone + ", password=" + password
				+ "]";
	}

}
