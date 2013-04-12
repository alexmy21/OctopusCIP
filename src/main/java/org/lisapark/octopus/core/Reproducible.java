/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved.                            *  
 * http://www.lisa-park.com                                                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core;

/**
 * A {@link Reproducible} is capable of creating new instance based on itself, i.e. a copy of itself but with a new
 * identity. This means that after calling {@link #newInstance()} on object a, the call
 * <code>a.equals(a.newInstance())</code> should return false. This differs from {@link Copyable} where the objects
 * should be equivalent.
 * <p/>
 * Shallow copying versus deep copying is up to the implementer, but we are generally making deeps copies unless the
 * object is immutable.
 *
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public interface Reproducible {

    /**
     * Implementers need to return a new instance based on <code>this</code> instance.
     *
     * @return new instance
     */
    Reproducible newInstance();
}
