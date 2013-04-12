/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved.                            *  
 * http://www.lisa-park.com                                                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.lisapark.octopus.repository.OctopusRepository;
import org.lisapark.octopus.repository.db4o.OctopusDb4oRepository;
import org.openide.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex Mylnikov (alexmy@lisa-park.com)
 */
public class ModelDao {

    private static final Logger LOG = LoggerFactory.getLogger(ModelRunner.class);
    
    private OctopusRepository repository;     
      
    public ModelDao(String fileName) {
        Properties properties;

        try {
            properties = parseProperties(fileName);
            String repositoryFile = properties.getProperty("octopus.repository.file");

            if (repositoryFile == null || repositoryFile.length() == 0) {
                System.err.printf("The property file %s is missing the octopus.repository.file property", fileName);
                System.exit(-1);
            }

            repository = new OctopusDb4oRepository(repositoryFile);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    } 
    
    /**
     * 
     * @return 
     */
    public OctopusRepository getRepository(){
        return repository;
    }    
        
    /**
     * 
     * @param propertyFileName
     * @return
     * @throws IOException 
     */
    private Properties parseProperties(String propertyFileName) throws IOException {
        InputStream fin = null;
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(getClass().getResourceAsStream("/" + propertyFileName));

        } finally {
            IOUtils.closeQuietly(fin);
        }

        return properties;
    }
}
