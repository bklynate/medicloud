package controller;
import repositories.User_repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.User;

@RestController
@RequestMapping("/user")
public class UserController {	
	@Autowired
    private User_repo userRepository;
    
	public UserController() {
		
	}
	
	/*@RequestMapping(method=GET)
	public List<User> users() {
		return userd.getUsers();
	}*/
	
	@RequestMapping(method=GET)
	public User user(@RequestParam("username") String username) {
		User u = userRepository.findByUsername(username);
		
		return u;
	}
	
	@RequestMapping(method=POST)
	public User user(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) {
		User u = User.createUser(username, email, password);
		return u;
	}
}
