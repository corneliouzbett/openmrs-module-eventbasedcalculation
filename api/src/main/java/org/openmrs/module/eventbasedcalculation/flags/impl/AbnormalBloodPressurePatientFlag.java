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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eventbasedcalculation.EventBasedCalculationConstants;
import org.openmrs.module.eventbasedcalculation.api.calculations.bloodpressure.DiastolicBloodPressureCalculation;
import org.openmrs.module.eventbasedcalculation.api.calculations.bloodpressure.SystolicBloodPressureCalculation;
import org.openmrs.module.eventbasedcalculation.flags.OpenMrsPatientFlag;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * For demonstration purposes. This blood pressure ranges aren't accurate (maybe)
 */

@Slf4j
@Setter(AccessLevel.MODULE)
@Getter(AccessLevel.MODULE)
@Component("EventBasedCalculation.AbnormalBloodPressurePatientFlag")
public class AbnormalBloodPressurePatientFlag extends BasePatientFlag implements OpenMrsPatientFlag<Patient> {

   // private FhirFlag.FlagPriority priority;

    private String display;

    //@Autowired
    //private FhirFlagService flagService;

    @Override
    public void createFlag(Integer patientId) {
        log.info("create patient flag record");
        //flagService.create(flagTranslator.toFhirResource(buildPatientFlag(getPatient(patientId))));
    }

    @Override
    public void createFlag(Collection<Integer> cohort) {
        cohort.forEach(this::createFlag);
    }

    @Override
    public String getName() {
        return "Abnormal blood pressure flag";
    }

    @Override
    public String getDisplay() {
        return this.display;
    }

//    @Override
//    public FhirFlag.FlagPriority getPriority() {
//        return this.priority;
//    }

    @Override
    public boolean isEnabled() {
        return adminService()
                .getGlobalPropertyValue(EventBasedCalculationConstants.ABNORMAL_BLOOD_PRESSURE_FLAG, false);
    }

    @Override
    public void evaluate(Patient patient) {
        this.evaluate(Collections.singleton(patient));
    }

    @Override
    public void evaluate(Collection<Patient> patients) {
        Set<Integer> patientIds = getPatientIds(patients);
        PatientCalculation diastolicCalculation = getCalculation(DiastolicBloodPressureCalculation.class);
        PatientCalculation systolicCalculation = getCalculation(SystolicBloodPressureCalculation.class);

        CalculationResultMap systolicMap = calculate(systolicCalculation, patientIds);
        CalculationResultMap diastolicMap = calculate(diastolicCalculation, patientIds);

        patientIds.forEach(patientId -> {
            Obs systolicObs = systolicMap.get(patientId).asType(Obs.class);
            log.info("Systolic blood pressure {}", systolicObs.getValueNumeric());

            Obs diastolicObs = diastolicMap.get(patientId).asType(Obs.class);
            log.info("Diastolic blood pressure {}", diastolicObs.getValueNumeric());

            handlingBloodPressure(diastolicObs, systolicObs);
        });


    }

    void handlingBloodPressure(Obs diastolicObs, Obs systolicObs) {
        Double systolicValue = systolicObs.getValueNumeric();
        Double diastolicValue = diastolicObs.getValueNumeric();
        if (isNumeric(diastolicObs.getConcept(), systolicObs.getConcept())) {
            ConceptNumeric systolicCN = getConceptNumeric(systolicObs.getConcept());
            ConceptNumeric diastolicCN = getConceptNumeric(diastolicObs.getConcept());

            if (isInNormalNumericRange(systolicValue, systolicCN) && isInNormalNumericRange(diastolicValue, diastolicCN)) {
                log.info("Blood pressure {}/{} mmHg is within Normal range", systolicValue.intValue(), diastolicValue.intValue());
            } else if (isInCriticalNumericRange(systolicValue, systolicCN) && isInCriticalNumericRange(diastolicValue, diastolicCN)) {
                log.info("Blood pressure {}/{} mmHg is Critical levels", systolicValue.intValue(), diastolicValue.intValue());
                //setPriority(FhirFlag.FlagPriority.MEDIUM);
                setDisplay("Patient has critical BP");
                createFlag(systolicObs.getPersonId());
            } else if (isInAbsoluteNumericRange(systolicValue, systolicCN) && isInAbsoluteNumericRange(diastolicValue, diastolicCN)) {
                //setPriority(FhirFlag.FlagPriority.HIGH);
                setDisplay("Patient has absolute high BP");
                createFlag(systolicObs.getPersonId());
                log.info("Blood pressure {}/{} mmHg is within the absolute range", systolicValue.intValue(), diastolicValue.intValue());
            }
        }
    }

    boolean isInAbsoluteNumericRange(Double value, ConceptNumeric conceptNumeric) {
        return OpenmrsUtil.isInAbsoluteNumericRange(value.floatValue(), conceptNumeric);
    }

    boolean isInCriticalNumericRange(Double value, ConceptNumeric conceptNumeric) {
        return OpenmrsUtil.isInCriticalNumericRange(value.floatValue(), conceptNumeric);
    }

    boolean isInNormalNumericRange(Double value, ConceptNumeric conceptNumeric) {
        return OpenmrsUtil.isInNormalNumericRange(value.floatValue(), conceptNumeric);
    }

    boolean isNumeric(Concept diastolicConcept, Concept systolicConcept) {
        return diastolicConcept.isNumeric() && systolicConcept.isNumeric();
    }

    ConceptNumeric getConceptNumeric(Concept concept) {
        return Context.getConceptService().getConceptNumeric(concept.getConceptId());
    }
}
