package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.web.bind.annotation.RestController;

import model.Contact;
import model.HPSignUp;
import model.HealthProfessional;
import model.Person;
import model.User;
import provider.MessageResponse;
import repository.ContactRepo;
import repository.HPSignUp_repo;
import repository.PersonDao;
import repository.User_repo;

@Configuration
@PropertySource("classpath:client-side.properties")
@RestController
@RequestMapping("/HPSignUp")
public class HPSignUpService {
	@Autowired
	private HPSignUp_repo hpsuRepo;
	
	@Autowired
	private User_repo userRepo;
	
	@Autowired
	private PersonDao personRepo;
	
	@Autowired
	private ContactRepo contactRepo;
	
	@Value("${client.root}")
	private String clientRoot;
	
	@Autowired
	private JavaMailSender mailer;
	
	private boolean sendVerificationEmail(HPSignUp hpSU) {
		String vMsg = "Please click on the following link (or copy & paste it to your browser's address bar): \n";
		try {
			vMsg += "http://" + this.clientRoot + "/HPSignUp/#/verification/?email=" 
					+ URLEncoder.encode(hpSU.getEmail(), "UTF-8") 
					+  "&token=" + hpSU.getVerificationKey();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.out.println("Unsupported Encoding UTF-8");
		}
		
		
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(hpSU.getEmail());
		msg.setCc("medicloud.sjsu@gmail.com");
		msg.setSubject("Verify your Medicloud account.");
		msg.setText(vMsg);
		
		try {
			mailer.send(msg);
			
			return true;
		} catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
            System.out.println(ex.getMessage());
            return false;
        }
	}
	
	@RequestMapping(method=POST) 
	public MessageResponse createHPSignUp(@RequestBody HPSignUp signup) 
	{		
		MessageResponse mr = new MessageResponse();
		
		HPSignUp hpSU  = hpsuRepo.findByEmail(signup.getEmail());
		if (hpSU == null) {
			HPSignUp newHP = HPSignUp.createHPSignUp(	signup.getName(), 
														signup.getEmail(), 
														signup.getBusinessName(), 
														signup.getBusinessAddress(), 
														signup.getBusinessPhone()
													);
			
			// send out an email with the token created in createHPSignUp
			if (this.sendVerificationEmail(newHP)) {
				mr.success = true;
				hpsuRepo.save(newHP); // only save if email went through ok
			} else {
				mr.success = false;
				mr.error = "An error occured while sending email.";
			}
		} else {
			mr.success = false;
			mr.message = "duplicate";
		}
		
		return mr;
	}
	
	@RequestMapping(value="/verify/{email}/{token}", method=GET)
	public MessageResponse verifyHPSignUp(
			@PathVariable("email") String email, @PathVariable("token") String token) 
	{
		MessageResponse mr = new MessageResponse();
		
		HPSignUp hp = hpsuRepo.findByEmail(email);
		if (hp == null) {
			mr.success = false;
			mr.message = "eof";
		} else if (hp.getIsVerified()) {
			mr.success = false;
			mr.message = "duplicate";
		} else if(hp.verify(email, token)){
			hpsuRepo.save(hp);
			mr.success = true;
		} else {
			mr.success = false;
			mr.error = "unknown";
		}
		
		return mr;
		
	}
	
	@Transactional
	@RequestMapping(value="/accountSetup/{email}/{token}", method=POST)
	public MessageResponse setupSecurity(
			@RequestBody User newUser, 
			@PathVariable("email") String email, @PathVariable("token") String token) 
	{
		MessageResponse mr = new MessageResponse();
		
		HPSignUp hpSignup = hpsuRepo.findByEmail(email);
		if (userRepo.findByUsername(newUser.getUsername()) instanceof User) {
			mr.success = false;
			mr.message = "duplicate";
			return mr;
		}
		
		if (!hpSignup.verify(email, token)) {
			mr.success = false;
			mr.message = "eof";
			return mr;
		}
		
		try {
			/* creating a health professional is a multi-steps process
			 * 1. create a new person
			 * 2. create a new user for that person
			 * 3. create a new health professional for that user
			 * 4. create a new contact (optional)
			 */
			
			String[] nameParts = hpSignup.getName().split(" ", 2);
			String firstName = nameParts[0];
			String lastName = "";
			if (nameParts.length > 1) lastName = nameParts[1];
			
			Person newPerson = Person.create(firstName, lastName);
			personRepo.save(newPerson);
			
			System.out.println(newUser.getUsername() +" " + email +" "+ newUser.getPassword());
			User u = User.create(newUser.getUsername(), email, newUser.getPassword(), newPerson);
			userRepo.save(u);
			
			HealthProfessional hp = HealthProfessional.create(u);
			hp.setCdo(hpSignup.getBusinessName());
			
			Contact c = Contact.create(u);
			c.setAddress(hpSignup.getBusinessAddress());
			c.setPhone(hpSignup.getBusinessPhone());
			
			contactRepo.save(c);
			
			hpSignup.changeVerificationKey();
			
			mr.success = true;
		} catch(Exception e) {
			mr.success = false;
			mr.error = e.getMessage();
		}
		
		return mr;
	}
}
