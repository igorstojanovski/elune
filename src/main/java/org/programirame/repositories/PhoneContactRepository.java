package org.programirame.repositories;


import org.programirame.models.contact.PhoneContact;
import org.springframework.data.repository.CrudRepository;

public interface PhoneContactRepository extends CrudRepository<PhoneContact, Long> {
}
