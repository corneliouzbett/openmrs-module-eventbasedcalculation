package org.openmrs.module.eventbasedcalculation.flags.impl;

import lombok.extern.slf4j.Slf4j;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.module.eventbasedcalculation.flags.OpenMrsPatientFlag;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Component
public class DueForVLPatientFlag extends BasePatientFlag implements OpenMrsPatientFlag<Encounter> {

    @Override
    public String getName() {
        return "Patient with VL >= 1000";
    }

    @Override
    public String getDisplay() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void evaluate(Encounter encounter) {
        log.error("Evaluating patient flag : {}", getName());

        // Get the affected patient, encounter, with all obs
        Patient patient = encounter.getPatient();
        Set<Obs> obsSet = encounter.getAllObs();

        // Get the Obs being created
        obsSet.forEach(this::handleViralLoad);
    }

    @Override
    public void evaluate(Collection<Encounter> encounters) {
        encounters.forEach(this::evaluate);
    }

    @Override
    public void createFlag(Integer patientId) {

        log.error("Creating flag for Patient ID {}", patientId);
        log.error("Creating flag for Patient {}", patientId);

        // Insert into flags table
    }

    @Override
    public void createFlag(Collection<Integer> cohort) {
        cohort.forEach(this::createFlag);
    }

    private void handleViralLoad(Obs obs) {
        Integer viralLoadConceptId = 843;
        if (obs.getConcept().getConceptId().equals(viralLoadConceptId)) {
            Double viralLoadResult = obs.getValueNumeric();
            if (viralLoadResult >= 1000.00) {
                createFlag(obs.getEncounter().getPatient().getPatientId());
            }
        }
    }
}
