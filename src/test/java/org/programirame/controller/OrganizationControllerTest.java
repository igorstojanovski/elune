package org.programirame.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    private static final String NEW_RESOURCE_URL = "http://localhost/api/organization/1";

    private OrganizationService organizationService;
    private EntityLinks entityLinks;
    private OrganizationController organizationController;
    private Organization organization;
    private Organization organizationCreated;

    @Before
    public void init() {

        organizationService = mock(OrganizationService.class);
        entityLinks = mock(EntityLinks.class);

        organizationController = new OrganizationController(organizationService, entityLinks);

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
        when(entityLinks.linkToSingleResource(Organization.class, 1L)).thenReturn(new Link(NEW_RESOURCE_URL));
    }


    @Test
    public void shouldReturnNonNullResultFromOrganizationService() {
        ResponseEntity<Resource<Organization>> result = organizationController.creatOrganization(organization);

        verify(organizationService, times(1)).createOrganization(organization);
        assertEquals(result.getBody().getContent(), organizationCreated);
    }
}
