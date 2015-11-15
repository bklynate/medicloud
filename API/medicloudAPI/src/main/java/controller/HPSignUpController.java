package controller;

import repositories.HPSignUp_repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.HPSignUp;

@RestController
@RequestMapping("/HPSignUp")
public class HPSignUpController {
	@Autowired
	private HPSignUp_repo hpsuRepo;
	
	public HPSignUpController() {
	}
	
	@RequestMapping(method=PUT, value="/new") 
	public boolean createHPSignUp(@RequestParam("name") String name, @RequestParam("email") String email
			, @RequestParam(value="businessName", required=false, defaultValue="") String busName
			, @RequestParam(value="businessAddress", required=false, defaultValue="") String busAddr
			, @RequestParam(value="businessPhone", required=false, defaultValue="") String busPhone) {
		HPSignUp hpSU  = hpsuRepo.findByEmail(email);
		if (hpSU == null) {
			HPSignUp.createHPSignUp(name, email, busName, busAddr, busPhone);
			return true;
		}
		
		return false;
	}
}
