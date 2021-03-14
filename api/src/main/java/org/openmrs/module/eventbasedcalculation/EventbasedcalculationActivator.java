/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.eventbasedcalculation;

import lombok.extern.slf4j.Slf4j;
import org.openmrs.Encounter;
import org.openmrs.event.Event;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.DaemonTokenAware;
import org.openmrs.module.eventbasedcalculation.events.EncounterEventListener;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
@Slf4j
public class EventbasedcalculationActivator extends BaseModuleActivator implements DaemonTokenAware {

	private DaemonToken daemonToken;

	private EncounterEventListener encounterEventListener;

	private static final Class<Encounter> encounterClazz = Encounter.class;

	private static final Event.Action ACTION = Event.Action.CREATED;

	/**
	 * @see #started()
	 */
	@Override
	public void started() {
		log.info("Started Event based calculation");
		encounterEventListener = new EncounterEventListener(daemonToken);
		Event.subscribe(encounterClazz, ACTION.name(), encounterEventListener);
	}

	/**
	 * @see #stopped() ()
	 */
	@Override
	public void stopped() {
		log.info("stopped Event based calculation");
		log.info("Removing Encounter event listener");
		Event.unsubscribe(encounterClazz, ACTION, encounterEventListener);
	}

	@Override
	public void setDaemonToken(DaemonToken daemonToken) {
		this.daemonToken = daemonToken;
	}
}
