package patientService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import model.LabResult;
import model.Patient;
import model.Person;
import provider.MessageResponse;
import repository.PatientRepo;
import repository.PersonDao;

@RestController
@RequestMapping(value="/api/patient/{personId}")
public class AllPatientLabResultCollectionService {
	@Autowired
	private PersonDao personRepo;
	
	@Autowired
	private PatientRepo patientRepo;
	
	/**
	 * @method: GET
	 * @PathVariable: personId
	 * @return ResponseEntity<List<Labs>
	 * 
	 */
	@RequestMapping(value="/labs", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPatientLabResults(@PathVariable("personId") int personId) {
		MessageResponse mr = new MessageResponse();
		mr.success = false;
		
		Person person = personRepo.findByPersonId(personId);
		
		if (person == null) {
			mr.error = "Profile not found";
			return new ResponseEntity<MessageResponse>(mr, HttpStatus.NOT_FOUND);
		} else {
			List<Patient> personAsPatients = patientRepo.findByPersonId(personId);
			List<LabResult> allLabResults = new ArrayList<LabResult>();
			
			for(int i = 0; i < personAsPatients.size(); i++){
				List<LabResult> patientLabResults = personAsPatients.get(i).getLabResults();
				
				allLabResults.addAll(patientLabResults);
			}
			
			return new ResponseEntity<List<LabResult>>(allLabResults, HttpStatus.OK);
		}
		
	}
}
