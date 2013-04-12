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

import org.lisapark.octopus.OctopusException;

/**
 * This is a general exception that can be thrown by a {@link org.lisapark.octopus.core.processor.CompiledProcessor}
 * while processing events.
 *
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public class ProcessingException extends OctopusException {

    public ProcessingException(String message) {
        super(message);
    }

    public ProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
