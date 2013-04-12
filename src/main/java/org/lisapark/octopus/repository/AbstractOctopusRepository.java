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

import com.google.common.collect.Lists;
import java.util.List;
import org.lisapark.octopus.core.processor.Processor;
import org.lisapark.octopus.core.processor.impl.Addition;
import org.lisapark.octopus.core.processor.impl.And;
import org.lisapark.octopus.core.processor.impl.CrossAbove;
import org.lisapark.octopus.core.processor.impl.CrossUnder;
import org.lisapark.octopus.core.processor.impl.Division;
import org.lisapark.octopus.core.processor.impl.ForecastSRM;
import org.lisapark.octopus.core.processor.impl.LinearRegressionProcessor;
import org.lisapark.octopus.core.processor.impl.Multiplication;
import org.lisapark.octopus.core.processor.impl.Or;
import org.lisapark.octopus.core.processor.impl.PearsonsCorrelationProcessor;
import org.lisapark.octopus.core.processor.impl.PipeDouble;
import org.lisapark.octopus.core.processor.impl.PipeString;
import org.lisapark.octopus.core.processor.impl.PipeStringDouble;
import org.lisapark.octopus.core.processor.impl.Sma;
import org.lisapark.octopus.core.processor.impl.Subtraction;
import org.lisapark.octopus.core.processor.impl.Xor;
import org.lisapark.octopus.core.sink.external.ExternalSink;
import org.lisapark.octopus.core.sink.external.impl.ChocoSimppGssSink;
import org.lisapark.octopus.core.sink.external.impl.ConsoleSink;
import org.lisapark.octopus.core.sink.external.impl.DatabaseSink;
import org.lisapark.octopus.core.sink.external.impl.GssSink;
import org.lisapark.octopus.core.sink.external.impl.ForecastGssSink;
import org.lisapark.octopus.core.sink.external.impl.ModelJsonNeo4jSink;
import org.lisapark.octopus.core.sink.external.impl.RabbitMqSink;
import org.lisapark.octopus.core.source.external.ExternalSource;
import org.lisapark.octopus.core.source.external.impl.Db4oQuerySource;
import org.lisapark.octopus.core.source.external.impl.GssListSourceQuery;
import org.lisapark.octopus.core.source.external.impl.GssListSourceRange;
import org.lisapark.octopus.core.source.external.impl.HtmlTableSource;
import org.lisapark.octopus.core.source.external.impl.RabbitMqSource;
import org.lisapark.octopus.core.source.external.impl.SimppGssSource;
import org.lisapark.octopus.core.source.external.impl.SqlQuerySource;
import org.lisapark.octopus.core.source.external.impl.TestSource;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public abstract class AbstractOctopusRepository implements OctopusRepository {
    @Override
    public List<ExternalSink> getAllExternalSinkTemplates() {
        return Lists.<ExternalSink>newArrayList(
                
                ConsoleSink.newTemplate(),
                RabbitMqSink.newTemplate(),
                DatabaseSink.newTemplate(),
//                GssPrognoseSink.newTemplate(),
                GssSink.newTemplate(),
                ForecastGssSink.newTemplate(),
//                JsonDatabaseSink.newTemplate(),
//                MongoDbSink.newTemplate(),
//                ChocoSimppXmlWebDavSink.newTemplate(),
                ChocoSimppGssSink.newTemplate(),
                ModelJsonNeo4jSink.newTemplate()
//                XmlWarehouseDatabaseSink.newTemplate(),
//                XmlProductionDatabaseSink.newTemplate()
                );
    }

    @Override
    public List<ExternalSource> getAllExternalSourceTemplates() {

        return Lists.<ExternalSource>newArrayList(
                RabbitMqSource.newTemplate(),
                HtmlTableSource.newTemplate(),
//                DbScannerSource.newTemplate(),
//                ExcelColumnsSource.newTemplate(),
//                ExcelFileDataSource.newTemplate(),
//                ExcelProductionSource.newTemplate(),
//                ExcelWarehouseSource.newTemplate(),
//                ExcelWebDavDataSource.newTemplate(),
//                FileJsonDataSource.newTemplate(),
//                GssListSource.newTemplate(),
                GssListSourceRange.newTemplate(),
                GssListSourceQuery.newTemplate(),
//                GssSource.newTemplate(),
//                MongoDbSource.newTemplate(),
//                SimppXmlWebDavSource.newTemplate(),
                SimppGssSource.newTemplate(),
//                SqlQuery2JsonSource.newTemplate(),
                SqlQuerySource.newTemplate(),
                Db4oQuerySource.newTemplate(),
                TestSource.newTemplate()
                );

    }

    @Override
    public List<Processor> getAllProcessorTemplates() {
        return Lists.<Processor>newArrayList(
                
                Addition.newTemplate(),
                And.newTemplate(),
                Or.newTemplate(),
                Xor.newTemplate(),
                CrossAbove.newTemplate(),
                CrossUnder.newTemplate(),
                ForecastSRM.newTemplate(),
                Division.newTemplate(),
                LinearRegressionProcessor.newTemplate(),
                Multiplication.newTemplate(),
                PearsonsCorrelationProcessor.newTemplate(),                
                PipeDouble.newTemplate(),
                PipeString.newTemplate(),
                PipeStringDouble.newTemplate(),
//                ProductionUnitProcessor.newTemplate(),
                Sma.newTemplate(),
                Subtraction.newTemplate()
//                VectorAddition.newTemplate(),
//                VectorSubtraction.newTemplate()
        );
    }
}
