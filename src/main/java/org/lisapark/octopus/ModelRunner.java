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

import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import org.lisapark.octopus.core.ProcessingModel;
import org.lisapark.octopus.core.ValidationException;
import org.lisapark.octopus.core.compiler.esper.EsperCompiler;
import org.lisapark.octopus.core.parameter.Parameter;
import org.lisapark.octopus.core.runtime.ProcessingRuntime;
import org.lisapark.octopus.core.sink.external.ExternalSink;
import org.lisapark.octopus.core.source.external.ExternalSource;
import org.openide.util.Exceptions;

/**
 *
 * @author Alex Mylnikov (alexmy@lisa-park.com)
 */
public class ModelRunner {
    
    private ProcessingModel model;
    
    public ModelRunner(ProcessingModel model){
        this.model = model;        
    }
    
    public ModelRunner(ProcessingModel model, 
            Map<String, String> sourceParam, 
            Map<String, String> sinkParam){
        
        this.model = model; 
        if (sourceParam != null) {
            Set<ExternalSource> extSources = this.model.getExternalSources();
            for (ExternalSource extSource : extSources) {
                Set<Parameter> params = extSource.getParameters();
                for (Parameter param : params) {
                    String paramName = param.getName();
                    if (sourceParam.containsKey(paramName)
                            && sourceParam.get(paramName) != null) {
                        try {
                            param.setValueFromString(sourceParam.get(paramName));
                        } catch (ValidationException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
        }
        
        if (sinkParam != null) {
            Set<ExternalSink> extSinks = this.model.getExternalSinks();
            for (ExternalSink extSink : extSinks) {
                Set<Parameter> params = extSink.getParameters();
                for (Parameter param : params) {
                    String paramName = param.getName();
                    if (sinkParam.containsKey(paramName)
                            && sinkParam.get(paramName) != null) {
                        try {
                            param.setValueFromString(sinkParam.get(paramName));
                        } catch (ValidationException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 
     * @param currentProcessingModel 
     */
    public void runModel() {
        
        if (model != null) {
            org.lisapark.octopus.core.compiler.Compiler compiler = new EsperCompiler();
            PrintStream stream = new PrintStream(System.out);
            compiler.setStandardOut(stream);
            compiler.setStandardError(stream);
            
            try {
                ProcessingRuntime runtime = compiler.compile(model);
                
                runtime.start();
                runtime.shutdown();
            } catch (ValidationException e1) {
                System.out.println(e1.getLocalizedMessage() + "\n");
            }
        }
    }
}
