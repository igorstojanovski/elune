package org.programirame.repositories;


import org.programirame.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for handling the User data.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
