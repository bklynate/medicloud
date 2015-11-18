package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.security.crypto.bcrypt.*;

@Entity
@Table(name="user")
public class User {
	@Id
	@GeneratedValue
	@Column(name="user_id")
	private int userID;
	
	@Column(name="username", nullable=false, length=45)
	private String username;
	
	@Column(name="password", nullable=false, length=60)
	private String password;
	
	@Column(name="salt", nullable=false, length=60)
	private String salt;
	
	@Column(name="email", nullable=false, length=45)
	private String email;
	
	public static User createUser(String username, String email, String password) {
        User user = new User();

        user.username = username;
        user.email = email;
        user.salt = BCrypt.gensalt();
        user.password = BCrypt.hashpw(password, user.salt);
        
        return user;
	}
        
	public int getUserID() {
		return this.userID;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String setUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.salt = BCrypt.gensalt();
		this.password = BCrypt.hashpw(password, this.salt);
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String setEmail() {
		return this.email;
	}
}