/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.eventbasedcalculation.flags.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.BooleanType;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eventbasedcalculation.EventBasedCalculationConstants;
import org.openmrs.module.eventbasedcalculation.api.calculations.vitals.TemperatureCalculation;
import org.openmrs.module.eventbasedcalculation.flags.OpenMrsPatientFlag;
import org.openmrs.module.fhir2.api.translators.EncounterTranslator;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Slf4j
@Setter
@Getter
public class TemperaturePatientFlag extends BasePatientFlag implements OpenMrsPatientFlag<Encounter> {

    //private EncounterTranslator<Encounter> encounterTranslator;

    private String expression = "Observation.code.coding.select(code = '8310-5' and system = 'http://loinc.org').anyTrue() and Observation.select(valueQuantity.value > 38 '°C').allTrue()";

    private String display;

    private String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplay() {
        return this.display;
    }

    @Override
    public boolean isEnabled() {
        return adminService()
                .getGlobalPropertyValue(EventBasedCalculationConstants.TEMPERATURE_FLAG, false);
    }

    @Override
    public void evaluate(Encounter encounter) {
        // Evaluation based on reporting & calculation module
        Set<Integer> patientIds = getPatientIds(Collections.singleton(encounter.getPatient()));
        CalculationResultMap resultMap = calculate(getCalculation(TemperatureCalculation.class), patientIds);
        patientIds.forEach(patientId -> handlePatientTemperature(resultMap.get(patientId).asType(Obs.class)));

        // Evaluation based on FHIRPath Expression
        //getExpressionEvaluator().evaluate(encounterTranslator.toFhirResource(encounter), expression, BooleanType.class);

    }

    @Override
    public void evaluate(Collection<Encounter> encounters) {
        encounters.forEach(this::evaluate);
    }

    @Override
    public void createFlag(Integer patientId) {
        log.info("Creating new flag for patient with ID {}", patientId);
        // insert new_patient_flag into fhir_flags table
    }

    @Override
    public void createFlag(Collection<Integer> cohort) {
        cohort.forEach(this::createFlag);
    }

    void handlePatientTemperature(Obs obs) {
        if (isInNormalNumericRange(obs.getValueNumeric(), getConceptNumeric(obs.getConcept()))) {
            log.info("Normal temperature {}", obs.getValueNumeric());
        } else if (isInCriticalNumericRange(obs.getValueNumeric(), getConceptNumeric(obs.getConcept()))) {
            log.info("The patient has mild fever {}", obs.getValueNumeric());

            // Create a flag with medium priority, this maybe vary
            createFlag(obs.getPersonId());

        } else if (isInAbsoluteNumericRange(obs.getValueNumeric(), getConceptNumeric(obs.getConcept()))) {
            log.info("In absolute range {}", obs.getValueNumeric());

            // probably create a flag with different priority & custom message with recommended actions
        } else {
            log.info("Invalid obs value");
        }
    }
}
