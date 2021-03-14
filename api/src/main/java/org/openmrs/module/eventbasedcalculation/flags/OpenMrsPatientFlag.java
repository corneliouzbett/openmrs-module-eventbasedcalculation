package org.openmrs.module.eventbasedcalculation.flags;

import org.openmrs.Auditable;
import org.openmrs.OpenmrsObject;
import org.openmrs.Patient;

import java.util.Collection;

public interface OpenMrsPatientFlag<T extends Auditable & OpenmrsObject> {

    String getName();

    String getDisplay();

    boolean isActive();

    //String flagDefinition();

    boolean evaluate(T openMrsObject);

    void evaluate(Collection<T> tCollection);

    void createFlag(Patient patient);
}
