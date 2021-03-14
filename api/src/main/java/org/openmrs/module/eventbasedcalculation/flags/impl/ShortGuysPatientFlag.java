package org.openmrs.module.eventbasedcalculation.flags.impl;

import lombok.extern.slf4j.Slf4j;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.module.eventbasedcalculation.flags.OpenMrsPatientFlag;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class ShortGuysPatientFlag implements OpenMrsPatientFlag<Encounter> {

    @Override
    public String getName() {
        return "Patient with height less than 100(CM)";
    }

    @Override
    public String getDisplay() {
        return null;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean evaluate(Encounter encounter) {
        log.error("Evaluating patient flag : {}", getName());
        return false;
    }

    @Override
    public void evaluate(Collection<Encounter> encounters) {

    }

    @Override
    public void createFlag(Patient patient) {

    }
}
