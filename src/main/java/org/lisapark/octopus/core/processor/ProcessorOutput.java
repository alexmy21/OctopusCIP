/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.processor;

import org.lisapark.octopus.core.Output;
import org.lisapark.octopus.core.Persistable;
import org.lisapark.octopus.core.ValidationException;
import org.lisapark.octopus.core.event.Attribute;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public class ProcessorOutput extends Output {

    private final Attribute attribute;

    protected ProcessorOutput(ProcessorOutput existingOutput) {
        super(existingOutput);

        // todo - getting a little hairy with the copying
        // when we call the super class copy constructor, it makes a deep copy of the event type which
        // also holds the attribute we are interested in. Need to get this NEW copy, now create one ourselves
        this.attribute = getEventType().getAttributeByName(existingOutput.attribute.getName());
    }

    public ProcessorOutput(Builder builder) {
        super(builder.id, builder.name, builder.description);
        this.attribute = builder.attribute;

        // add the output to the event
        addAttribute(builder.attribute);
    }

    public Class<?> getAttributeType() {
        return attribute.getType();
    }

    public void setAttributeName(String name) throws ValidationException {
        attribute.setName(name);
    }

    public String getAttributeName() {
        return attribute.getName();
    }

    @Override
    public ProcessorOutput copyOf() {
        return new ProcessorOutput(this);
    }

    public static Builder<String> stringOutputWithId(int id) {
        return new Builder<String>(id, String.class);
    }

    public static Builder<Boolean> booleanOutputWithId(int id) {
        return new Builder<Boolean>(id, Boolean.class);
    }

    public static Builder<Long> longOutputWithId(int id) {
        return new Builder<Long>(id, Long.class);
    }

    public static Builder<Short> shortOutputWithId(int id) {
        return new Builder<Short>(id, Short.class);
    }

    public static Builder<Integer> integerOutputWithId(int id) {
        return new Builder<Integer>(id, Integer.class);
    }

    public static Builder<Double> doubleOutputWithId(int id) {
        return new Builder<Double>(id, Double.class);
    }

    public static Builder<Float> floatOutputWithId(int id) {
        return new Builder<Float>(id, Float.class);
    }

    public static class Builder<T> {
        private final int id;
        private String name;
        private String description;
        private Class<T> type;
        private String attributeName;
        private Attribute attribute;

        private Builder(int id, Class<T> type) {
            this.id = id;
            this.type = type;
        }

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> nameAndDescription(String name) {
            this.name = name;
            this.description = name;
            return this;
        }

        public Builder<T> attributeName(String attributeName) {
            this.attributeName = attributeName;
            return this;
        }

        public Builder<T> description(String description) {
            this.description = description;
            return this;
        }

        @SuppressWarnings("unchecked")
        public ProcessorOutput build() throws ValidationException {
            checkState(attributeName != null, "attributeName is required");
            attribute = Attribute.newAttribute(type, attributeName);
            return new ProcessorOutput(this);
        }
    }
}
