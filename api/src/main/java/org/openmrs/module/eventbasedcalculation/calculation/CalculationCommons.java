package org.openmrs.module.eventbasedcalculation.calculation;

import org.openmrs.Cohort;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
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

public class CalculationCommons {

    public static CalculationResultMap evaluateWithReporting(DataDefinition dataDefinition, Collection<Integer> cohort,
                                                             Map<String, Object> parameterValues, PatientCalculation calculation,
                                                             PatientCalculationContext calculationContext) throws EvaluationException {
            EvaluationContext reportingContext = ensureReportingContext(calculationContext, cohort, parameterValues);
            Map<Integer, Object> data;

            if (dataDefinition instanceof PersonDataDefinition) {
                EvaluatedPersonData result = Context.getService(PersonDataService.class).evaluate(
                        (PersonDataDefinition) dataDefinition, reportingContext);
                data = result.getData();
            }
            else if (dataDefinition instanceof PatientDataDefinition) {
                EvaluatedPatientData result = Context.getService(PatientDataService.class).evaluate(
                        (PatientDataDefinition) dataDefinition, reportingContext);
                data = result.getData();
            }
            else {
                throw new RuntimeException("Unknown DataDefinition type: " + dataDefinition.getClass());
            }

            CalculationResultMap ret = new CalculationResultMap();
            for (Integer ptId : cohort) {
                Object reportingResult = data.get(ptId);
                ret.put(ptId, new SimpleResult(reportingResult, calculation, calculationContext));
            }

            return ret;
    }

    protected static EvaluationContext ensureReportingContext(PatientCalculationContext calculationContext, Collection<Integer> cohort, Map<String, Object> parameterValues) {
        EvaluationContext ret = new EvaluationContext();
        ret.setEvaluationDate(calculationContext.getNow());
        ret.setBaseCohort(new Cohort(cohort));
        ret.setParameterValues(parameterValues);
        calculationContext.addToCache("reportingEvaluationContext", ret);
        return ret;
    }
}
