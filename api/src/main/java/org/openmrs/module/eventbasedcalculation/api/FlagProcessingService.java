package org.openmrs.module.eventbasedcalculation.api;

import org.openmrs.Auditable;
import org.openmrs.OpenmrsObject;

import java.util.Collection;

public interface FlagProcessingService <K extends OpenmrsObject & Auditable>{

    void processFlags(Collection<K> collections);

    void processFlags(K object);
}
