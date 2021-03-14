package org.openmrs.module.eventbasedcalculation.flags;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openmrs.Encounter;
import org.openmrs.module.eventbasedcalculation.flags.impl.BasePatientFlag;
import org.openmrs.module.eventbasedcalculation.flags.impl.DueForVLPatientFlag;
import org.openmrs.module.eventbasedcalculation.flags.impl.ShortGuysPatientFlag;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@Slf4j
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class PatientFlagProcessor {

    private DueForVLPatientFlag dueForVLPatientFlag;

    private ShortGuysPatientFlag shortGuysPatientFlag;

    private Collection<Class<? extends BasePatientFlag>> flags;

    private final Reflections reflections;

    private static final String BASE_PACKAGE_FLAGS = "org.openmrs.module.eventbasedcalculation.flags";

    public PatientFlagProcessor() {
        this.flags = new ArrayList<>();
        this.reflections = new Reflections(BASE_PACKAGE_FLAGS);
        this.dueForVLPatientFlag = new DueForVLPatientFlag();
        this.shortGuysPatientFlag = new ShortGuysPatientFlag();
    }

    @SafeVarargs
    public final void registerPatientFlags(Class<? extends BasePatientFlag>... flags) {
        this.flags.addAll(Arrays.asList(flags));
    }

    public void processFlags(Encounter encounter) {
        log.error("Number of patient flags: {} ", flags.size());
        if (this.getFlags().isEmpty()) {
            Set<Class<? extends BasePatientFlag>> classes = reflections.getSubTypesOf(BasePatientFlag.class);
           this.flags.addAll(classes);
            log.error("Number of patient flags: {} ", flags.size());
        }

        dueForVLPatientFlag.evaluate(encounter);
        shortGuysPatientFlag.evaluate(encounter);

    }

}
