/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.memory;

import org.lisapark.octopus.core.event.Event;

import java.util.Collection;

/**
 * A {@link Memory} is used to store {@link Event}s for a {@link org.lisapark.octopus.core.processor.Processor}.
 * <p/>
 * Some {@link org.lisapark.octopus.core.processor.Processor}s required memory to store computed or temporary values
 * in order to compute a computation.  An example of this is the {@link org.lisapark.octopus.core.processor.Sma} that
 * contains a window length which is a number of {@link Event}s for which the average should be computed over.
 *
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public interface Memory<T> {

    void add(T value);

    boolean remove(T value);

    Collection<T> values();
}
