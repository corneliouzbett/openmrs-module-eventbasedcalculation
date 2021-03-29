package org.openmrs.module.eventbasedcalculation.flags.impl;

import org.openmrs.Encounter;
import org.openmrs.module.eventbasedcalculation.flags.OpenMrsPatientFlag;

import java.util.Collection;

public class DueForVLPatientFlag extends BasePatientFlag implements OpenMrsPatientFlag<Encounter> {

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

    @Override
    public void evaluate(Encounter openMrsObject) {

    }

    @Override
    public void evaluate(Collection<Encounter> encounters) {

    }

    @Override
    public void createFlag(Integer patientId) {

    }

    @Override
    public void createFlag(Collection<Integer> cohort) {

    }
}
