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
import com.google.common.collect.Maps;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import org.lisapark.octopus.core.AbstractNode;
import org.lisapark.octopus.core.Input;
import org.lisapark.octopus.core.ProcessingException;
import org.lisapark.octopus.core.ValidationException;
import org.lisapark.octopus.core.event.Event;
import org.lisapark.octopus.core.parameter.Parameter;
import org.lisapark.octopus.core.runtime.SinkContext;
import org.lisapark.octopus.core.sink.external.CompiledExternalSink;
import org.lisapark.octopus.core.sink.external.ExternalSink;
import org.lisapark.octopus.core.source.Source;
import org.lisapark.octopus.util.jdbc.Connections;

/**
 *
 * @author Alex Mylnikov (alexmy@lisa-park.com)
 */
public class ModelJsonNeo4jSink extends AbstractNode implements ExternalSink {
    
    private final static java.util.logging.Logger logger 
            = java.util.logging.Logger.getLogger(ModelJsonNeo4jSink.class.getName());
       
    private static final String DEFAULT_NAME                = "Json Neo4j";
    private static final String DEFAULT_DESCRIPTION         = "Otopus Model JSON to Neo4j Database.";
    
    private static final int URL_PARAMETER_ID               = 1;
    private static final int USER_NAME_PARAMETER_ID         = 2;
    private static final int PASSWORD_PARAMETER_ID          = 3;
    
    private static final String ATTRIBUTE_LIST              = "Attribute list";
    private static final String ATTRIBUTE_LIST_DESCRIPTION  = 
            "Comma separated attribute list.";

    private static final String URL             = "URL:";
    private static final String USER_NAME       = "User name:";
    private static final String PASSWORD        = "Passwors:";
    
    private static final String DEFAULT_INPUT   = "Input data";
   
    private Input<Event> input;

    private ModelJsonNeo4jSink(UUID id, String name, String description) {
        super(id, name, description);
        input = Input.eventInputWithId(1);
        input.setName(DEFAULT_INPUT);
        input.setDescription(DEFAULT_INPUT);
    }

    private ModelJsonNeo4jSink(UUID id, ModelJsonNeo4jSink copyFromNode) {
        super(id, copyFromNode);
        input = copyFromNode.getInput().copyOf();
    }    
   
    public String getUrl() {
        return getParameter(URL_PARAMETER_ID).getValueAsString();
    }    
   
    public String getUserName() {
        return getParameter(USER_NAME_PARAMETER_ID).getValueAsString();
    }
        
    public String getPassword() {
        return getParameter(PASSWORD_PARAMETER_ID).getValueAsString();
    }
   
    private ModelJsonNeo4jSink(ModelJsonNeo4jSink copyFromNode) {
        super(copyFromNode);
        this.input = copyFromNode.input.copyOf();
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
    public ModelJsonNeo4jSink newInstance() {
        return new ModelJsonNeo4jSink(UUID.randomUUID(), this);
    }

    @Override
    public ModelJsonNeo4jSink copyOf() {
        return new ModelJsonNeo4jSink(this);
    }

    public static ModelJsonNeo4jSink newTemplate() {
        UUID sinkId = UUID.randomUUID();

        ModelJsonNeo4jSink databaseSink = new ModelJsonNeo4jSink(sinkId, DEFAULT_NAME, DEFAULT_DESCRIPTION);
        
        databaseSink.addParameter(Parameter.stringParameterWithIdAndName(URL_PARAMETER_ID, URL)
                .description("Neo4j URL.")
                .defaultValue("")
                .required(true));
        databaseSink.addParameter(Parameter.stringParameterWithIdAndName(USER_NAME_PARAMETER_ID, USER_NAME)
                .description("")
                .defaultValue("")
                .required(true));
        databaseSink.addParameter(Parameter.stringParameterWithIdAndName(PASSWORD_PARAMETER_ID, PASSWORD)
                .description("")
                .defaultValue("")
                .required(true));        
        
        return databaseSink;
    }

    @Override
    public CompiledExternalSink compile() throws ValidationException {
        return new CompiledDatabaseSink(copyOf());
    }

    static class CompiledDatabaseSink extends CompiledExternalSink {
        
        private final ModelJsonNeo4jSink databaseSink;
        private Connection connection = null;
        
        protected CompiledDatabaseSink(ModelJsonNeo4jSink databaseSink) {
            super(databaseSink);            
            this.databaseSink = databaseSink;            
        }

        @Override
        public void processEvent(SinkContext ctx, Map<Integer, Event> eventsByInputId) {
            
            Event event = eventsByInputId.get(1);
                        
            // TODO
        }

        private Connection getConnection(String className, String url, String userName, String password)  throws ProcessingException {
            
            try {
                Class.forName(className);
            } catch (ClassNotFoundException e) {
                // this should never happen since the parameter is constrained
                throw new ProcessingException("Could not find JDBC Driver Class " + className, e);
            }            

            try {
                if (connection == null) {
                    if (userName == null && password == null) {
                        connection = DriverManager.getConnection(url);
                    } else {
                        connection = DriverManager.getConnection(url, userName, password);
                    }
                }
            } catch (SQLException e) {
                throw new ProcessingException("Could not connect to database. Please check your settings.", e);
            }
            return connection;
        }

        private Map<String, Object> extractDataFromEvent(Event event, String attributeList) {
            Map<String, Object> retMap = Maps.newHashMap();
            Map<String, Object> eventMap = event.getData();
            
            String[] attList = attributeList.split(",");
            
            for(int i = 0; i < attList.length; i++){
//                String attr = attList[i]; 
                
//                if (attr.equalsIgnoreCase(databaseSink.getUuidFieldName())) {
//                    retMap.put(attr, UUID.randomUUID());
//                } else {
//                    retMap.put(attr, eventMap.get(attr));
//                }
            }            
            
            return retMap;
        }
        
        @Override
        protected void finalize() throws Throwable{
            Connections.closeQuietly(connection);
            
            logger.log(Level.INFO, "Connection: ====> {0}", "Closed!!!");
            
            super.finalize();            
        }
    }
}