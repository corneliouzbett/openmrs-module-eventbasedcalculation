package org.openmrs.module.eventbasedcalculation.flags.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eventbasedcalculation.api.calculations.DeceasedPatientCalculation;
import org.openmrs.module.eventbasedcalculation.flags.OpenMrsPatientFlag;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@Setter(AccessLevel.MODULE)
@Getter(AccessLevel.MODULE)
public class DeceasedPatientFlag extends BasePatientFlag implements OpenMrsPatientFlag<Patient> {

    @Override
    public void evaluate(Patient patient) {
      this.evaluate(Collections.singleton(patient));
    }

    @Override
    public void evaluate(Collection<Patient> patients) {
        PatientCalculation calculation = getCalculation(DeceasedPatientCalculation.class);
        Set<Integer> patientIds = getPatientIds(patients);

       CalculationResultMap resultMap = calculate(calculation, patientIds);
        for (Map.Entry<Integer, CalculationResult> entry : resultMap.entrySet()) {
            boolean hasThePatientDied = entry.getValue().asType(Boolean.class);
            if (hasThePatientDied) {
                createFlag(entry.getKey());
            }
        }
    }

    @Override
    public void createFlag(Integer patientId) {
        PatientService patientService = Context.getPatientService();
        Patient patient = patientService.getPatient(patientId);
        Person person = patient.getPerson();
        // Add logic to create flag record for the specified patient
        log.info("Creating the patient flag record");
        log.info("Patient {} {} marked as deceased", person.getFamilyName(), person.getGivenName());
    }

    @Override
    public void createFlag(Collection<Integer> cohort) {
        cohort.forEach(this::createFlag);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDisplay() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
