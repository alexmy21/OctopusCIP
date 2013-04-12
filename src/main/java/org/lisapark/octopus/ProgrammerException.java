/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved.                            *  
 * http://www.lisa-park.com                                                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus;

/**
 * This is a unique exception in Octopus, it is thrown only when the exception is the cause of a programmer error. Any
 * time this happens means there is a whole in the programmer's logic and needs to be corrected.
 *
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public class ProgrammerException extends RuntimeException {

    public ProgrammerException(Throwable cause) {
        super(cause);
    }

    public ProgrammerException(String message) {
        super(message);
    }

    public ProgrammerException(String message, Throwable cause) {
        super(message, cause);
    }
}
