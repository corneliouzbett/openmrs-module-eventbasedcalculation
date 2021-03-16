/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.eventbasedcalculation.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.module.eventbasedcalculation.api.FlagProcessingService;
import org.openmrs.module.eventbasedcalculation.flags.impl.AbnormalBloodPressurePatientFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Component("EventBasedCalculation.EncounterFlagProcessingService")
public class EncounterFlagProcessingServiceImpl implements FlagProcessingService<Encounter> {

    @Autowired
    @Qualifier("EventBasedCalculation.AbnormalBloodPressurePatientFlag")
    private AbnormalBloodPressurePatientFlag bloodPressurePatientFlag;

    public EncounterFlagProcessingServiceImpl() {
        if (this.bloodPressurePatientFlag == null) {
            //Autowiring not working
            this.bloodPressurePatientFlag = new AbnormalBloodPressurePatientFlag();
        }
    }


    @Override
    public void processFlags(Collection<Encounter> encounters) {
        Collection<Patient> patients = new ArrayList<>();
        encounters.forEach(encounter -> patients.add(encounter.getPatient()));
        if (bloodPressurePatientFlag.isEnabled())
            bloodPressurePatientFlag.evaluate(patients);
    }

    @Override
    public void processFlags(Encounter encounter) {
        if (bloodPressurePatientFlag.isEnabled())
            bloodPressurePatientFlag.evaluate(encounter.getPatient());

    }
}
