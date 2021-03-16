package org.openmrs.module.eventbasedcalculation.api.impl;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.module.eventbasedcalculation.api.FlagProcessingService;
import org.openmrs.module.eventbasedcalculation.flags.impl.AbnormalBloodPressurePatientFlag;
import org.openmrs.module.eventbasedcalculation.flags.impl.DueForVLPatientFlag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class EncounterFlagProcessingServiceImpl implements FlagProcessingService<Encounter> {

    //@Autowired
    private final DueForVLPatientFlag dueForVLPatientFlag;

    //@Autowired
    private final AbnormalBloodPressurePatientFlag bloodPressurePatientFlag;

    public EncounterFlagProcessingServiceImpl() {
        bloodPressurePatientFlag = new AbnormalBloodPressurePatientFlag();
        dueForVLPatientFlag = new DueForVLPatientFlag();
    }


    @Override
    public void processFlags(Collection<Encounter> encounters) {
        dueForVLPatientFlag.evaluate(encounters);

        Collection<Patient> patients = new ArrayList<>();
        encounters.forEach(encounter -> patients.add(encounter.getPatient()));
        bloodPressurePatientFlag.evaluate(patients);
    }

    @Override
    public void processFlags(Encounter encounter) {
        dueForVLPatientFlag.evaluate(encounter);
        bloodPressurePatientFlag.evaluate(encounter.getPatient());
    }
}
