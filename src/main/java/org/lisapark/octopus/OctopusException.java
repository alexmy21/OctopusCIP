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
 * This is the top level checked {@link Exception} in the Octopus application. All other exception types should
 * be a subclass of this exception.
 *
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public class OctopusException extends Exception {

    public OctopusException(Throwable cause) {
        super(cause);
    }

    public OctopusException(String message) {
        super(message);
    }

    public OctopusException(String message, Throwable cause) {
        super(message, cause);
    }
}
