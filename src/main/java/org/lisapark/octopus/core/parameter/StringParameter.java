/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.parameter;

import org.lisapark.octopus.core.Persistable;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public class StringParameter extends Parameter<String> {

    protected StringParameter(Builder<String> spec) {
        super(spec);
    }

    protected StringParameter(StringParameter existingParameter) {
        super(existingParameter);
    }

    @Override
    public String getValueForDisplay() {
        return getValue();
    }

    @Override
    public Parameter<String> copyOf() {
        return new StringParameter(this);
    }

    @Override
    public String parseValueFromString(String stringValue) throws ConversionException {
        return stringValue;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}
