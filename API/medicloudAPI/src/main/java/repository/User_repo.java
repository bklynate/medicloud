package repository;

import model.User;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier(value = "User_repo")
public interface User_repo extends CrudRepository<User, String> {
    public User findByUsername(String username);
    public User findByUserId(int userId);
    
    public List<User> findByUserId(Collection<User> userId);
}