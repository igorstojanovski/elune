package org.programirame.services;


import org.programirame.models.OfferedService;
import org.programirame.repositories.OfferedServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferedServiceService {

    private final OfferedServiceRepository offeredServiceRepository;

    @Autowired
    public OfferedServiceService(OfferedServiceRepository offeredServiceRepository) {
        this.offeredServiceRepository = offeredServiceRepository;
    }

    public OfferedService createService(OfferedService offeredService) {
        return offeredServiceRepository.save(offeredService);
    }
}
