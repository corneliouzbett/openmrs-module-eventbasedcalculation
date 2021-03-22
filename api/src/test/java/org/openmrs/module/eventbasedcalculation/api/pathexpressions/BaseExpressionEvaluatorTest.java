package org.openmrs.module.eventbasedcalculation.api.pathexpressions;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.parser.IParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.instance.model.api.IBase;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.eventbasedcalculation.SpringTestConfiguration;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@ContextConfiguration(classes = SpringTestConfiguration.class, inheritLocations = false)
public class BaseExpressionEvaluatorTest extends BaseModuleContextSensitiveTest {

    protected ExpressionEvaluator evaluator;

    private IBase InputCollection;

    @Before
    public void setup() {
        evaluator =  new ExpressionEvaluator();
        FhirContext fhirContext = Context.getRegisteredComponent("fhirR4", FhirContext.class);
        evaluator.setFhirContext(fhirContext);
    }

    protected <T extends IBase> List<T> evaluate(String expression, Class<T> returnType) {
        return evaluate(getInputCollection(), expression, returnType);
    }

    protected  <T extends IBase> List<T> evaluate(IBase iBase, String expression, Class<T> returnType) {
        return evaluator.evaluate(iBase, expression, returnType);
    }

    protected IParser getParser() {
        return evaluator.getFhirContext().newJsonParser();
    }

    protected IBase fromJson(String resourceUrl) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceUrl);

        assertThat(inputStream, notNullValue());
        return getParser().parseResource(inputStream);
    }



    @Test
    public void shouldGetNonNullFhirContext() {
        assertThat(evaluator.getFhirContext(), notNullValue());
    }

    @Test
    public void shouldGetFhirContextR4Version() {
        assertThat(evaluator.getFhirContext().getVersion().getVersion(), is(FhirVersionEnum.R4));
    }

    @Test
    public void shouldGetNonNullFhirPathEvaluatorEngineForR4() {
        assertThat(evaluator.getFhirPath(), notNullValue());
    }

}
