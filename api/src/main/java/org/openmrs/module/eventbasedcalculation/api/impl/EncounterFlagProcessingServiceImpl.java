package org.openmrs.module.eventbasedcalculation.api.impl;

import org.openmrs.Encounter;
import org.openmrs.module.eventbasedcalculation.api.FlagProcessingService;
import org.openmrs.module.eventbasedcalculation.flags.impl.DueForVLPatientFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class EncounterFlagProcessingServiceImpl implements FlagProcessingService<Encounter> {

    @Autowired
    private DueForVLPatientFlag dueForVLPatientFlag;

    @Override
    public void processFlags(Collection<Encounter> encounters) {
        dueForVLPatientFlag.evaluate(encounters);
    }

    @Override
    public void processFlags(Encounter encounter) {
        dueForVLPatientFlag.evaluate(encounter);
    }
}
