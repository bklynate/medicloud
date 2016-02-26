package repository;

import model.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier(value="PersonDao")
public interface PersonDao extends CrudRepository<Person,String> {
	public Person findByPersonId(int personId);
	public Person findByFirstName(String firstName);
	public Person findByLastName(String lastName);
	public Person findByBirthdate(String birthdate);
}