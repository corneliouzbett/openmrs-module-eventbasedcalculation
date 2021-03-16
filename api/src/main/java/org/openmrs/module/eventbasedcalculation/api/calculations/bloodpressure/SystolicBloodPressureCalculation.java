package org.openmrs.module.eventbasedcalculation.api.calculations.bloodpressure;

import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.BaseCalculation;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eventbasedcalculation.api.calculations.shared.Calculation;

import java.util.Collection;
import java.util.Map;

public class SystolicBloodPressureCalculation extends BaseCalculation implements PatientCalculation {

    // Refactor to use global properties maybe
    private static final String SYSTOLIC_BLOOD_PRESSURE_CONCEPT_UUID = "5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    @Override
    public CalculationResultMap evaluate(Collection<Integer> collection, Map<String, Object> map, PatientCalculationContext patientCalculationContext) {
        return Calculation.lastObs(getSystolicConcept(), collection, patientCalculationContext);
    }

    Concept getSystolicConcept() {
        ConceptService conceptService = Context.getConceptService();
        return conceptService.getConceptByUuid(SystolicBloodPressureCalculation.SYSTOLIC_BLOOD_PRESSURE_CONCEPT_UUID);
    }
}
