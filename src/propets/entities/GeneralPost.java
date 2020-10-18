package propets.entities;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="posts")
public class GeneralPost {
	 @Transient
	public static final String SEQUENCE_NUMBER = "propets_sequence";
	private Long id;
	String userLogin;
	String userName;
	String avatar;
	private String text;
	@Indexed
	private LocalDateTime datePost;
	private String[] images;
	
	public  GeneralPost(){}

	public  GeneralPost(Long id, String userLogin, String userName, String avatar, String text, LocalDateTime datePost,
			String[] images) {
		super();
		this.id = id;
		this.userLogin = userLogin;
		this.userName = userName;
		this.avatar = avatar;
		this.text = text;
		this.datePost = datePost;
		this.images = images;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getDatePost() {
		return datePost;
	}

	public void setDatePost(LocalDateTime datePost) {
		this.datePost = datePost;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", userLogin=" + userLogin + ", userName=" + userName + ", avatar=" + avatar
				+ ", text=" + text + ", datePost=" + datePost + ", images=" + Arrays.toString(images) + "]";
	}
    
	
	
}
