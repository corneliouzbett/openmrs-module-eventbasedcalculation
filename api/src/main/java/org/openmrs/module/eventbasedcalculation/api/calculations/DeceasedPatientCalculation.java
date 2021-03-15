package org.openmrs.module.eventbasedcalculation.api.calculations;

import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.BaseCalculation;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;

import java.util.Collection;
import java.util.Map;

public class DeceasedPatientCalculation extends BaseCalculation implements PatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext patientCalculationContext) {
        PatientService patientService = Context.getPatientService();
        CalculationResultMap resultMap = new CalculationResultMap();
        for (Integer patientId : cohort) {
           resultMap.put(patientId, new SimpleResult(isAlive(patientService.getPatient(patientId)),this, patientCalculationContext));
        }
        return resultMap;
    }

    private boolean isAlive(Patient patient) {
        return patient.getDead();
    }
}
