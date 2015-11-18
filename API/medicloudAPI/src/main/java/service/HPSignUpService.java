package service;

import repositories.HPSignUp_repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.web.bind.annotation.RestController;

import model.HPSignUp;
import provider.MessageResponse;

@Configuration
@PropertySource("classpath:client-side.properties")
@RestController
@RequestMapping("/HPSignUp")
public class HPSignUpService {
	@Autowired
	private HPSignUp_repo hpsuRepo;
	
	@Value("${client.root}")
	private String clientRoot;
	
	@Autowired
	private JavaMailSender mailer;
	
	public HPSignUpService() {
		
	}
	
	@RequestMapping(method=POST) 
	public MessageResponse createHPSignUp(@RequestBody HPSignUp signup) {
		
		MessageResponse mr = new MessageResponse();
		
		HPSignUp hpSU  = hpsuRepo.findByEmail(signup.getEmail());
		if (hpSU == null) {
			HPSignUp newHP = HPSignUp.createHPSignUp(signup.getName(), signup.getEmail(), signup.getBusinessName(), signup.getBusinessAddress(), signup.getBusinessPhone());
			
			// send out an email with the token created
			
			String vMsg = "Please click on the following link (or copy & paste it to your browser's address bar): \n";
			try {
				vMsg += "http://" + this.clientRoot + "/HPSignUp/#/verification/?email=" + URLEncoder.encode(newHP.getEmail(), "UTF-8") +  "&token=" + newHP.getVerificationKey();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println("Unsupported Encoding UTF-8");
			}
			
			
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(newHP.getEmail());
			msg.setCc("medicloud.sjsu@gmail.com");
			msg.setSubject("Verify your Medicloud account.");
			msg.setText(vMsg);
			
			try {
				mailer.send(msg);
				mr.success = true;
				hpsuRepo.save(newHP); // only save if email went through ok
			} catch (MailException ex) {
	            // simply log it and go on...
	            System.err.println(ex.getMessage());
	            System.out.println(ex.getMessage());
	            mr.success = false;
	        }
		} else {
			mr.success = false;
			mr.message = "duplicate";
		}
		
		return mr;
	}
	
	@RequestMapping(value="/verify/{email}/{token}", method=GET)
	public MessageResponse verifyHPSignUp(@PathVariable("email") String email, @PathVariable("token") String token) {
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
}