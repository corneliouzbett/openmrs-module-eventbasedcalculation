package org.openmrs.module.eventbasedcalculation.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openmrs.BaseOpenmrsMetadata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Not certain if this table is necessary
 * It act as a store for fhir expressions - provides samples & reusable expressions.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "fhir_expression")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class FhirExpression extends BaseOpenmrsMetadata {

    private static final long serialVersionUID = 456112L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fhir_expression_id")
    private Integer id;

    @Column(name = "expression")
    private String expression;

    @Column(name = "return_type", nullable = false)
    private String returnType;
}
