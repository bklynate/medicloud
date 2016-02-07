package repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import model.PersonalView;

@Repository
@Qualifier(value="PersonalViewRepo")
public interface PersonalViewRepo extends CrudRepository<PersonalView, String> {

	public List<PersonalView> findByPersonId(List<Integer> personId);
}
