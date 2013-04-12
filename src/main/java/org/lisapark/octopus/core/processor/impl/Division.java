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

import java.util.Map;
import java.util.UUID;
import org.lisapark.octopus.ProgrammerException;
import org.lisapark.octopus.core.ValidationException;
import org.lisapark.octopus.core.event.Event;
import org.lisapark.octopus.core.processor.CompiledProcessor;
import org.lisapark.octopus.core.processor.Processor;
import org.lisapark.octopus.core.processor.ProcessorInput;
import org.lisapark.octopus.core.processor.ProcessorOutput;
import org.lisapark.octopus.core.runtime.ProcessorContext;

/**
 *
 * @author Alex Mylnikov (alexmy@lisa-park.com)
 */
public class  Division extends Processor<Void> {
//    private static final String DEFAULT_NAME = "Division";
//    private static final String DEFAULT_DESCRIPTION = "Divide first operand by second operand";
    
    private static final String DEFAULT_NAME = "DIV";
    private static final String DEFAULT_DESCRIPTION = "Division";

    /**
     * Division takes two inputs
     */
    private static final int FIRST_INPUT_ID = 1;
    private static final int SECOND_INPUT_ID = 2;
    private static final int OUTPUT_ID = 1;

    protected Division(UUID id, String name, String description) {
        super(id, name, description);
    }

    protected Division(UUID id, Division divisionToCopy) {
        super(id, divisionToCopy);
    }

    protected Division(Division divisionToCopy) {
        super(divisionToCopy);
    }

    public ProcessorInput getFirstInput() {
        // there are two inputs for Division
        return getInputs().get(0);
    }

    public ProcessorInput getSecondInput() {
        // there are two inputs for Division
        return getInputs().get(1);
    }

    @Override
    public Division newInstance() {
        return new Division(UUID.randomUUID(), this);
    }

    @Override
    public Division copyOf() {
        return new Division(this);
    }

    @Override
    public CompiledProcessor<Void> compile() throws ValidationException {
        validate();

        // we copy all the inputs and output taking a "snapshot" of this processor so we are isolated of changes
        Division copy = copyOf();

        return new CompiledDivision(copy);
    }

    /**
     * Returns a new {@link Division} processor configured with all the appropriate
     * {@link org.lisapark.octopus.core.parameter.Parameter}s, {@link org.lisapark.octopus.core.Input}s and {@link org.lisapark.octopus.core.Output}.
     *
     * @return new {@link Division}
     */
    public static Division newTemplate() {
        UUID processorId = UUID.randomUUID();
        Division division = new Division(processorId, DEFAULT_NAME, DEFAULT_DESCRIPTION);

        // two double inputs
        ProcessorInput<Double> firstInput = ProcessorInput.doubleInputWithId(FIRST_INPUT_ID).name("First operand").description("First operand.").build();
        division.addInput(firstInput);

        ProcessorInput<Double> secondInput = ProcessorInput.doubleInputWithId(SECOND_INPUT_ID).name("Second operand.").description("Second operand").build();
        division.addInput(secondInput);

        division.addJoin(firstInput, secondInput);

        // double output
        try {
            division.setOutput(ProcessorOutput.doubleOutputWithId(OUTPUT_ID).nameAndDescription("Result").attributeName("result"));
        } catch (ValidationException ex) {
            // this should NOT happen. It means we created the Division with an invalid attribute name
            throw new ProgrammerException(ex);
        }

        return division;
    }

    static class CompiledDivision extends CompiledProcessor<Void> {
        private final String firstAttributeName;
        private final String secondAttributeName;

        protected CompiledDivision(Division Division) {
            super(Division);

            firstAttributeName = Division.getFirstInput().getSourceAttributeName();
            secondAttributeName = Division.getSecondInput().getSourceAttributeName();
        }

        @Override
        public Object processEvent(ProcessorContext<Void> ctx, Map<Integer, Event> eventsByInputId) {
            Event firstEvent = eventsByInputId.get(FIRST_INPUT_ID);
            Event secondEvent = eventsByInputId.get(SECOND_INPUT_ID);

            Double firstOperand = firstEvent.getAttributeAsDouble(firstAttributeName);
            Double secondOperand = secondEvent.getAttributeAsDouble(secondAttributeName);

            firstOperand = (firstOperand == null) ? 0 : firstOperand;
            secondOperand = (secondOperand == null) ? 0 : secondOperand;

            return secondOperand != 0 ? firstOperand / secondOperand : 0;
        }
    }
}