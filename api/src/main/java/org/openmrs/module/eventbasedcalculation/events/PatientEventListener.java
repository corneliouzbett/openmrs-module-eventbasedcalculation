package org.openmrs.module.eventbasedcalculation.events;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.event.EventListener;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.eventbasedcalculation.api.FlagProcessingService;
import org.openmrs.module.eventbasedcalculation.api.impl.PatientFlagProcessingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

@Slf4j
@Component
@NoArgsConstructor
public class PatientEventListener implements EventListener {

    private DaemonToken daemonToken;

    private PatientService patientService;

    @Autowired
    private FlagProcessingService<Patient> processingService;

    public PatientEventListener(DaemonToken daemonToken) {
        this.daemonToken = daemonToken;
        this.patientService = Context.getPatientService();

        if (processingService == null) {
           this.processingService = new PatientFlagProcessingServiceImpl();
        }
    }

    @Override
    public void onMessage(Message message) {
        log.error("Patient record updated event");
        Daemon.runInDaemonThread( () -> {
            try {
                processMessage(message);
            } catch (Exception exception) {
                log.error("Error occurred processing Patient updated", exception);
            }
        }, daemonToken);
    }

    private void processMessage(Message message) throws JMSException {
        String patientUuid = ((MapMessage) message).getString("uuid");
        log.error("Updated patient uuid: {} ", patientUuid);

        Patient patient = patientService.getPatientByUuid(patientUuid);
        log.error("Parsed patient updated:  {} ", patient.toString());

        this.processingService.processFlags(patient);

        //PatientFlagProvider provider = new PatientFlagProvider();
    }
}