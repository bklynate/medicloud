package model;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import repository.PersonDao;

@Entity
@Table(name="observation")
public class Observation {

	public Observation(){
		
	}
	
	@Id
	@GeneratedValue
	@Column(name="obs_id")
	private int obsId=0;
	
//	@ManyToOne(fetch=FetchType.LAZY)
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="person_id", insertable = false, updatable = false, referencedColumnName="person_id")
	private Person person;
	
	
	@Column(name="person_id")
	private int person_id;
	
	@Column(name="encounter_id")
	private int encounterId;
	
	@Column(name="comments")
	private String comments;
	
	@Column(name="creators")
	private String creators;
	
	@Column(name="state")
	private String state;
	
	
//	@Column(name="date_changed")
//	private Timestamp dateChanged;
//	
//	@Column(name="date_created")
//	private Timestamp dateCreated;
//	
	public int getObsId(){
		return this.obsId;
	}
	
	
	public int getPersonId(){
		return this.person.getPersonId();
	}
	
	public int getEncounterId(){
		return this.encounterId;
	}
	public String getComments(){
		return this.comments;
	}
	public String getCreator(){
		return this.creators;
	}
	public String getState(){
		return this.state;
	}
//	public Timestamp getDateChanged(){
//		return this.dateChanged;
//	}
////	public Timestamp getDateCreated(){
//		return this.dateCreated;
//	}
	
	public void setObsId(int newObsId){
		this.obsId = newObsId;
	}
	public void setPersonId(int newPersonId){
		this.person_id = newPersonId;
	}

	public void setEncounterId(int newEncounterId){
		this.encounterId = newEncounterId;
	}
	public void setComments(String newComments){
		this.comments = newComments;
	}
	
	public void setState(String newState){
		this.state = newState;
	}
	public void setCreators(String newCreator){
		this.creators = newCreator;
	}
//	public void setDateChanged(Timestamp newDateChanged){
//		this.dateChanged = newDateChanged;
//	}
//	public void setDateCreatedf(Timestamp newDateCreated){
//		this.dateCreated = newDateCreated;
//	}
	
	
}