package org.openmrs.module.eventbasedcalculation.api.calculations.vitals;

import org.openmrs.calculation.BaseCalculation;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eventbasedcalculation.api.calculations.shared.Calculation;

import java.util.Collection;
import java.util.Map;

public class TemperatureCalculation extends BaseCalculation implements PatientCalculation {

    private static final String TEMPERATURE_CONCEPT_UUID = "";

    @Override
    public CalculationResultMap evaluate(Collection<Integer> collection, Map<String, Object> map, PatientCalculationContext patientCalculationContext) {
        return Calculation.lastObs(Calculation.getConceptByUuid(TEMPERATURE_CONCEPT_UUID), collection, patientCalculationContext);
    }
}
