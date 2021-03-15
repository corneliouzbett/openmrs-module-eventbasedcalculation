package org.openmrs.module.eventbasedcalculation.flags.impl;

import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;

import java.util.Collection;

/**
 * A marker class
 */
public abstract class BasePatientFlag {

    BasePatientFlag() {
    }

    /**
     *
     * @param calculation Calculation
     * @param cohort cohort of patient
     * @param calculationContext the context
     * @return {@link CalculationResultMap}
     */
    protected static CalculationResultMap calculate(PatientCalculation calculation, Collection<Integer> cohort,
                                                    PatientCalculationContext calculationContext) {
        return Context.getService(PatientCalculationService.class).evaluate(cohort, calculation, calculationContext);
    }

}
