package org.openmrs.module.eventbasedcalculation.flags.impl;

import lombok.extern.slf4j.Slf4j;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eventbasedcalculation.calculation.LastViralCountCalculation;
import org.openmrs.module.eventbasedcalculation.flags.OpenMrsPatientFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Slf4j
@Component
public class DueForVLPatientFlag extends BasePatientFlag implements OpenMrsPatientFlag<Encounter> {

    @Autowired
    private LastViralCountCalculation viralCountCalculation;

    @Override
    public String getName() {
        return "Patient with VL >= 1000";
    }

    @Override
    public String getDisplay() {
        return null;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean evaluate(Encounter encounter) {
        log.error("Evaluating patient flag : {}", getName());

        // Get the affected patient, encounter, with all obs
        Patient patient = encounter.getPatient();
        Set<Obs> obsSet = encounter.getAllObs();

        // Get the Obs being created
        obsSet.forEach(this::handleViralLoad);

        CalculationResultMap resultMap = viralCountCalculation.evaluate(Collections.singleton(patient.getPatientId()),
                null, null);
        if (!resultMap.isEmpty()) {
            CalculationResult lastVl = resultMap.get(patient.getPatientId());
            if (!lastVl.isEmpty()) {
                log.error("Last VL {}", lastVl.getValue().toString());
            }
            createFlag(patient);
        }

        return false;
    }

    @Override
    public void evaluate(Collection<Encounter> encounters) {
        encounters.forEach(this::evaluate);
    }

    @Override
    public void createFlag(Patient patient) {

        log.error("Creating flag for Patient ID {}", patient.getPatientId());
        log.error("Creating flag for Patient {}", patient.getPerson().getFamilyName());

        // Insert into flags table
    }

    private void handleViralLoad(Obs obs) {
        Integer viralLoadConceptId = 843;
        if (obs.getConcept().getConceptId().equals(viralLoadConceptId)) {
            Double viralLoadResult = obs.getValueNumeric();
            if (viralLoadResult >= 1000.00) {
                createFlag(obs.getEncounter().getPatient());
            }
        }
    }
}
