/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.source.external.impl;

import com.google.common.collect.Maps;
import org.lisapark.octopus.core.Output;
import org.lisapark.octopus.core.Persistable;
import org.lisapark.octopus.core.ProcessingException;
import org.lisapark.octopus.core.ValidationException;
import org.lisapark.octopus.core.event.Attribute;
import org.lisapark.octopus.core.event.Event;
import org.lisapark.octopus.core.event.EventType;
import org.lisapark.octopus.core.parameter.Constraints;
import org.lisapark.octopus.core.parameter.Parameter;
import org.lisapark.octopus.core.runtime.ProcessingRuntime;
import org.lisapark.octopus.util.Booleans;
import org.lisapark.octopus.util.jdbc.Connections;
import org.lisapark.octopus.util.jdbc.ResultSets;
import org.lisapark.octopus.util.jdbc.Statements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.UUID;

import org.lisapark.octopus.core.source.external.CompiledExternalSource;
import org.lisapark.octopus.core.source.external.ExternalSource;
import static com.google.common.base.Preconditions.checkState;

/**
 * This class is an {@link ExternalSource} that is used to access relational databases. It can be configured with
 * a JDBC Url for the database, username, password, Driver fully qualified class name, and a query to execute.
 * <p/>
 * Currently, the source uses the {@link org.lisapark.octopus.core.Output#getEventType()} to get the names of the
 * columns and types of the columns, but it will probably be changed in the future to support a mapper that takes
 * a {@link ResultSet} and produces an {@link Event}.
 *
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public class SqlQuerySource extends ExternalSource {
    private static final String DEFAULT_NAME = "Database Source";
    private static final String DEFAULT_DESCRIPTION = "Access to Database using SQL query.";

    private static final int URL_PARAMETER_ID = 1;
    private static final int USER_NAME_PARAMETER_ID = 2;
    private static final int PASSWORD_PARAMETER_ID = 3;
    private static final int DRIVER_PARAMETER_ID = 4;
    private static final int QUERY_PARAMETER_ID = 5;

    private SqlQuerySource(UUID sourceId, String name, String description) {
        super(sourceId, name, description);
    }

    private SqlQuerySource(UUID sourceId, SqlQuerySource copyFromSource) {
        super(sourceId, copyFromSource);
    }

    private SqlQuerySource(SqlQuerySource copyFromSource) {
        super(copyFromSource);
    }

    @SuppressWarnings("unchecked")
    public void setUrl(String url) throws ValidationException {
        getParameter(URL_PARAMETER_ID).setValue(url);
    }

    public String getUrl() {
        return getParameter(URL_PARAMETER_ID).getValueAsString();
    }

    @SuppressWarnings("unchecked")
    public void setUsername(String username) throws ValidationException {
        getParameter(USER_NAME_PARAMETER_ID).setValue(username);
    }

    public String getUsername() {
        return getParameter(USER_NAME_PARAMETER_ID).getValueAsString();
    }

    @SuppressWarnings("unchecked")
    public void setPassword(String password) throws ValidationException {
        getParameter(PASSWORD_PARAMETER_ID).setValue(password);
    }

    public String getPassword() {
        return getParameter(PASSWORD_PARAMETER_ID).getValueAsString();
    }

    @SuppressWarnings("unchecked")
    public void setDriverClass(String driverClass) throws ValidationException {
        getParameter(DRIVER_PARAMETER_ID).setValue(driverClass);
    }

    public String getDriverClass() {
        return getParameter(DRIVER_PARAMETER_ID).getValueAsString();
    }

    @SuppressWarnings("unchecked")
    public void setQuery(String query) throws ValidationException {
        getParameter(QUERY_PARAMETER_ID).setValue(query);
    }

    public String getQuery() {
        return getParameter(QUERY_PARAMETER_ID).getValueAsString();
    }

    public EventType getEventType() {
        return getOutput().getEventType();
    }

    @Override
    public SqlQuerySource newInstance() {
        UUID sourceId = UUID.randomUUID();
        return new SqlQuerySource(sourceId, this);
    }

    @Override
    public SqlQuerySource copyOf() {
        return new SqlQuerySource(this);
    }

    public static SqlQuerySource newTemplate() {
        UUID sourceId = UUID.randomUUID();
        SqlQuerySource jdbc = new SqlQuerySource(sourceId, DEFAULT_NAME, DEFAULT_DESCRIPTION);

        jdbc.addParameter(Parameter.stringParameterWithIdAndName(URL_PARAMETER_ID, "URL").required(true));
        jdbc.addParameter(Parameter.stringParameterWithIdAndName(USER_NAME_PARAMETER_ID, "User name"));
        jdbc.addParameter(Parameter.stringParameterWithIdAndName(PASSWORD_PARAMETER_ID, "Password:"));
        jdbc.addParameter(Parameter.stringParameterWithIdAndName(DRIVER_PARAMETER_ID, "Driver class name:").required(true).
                constraint(Constraints.classConstraintWithMessage("%s is not a valid Driver Class")));
        jdbc.addParameter(Parameter.stringParameterWithIdAndName(QUERY_PARAMETER_ID, "SQL query:").required(true));

        jdbc.setOutput(Output.outputWithId(1).setName("Output data:"));

        return jdbc;
    }

    @Override
    public CompiledExternalSource compile() throws ValidationException {
        validate();

        return new CompiledSqlQuerySource(this.copyOf());
    }

    private static class CompiledSqlQuerySource implements CompiledExternalSource {
        private final SqlQuerySource source;

        private volatile boolean running;

        public CompiledSqlQuerySource(SqlQuerySource source) {
            this.source = source;
        }

        @Override
        public void startProcessingEvents(ProcessingRuntime runtime) throws ProcessingException {
            // this needs to be atomic, both the check and set
            synchronized (this) {
                checkState(!running, "Source is already processing events. Cannot call processEvents again");
                running = true;
            }

            Connection connection = getConnection(source.getDriverClass(), source.getUrl(), source.getUsername(), source.getPassword());
            Statement statement = null;
            ResultSet rs = null;
            try {
                statement = connection.createStatement();

                rs = statement.executeQuery(source.getQuery());
                processResultSet(rs, runtime);
            } catch (SQLException e) {
                throw new ProcessingException("Problem processing result set from database. Please check your settings.", e);

            } finally {
                ResultSets.closeQuietly(rs);
                Statements.closeQuietly(statement);
                Connections.closeQuietly(connection);
            }
        }

        void processResultSet(ResultSet rs, ProcessingRuntime runtime) throws SQLException {
            Thread thread = Thread.currentThread();
            EventType eventType = source.getEventType();

            while (!thread.isInterrupted() && running && rs.next()) {
                Event newEvent = createEventFromResultSet(rs, eventType);

                runtime.sendEventFromSource(newEvent, source);
            }
        }

        @Override
        public void stopProcessingEvents() {
            this.running = false;
        }

        Connection getConnection(String className, String url, String userName, String password) throws ProcessingException {

            try {
                Class.forName(className);
            } catch (ClassNotFoundException e) {
                // this should never happen since the parameter is constrained
                throw new ProcessingException("Could not find JDBC Driver Class " + className, e);
            }

            Connection connection;

            try {
                if (userName == null && password == null) {
                    connection = DriverManager.getConnection(url);
                } else {
                    connection = DriverManager.getConnection(url, userName, password);
                }
            } catch (SQLException e) {
                throw new ProcessingException("Could not connect to database. Please check your settings.", e);
            }

            return connection;
        }

        Event createEventFromResultSet(ResultSet rs, EventType eventType) throws SQLException {
            Map<String, Object> attributeValues = Maps.newHashMap();

            for (Attribute attribute : eventType.getAttributes()) {
                Class type = attribute.getType();
                String attributeName = attribute.getName();

                if (type == String.class) {
                    String value = rs.getString(attributeName);
                    attributeValues.put(attributeName, value);

                } else if (type == Integer.class) {
                    int value = rs.getInt(attributeName);
                    attributeValues.put(attributeName, value);

                } else if (type == Short.class) {
                    short value = rs.getShort(attributeName);
                    attributeValues.put(attributeName, value);

                } else if (type == Long.class) {
                    long value = rs.getLong(attributeName);
                    attributeValues.put(attributeName, value);

                } else if (type == Double.class) {
                    double value = rs.getDouble(attributeName);
                    attributeValues.put(attributeName, value);

                } else if (type == Float.class) {
                    float value = rs.getFloat(attributeName);
                    attributeValues.put(attributeName, value);

                } else if (type == Boolean.class) {
                    String value = rs.getString(attributeName);
                    attributeValues.put(attributeName, Booleans.parseBoolean(value));
                } else {
                    throw new IllegalArgumentException(String.format("Unknown attribute type %s", type));
                }
            }

            return new Event(attributeValues);
        }
    }
}
