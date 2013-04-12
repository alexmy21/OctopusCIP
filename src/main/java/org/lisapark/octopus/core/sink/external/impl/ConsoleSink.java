/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.sink.external.impl;

import com.google.common.collect.ImmutableList;
import org.lisapark.octopus.core.AbstractNode;
import org.lisapark.octopus.core.Input;
import org.lisapark.octopus.core.Persistable;
import org.lisapark.octopus.core.ValidationException;
import org.lisapark.octopus.core.event.Event;
import org.lisapark.octopus.core.runtime.SinkContext;
import org.lisapark.octopus.core.source.Source;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.lisapark.octopus.core.parameter.Parameter;
import org.lisapark.octopus.core.sink.external.CompiledExternalSink;
import org.lisapark.octopus.core.sink.external.ExternalSink;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public class ConsoleSink extends AbstractNode implements ExternalSink {
    private static final String DEFAULT_NAME = "Console";
    private static final String DEFAULT_DESCRIPTION = "Console Output";
    private static final String DEFAULT_INPUT = "Input";    
    
    private static final int ATTRIBUTE_LIST_PARAMETER_ID = 1;
    private static final String ATTRIBUTE_LIST = "Show Attributes";
    private static final String ATTRIBUTE_LIST_DESCRIPTION = 
            "List comma separated attribute names that you would like to show on Console. Empty - will show all attributes.";
    
//    private static final String DEFAULT_NAME = "Консоль";
//    private static final String DEFAULT_DESCRIPTION = "Консоль для выходных данных";
//    private static final String DEFAULT_INPUT = "Входные данные";    
//    
//    private static final int ATTRIBUTE_LIST_PARAMETER_ID = 1;
//    private static final String ATTRIBUTE_LIST = "Перечень выходных атрибутов";
//    private static final String ATTRIBUTE_LIST_DESCRIPTION = 
//            "Список разделенных запятой имен атрибутов, которые необходимо вывести на консоль. Если данный параметр пустой, то на Консоль выводятся все атрибуты.";

    private Input<Event> input;

    private ConsoleSink(UUID id, String name, String description) {
        super(id, name, description);
        input = Input.eventInputWithId(1);
        input.setName(DEFAULT_INPUT);
        input.setDescription(DEFAULT_INPUT);
    }

    private ConsoleSink(UUID id, ConsoleSink copyFromNode) {
        super(id, copyFromNode);
        input = copyFromNode.getInput().copyOf();
    }

    private ConsoleSink(ConsoleSink copyFromNode) {
        super(copyFromNode);
        this.input = copyFromNode.input.copyOf();
    }
    
    @SuppressWarnings("unchecked")
    public void setAttributeList(String attributeList) throws ValidationException {
        getParameter(ATTRIBUTE_LIST_PARAMETER_ID).setValue(attributeList);
    }

    public String getAttributeList() {
        return getParameter(ATTRIBUTE_LIST_PARAMETER_ID).getValueAsString();
    }

    public Input<Event> getInput() {
        return input;
    }

    @Override
    public List<Input<Event>> getInputs() {
        return ImmutableList.of(input);
    }

    @Override
    public boolean isConnectedTo(Source source) {

        return input.isConnectedTo(source);
    }

    @Override
    public void disconnect(Source source) {
        if (input.isConnectedTo(source)) {
            input.clearSource();
        }
    }

    @Override
    public ConsoleSink newInstance() {
        return new ConsoleSink(UUID.randomUUID(), this);
    }

    @Override
    public ConsoleSink copyOf() {
        return new ConsoleSink(this);
    }

    public static ConsoleSink newTemplate() {
        UUID sinkId = UUID.randomUUID();
        ConsoleSink consoleSink = new ConsoleSink(sinkId, DEFAULT_NAME, DEFAULT_DESCRIPTION);
        
        consoleSink.addParameter(
                Parameter.stringParameterWithIdAndName(ATTRIBUTE_LIST_PARAMETER_ID, ATTRIBUTE_LIST)
                .description(ATTRIBUTE_LIST_DESCRIPTION)
                );
        
        return consoleSink;
    }

    @Override
    public CompiledExternalSink compile() throws ValidationException {
        return new CompiledConsole(copyOf());
    }

    static class CompiledConsole extends CompiledExternalSink {
        
        private ConsoleSink consoleSink; 
        
        protected CompiledConsole(ConsoleSink processor) {
            super(processor);
            this.consoleSink = processor;
        }

        @Override
        public void processEvent(SinkContext ctx, Map<Integer, Event> eventsByInputId) {
            Event event = eventsByInputId.get(1);
            if (event != null) {
                String attributeList = consoleSink.getAttributeList();
                if(attributeList.isEmpty() || attributeList.split(",").length == 0){
                    // Print out everything
                    ctx.getStandardOut().println(event);
                } else {
                    // Print out only selected attributes
                    String outputString = formatOutput(event, attributeList);
                    
                    // Print out only if there are some attributes are not null
                    if(outputString != null){
                        ctx.getStandardOut().println(outputString);
                    }
                }
            } else {
                ctx.getStandardOut().println("event is null");
            }
        }

        private String formatOutput(Event event, String attributeList) {
            
            Map<String, Object> data = event.getData();            
            StringBuilder outputString = new StringBuilder();            
            String[] attList = attributeList.split(",");
            
            for(int i = 0; i < attList.length; i++){
                String attr = attList[i];                
                if(outputString.length() > 1) outputString.append(", ");
                if(data.get(attr) != null){
                    outputString.append(attr).append("=").append(data.get(attr));
                }
            }            

            if(outputString.length() > 0){
                return "{" + outputString.toString() + "}";
            } else {
                return null;
            }
        }
    }
}
