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
import org.openmrs.Patient;
import org.openmrs.module.eventbasedcalculation.api.FlagProcessingService;
import org.openmrs.module.eventbasedcalculation.flags.impl.DeceasedPatientFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class PatientFlagProcessingServiceImpl implements FlagProcessingService<Patient> {

    @Autowired
    private DeceasedPatientFlag deceasedPatientFlag;

    public PatientFlagProcessingServiceImpl() {
        // TODO
        // @Autowired not wiring
        this.deceasedPatientFlag = new DeceasedPatientFlag();
    }

    @Override
    public void processFlags(Collection<Patient> patients) {
        deceasedPatientFlag.evaluate(patients);
    }

    @Override
    public void processFlags(Patient patient) {
        deceasedPatientFlag.evaluate(patient);
    }
}
