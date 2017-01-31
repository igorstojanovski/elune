package org.programirame.controller;


import org.junit.Before;
import org.junit.Test;
import org.programirame.controller.organizations.OrganizationController;
import org.programirame.models.Organization;
import org.programirame.models.User;
import org.programirame.services.organizations.OrganizationService;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OrganizationControllerTest {

    private OrganizationService organizationService;
    private OrganizationController organizationController;
    private Organization organization;
    private Organization organizationCreated;

    @Before
    public void init() {

        organizationService = mock(OrganizationService.class);

        organizationController = new OrganizationController(organizationService);

        User owner = new User("Owner", "One", "owner", "***");
        owner.setId(1L);

        organization = new Organization();
        organization.setName("New Org");
        organization.setOwner(owner);

        organizationCreated = new Organization();
        organizationCreated.setName("New Org");
        organizationCreated.setOwner(owner);
        organizationCreated.setId(1L);

        when(organizationService.createOrganization(organization)).thenReturn(organizationCreated);
    }


    @Test
    public void shouldReturnNonNullResultFromOrganizationService() {
        ResponseEntity<Organization> result = organizationController.creatOrganization(organization);

        verify(organizationService, times(1)).createOrganization(organization);
        assertEquals(result.getBody(), organizationCreated);
    }
}
