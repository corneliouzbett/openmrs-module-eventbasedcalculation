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
import org.openmrs.module.eventbasedcalculation.api.calculations.CalculationsProvider;
import org.openmrs.module.eventbasedcalculation.api.calculations.DeceasedPatientCalculation;
import org.openmrs.module.eventbasedcalculation.flags.OpenMrsPatientFlag;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
public class DeceasedPatientFlag extends BasePatientFlag implements OpenMrsPatientFlag<Patient> {

    protected boolean isEnabled;

    protected String display = "";

    protected String name = "Deceased patient flag";

    @Override
    public void evaluate(Patient patient) {
      this.evaluate(Collections.singleton(patient));
    }

    @Override
    public void evaluate(Collection<Patient> patients) {
        PatientCalculation calculation = (PatientCalculation) new CalculationsProvider().getCalculation(
                DeceasedPatientCalculation.class.getSimpleName(), null);
        Set<Integer> patientIds = patients.stream().map(Patient::getPatientId).collect(Collectors.toSet());

       CalculationResultMap resultMap = calculate(calculation, patientIds, null);
        for (Map.Entry<Integer, CalculationResult> entry : resultMap.entrySet()) {
            boolean value = entry.getValue().asType(Boolean.class);
            log.info("Patient ID {} with VALUE: {}", entry.getKey(), value);
            if (value) {
                createFlag(entry.getKey());
            }
        }
    }

    @Override
    public void createFlag(Integer patientId) {
        PatientService patientService = Context.getPatientService();
        Patient patient = patientService.getPatient(patientId);
        Person person = patient.getPerson();
        log.info("Creating the patient flag... Patient is dead");
        log.error("Patient {} {} has passed on", person.getFamilyName(), person.getGivenName());
    }

    @Override
    public void createFlag(Collection<Integer> cohort) {
        cohort.forEach(this::createFlag);
    }
}
