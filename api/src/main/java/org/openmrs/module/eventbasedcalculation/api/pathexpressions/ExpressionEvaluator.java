package org.openmrs.module.eventbasedcalculation.api.pathexpressions;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.instance.model.api.IBase;
import org.openmrs.api.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@Component
public class ExpressionEvaluator implements IFhirPath {

    @Autowired
    @Qualifier("fhirR4")
    private FhirContext fhirContext;

    public ExpressionEvaluator() {
        if (fhirContext == null) {
            fhirContext = Context.getRegisteredComponent("fhirR4", FhirContext.class);
        }
    }

    protected IFhirPath getFhirPath() {
        return getFhirContext().newFhirPath();
    }

    @Override
    public <T extends IBase> List<T> evaluate(IBase inputCollection, String expression, Class<T> aClass) {
        return getFhirPath().evaluate(inputCollection, expression, aClass);
    }

    @Override
    public <T extends IBase> Optional<T> evaluateFirst(IBase inputCollection, String expression, Class<T> aClass) {
        return getFhirPath().evaluateFirst(inputCollection, expression, aClass);
    }

    @Override
    public void parse(String s) throws Exception {
        getFhirPath().parse(s);
    }

}
