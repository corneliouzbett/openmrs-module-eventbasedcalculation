package org.openmrs.module.eventbasedcalculation.api.calculations.util;

import org.openmrs.Cohort;
import org.openmrs.Obs;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.calculation.result.ObsResult;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.patient.EvaluatedPatientData;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.service.PatientDataService;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.service.PersonDataService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

import java.util.Collection;
import java.util.Map;

public class CalculationUtils {

    CalculationUtils() {}

    /**
     * Evaluates a data definition on each patient using a reporting context
     * @param dataDefinition the data definition
     * @param cohort the patient ids
     * @param parameterValues the parameters for the reporting context
     * @param calculation the calculation (optional)
     * @param calculationContext the calculation context
     * @return the calculation result map
     */
    public static CalculationResultMap evaluateWithReporting(DataDefinition dataDefinition, Collection<Integer> cohort,
                                                             Map<String, Object> parameterValues, PatientCalculation calculation,
                                                             PatientCalculationContext calculationContext) {
        try {
            EvaluationContext reportingContext = ensureReportingContext(calculationContext, cohort, parameterValues);
            Map<Integer, Object> data;

            if (dataDefinition instanceof PersonDataDefinition) {
                EvaluatedPersonData result = Context.getService(PersonDataService.class).evaluate((PersonDataDefinition) dataDefinition, reportingContext);
                data = result.getData();
            }
            else if (dataDefinition instanceof PatientDataDefinition) {
                EvaluatedPatientData result = Context.getService(PatientDataService.class).evaluate((PatientDataDefinition) dataDefinition, reportingContext);
                data = result.getData();
            }
            else {
                throw new RuntimeException("Unknown DataDefinition type: " + dataDefinition.getClass());
            }

            CalculationResultMap ret = new CalculationResultMap();
            for (Integer ptId : cohort) {
                Object reportingResult = data.get(ptId);
                ret.put(ptId, toCalculationResult(reportingResult, calculation, calculationContext));
            }

            return ret;
        } catch (EvaluationException ex) {
            throw new APIException(ex);
        }
    }

    /**
     * Convenience method to wrap a plain object in the appropriate calculation result subclass
     * @param obj the plain object
     * @param calculation the calculation (optional)
     * @param calculationContext the calculation context
     * @return the calculation result
     */
    protected static CalculationResult toCalculationResult(Object obj, PatientCalculation calculation, PatientCalculationContext calculationContext) {
        if (obj == null) {
            return null;
        }
        else if (obj instanceof Obs) {
            return new ObsResult((Obs) obj, calculation, calculationContext);
        }
        else if (obj instanceof Collection) {
            ListResult ret = new ListResult();
            for (Object item : (Collection) obj) {
                ret.add(toCalculationResult(item, calculation, calculationContext));
            }
            return ret;
        }
        else {
            return new SimpleResult(obj, calculation, calculationContext);
        }
    }

    /**
     * @param calculationContext the calculation context
     * @param cohort the patient ids
     * @param parameterValues the parameters for the reporting context
     * @return the reporting evaluation context
     */
    protected static EvaluationContext ensureReportingContext(PatientCalculationContext calculationContext,
                                                              Collection<Integer> cohort, Map<String, Object> parameterValues) {
        EvaluationContext evaluationContext = new EvaluationContext();
        evaluationContext.setEvaluationDate(calculationContext.getNow());
        evaluationContext.setBaseCohort(new Cohort(cohort));
        evaluationContext.setParameterValues(parameterValues);
        calculationContext.addToCache("reportingEvaluationContext", evaluationContext);
        return evaluationContext;
    }
}
