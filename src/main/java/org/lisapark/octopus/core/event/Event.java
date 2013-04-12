/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved.                            *  
 * http://www.lisa-park.com                                                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.event;

import com.google.common.collect.Maps;
import org.lisapark.octopus.core.Persistable;

import java.util.Collection;
import java.util.Map;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public class Event {
    private final Map<String, Object> data = Maps.newHashMap();

    public Event(String attributeName, Object value) {
        data.put(attributeName, value);
    }

    public Event(Map<String, Object> data) {
        this.data.putAll(data);
    }

    public Event unionWith(Event event) {
        Map<String, Object> newData = Maps.newHashMap(data);
        newData.putAll(event.getData());

        return new Event(newData);
    }

    public Event unionWith(Collection<Event> events) {
        Map<String, Object> newData = Maps.newHashMap(data);
        for (Event event : events) {
            newData.putAll(event.getData());
        }

        return new Event(newData);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Integer getAttributeAsInteger(String attributeName) {
        Object value = data.get(attributeName);

        if (value != null) {
            return ((Number) value).intValue();
        } else {
            return null;
        }
    }

    public Short getAttributeAsShort(String attributeName) {
        Object value = data.get(attributeName);

        if (value != null) {
            return ((Number) value).shortValue();
        } else {
            return null;
        }
    }

    public Long getAttributeAsLong(String attributeName) {
        Object value = data.get(attributeName);

        if (value != null) {
            return ((Number) value).longValue();
        } else {
            return null;
        }
    }

    public Float getAttributeAsFloat(String attributeName) {
        Object value = data.get(attributeName);

        if (value != null) {
            return ((Number) value).floatValue();
        } else {
            return null;
        }
    }

    public Double getAttributeAsDouble(String attributeName) {
        Object value = data.get(attributeName);

        if (value != null) {
            return ((Number) value).doubleValue();
        } else {
            return null;
        }
    }

    public String getAttributeAsString(String attributeName) {
        return (String) data.get(attributeName);
    }

    public Boolean getAttributeAsBoolean(String attributeName) {
        return (Boolean) data.get(attributeName);
    }

    @Override
    public String toString() {
        return "Event{" +
                "data=" + data +
                '}';
    }
}
