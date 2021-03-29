package org.openmrs.module.eventbasedcalculation.flags.impl;

import lombok.extern.slf4j.Slf4j;
import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Patient;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eventbasedcalculation.api.calculations.CalculationsProvider;
import org.openmrs.module.eventbasedcalculation.api.pathexpressions.ExpressionEvaluator;
import org.openmrs.util.OpenmrsUtil;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A marker class
 */
@Slf4j
public abstract class BasePatientFlag {

    BasePatientFlag() {
    }

    protected static boolean isBetween(double value, double lowerBound, double upperBound) {
        return value >= lowerBound && value <= upperBound;
    }

    protected static boolean isInAbsoluteNumericRange(Double value, ConceptNumeric conceptNumeric) {
        return OpenmrsUtil.isInAbsoluteNumericRange(value.floatValue(), conceptNumeric);
    }

    protected static boolean isInCriticalNumericRange(Double value, ConceptNumeric conceptNumeric) {
        return OpenmrsUtil.isInCriticalNumericRange(value.floatValue(), conceptNumeric);
    }

    protected static boolean isInNormalNumericRange(Double value, ConceptNumeric conceptNumeric) {
        return OpenmrsUtil.isInNormalNumericRange(value.floatValue(), conceptNumeric);
    }

    protected static boolean isNumeric(Concept diastolicConcept, Concept systolicConcept) {
        return diastolicConcept.isNumeric() && systolicConcept.isNumeric();
    }

    protected static ConceptNumeric getConceptNumeric(Concept concept) {
        return Context.getConceptService().getConceptNumeric(concept.getConceptId());
    }

    /**
     *  Checks if the value passed in is in the between the range
     * @param value the value
     * @param lowerBound lower bound limit
     * @param upperBound upper bound limit
     * @param inclusiveOfUpperBound inclusive of the upper bound
     * @return {@link java.lang.Boolean}
     */
    protected static boolean isBetween(double value, double lowerBound, double upperBound, boolean inclusiveOfUpperBound) {
        return inclusiveOfUpperBound ? value >= lowerBound && value <= upperBound : value >= lowerBound && value < upperBound;
    }

    protected static Set<Integer> getPatientIds(Collection<Patient> patients) {
        return patients.stream().map(Patient::getPatientId).collect(Collectors.toSet());
    }

    protected static PatientCalculation getCalculation(Class<? extends PatientCalculation> clazz) {
        return (PatientCalculation) new CalculationsProvider().getCalculation(
                clazz.getSimpleName(), null);
    }

    /**
     *
     * @param calculation Calculation
     * @param cohort cohort of patient
     * @return {@link CalculationResultMap}
     */
    protected static CalculationResultMap calculate(PatientCalculation calculation, Collection<Integer> cohort) {
        return Context.getService(PatientCalculationService.class).evaluate(cohort, calculation, null);
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

    protected static AdministrationService adminService() {
        return Context.getAdministrationService();
    }

    protected static Patient getPatient(Integer patientId) {
        return Context.getPatientService().getPatient(patientId);
    }

    protected static ExpressionEvaluator getExpressionEvaluator() {
        return new ExpressionEvaluator();
    }
}
