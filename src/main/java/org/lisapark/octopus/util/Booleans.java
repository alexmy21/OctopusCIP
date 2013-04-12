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

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public abstract class Booleans {
    public static boolean parseBoolean(String value) {
        return parseBoolean(value, false);
    }

    public static boolean parseBoolean(String value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        return parseBoolean(value, Boolean.valueOf(defaultValue));
    }

    public static Boolean parseBoolean(String value, Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        String lowerCasedValue = value.toLowerCase();

        return (lowerCasedValue.equals("true") || lowerCasedValue.equals("1") ||
                lowerCasedValue.equals("on") || lowerCasedValue.equals("yes") || lowerCasedValue.equals("y"));
    }
}