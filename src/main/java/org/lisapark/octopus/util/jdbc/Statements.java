/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.util.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public abstract class Statements {

    private static final Logger LOG = LoggerFactory.getLogger(Connections.class);

    public static void closeQuietly(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOG.warn("Problem closing statement", e);
            }
        }
    }
}
