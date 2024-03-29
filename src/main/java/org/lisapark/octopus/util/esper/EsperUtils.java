/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.util.esper;

import org.lisapark.octopus.core.processor.CompiledProcessor;
import org.lisapark.octopus.core.source.Source;

import java.util.UUID;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public abstract class EsperUtils {

    /**
     * Returns the event name that will be used within Esper for the specified source.
     *
     * @param source to get event name for
     * @return event name
     */
    public static String getEventNameForSource(Source source) {
        return getEventNameForUUID(source.getId());
    }

    /**
     * Returns the event name that will be used within Esper for the specified processor.
     *
     * @param processor to get event name for
     * @return event name
     */
    public static String getEventNameForProcessor(CompiledProcessor<?> processor) {
        return getEventNameForUUID(processor.getId());
    }

    static String getEventNameForUUID(UUID id) {
        // esper doesn't like event name that start with a number, so we always add a prefix of '_'
        StringBuilder eventName = new StringBuilder("_");

        String idAsString = id.toString();
        for (int i = 0; i < idAsString.length(); ++i) {
            // esper also doesn't like '-', so we skip them
            if (idAsString.charAt(i) != '-') {
                eventName.append(idAsString.charAt(i));
            }
        }

        return eventName.toString();
    }
}
