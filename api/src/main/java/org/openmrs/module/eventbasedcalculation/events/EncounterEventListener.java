/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.eventbasedcalculation.events;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openmrs.Encounter;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.event.EventListener;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.eventbasedcalculation.api.FlagProcessingService;
import org.openmrs.module.eventbasedcalculation.api.impl.EncounterFlagProcessingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

@Slf4j
@Component
@NoArgsConstructor
public class EncounterEventListener implements EventListener {

    @Autowired
    private EncounterService encounterService;

    private DaemonToken daemonToken;

    @Autowired
    @Qualifier("EventBasedCalculation.EncounterFlagProcessingService")
    private FlagProcessingService<Encounter> processingService;

    public EncounterEventListener(DaemonToken daemonToken) {
        this.daemonToken = daemonToken;
        this.encounterService = Context.getEncounterService();

        if (processingService == null) {
            log.info("Encounter processing service is NUll");
            this.processingService = new EncounterFlagProcessingServiceImpl();
        }
    }

    @Override
    public void onMessage(Message message) {
        log.debug("Encounter event created");
        Daemon.runInDaemonThread(() -> {
            try {
                processMessage(message);
            } catch (Exception exception) {
                log.error("Error occurred processing Encounter created", exception);
            }
        }, daemonToken);
    }

    public void processMessage(Message message) throws JMSException {
        String encounterUuid = ((MapMessage) message).getString("uuid");
        log.info("Created encounter uuid: {} ", encounterUuid);

        Encounter encounter = encounterService.getEncounterByUuid(encounterUuid);
        log.info("Parsed Encounter created:  {} ", encounter.toString());

        processingService.processFlags(encounter);
    }
}
