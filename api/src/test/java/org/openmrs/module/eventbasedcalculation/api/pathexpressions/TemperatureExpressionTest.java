/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.eventbasedcalculation.api.pathexpressions;


import org.hl7.fhir.r4.model.BooleanType;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TemperatureExpressionTest extends BaseExpressionEvaluatorTest {

    private static final String TEMPERATURE_OBSERVATION_RESOURCE = "observations/temperature_observation.json";

    /**
     * Absolute high - 43.0
     * Critical high - 38.0
     * Normal high - 37.5
     * Normal low - 36.0
     * Critical low - 35.0
     * Absolute low - 25.0
     * <p>
     * The expression will evaluates to true if the temperature value is greaterThan or equalTo (>=) 38 '°C' units respected.
     * N/B: this is an example reference ranges to be revise/review
     */
    private static final String TEMPERATURE_GREATER_THAN_EXPRESSION = "Observation.select(valueQuantity.value > 38 '°C').allTrue()";

    private static final String IS_TEMPERATURE_EXPRESSION = "Observation.code.coding.select(code = '5088AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA').anyTrue()";

    private static final String IS_TEMPERATURE_CODE_AND_SYSTEM_EXPRESSION = "Observation.code.coding.select(code = '8310-5' and system = 'http://loinc.org').anyTrue()";


    @Test
    public void shouldReturnCollectionWithTrueValueIfObservationValueIsGreaterThanTheSpecified() {
        setInputCollection(fromJson(TEMPERATURE_OBSERVATION_RESOURCE));
        List<BooleanType> result = evaluate(TEMPERATURE_GREATER_THAN_EXPRESSION, BooleanType.class);

        assertThat(result, notNullValue());
        assertThat(result, hasSize(1));
        assertThat(result, everyItem(hasProperty("value", is(true))));
    }

    @Test
    public void shouldReturnCollectionWithTrueValueIfObservationIsTemperature() {
        setInputCollection(fromJson(TEMPERATURE_OBSERVATION_RESOURCE));
        List<BooleanType> result = evaluate(IS_TEMPERATURE_EXPRESSION, BooleanType.class);

        assertThat(result, notNullValue());
        assertThat(result, hasSize(1));
        assertThat(result, everyItem(hasProperty("value", is(true))));
    }

    @Test
    public void shouldReturnCollectionWithTrueValueIfObservationIsTemperatureUsingCodeAndSystem() {
        setInputCollection(fromJson(TEMPERATURE_OBSERVATION_RESOURCE));
        List<BooleanType> result = evaluate(IS_TEMPERATURE_CODE_AND_SYSTEM_EXPRESSION, BooleanType.class);

        assertThat(result, notNullValue());
        assertThat(result, hasSize(1));
        assertThat(result, everyItem(hasProperty("value", is(true))));
    }

    @Test
    public void shouldReturnCollectionWithTrueValueIfObservationIsTemperatureAndGreaterThanTheSpecified() {
        setInputCollection(fromJson(TEMPERATURE_OBSERVATION_RESOURCE));
        List<BooleanType> result = evaluate(IS_TEMPERATURE_EXPRESSION +" and "+ TEMPERATURE_GREATER_THAN_EXPRESSION, BooleanType.class);

        assertThat(result, notNullValue());
        assertThat(result, hasSize(1));
        assertThat(result, everyItem(hasProperty("value", is(true))));
    }
}
