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
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eventbasedcalculation.api.calculations.bloodpressure.DiastolicBloodPressureCalculation;
import org.openmrs.module.eventbasedcalculation.api.calculations.bloodpressure.SystolicBloodPressureCalculation;
import org.openmrs.module.eventbasedcalculation.flags.OpenMrsPatientFlag;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * For demonstration purposes. This blood pressure ranges aren't accurate (maybe)
 * Low blood pressure is less common
 *
 * NORMAL - less than 120 systolic(upper #) and less than 80 diastolic(Lower #)
 * ELEVATED - 120-129 systolic(upper #) and less than 80 diastolic(Lower #)
 * HYPERTENSION (Stage 1) - 130-139 systolic(upper #) or 80-89 diastolic(Lower #)
 * HYPERTENSION (Stage 2) - 140 or higher or 90 or higher
 * HYPERTENSIVE CRISIS - higher than 180 or higher than 120 diastolic(Lower #)
 */

@Slf4j
@Component
@Setter(AccessLevel.MODULE)
@Getter(AccessLevel.MODULE)
public class AbnormalBloodPressurePatientFlag extends BasePatientFlag implements OpenMrsPatientFlag<Patient> {

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

        patientIds.forEach( patientId -> {
            Obs systolicObs = systolicMap.get(patientId).asType(Obs.class);
            log.info("Systolic blood pressure {}", systolicObs.getValueNumeric());

            Obs diastolicObs = diastolicMap.get(patientId).asType(Obs.class);
            log.info("Diastolic blood pressure {}", diastolicObs.getValueNumeric());

            handlingBloodPressure(diastolicObs, systolicObs);
        });


    }

    private void handlingBloodPressure(Obs diastolicObs, Obs systolicObs) {
        if (systolicObs.getValueNumeric() > 180.00 || diastolicObs.getValueNumeric() > 120.00) {
            // Hypertensive crisis (call 911)

        } else if (isBetween(systolicObs.getValueNumeric(), 120.00, 129.00)
        && diastolicObs.getValueNumeric() < 80.0) {
            // Elevated blood pressure
        }
    }

    @Override
    public void createFlag(Integer patientId) {

    }

    @Override
    public void createFlag(Collection<Integer> cohort) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDisplay() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
