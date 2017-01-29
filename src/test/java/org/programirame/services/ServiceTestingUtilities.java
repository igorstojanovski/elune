package org.programirame.services;

import org.programirame.models.OfferedService;

/**
 * Contains utility methods to ease the testing of
 * {@Link OfferedService} and related classes.
 */
public class ServiceTestingUtilities {

    public static final String TEST_NAME = "Limo Rental";
    public static final String TEST_DESCRIPTION = "Rental of limos and drivers.";

    public static OfferedService getService() {
        OfferedService offeredService = new OfferedService();
        offeredService.setName(TEST_NAME);
        offeredService.setDescription(TEST_DESCRIPTION);
        return offeredService;
    }

}
