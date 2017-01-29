package org.programirame.services;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.programirame.models.OfferedService;
import org.programirame.repositories.OfferedServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceServiceIT {

    @Autowired
    private OfferedServiceRepository offeredServiceRepository;
    private OfferedServiceService offeredServiceService;

    @Before
    public void setUp() {
        offeredServiceService = new OfferedServiceService(offeredServiceRepository);
    }

    @Test
    public void shouldCreateNew() {
        OfferedService offeredService = offeredServiceService.createService(ServiceTestingUtilities.getService());

        assertNotNull(offeredService.getId());
        assertEquals(offeredService.getName(), ServiceTestingUtilities.TEST_NAME);
        assertEquals(offeredService.getDescription(), ServiceTestingUtilities.TEST_DESCRIPTION);
    }


}
