package org.openmrs.module.eventbasedcalculation.flags;

import org.openmrs.Auditable;
import org.openmrs.OpenmrsObject;

import java.util.Collection;

/**
 * Marker interface
 */
public interface PatientFlag<W extends Auditable & OpenmrsObject> {

    void evaluate(W openMrsObject);

    void evaluate(Collection<W> wCollection);

    void createFlag(Integer patientId);

    void createFlag(Collection<Integer> cohort);
}
