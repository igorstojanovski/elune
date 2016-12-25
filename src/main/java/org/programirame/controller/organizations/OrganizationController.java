package org.programirame.controller.organizations;

import org.programirame.models.Organization;
import org.programirame.models.contact.PhoneContact;
import org.programirame.services.organizations.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for processing requests for the {@link Organization} resource.
 */
@RestController
@RequestMapping("/api/organization")
@ExposesResourceFor(Organization.class)
public class OrganizationController {

    private final EntityLinks entityLinks;
    private OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService, EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
        this.organizationService = organizationService;
    }

    /**
     * Method for processing requests for creating an {@link Organization}.
     *
     * @param organization The new organization to be created.
     * @return The created organization.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<Organization>> creatOrganization(@RequestBody Organization organization) {
        Organization createdOrganization = organizationService.createOrganization(organization);
        return new ResponseEntity<>(createOganizationResource(createdOrganization), HttpStatus.CREATED);
    }

    /**
     * Method that processes request for retrieving an {@link Organization} by id.
     *
     * @param organizationId The id to search by.
     * @return The found organization or null.
     */
    @RequestMapping(path = "/{organizationId}", method = RequestMethod.GET)
    public ResponseEntity<Resource<Organization>> getOrganization(@PathVariable Long organizationId) {

        Organization organization = organizationService.findOrganization(organizationId);

        return new ResponseEntity<>(createOganizationResource(organization), HttpStatus.FOUND);
    }

    @RequestMapping(path = "/{organizationId}/phone", method = RequestMethod.POST)
    public ResponseEntity<PhoneContact> addPhoneContact(@PathVariable Long organizationId,
                                                        @RequestBody PhoneContact phoneContact) {

        if (setOrganization(organizationId, phoneContact)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        PhoneContact result = organizationService.addPhoneContact(phoneContact);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    private boolean setOrganization(@PathVariable Long organizationId, @RequestBody PhoneContact phoneContact) {
        Organization organization = organizationService.findOrganization(organizationId);
        phoneContact.setOrganization(organization);

        return organization == null;
    }

    private Resource<Organization> createOganizationResource(Organization organization) {
        Resource<Organization> resource = new Resource<>(organization);
        resource.add(entityLinks.linkToSingleResource(Organization.class, resource.getContent().getId()));

        return resource;
    }
}
