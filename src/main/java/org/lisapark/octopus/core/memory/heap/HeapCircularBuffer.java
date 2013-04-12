/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.memory.heap;

import com.google.common.collect.Lists;
import org.lisapark.octopus.core.memory.Memory;

import java.util.Collection;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public class HeapCircularBuffer<T> implements Memory<T> {

    private final T[] buffer;

    private int currentIndex;

    @SuppressWarnings("unchecked")
    public HeapCircularBuffer(int n) {
        buffer = (T[]) new Object[n];
        currentIndex = 0;
    }

    @Override
    public void add(T value) {
        buffer[currentIndex] = value;

        currentIndex = (currentIndex + 1) % buffer.length;
    }

    @Override
    public boolean remove(T value) {
        throw new UnsupportedOperationException("Remove not supported");
    }

    @Override
    public Collection<T> values() {
        Collection<T> values = Lists.newArrayListWithCapacity(buffer.length);

        for (T item : buffer) {
            if (item != null) {
                values.add(item);
            }
        }

        return values;
    }
}
