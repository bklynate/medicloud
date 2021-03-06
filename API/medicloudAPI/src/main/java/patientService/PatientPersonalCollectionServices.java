
/**
 * Collection of hps, health histories
 * @author keon_win8
 *
 */
package patientService;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.jsondoc.core.annotation.ApiPathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.jsondoc.core.annotation.Api;

import repository.PatientRepo;
import repository.AppointmentRepo;
import repository.EncounterRepo;
import repository.HealthProfessional_repo;
import model.Appointment;
import model.Encounter;
import model.HealthProfessional;
import model.Patient;
import provider.MessageResponse;

@RestController
@RequestMapping(value="/api/patient")
@Api(description = "Patients can view individual health professional,"
		+ " their appointment, encounters, and conditions.", name="Patient Collection Service")
public class PatientPersonalCollectionServices {

	@Autowired
	private PatientRepo patientRepo;
	
	@Autowired
	private HealthProfessional_repo hpRepo;
	
	@Autowired
	private AppointmentRepo appointmentRepo;
	
	@Autowired
	private EncounterRepo encounterRepo;
	
	
//----------------------------------------------------------------------------------Helper Class--------------------------------------------------------------------------
	public static class HpAndPatient{
		private HealthProfessional hp;
		private Patient patient;
		public HealthProfessional getHp(){
			return this.hp;
		}
		public Patient getPatient(){
			return this.patient;
		}
		public void setHp(HealthProfessional newHP){
			this.hp = newHP;
		}
		public void setPatient(Patient newPatient){
			this.patient = newPatient;
		}
	}
	
	//------------------------------------------------------GET--------------------------------------
	/**
	 * Patient gets individual HealthProfessional information.
	 * @param patientId
	 * @param hpId
	 * @return
	 */
	@RequestMapping(value="/{patientId}/hps/{hpId}", method=RequestMethod.GET)
	@ApiMethod(description="Patient views individual hp's information.")
	public ResponseEntity<?> getAnHp(@ApiPathParam(name="patient id")@PathVariable("patientId")int patientId
			, @ApiPathParam(name="health professional id")@PathVariable("hpId")int hpId){
		Patient foundPatient = patientRepo.findByHpIdAndPatientId(hpId, patientId);
		
		if(foundPatient==null){
			MessageResponse mr = new MessageResponse();
			mr.success = false;
			mr.error = "Not Found: [patientId: " + patientId + " or hpId: " + hpId + "]";
			mr.message = "";
			return new ResponseEntity<MessageResponse>(mr, HttpStatus.NOT_FOUND);
		}else{
			HealthProfessional hp = hpRepo.findByHpId(hpId);
			HpAndPatient result = new HpAndPatient();
			result.setPatient(foundPatient);
			result.setHp(hp);
			
			return new ResponseEntity<HpAndPatient>(result, HttpStatus.OK);
		}	
		
	}
	
	
	
	/**
	 * Get all appointments from a patient.
	 * @param personId
	 * @return
	 */
	@RequestMapping(value="/{personId}/appointments", method=RequestMethod.GET)
	@ApiMethod(description="A patient lookup his or her appointments.")
		public ResponseEntity<?> getAllAppointment(@ApiPathParam(name="person id")@PathVariable("personId")int personId){
			List<Appointment> foundPatient = new ArrayList<Appointment>();
			List<Appointment> appointment = new ArrayList<Appointment>();
			List<Patient> listOfPatient= new ArrayList<Patient>();

			Appointment temp = new Appointment();
			List<Appointment> listOfFoundPatient= new ArrayList<Appointment>();
			listOfPatient = patientRepo.findByPersonId(personId);
			for(int j = 0; j < listOfPatient.size(); j++){
				int patientId=listOfPatient.get(j).getPatientId();
				
				listOfFoundPatient =  appointmentRepo.findByPatientId(patientId);

				foundPatient.addAll(listOfFoundPatient);
				
				
			}
			
			
			if(foundPatient == null){
				MessageResponse mr = new MessageResponse();
				
				mr.success = false;
				mr.error = "Not Found: [patientId: " + personId + " ]";
			
				System.out.println(mr.error);
				
				return new ResponseEntity<MessageResponse>(mr, HttpStatus.NOT_FOUND);
			}else{
				for(int i=0; i<foundPatient.size(); i++){
						Scanner scanDate = new Scanner(foundPatient.get(i).getAppointmentDate());
						scanDate.useDelimiter("-");
						String date="";
						
						while(scanDate.hasNext()){
							date = date + scanDate.next();
						}
					
						int dateTime= Integer.parseInt(date);
					
						if((dateTime-temp.dateTimeChecker()) > 0){	
							appointment.add(foundPatient.get(i));
						}						
				}
				
				return new ResponseEntity<List<Appointment>>(appointment,HttpStatus.OK);
			}
			
	
		}
	
	
	
		
	
	
	
	
// class closing brace.	
}
