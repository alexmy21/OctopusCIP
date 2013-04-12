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
 * A {@link Validatable} is capable of validating itself to ensure its internal state is valid. If it is <b>not</b>,
 * said object should throw a {@link org.lisapark.octopus.core.ValidationException}
 *
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public interface Validatable {

    /**
     * Implementers validate the correctness of the current state and throw a {@link ValidationException} if they
     * are not.
     *
     * @throws ValidationException thrown if this object is not valid
     */
    void validate() throws ValidationException;
}
