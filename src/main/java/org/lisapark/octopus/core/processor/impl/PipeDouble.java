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
import org.lisapark.octopus.core.Input;
import org.lisapark.octopus.core.Output;
import org.lisapark.octopus.core.Persistable;
import org.lisapark.octopus.core.ValidationException;
import org.lisapark.octopus.core.event.Event;
import org.lisapark.octopus.core.parameter.Parameter;
import org.lisapark.octopus.core.processor.CompiledProcessor;
import org.lisapark.octopus.core.processor.Processor;
import org.lisapark.octopus.core.processor.ProcessorInput;
import org.lisapark.octopus.core.processor.ProcessorOutput;
import org.lisapark.octopus.core.runtime.ProcessorContext;

/**
 * This {@link Processor} is used for transferring Double value from one processor to another.
 * <p/>
 *
 * @author dave sinclair(david.sinclair@lisa-park.com)
 * @author Alex Mylnikov (alexmy@lisa-park.com) mylnikov(alexmy@lisa-park.com)
 */
@Persistable
public class PipeDouble extends Processor<Double> {
    
    private static final String DEFAULT_NAME = "Connector Double";
    private static final String DEFAULT_DESCRIPTION = "Transfere doubles from one processor to another.";
//    private static final String DEFAULT_WINDOW_LENGTH_DESCRIPTION = "Количество наблюдаемых значений";
    private static final String DEFAULT_INPUT_DESCRIPTION = "Input data";
    private static final String DEFAULT_OUTPUT_DESCRIPTION = "Output data name.";

    /**
     * Pipe takes a single input
     */
    private static final int INPUT_ID = 1;
    private static final int OUTPUT_ID = 1;

    protected PipeDouble(UUID id, String name, String description) {
        super(id, name, description);
    }

    protected PipeDouble(UUID id, PipeDouble copyFromSma) {
        super(id, copyFromSma);
    }

    protected PipeDouble(PipeDouble copyFromSma) {
        super(copyFromSma);
    }

    public ProcessorInput getInput() {
        // there is only one input for an Sma
        return getInputs().get(0);
    }

    @Override
    public PipeDouble newInstance() {
        return new PipeDouble(UUID.randomUUID(), this);
    }

    @Override
    public PipeDouble copyOf() {
        return new PipeDouble(this);
    }

    /**
     * Validates and compile this Pipe. Doing so takes a "snapshot" of the {@link #getInputs()} and {@link #output}
     * and returns a {@link CompiledProcessor}.
     *
     * @return CompiledProcessor
     */
    @Override
    public CompiledProcessor<Double> compile() throws ValidationException {
        validate();

        // we copy all the inputs and output taking a "snapshot" of this processor so we are isolated of changes
        PipeDouble copy = copyOf();
        return new CompiledPipeDouble(copy);
    }

    /**
     * Returns a new {@link Sma} processor configured with all the appropriate {@link org.lisapark.octopus.core.parameter.Parameter}s, {@link Input}s
     * and {@link Output}.
     *
     * @return new {@link Sma}
     */
    public static PipeDouble newTemplate() {
        UUID processorId = UUID.randomUUID();
        PipeDouble sma = new PipeDouble(processorId, DEFAULT_NAME, DEFAULT_DESCRIPTION);

        // only a single double input
        sma.addInput(
                ProcessorInput.doubleInputWithId(INPUT_ID).name("Input data").description(DEFAULT_INPUT_DESCRIPTION)
        );
        // double output
        try {
            sma.setOutput(
                    ProcessorOutput.doubleOutputWithId(OUTPUT_ID).name("Output").description(DEFAULT_OUTPUT_DESCRIPTION).attributeName("output")
            );
        } catch (ValidationException ex) {
            // this should NOT happen. It means we created the SMA with an invalid attriubte name
            throw new ProgrammerException(ex);
        }

        return sma;
    }

    /**
     * This {@link CompiledProcessor} is the actual logic that implements the Simple Moving Average.
     */
    static class CompiledPipeDouble extends CompiledProcessor<Double> {
        private final String inputAttributeName;

        protected CompiledPipeDouble(PipeDouble pipe) {
            super(pipe);
            this.inputAttributeName = pipe.getInput().getSourceAttributeName();
        }

        @Override
        public Object processEvent(ProcessorContext<Double> ctx, Map<Integer, Event> eventsByInputId) {
            // sma only has a single event
            Event event = eventsByInputId.get(INPUT_ID);

            Double newItem = 0D;
            
            Object obj = event.getData().get(inputAttributeName);                    

            if (obj instanceof Double) {
                newItem = event.getAttributeAsDouble(inputAttributeName);
            } else if (obj instanceof String) {
                newItem = Double.valueOf((String)obj);
            }
            
            return newItem;
        }
    }
}
