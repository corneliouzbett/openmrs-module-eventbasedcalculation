package org.openmrs.module.eventbasedcalculation.api.calculations;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.calculation.Calculation;
import org.openmrs.calculation.CalculationProvider;
import org.openmrs.calculation.ConfigurableCalculation;
import org.openmrs.calculation.InvalidCalculationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CalculationsProvider implements CalculationProvider {

    private final Map<String, Class<? extends Calculation>> calculations = new HashMap<>();

    public CalculationsProvider() {
        calculations.put(DeceasedPatientCalculation.class.getSimpleName(), DeceasedPatientCalculation.class);
    }

    @Override
    public Calculation getCalculation(String calculationName, String configuration) {

        if (calculationName != null) {
            Class<? extends Calculation> clazz = calculations.get(calculationName);
            if (clazz != null) {
                try {
                    Calculation calculation = clazz.newInstance();
                    handleConfigurableCalculations(calculationName, configuration, calculation);
                    return calculation;
                }
                catch (Exception e) {
                    log.error("Error getting calculation {}, {}", calculationName, e);
                    return null;
                }
            }

        }
        return null;
    }

    private void handleConfigurableCalculations(String calculationName, String configuration, Calculation calculation) throws InvalidCalculationException {
        if (StringUtils.isNotBlank(configuration)) {
            if (calculation instanceof ConfigurableCalculation) {
                ((ConfigurableCalculation) calculation).setConfiguration(configuration);
            }
            else {
                if (StringUtils.isNotBlank(configuration)) {
                    throw new InvalidCalculationException(this, calculationName, configuration);
                }
            }
        }
    }
}
