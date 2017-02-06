package org.programirame.services;

import org.junit.Before;
import org.junit.Test;
import org.programirame.exceptions.InvalidDataException;
import org.programirame.models.Organization;
import org.programirame.models.User;
import org.programirame.models.UserTypes;
import org.programirame.repositories.OrganizationRepository;
import org.programirame.repositories.PhoneContactRepository;
import org.programirame.services.organizations.OrganizationService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrganizationsServiceTest {

    private static final String NEW_ORG_NAME = "New Org";
    private Organization organizationCreated;
    private Organization organization;
    private OrganizationService organizationService;
    private User owner;

    @Before
    public void init() {
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
        PhoneContactRepository phoneContactRepository = mock(PhoneContactRepository.class);
        organizationService = new OrganizationService(organizationRepository, phoneContactRepository);

        owner = new User("Owner", "One", "owner", "***");
        owner.setId(1L);

        organization = new Organization();
        organization.setName(NEW_ORG_NAME);
        organization.setOwner(owner);

        organizationCreated = new Organization();
        organizationCreated.setName(NEW_ORG_NAME);
        organizationCreated.setOwner(owner);
        organizationCreated.setId(1L);

        when(organizationRepository.save(organization)).thenReturn(organizationCreated);
    }

    @Test
    public void shouldCallTheRightRepository() throws InvalidDataException {
        organization.getOwner().setUserType(UserTypes.ADMIN);
        Organization result = organizationService.createOrganization(organization);

        assertEquals(result, organizationCreated);
    }

    @Test(expected = InvalidDataException.class)
    public void shouldNotCreateOrganizationWithoutUser() throws InvalidDataException {
        Organization organization = new Organization();
        organization.setName(NEW_ORG_NAME);
        organizationService.createOrganization(organization);
    }

    @Test(expected = InvalidDataException.class)
    public void shouldNotCreateOrganizationIfUserNotAdmin() throws InvalidDataException {
        Organization organization = new Organization();
        organization.setName(NEW_ORG_NAME);
        organization.setOwner(owner);
        organizationService.createOrganization(organization);
    }

    @Test(expected = InvalidDataException.class)
    public void shouldNotCreateOrganizationWithoutAName() throws InvalidDataException {
        Organization organization = new Organization();
        organization.setOwner(owner);
        organizationService.createOrganization(organization);
    }
}
