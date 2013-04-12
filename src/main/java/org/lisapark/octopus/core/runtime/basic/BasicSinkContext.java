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

import org.lisapark.octopus.core.runtime.SinkContext;

import java.io.PrintStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public class BasicSinkContext implements SinkContext {

    private final PrintStream standardOut;
    private final PrintStream standardError;

    public BasicSinkContext(PrintStream standardOut, PrintStream standardError) {
        checkArgument(standardOut != null, "standardOut cannot be null");
        checkArgument(standardError != null, "standardError cannot be null");
        this.standardOut = standardOut;
        this.standardError = standardError;
    }

    @Override
    public PrintStream getStandardOut() {
        return standardOut;
    }

    @Override
    public PrintStream getStandardError() {
        return standardError;
    }
}
