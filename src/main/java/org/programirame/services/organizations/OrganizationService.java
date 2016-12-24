package org.programirame.services.organizations;


import org.programirame.models.Organization;
import org.programirame.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Services for the {@link Organization} resource.
 */
@Service
public class OrganizationService {

    private OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Organization findOrganization(Long id) {
        return organizationRepository.findOne(id);
    }

}
