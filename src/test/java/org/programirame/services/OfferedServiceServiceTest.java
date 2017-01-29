package org.programirame.services;


import org.junit.Before;
import org.junit.Test;
import org.programirame.models.OfferedService;
import org.programirame.repositories.OfferedServiceRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OfferedServiceServiceTest {

    private OfferedServiceService offeredServiceService;
    private OfferedService offeredService;

    @Before
    public void setUp() {
        OfferedServiceRepository repository = mock(OfferedServiceRepository.class);
        offeredServiceService = new OfferedServiceService(repository);

        offeredService = ServiceTestingUtilities.getService();

        OfferedService createdOfferedService = ServiceTestingUtilities.getService();
        createdOfferedService.setId(1L);

        when(repository.save(offeredService)).thenReturn(createdOfferedService);
    }

    @Test
    public void shouldCreateService() {

        OfferedService createdOfferedService = offeredServiceService.createService(offeredService);

        assertNotNull(createdOfferedService.getId());
        assertEquals(ServiceTestingUtilities.TEST_NAME, createdOfferedService.getName());

    }
}
