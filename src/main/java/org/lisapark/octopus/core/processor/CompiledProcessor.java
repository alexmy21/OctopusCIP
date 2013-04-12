/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.processor;

import org.lisapark.octopus.core.event.Event;
import org.lisapark.octopus.core.runtime.ProcessorContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public abstract class CompiledProcessor<MEMORY_TYPE> {
    private final List<ProcessorInput> inputs;
    private final List<ProcessorJoin> joins;
    private final ProcessorOutput output;
    private final UUID id;

    protected CompiledProcessor(Processor<MEMORY_TYPE> processor) {
        this.id = processor.getId();
        this.inputs = processor.getInputs();
        this.joins = processor.getJoins();
        this.output = processor.getOutput();
    }

    public UUID getId() {
        return id;
    }

    public List<ProcessorJoin> getJoins() {
        return joins;
    }

    public List<ProcessorInput> getInputs() {
        return inputs;
    }

    public ProcessorOutput getOutput() {
        return output;
    }

    public ProcessorJoin getJoinForInput(ProcessorInput input) {
        checkArgument(input != null, "input cannot be null");

        ProcessorJoin join = null;
        for (ProcessorJoin candidate : joins) {
            if (candidate.getFirstInput().equals(input) || candidate.getSecondInput().equals(input)) {
                join = candidate;
                break;
            }
        }

        return join;
    }

    public abstract Object processEvent(ProcessorContext<MEMORY_TYPE> ctx, Map<Integer, Event> eventsByInputId);
}
