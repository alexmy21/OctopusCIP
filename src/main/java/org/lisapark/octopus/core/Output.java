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

import org.lisapark.octopus.core.event.Attribute;
import org.lisapark.octopus.core.event.EventType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public class Output extends AbstractComponent {

    private EventType eventType;

    protected Output(int id) {
        super(id);
        this.eventType = new EventType();
    }

    protected Output(int id, EventType eventType) {
        super(id);
        this.eventType = eventType;
    }

    protected Output(int id, String name, String description) {
        super(id, name, description);
        this.eventType = new EventType();
    }

    protected Output(Output existingOutput) {
        super(existingOutput);
        this.eventType = existingOutput.eventType.copyOf();
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    @Override
    public Output copyOf() {
        return new Output(this);
    }

    @Override
    public Output setDescription(String description) {
        return (Output) super.setDescription(description);
    }

    @Override
    public Output setName(String name) {
        return (Output) super.setName(name);
    }

    public EventType addAttribute(Attribute attribute) {
        return eventType.addAttribute(attribute);
    }

    public EventType removeAttribute(Attribute attribute) {
        return eventType.removeAttribute(attribute);
    }

    public boolean containsAttribute(Attribute attribute) {
        return eventType.containsAttribute(attribute);
    }

    public Attribute getAttributeByName(String name) {
        return eventType.getAttributeByName(name);
    }

    public Map<String, Object> getEventDefinition() {
        return eventType.getEventDefinition();
    }

    public List<Attribute> getAttributes() {
        return eventType.getAttributes();
    }

    public Collection<String> getAttributeNames() {
        return eventType.getAttributeNames();
    }

    public static Output outputWithId(int id) {
        return new Output(id);
    }

    public static Output outputWithIdAndEventType(int id, EventType eventType) {
        return new Output(id, eventType);
    }
}
