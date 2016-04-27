package patientService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import model.Contact;

import model.Patient;
import model.Person;
import model.Prescription;
import model.Appointment;
import provider.MessageResponse;
import repository.AppointmentRepo;

import repository.Contact_repo;
import repository.HealthProfessional_repo;
import repository.PatientRepo;
import repository.PrescriptionRepo;


@RestController
@RequestMapping(value="/api/patient")
public class PatientPersonalResourceServices {


		
		@Autowired
		private PatientRepo patientRepo;
		@Autowired
		private Contact_repo contactRepo;
		@Autowired
		private JavaMailSender mailer;
		@Autowired
		private HealthProfessional_repo hpRepo;
		@Autowired
		private PrescriptionRepo prescriptionRepo;
	
		
		@Autowired
		private AppointmentRepo appointmentRepo;
		
	
		
		//--------------------------------------------------------------------GET------------------------------------------------------
		/**
		 * Get all appointments from a patient.
		 * @param patientId
		 * @return
		 */
		@RequestMapping(value="/{patient_id}/appointments", method=RequestMethod.GET)
		public ResponseEntity<?> getAllAppointment(@PathVariable("patient_id")int patientId){
			List<Appointment> foundPatient = new ArrayList<Appointment>();
			List<Appointment> appointment = new ArrayList<Appointment>();	
			
			foundPatient =  appointmentRepo.findByPatientId(patientId);

			Appointment temp = new Appointment();
			
			if(foundPatient == null){
				MessageResponse mr = new MessageResponse();
				
				mr.success = false;
				mr.error = "Not Found: [patientId: " + patientId + " ]";
			
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
		
		/**
		 * Get all the prescription of an individual patient.
		 * @param patientId
		 * @return
		 */
		@RequestMapping(value="/{patient_id}/prescriptions", method=RequestMethod.GET)
		public ResponseEntity<?> getAllPrescription(@PathVariable("patient_id")int patientId
				){
			List<Prescription> foundPatient = new ArrayList<Prescription>();
			foundPatient = prescriptionRepo.findByPatientId(patientId);
			MessageResponse mr = new MessageResponse();
			mr.success = true;
			List<Prescription> prescription = new ArrayList<Prescription>();
			prescription = foundPatient;
			if(foundPatient == null){
				
				mr.success = false;
				mr.error ="Not Found: [patientId: " + patientId + " ]";
				
				return new ResponseEntity<MessageResponse>(mr, HttpStatus.NOT_FOUND);	
			}
			for(int i=0; i<foundPatient.size(); i++){
				if(foundPatient.get(i).getIsActive() == false){
					prescription.remove(i);
				}
			}
			return new ResponseEntity<List<Prescription>>(prescription, HttpStatus.OK);
			
		}
		
	

		
		//--------------------------------------------------------------------POST-----------------------------------------------------
		
		/**
		 * Patient requests an Appointment.
		 * @param patientId
		 */
		@RequestMapping(value="/{patient_id}/appointment", method=RequestMethod.POST)
		public ResponseEntity<?> setAppointment(@PathVariable("patient_id")int patientId	
				
				, @RequestBody Appointment newAppointment){
			
			Appointment temp = new Appointment();
			int hpId = newAppointment.getHPId();
			newAppointment.setRequestDate();
			newAppointment.setPatient(patientId);
			newAppointment.setPatientName(patientRepo.findByPatientId(patientId).getFirstName());
			newAppointment.setHPName(hpRepo.findByHpId(hpId).getUser().getPerson().getFirstName());
			Patient foundPatient = patientRepo.findByHpIdAndPatientId(hpId, patientId);
			if(foundPatient == null){
				MessageResponse mr = new MessageResponse();
				mr.success = false;
				mr.error = "HealthProfessional and Patient are not connected.";
				System.out.println(mr.error);
				return new ResponseEntity<MessageResponse>(mr, HttpStatus.NOT_FOUND);			
			}else{			
				temp = newAppointment;
			
				return new ResponseEntity<Appointment>(appointmentRepo.save(temp),HttpStatus.OK);
			}
				
		}
		
		
		
		//---------------------------------------------PUT-------------------------------------------------------
		
		/**
		 * Patient changes requested appointment.
		 * @param patientId
		 * @param appointmentId
		 */
		@RequestMapping(value="/appointment/{appointment_id}", method = RequestMethod.PUT)
		public ResponseEntity<?> updatePatientAppointment(@PathVariable("patient_id")int patientId
				,@PathVariable("appointment_id")int appointmentId
				,@RequestBody Appointment updateAppointment){
			Appointment temp = new Appointment();
			
			
			temp = appointmentRepo.findByPatientIdAndAppointmentId(patientId, appointmentId);
			if(temp==null){
				MessageResponse mr = new MessageResponse();
				mr.success = false;
				mr.error = "HealthProfessional and Patient are not connected.";
				System.out.println(mr.error);
				return new ResponseEntity<MessageResponse>(mr, HttpStatus.NOT_FOUND);
					
				}else{
				updateAppointment.setRequestDate();
				updateAppointment.setAppointmentId(appointmentId);
				updateAppointment.setPatient(patientId);
				int hpId = updateAppointment.getHPId();
				updateAppointment.setHPName(hpRepo.findByHpId(hpId).getUser().getUsername());
				appointmentRepo.save(updateAppointment);
				temp = updateAppointment;
				return new ResponseEntity<Appointment>(temp, HttpStatus.OK);
			
			}
			
		}
		
		
		
	
	
}
