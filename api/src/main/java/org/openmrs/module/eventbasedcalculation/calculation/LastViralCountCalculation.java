package org.openmrs.module.eventbasedcalculation.calculation;

import lombok.extern.slf4j.Slf4j;
import org.openmrs.Concept;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eventbasedcalculation.EventBasedCalculationConstants;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Component
public class LastViralCountCalculation extends BasePatientCalculation {

    private final ConceptService conceptService;

    private final AdministrationService administrationService;

    public LastViralCountCalculation() {
        this.conceptService = Context.getConceptService();
        this.administrationService = Context.getAdministrationService();
    }

    @Override
    public CalculationResultMap evaluate(Collection<Integer> collection, Map<String, Object> map, PatientCalculationContext patientCalculationContext) {
        String cielViralLoadConceptInternalUUID = administrationService.getGlobalProperty(
                EventBasedCalculationConstants.CONCEPT_VIRAL_LOAD, "");
        Concept concept = conceptService.getConceptByUuid(cielViralLoadConceptInternalUUID);

        log.error("LastViralCountCalc: InternalUUID {}", cielViralLoadConceptInternalUUID);
        log.error("LastViralCountCalc: concept {}", concept.getConceptId());

        return lastObs(concept, collection, patientCalculationContext);
    }
}
