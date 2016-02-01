package service;


import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import repository.PersonDao;
import model.Person;

@RestController
@RequestMapping(value="/person")
public class PersonServiceImpl {
	public PersonServiceImpl() {
		
	};
	
	@Autowired
	private PersonDao personDao;
	
	
	@RequestMapping(value = "/api/persons", method=RequestMethod.GET, produces =MediaType.APPLICATION_JSON_VALUE)
	public List<Person> getPerson(){
		Iterable<Person> persons = new ArrayList<Person>();
		persons = personDao.findAll();
		return (List<Person>) persons;
	}
	
	
	
	@RequestMapping(value="api/persons/test", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String person(){
		return "This is testing and it is working!";
	}
	
	
	
	@RequestMapping(value="/api/persons/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Person getPersonByID(@PathVariable("id") int id){
		
		return personDao.findByPersonId(id);
	}
	
	
	
	@RequestMapping(method=GET, value="/persons")
	public Iterable<Person> loadPersons() {
		Iterable<Person> people = new ArrayList<Person>();
		people = personDao.findAll();
		return people;
	}
	
	@RequestMapping(method=GET, value="/personId")
	public Person loadPersonByPersonId(@RequestParam("personId") int personId) {
		return personDao.findByPersonId(personId);
	}
	
	@RequestMapping(method=GET, value="/whatever")
	public String whateverIWant() {
		return "Whatever I want";
	}
}

