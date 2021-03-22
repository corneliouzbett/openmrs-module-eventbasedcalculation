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

import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.BooleanType;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class AbnormalBloodPressureExpressionTest extends BaseExpressionEvaluatorTest {

    private static final String SYSTOLIC_OBSERVATION_RESOURCE = "observations/systolic_blood_pressure_observation.json";

    private static final String DIASTOLIC_OBSERVATION_RESOURCE = "observations/diastolic_blood_pressure_observation.json";

    private static final String DIASTOLIC_SYSTOLIC_BUNDLE_RESOURCE = "observations/diastolic_systolic_bundle_resource.json";

    private static final String SYSTOLIC_CODE = "5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    private static final String DIASTOLIC_CODE = "5086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    //private static final String HAS_CODE_EXPRESSION = "Observation.code.where(code = '5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA').exists()";

    private static final String DIASTOLIC_PATH_EXPRESSION = "Observation.code.coding.select(code = '5086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA').anyTrue() and Observation.select(valueQuantity.value > 80 'mm[Hg]').allTrue()";

    private static final String SYSTOLIC_PATH_EXPRESSION = "Observation.code.coding.select(code = '5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA').anyTrue() and Observation.select(valueQuantity.value > 120 'mm[Hg]').allTrue()";

    String hasCodeExpression(String code) {
        return ("Observation.code.coding.select(code = '" + code + "').anyTrue()");
    }

    String hasCodeAndSystemExpression(String code, String system) {
        return ("Observation.code.coding.select(code = '" + code + "' and system = '" + system + "').anyTrue()");
    }

    @Test
    public void shouldReturnCollectionWithTrueValueIfObservationIsSystolic() {
        setInputCollection(fromJson(SYSTOLIC_OBSERVATION_RESOURCE));
        List<BooleanType> result = evaluate(hasCodeExpression(SYSTOLIC_CODE), BooleanType.class);

        assertThat(result, notNullValue());
        assertThat(result, hasSize(1));
        //assertThat(result, everyItem(is(true)));
        result.forEach(item -> {
            assertThat(item.booleanValue(), is(true));
        });
    }

    @Test
    public void shouldReturnCollectionWithTrueValueIfObservationIsDiastolic() {
        log.info("Expression: {}", hasCodeExpression(DIASTOLIC_CODE));
        setInputCollection(fromJson(DIASTOLIC_OBSERVATION_RESOURCE));
        List<BooleanType> result = evaluate(hasCodeExpression(DIASTOLIC_CODE), BooleanType.class);

        assertThat(result, notNullValue());
        assertThat(result, hasSize(1));
        //assertThat(result, hasItems(everyItem(is(true))));
        result.forEach(item -> {
            assertThat(item.booleanValue(), is(true));
        });

        //"system": "http://loinc.org", "code": "35094-2",
        result = evaluate(hasCodeAndSystemExpression("35094-2", "http://loinc.org"), BooleanType.class);
        assertThat(result, notNullValue());
        assertThat(result, hasSize(1));
        //assertThat(result, hasItems(everyItem(is(true))));
        result.forEach(item -> {
            assertThat(item.booleanValue(), is(true));
        });
    }

    @Test
    public void shouldReturnTrueIfObservationValueQuantityIsHigh() {
        setInputCollection(fromJson(DIASTOLIC_SYSTOLIC_BUNDLE_RESOURCE));
        List<BooleanType> result = evaluate(DIASTOLIC_PATH_EXPRESSION, BooleanType.class);

        assertThat(result, notNullValue());
        // Add more assertions
    }

    @Test
    public void shouldReturnTrueIfSystolicBloodPressureIsHigh() {
        setInputCollection(fromJson(DIASTOLIC_SYSTOLIC_BUNDLE_RESOURCE));
        List<BooleanType> result = evaluate(SYSTOLIC_PATH_EXPRESSION, BooleanType.class);

        assertThat(result.isEmpty(), is(false));
        // Add more assertions
    }
}
