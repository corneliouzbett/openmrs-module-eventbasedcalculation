package org.openmrs.module.eventbasedcalculation.api.calculations.shared;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eventbasedcalculation.api.calculations.util.CalculationUtils;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.patient.definition.EncountersForPatientDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ObsForPersonDataDefinition;

import java.util.Collection;

public class Calculation {

    Calculation() {}

    public static CalculationResultMap lastObs(Concept concept, Collection<Integer> cohort, PatientCalculationContext context) {
        ObsForPersonDataDefinition def = new ObsForPersonDataDefinition("last obs", TimeQualifier.LAST, concept, context.getNow(), null);
        return CalculationUtils.evaluateWithReporting(def, cohort, null, null, context);
    }

    public static CalculationResultMap lastEncounter(EncounterType encounterType, Collection<Integer> cohort, PatientCalculationContext context) {
        EncountersForPatientDataDefinition encountersForPatientDataDefinition = new EncountersForPatientDataDefinition();
        if (encounterType != null) {
            encountersForPatientDataDefinition.setName("last encounter of type " + encounterType.getName());
            encountersForPatientDataDefinition.addType(encounterType);
        }
        else {
            encountersForPatientDataDefinition.setName("last encounter of any type");
        }
        encountersForPatientDataDefinition.setWhich(TimeQualifier.LAST);
        encountersForPatientDataDefinition.setOnOrBefore(context.getNow());
        return CalculationUtils.evaluateWithReporting(encountersForPatientDataDefinition, cohort, null, null, context);
    }
}
