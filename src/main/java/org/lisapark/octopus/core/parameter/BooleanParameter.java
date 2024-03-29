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
public class BooleanParameter extends Parameter<Boolean> {

    protected BooleanParameter(Builder<Boolean> builder) {
        super(builder);
    }

    protected BooleanParameter(BooleanParameter existingParameter) {
        super(existingParameter);
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public String getValueForDisplay() {
        return String.valueOf(getValue());
    }

    @Override
    public Boolean parseValueFromString(String stringValue) throws ConversionException {
        return parseBoolean(stringValue);
    }

    @Override
    public Parameter<Boolean> copyOf() {
        return new BooleanParameter(this);
    }

    static Boolean parseBoolean(String value) throws ConversionException {
        String lowerCasedValue = value.toLowerCase();
        Boolean parsedValue = null;

        if (lowerCasedValue.equals("true") || lowerCasedValue.equals("1") ||
                lowerCasedValue.equals("on") || lowerCasedValue.equals("yes") || lowerCasedValue.equals("y")) {
            parsedValue = true;

        } else if (lowerCasedValue.equals("false") || lowerCasedValue.equals("0") ||
                lowerCasedValue.equals("off") || lowerCasedValue.equals("no") || lowerCasedValue.equals("n")) {

            parsedValue = false;
        }

        if (parsedValue == null) {
            throw new ConversionException(String.format("%s is not a valid Boolean value", value));
        }
        return parsedValue;
    }
}
