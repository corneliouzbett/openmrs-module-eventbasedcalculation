package org.openmrs.module.eventbasedcalculation.calculation;

import lombok.SneakyThrows;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.BaseCalculation;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.person.definition.ObsForPersonDataDefinition;

import java.util.Collection;

/**
 * An abstract class for patient calculations
 */
public abstract class BasePatientCalculation extends BaseCalculation implements PatientCalculation {

    protected static CalculationResultMap calculate(PatientCalculation calculation, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
        return Context.getService(PatientCalculationService.class).evaluate(cohort, calculation, calculationContext);
    }

    @SneakyThrows
    public static CalculationResultMap lastObs(Concept concept, Collection<Integer> cohort, PatientCalculationContext context) {
        ObsForPersonDataDefinition def = new ObsForPersonDataDefinition("last obs", TimeQualifier.LAST, concept, context.getNow(), null);
        return CalculationCommons.evaluateWithReporting(def, cohort, null, null, context);
    }
}
