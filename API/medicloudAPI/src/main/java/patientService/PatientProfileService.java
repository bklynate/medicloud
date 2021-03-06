package patientService;

import java.util.List;

import javax.transaction.Transactional;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jsondoc.core.annotation.*;

import model.Contact;
import model.Person;
import provider.MessageResponse;
import repository.Contact_repo;
import repository.PersonDao;
import repository.User_repo;

@RestController
@RequestMapping(value="/api/patient/{personId}")
@Api(name="Patient profile service", description="Patient can views or change his/her profile.")
public class PatientProfileService {
	
	@Autowired
	private PersonDao personRepo;
	
	@Autowired
	private Contact_repo contactRepo;

	@Autowired
	private User_repo userRepo;
	
	@Transactional
	private boolean savePersonalInformation(Person p, Person newInfo) {
		p.setFirstName(newInfo.getFirstName());
		p.setLastName(newInfo.getLastName());
		p.setBirthdate(newInfo.getBirthdate());
		p.setGender(newInfo.getGender());
		
		Person saved = personRepo.save(p);
		
		if (saved != null)
			return true;
		else
			return false;
	}
	
	@Transactional
	private boolean saveContactInformation(Person p, List<Contact> listContacts) {
		for (Contact c : listContacts) {
			Contact existingContact = contactRepo.findByContactId(c.getContactId());
			
			if (existingContact == null) {
				existingContact = Contact.create(p);
			}
			existingContact.setAddress(c.getAddress());
			existingContact.setCity(c.getCity());
			existingContact.setState(c.getState());
			existingContact.setZip(c.getZip());
			existingContact.setEmail(c.getEmail());
			existingContact.setPhone(c.getPhone());
			
			contactRepo.save(existingContact);
		}
		
		return true;
	}
	
	/**
	 * @method: GET
	 * @PathVariable: personId
	 * @return ResponseEntity<Patient>
	 * 
	 */
	@RequestMapping(value="/profile", method=RequestMethod.GET)
	@ApiMethod(description="Patient views his/her contact information.")
	@ResponseBody
	public ResponseEntity<?> getPatientProfile(@ApiPathParam(name="patient id", description="require patient id")@PathVariable("personId") int personId) {
		MessageResponse mr = new MessageResponse();
		mr.success = false;
		
		Person person = personRepo.findByPersonId(personId);
		
		if (person == null) {
			mr.error = "Profile not found";
			return new ResponseEntity<MessageResponse>(mr, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Person>(person, HttpStatus.OK);
		}
		
	}
	
	/**
	 * @method: POST
	 * @PathVariable: personId
	 * @RequestBody: personProfile
	 * @return: savedProfile
	 */
	@RequestMapping(value="/profile", method=RequestMethod.POST)
	@ApiMethod( description="Patient updates or makes changes on his/her contact information.")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> postPatientProfile(@ApiPathParam(name="person id")@PathVariable("personId") int personId, 
			@ApiBodyObject@RequestBody Person newProfile) {
		MessageResponse mr = new MessageResponse();
		mr.success = false;
		
		Person person = personRepo.findByPersonId(personId);
		
		if (person == null) {
			mr.error = "Profile not found";
			return new ResponseEntity<MessageResponse>(mr, HttpStatus.NOT_FOUND);
		} else {
			savePersonalInformation(person, newProfile);
			saveContactInformation(person, newProfile.getContacts());
			
			return new ResponseEntity<Person>(personRepo.findByPersonId(personId), HttpStatus.OK);
		}
	}

	private static class userInvitationCode {
		public int invitationCode;
	}

	@Transactional
	private void generate(User user) {
		user.setInvitationCode();
		userRepo.save(user);
	}

	/**
	 * Generate invitation code.
	 * @param personId
	 * @return
	 */
	@RequestMapping(value="/invitation-code", method=RequestMethod.PUT)
	@ApiMethod(description="Patient generates an invitation code.")
	public ResponseEntity<?> setInvitationCode(@PathVariable("personId")int personId) {

		User user = userRepo.findByPersonId(personId);

		if(user==null){
			MessageResponse mr = new MessageResponse();
			mr.success = false;
			mr.error ="Not found: personId [ " + personId + "] ";
			mr.message = "";
			return new ResponseEntity<MessageResponse>(mr, HttpStatus.NOT_FOUND);
		}

		this.generate(user);
		userInvitationCode code = new userInvitationCode();
		code.invitationCode = user.getInvitationCode();
		return new ResponseEntity<>(code, HttpStatus.OK);
	}

	/**
	 * Generate invitation code.
	 * @param personId
	 * @return
	 */

	@RequestMapping(value="/invitation-code", method=RequestMethod.GET)
	@ApiMethod(description="Get current invitation code.")
	public ResponseEntity<?> getInvitationCode(@PathVariable("personId")int personId) {

		User user = userRepo.findByPersonId(personId);

		if(user==null){
			MessageResponse mr = new MessageResponse();
			mr.success = false;
			mr.error ="Not found: personId [ " + personId + "]";
			mr.message = "";
			return new ResponseEntity<>(mr, HttpStatus.NOT_FOUND);
		}

		userInvitationCode code = new userInvitationCode();
		code.invitationCode = user.getInvitationCode();

		return new ResponseEntity<>(code, HttpStatus.OK);
	}
}
