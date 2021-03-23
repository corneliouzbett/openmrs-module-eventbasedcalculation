package org.openmrs.module.eventbasedcalculation.flags;

import org.openmrs.Auditable;
import org.openmrs.OpenmrsObject;

public interface OpenMrsPatientFlag<T extends Auditable & OpenmrsObject> extends PatientFlag<T> {

    String getName();

    String getDisplay();

    //FhirFlag.FlagPriority getPriority();

    boolean isEnabled();
}
