package org.openmrs.module.eventbasedcalculation.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.openmrs.Patient;
import org.openmrs.module.eventbasedcalculation.api.FlagProcessingService;
import org.openmrs.module.eventbasedcalculation.flags.impl.DeceasedPatientFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class PatientFlagProcessingServiceImpl implements FlagProcessingService<Patient> {

    @Autowired
    private DeceasedPatientFlag deceasedPatientFlag;

    public PatientFlagProcessingServiceImpl() {
        // Todo
        // @Autowired not wiring
        this.deceasedPatientFlag = new DeceasedPatientFlag();
    }

    @Override
    public void processFlags(Collection<Patient> patients) {
        deceasedPatientFlag.evaluate(patients);
    }

    @Override
    public void processFlags(Patient patient) {
        deceasedPatientFlag.evaluate(patient);
    }
}
