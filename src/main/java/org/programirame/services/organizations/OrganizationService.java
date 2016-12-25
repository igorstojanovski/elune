package org.programirame.services.organizations;


import org.programirame.models.Organization;
import org.programirame.models.contact.PhoneContact;
import org.programirame.repositories.OrganizationRepository;
import org.programirame.repositories.PhoneContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Services for the {@link Organization} resource.
 */
@Service
public class OrganizationService {

    private OrganizationRepository organizationRepository;
    private PhoneContactRepository phoneContactRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository,
                               PhoneContactRepository phoneContactRepository) {
        this.organizationRepository = organizationRepository;
        this.phoneContactRepository = phoneContactRepository;
    }

    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Organization findOrganization(Long id) {
        return organizationRepository.findOne(id);
    }

    public PhoneContact addPhoneContact(PhoneContact phoneContact) {
        return phoneContactRepository.save(phoneContact);
    }
}
