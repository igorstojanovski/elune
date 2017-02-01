package org.programirame.services.organizations;


import org.programirame.exceptions.InvalidDataException;
import org.programirame.models.Organization;
import org.programirame.models.UserTypes;
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

    public Organization createOrganization(Organization organization) throws InvalidDataException {
        validateOrganization(organization);
        return organizationRepository.save(organization);
    }

    public Organization findOrganization(Long id) {
        return organizationRepository.findOne(id);
    }

    public PhoneContact addPhoneContact(PhoneContact phoneContact) {
        return phoneContactRepository.save(phoneContact);
    }

    /**
     * If the organization's data is not valid it throws an exception.
     *
     * @param organization the <code>Organization<code/> to validate.
     */
    public void validateOrganization(Organization organization) throws InvalidDataException {
        if (!isOrganizationValid(organization)) throw new InvalidDataException();
    }

    private boolean isOrganizationValid(Organization organization) {
        boolean isOrganizationValid = true;

        if (organization.getOwner() == null
                || organization.getName() == null
                || organization.getOwner().getUserType() != UserTypes.ADMIN) {
            isOrganizationValid = false;
        }

        return isOrganizationValid;
    }
}
