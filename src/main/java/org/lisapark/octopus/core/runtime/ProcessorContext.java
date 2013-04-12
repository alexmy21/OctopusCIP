/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.runtime;

import org.lisapark.octopus.core.memory.Memory;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public interface ProcessorContext<MEMORY_TYPE> extends SinkContext {

    Memory<MEMORY_TYPE> getProcessorMemory();
}
