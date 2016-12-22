package org.programirame.repositories;


import org.programirame.models.UserApp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for handling the UserApp data.
 */
@Repository
public interface UserRepository extends CrudRepository<UserApp, Long> {
}
