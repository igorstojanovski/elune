package org.programirame.repositories;


import org.programirame.models.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for handling the {@link Organization} data.
 */
@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long> {
}
