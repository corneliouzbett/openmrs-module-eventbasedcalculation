package org.openmrs.module.eventbasedcalculation.flags;

import org.openmrs.Auditable;
import org.openmrs.Encounter;

import java.util.Map;
import java.util.Set;

public class PatientFlagsProcessor {

    private PatientFlagProvider provider;

    public PatientFlagsProcessor() {
        provider = new PatientFlagProvider();
    }

    public void processFlags(Encounter openmrsObject) {
        Set<Map.Entry<String, Class<? extends PatientFlag<? extends Auditable>>>> entries = provider.getFlags().entrySet();

        for (Map.Entry<String, Class<? extends PatientFlag<? extends Auditable>>> entry : entries) {
            Class<? extends PatientFlag<?>> clazz = entry.getValue();
            if (clazz != null) {
                try {
                    PatientFlag<?> patientFlag = clazz.newInstance();
                   // patientFlag.evaluate(openmrsObject);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
