package org.programirame.controller;


import org.junit.Before;
import org.junit.Test;
import org.programirame.controller.organizations.OrganizationController;
import org.programirame.exceptions.InvalidDataException;
import org.programirame.models.Organization;
import org.programirame.models.User;
import org.programirame.models.contact.PhoneContact;
import org.programirame.models.contact.PhoneContactType;
import org.programirame.services.organizations.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrganizationControllerTest {

    public static final String NEW_ORG_NAME = "New Org";
    private OrganizationService organizationService;
    private OrganizationController organizationController;
    private Organization organization;
    private Organization organizationCreated;

    @Before
    public void init() throws InvalidDataException {

        organizationService = mock(OrganizationService.class);

        organizationController = new OrganizationController(organizationService);

        User owner = new User("Owner", "One", "owner", "***");
        owner.setId(1L);

        organization = new Organization();
        organization.setId(1L);
        organization.setName(NEW_ORG_NAME);
        organization.setOwner(owner);

        organizationCreated = new Organization();
        organizationCreated.setName(NEW_ORG_NAME);
        organizationCreated.setOwner(owner);
        organizationCreated.setId(1L);

        when(organizationService.createOrganization(organization)).thenReturn(organizationCreated);
    }


    @Test
    public void shouldReturnNonNullResultFromOrganizationService() throws InvalidDataException {
        ResponseEntity result = organizationController.createOrganization(organization);

        verify(organizationService, times(1)).createOrganization(organization);
        assertEquals(result.getBody(), organizationCreated);
    }

    @Test
    public void shouldCallServiceToAddPhones() {
        PhoneContact phoneContact = getTestPhoneContact();
        PhoneContact phoneContactWithId = getTestPhoneContact();
        phoneContactWithId.setId(2L);

        doReturn(phoneContactWithId).when(organizationService).addPhoneContact(phoneContact);
        doReturn(organization).when(organizationService).findOrganization(1L);

        ResponseEntity<PhoneContact> result = organizationController.addPhoneContact(1L, phoneContact);

        assertEquals(result.getBody(), phoneContactWithId);
        assertEquals(result.getStatusCode(), HttpStatus.CREATED);

    }

    @Test
    public void shouldReturnNotFoundIfOrganizationIdIsInvalid() {
        PhoneContact phoneContact = getTestPhoneContact();

        doReturn(null).when(organizationService).findOrganization(1L);

        ResponseEntity<PhoneContact> result = organizationController.addPhoneContact(1L, phoneContact);

        assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    private PhoneContact getTestPhoneContact() {
        PhoneContact phoneContact = new PhoneContact();
        phoneContact.setOrganization(organization);
        phoneContact.setPhoneNumber("070223305");
        phoneContact.setType(PhoneContactType.MOBILE);
        return phoneContact;
    }
}
