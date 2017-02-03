package org.programirame.controller.organizations;

import org.programirame.exceptions.InvalidDataException;
import org.programirame.models.Organization;
import org.programirame.models.contact.PhoneContact;
import org.programirame.services.organizations.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for processing requests for the {@link Organization} resource.
 */
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    private OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    /**
     * Method for processing requests for creating an {@link Organization}.
     *
     * @param organization The new organization to be created.
     * @return The created organization.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity creatOrganization(@RequestBody Organization organization) throws InvalidDataException {
        Organization createdOrganization = organizationService.createOrganization(organization);
        return new ResponseEntity<>(createdOrganization, HttpStatus.CREATED);
    }

    /**
     * Method that processes request for retrieving an {@link Organization} by id.
     *
     * @param organizationId The id to search by.
     * @return The found organization or null.
     */
    @RequestMapping(path = "/{organizationId}", method = RequestMethod.GET)
    public ResponseEntity getOrganization(@PathVariable Long organizationId) {

        Organization organization = organizationService.findOrganization(organizationId);

        return new ResponseEntity<>(organization, HttpStatus.FOUND);
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
}
