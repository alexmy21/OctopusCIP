/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.processor.impl;

import org.lisapark.octopus.ProgrammerException;
import org.lisapark.octopus.core.Persistable;
import org.lisapark.octopus.core.ValidationException;
import org.lisapark.octopus.core.event.Event;
import org.lisapark.octopus.core.runtime.ProcessorContext;

import java.util.Map;
import java.util.UUID;
import org.lisapark.octopus.core.processor.CompiledProcessor;
import org.lisapark.octopus.core.processor.Processor;
import org.lisapark.octopus.core.processor.ProcessorInput;
import org.lisapark.octopus.core.processor.ProcessorOutput;

/**
 * This {@link Processor} is used to adding two inputs together and producing an output.
 * <p/>
 * Addition is a mathematical operation that represents combining collections of objects together into a larger
 * collection. It is signified by the plus sign (+).
 *
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public class Or extends Processor<Void> {
//    private static final String DEFAULT_NAME = "Addition";
//    private static final String DEFAULT_DESCRIPTION = "Add 2 operands";
    
    private static final String DEFAULT_NAME = "Or";
    private static final String DEFAULT_DESCRIPTION = "Logical \"OR\"";

    /**
     * Addition takes two inputs
     */
    private static final int FIRST_INPUT_ID = 1;
    private static final int SECOND_INPUT_ID = 2;
    
    private static final int OUTPUT_ID = 1;

    protected Or(UUID id, String name, String description) {
        super(id, name, description);
    }

    protected Or(UUID id, Or additionToCopy) {
        super(id, additionToCopy);
    }

    protected Or(Or additionToCopy) {
        super(additionToCopy);
    }

    public ProcessorInput getFirstInput() {
        // there are two inputs for addition
        return getInputs().get(0);
    }

    public ProcessorInput getSecondInput() {
        // there are two inputs for addition
        return getInputs().get(1);
    }

    @Override
    public Or newInstance() {
        return new Or(UUID.randomUUID(), this);
    }

    @Override
    public Or copyOf() {
        return new Or(this);
    }

    @Override
    public CompiledProcessor<Void> compile() throws ValidationException {
        validate();

        // we copy all the inputs and output taking a "snapshot" of this processor so we are isolated of changes
        Or copy = copyOf();

        return new CompiledAddition(copy);
    }

    /**
     * Returns a new {@link Addition} processor configured with all the appropriate
     * {@link org.lisapark.octopus.core.parameter.Parameter}s, {@link org.lisapark.octopus.core.Input}s and {@link org.lisapark.octopus.core.Output}.
     *
     * @return new {@link Addition}
     */
    public static Or newTemplate() {
        UUID processorId = UUID.randomUUID();
        Or addition = new Or(processorId, DEFAULT_NAME, DEFAULT_DESCRIPTION);

        // two double inputs
        ProcessorInput<Boolean> firstInput = ProcessorInput.booleanInputWithId(FIRST_INPUT_ID).name("First operand").description("First operand").build();
        addition.addInput(firstInput);

        ProcessorInput<Boolean> secondInput = ProcessorInput.booleanInputWithId(SECOND_INPUT_ID).name("Second operand").description("Second operand").build();
        addition.addInput(secondInput);

        addition.addJoin(firstInput, secondInput);

        // double output
        try {
            addition.setOutput(ProcessorOutput.doubleOutputWithId(OUTPUT_ID).nameAndDescription("Result").attributeName("OR"));
        } catch (ValidationException ex) {
            // this should NOT happen. It means we created the Addition with an invalid attribute name
            throw new ProgrammerException(ex);
        }

        return addition;
    }

    static class CompiledAddition extends CompiledProcessor<Void> {
        private final String firstAttributeName;
        private final String secondAttributeName;

        protected CompiledAddition(Or addition) {
            super(addition);

            firstAttributeName = addition.getFirstInput().getSourceAttributeName();
            secondAttributeName = addition.getSecondInput().getSourceAttributeName();
        }

        @Override
        public Object processEvent(ProcessorContext<Void> ctx, Map<Integer, Event> eventsByInputId) {
            Event firstEvent = eventsByInputId.get(FIRST_INPUT_ID);
            Event secondEvent = eventsByInputId.get(SECOND_INPUT_ID);

            Boolean firstOperand = firstEvent.getAttributeAsBoolean(firstAttributeName);
            Boolean secondOperand = secondEvent.getAttributeAsBoolean(secondAttributeName);

            firstOperand = (firstOperand == null) ? false : firstOperand;
            secondOperand = (secondOperand == null) ? false : secondOperand;

            return firstOperand || secondOperand;
        }
    }
}
