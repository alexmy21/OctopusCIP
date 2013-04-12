/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.util;

import com.google.common.base.Objects;

/**
 * Data structure for holding a pair of objects.
 *
 * @author dave sinclair (dsinclair@chariotsolutions.com)
 * @param <F> type of First object of Pair
 * @param <S> type if Second object of Pair
 */
public class Pair<F, S> {

    private final F first;
    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs) {
            return true;
        }
        if (rhs == null || getClass() != rhs.getClass()) {
            return false;
        }

        Pair otherPair = (Pair) rhs;

        if (first != null && otherPair.first != null && (!first.equals(otherPair.first))) {
            return false;
        }

        return second != null && otherPair.second != null && second.equals(otherPair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(first, second);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("first", first).
                add("second", second).toString();
    }

    public static <F, S> Pair<F, S> newInstance(F first, S second) {
        return new Pair<F, S>(first, second);
    }
}
