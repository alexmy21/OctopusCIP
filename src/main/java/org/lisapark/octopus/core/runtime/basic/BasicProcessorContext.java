/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.runtime.basic;

import org.lisapark.octopus.core.memory.Memory;
import org.lisapark.octopus.core.runtime.ProcessorContext;

import java.io.PrintStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public class BasicProcessorContext<MEMORY_TYPE> extends BasicSinkContext implements ProcessorContext<MEMORY_TYPE> {
    private final Memory<MEMORY_TYPE> memory;

    public BasicProcessorContext(PrintStream standardOut, PrintStream standardError) {
        super(standardOut, standardError);
        memory = null;
    }

    public BasicProcessorContext(PrintStream standardOut, PrintStream standardError, Memory<MEMORY_TYPE> memory) {
        super(standardOut, standardError);
        checkArgument(memory != null, "memory cannot be null");
        this.memory = memory;
    }

    @Override
    public Memory<MEMORY_TYPE> getProcessorMemory() {
        return memory;
    }
}
