/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.repository;

import org.lisapark.octopus.core.ProcessingModel;
import org.lisapark.octopus.core.processor.Processor;
import org.lisapark.octopus.core.sink.external.ExternalSink;
import org.lisapark.octopus.core.source.external.ExternalSource;

import java.util.List;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public interface OctopusRepository {
    void saveProcessingModel(ProcessingModel model) throws RepositoryException;

    List<ProcessingModel> getProcessingModelsByName(String name) throws RepositoryException;

    ProcessingModel getProcessingModelByName(String name) throws RepositoryException;
    
    List<ExternalSink> getAllExternalSinkTemplates() throws RepositoryException;

    List<ExternalSource> getAllExternalSourceTemplates() throws RepositoryException;

    List<Processor> getAllProcessorTemplates() throws RepositoryException;
}
