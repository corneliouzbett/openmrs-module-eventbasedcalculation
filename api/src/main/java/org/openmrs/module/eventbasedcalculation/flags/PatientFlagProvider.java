package org.openmrs.module.eventbasedcalculation.flags;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openmrs.Auditable;
import org.openmrs.OpenmrsObject;
import org.openmrs.module.eventbasedcalculation.flags.impl.DeceasedPatientFlag;
import org.openmrs.module.eventbasedcalculation.flags.impl.DueForVLPatientFlag;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PatientFlagProvider<T extends OpenmrsObject & Auditable> {

    // In-memory patient flags store
    // TODO refactor - use a queue
    @Getter(AccessLevel.PUBLIC)
    private Map<String, Class<? extends PatientFlag<T>>> flags = new HashMap<>();

    public PatientFlagProvider() {
        this.addFlags(Arrays.asList(new Class[]{DeceasedPatientFlag.class, DueForVLPatientFlag.class}));
    }

    public void addFlag(Class<? extends PatientFlag<T>> clazz) {
        this.flags.put(clazz.getSimpleName(), clazz);
    }

    public void addFlags(Collection<Class<? extends PatientFlag<T>>> classCollection) {
        classCollection.forEach(this::addFlag);
    }
}
