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

/**
 *
 * @author Alex Mylnikov (alexmy@lisa-park.com)
 */
import com.db4o.internal.logging.Logger;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import org.lisapark.octopus.ProgrammerException;
import org.lisapark.octopus.core.Persistable;
import org.lisapark.octopus.core.ValidationException;
import org.lisapark.octopus.core.event.Event;
import org.lisapark.octopus.core.runtime.ProcessorContext;

import java.util.Map;
import java.util.UUID;
import org.lisapark.octopus.core.memory.Memory;
import org.lisapark.octopus.core.memory.MemoryProvider;
import org.lisapark.octopus.core.processor.CompiledProcessor;
import org.lisapark.octopus.core.processor.Processor;
import org.lisapark.octopus.core.processor.ProcessorInput;
import org.lisapark.octopus.core.processor.ProcessorOutput;
import org.lisapark.octopus.util.Pair;

/**
 * This {@link Processor} is used to determine if two SMAs are crossed.
 * <p/>
 *
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public class CrossAbove extends Processor<Pair> {
    
    private final static java.util.logging.Logger logger 
            = java.util.logging.Logger.getLogger(CrossAbove.class.getName());
    
//    private static final String DEFAULT_NAME = "CrossAbove";
//    private static final String DEFAULT_DESCRIPTION = "Check if 2 SMA are crossed.";
    
    private static final String DEFAULT_NAME = "Cross from above";
    private static final String DEFAULT_DESCRIPTION = "Checks if crossing happened";

    /**
     * CrossAbove takes two inputs
     */
    private static final int FIRST_INPUT_ID = 1;
    private static final int SECOND_INPUT_ID = 2;
    private static final int OUTPUT_ID = 1;
    private static final int BUFFER_SIZE = 2;

    protected CrossAbove(UUID id, String name, String description) {
        super(id, name, description);
    }

    protected CrossAbove(UUID id, CrossAbove crossAboveToCopy) {
        super(id, crossAboveToCopy);
    }

    protected CrossAbove(CrossAbove crossAboveToCopy) {
        super(crossAboveToCopy);
    }

    public ProcessorInput getFirstInput() {
        // there are two inputs for crossAbove
        return getInputs().get(0);
    }

    public ProcessorInput getSecondInput() {
        // there are two inputs for crossAbove
        return getInputs().get(1);
    }

    @Override
    public CrossAbove newInstance() {
        return new CrossAbove(UUID.randomUUID(), this);
    }

    @Override
    public CrossAbove copyOf() {
        return new CrossAbove(this);
    }
    
    /**
     * {@link CrossAbove}s need memory to store the prior events that will be used 
     * to determine if two SMAs are crossed. We
     * used a {@link MemoryProvider#createCircularBuffer(int)} to store this data.
     *
     * @param memoryProvider used to create CrosAbove's memory
     * @return circular buffer
     */
    @Override
    public Memory<Pair> createMemoryForProcessor(MemoryProvider memoryProvider) {
        return memoryProvider.createCircularBuffer(BUFFER_SIZE);
    }
    
    @Override
    public CompiledProcessor<Pair> compile() throws ValidationException {
        validate();

        // we copy all the inputs and output taking a "snapshot" of this processor so we are isolated of changes
        CrossAbove copy = copyOf();

        return new CompiledCrossAbove(copy);
    }

    /**
     * Returns a new {@link CrossAbove} processor configured with all the appropriate
     * {@link org.lisapark.octopus.core.parameter.Parameter}s, {@link org.lisapark.octopus.core.Input}s and {@link org.lisapark.octopus.core.Output}.
     *
     * @return new {@link CrossAbove}
     */
    public static CrossAbove newTemplate() {
        UUID processorId = UUID.randomUUID();
        CrossAbove crossAbove = new CrossAbove(processorId, DEFAULT_NAME, DEFAULT_DESCRIPTION);

        // two double inputs
        ProcessorInput<Double> firstInput = ProcessorInput.doubleInputWithId(FIRST_INPUT_ID).name("Short SMA").description("Short Simple Moving Average.").build();
        crossAbove.addInput(firstInput);

        ProcessorInput<Double> secondInput = ProcessorInput.doubleInputWithId(SECOND_INPUT_ID).name("Long SMA").description("Long Simple Moving Average.").build();
        crossAbove.addInput(secondInput);

        crossAbove.addJoin(firstInput, secondInput);

        // double output
        try {
            crossAbove.setOutput(ProcessorOutput.booleanOutputWithId(OUTPUT_ID).nameAndDescription("Result").attributeName("CrossedFromAbove"));
        } catch (ValidationException ex) {
            // this should NOT happen. It means we created the CrossAbove with an invalid attribute name
            throw new ProgrammerException(ex);
        }

        return crossAbove;
    }

    static class CompiledCrossAbove extends CompiledProcessor<Pair> {
        private final String firstAttributeName;
        private final String secondAttributeName;

        protected CompiledCrossAbove(CrossAbove crossAbove) {
            super(crossAbove);

            firstAttributeName = crossAbove.getFirstInput().getSourceAttributeName();
            secondAttributeName = crossAbove.getSecondInput().getSourceAttributeName();
        }

        @Override
        public Object processEvent(ProcessorContext<Pair> ctx, Map<Integer, Event> eventsByInputId) {
            Event firstEvent = eventsByInputId.get(FIRST_INPUT_ID);
            Event secondEvent = eventsByInputId.get(SECOND_INPUT_ID);

            Double firstOperand = firstEvent.getAttributeAsDouble(firstAttributeName);
            Double secondOperand = secondEvent.getAttributeAsDouble(secondAttributeName);
            
            Double retValue = 0D;
            
            if (firstOperand != null && secondOperand != null) {
                
                Memory<Pair> processorMemory = ctx.getProcessorMemory();
                
                Pair<Double, Double> newPair = new Pair<Double, Double>(firstOperand, secondOperand);
                processorMemory.add(newPair);
                
                List<Pair> list = Lists.newArrayList();

                final Collection<Pair> memoryItems = processorMemory.values();
                for (Pair memoryItem : memoryItems) {
                    list.add(memoryItem);
                }

//logger.log(     Level.INFO, "list.add(memoryItem);{0}", list);
                
                if (list.size() >= BUFFER_SIZE) {
                  
                    Pair<Double, Double> firstPair = list.get(0);
                    Pair<Double, Double> secondPair = list.get(1);

                    if (firstPair.getFirst() >= firstPair.getSecond()
                            && secondPair.getFirst() < secondPair.getSecond()) {
                        retValue = firstOperand;
                    }
                }
//                retValue = newPair.getFirst();
            }            
            
            return retValue > 0 ? Boolean.TRUE : Boolean.FALSE;
        }
    }
}
