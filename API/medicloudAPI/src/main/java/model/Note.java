package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="note")
public class Note {

	@Id
	@Column(name="note_id")
	@GeneratedValue
	private int noteId = 0;
	
	@Column(name="text")
	private String text;
	
	@Column(name="creator")
	private String creator;
	
	
	public int getNoteId(){
		return this.noteId;
	}
	public void setNoteId(int newId){
		this.noteId = newId;
	}
	
	public String getText(){
		return this.text;  
	}
	public void setText(String newText){
		this.text = newText;
	}
	
	public String getCreator(){
		return this.creator;
	}
	public void setCreator(String newCreatorName){
		this.creator = newCreatorName;
	}
}
