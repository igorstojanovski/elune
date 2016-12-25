package org.programirame.services;

import org.junit.Before;
import org.junit.Test;
import org.programirame.models.Organization;
import org.programirame.models.User;
import org.programirame.repositories.OrganizationRepository;
import org.programirame.repositories.PhoneContactRepository;
import org.programirame.services.organizations.OrganizationService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrganizationsServiceTest {

    private Organization organizationCreated;
    private Organization organization;
    private OrganizationService organizationService;


    @Before
    public void init() {
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
        PhoneContactRepository phoneContactRepository = mock(PhoneContactRepository.class);
        organizationService = new OrganizationService(organizationRepository, phoneContactRepository);

        User owner = new User("Owner", "One", "owner", "***");
        owner.setId(1L);

        organization = new Organization();
        organization.setName("New Org");
        organization.setOwner(owner);

        organizationCreated = new Organization();
        organizationCreated.setName("New Org");
        organizationCreated.setOwner(owner);
        organizationCreated.setId(1L);

        when(organizationRepository.save(organization)).thenReturn(organizationCreated);
    }

    @Test
    public void shouldCallTheRightRepository() {
        Organization result = organizationService.createOrganization(organization);

        assertEquals(result, organizationCreated);
    }

}
