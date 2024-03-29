/**
 * ************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved.
 * http://www.lisa-park.com * E-Mail: alexmy@lisa-park.com *
 * ----------------------------------------------------------------------------------
 * * The software in this package is published under the terms of the GPL
 * license * a copy of which has been included with this distribution in the
 * license.txt file. * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt *
 * ************************************************************************************
 */
package org.lisapark.octopus.core.source.external.impl;

import static com.google.common.base.Preconditions.checkState;
import com.google.common.collect.Maps;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import org.lisapark.octopus.core.Output;
import org.lisapark.octopus.core.ProcessingException;
import org.lisapark.octopus.core.ValidationException;
import org.lisapark.octopus.core.event.Attribute;
import org.lisapark.octopus.core.event.Event;
import org.lisapark.octopus.core.event.EventType;
import org.lisapark.octopus.core.parameter.Parameter;
import org.lisapark.octopus.core.runtime.ProcessingRuntime;
import org.lisapark.octopus.core.source.external.CompiledExternalSource;
import org.lisapark.octopus.core.source.external.ExternalSource;
import org.lisapark.octopus.util.Booleans;
import org.openide.util.Exceptions;

/**
 *
 * @author Alex Mylnikov (alexmy@lisa-park.com)
 */
public class GssListSourceRange extends ExternalSource {

    private static final String DEFAULT_NAME = "Google Spreadsheet Range";
    private static final String DEFAULT_DESCRIPTION = "Provides access to Google Spreadsheet."
            + " Can be used to retrieve a specified range of rows.";
    private static final int USER_EMAIL_PARAMETER_ID = 1;
    private static final int PASSWORD_PARAMETER_ID = 2;
    private static final int WORK_BOOK_PARAMETER_ID = 3;
    private static final int SPREAD_SHEET_PARAMETER_ID = 4;
    private static final int ROW_START_PARAMETER_ID = 8;
    private static final int ROW_END_PARAMETER_ID = 9;
    private static final int INDEX_PARAMETER_ID = 10;
    
    private final static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GssListSourceRange.class.getName());

    private GssListSourceRange(UUID sourceId, String name, String description) {
        super(sourceId, name, description);
    }

    private GssListSourceRange(UUID sourceId, GssListSourceRange copyFromSource) {
        super(sourceId, copyFromSource);
    }

    private GssListSourceRange(GssListSourceRange copyFromSource) {
        super(copyFromSource);
    }

    public String getWorkBook() {
        return getParameter(WORK_BOOK_PARAMETER_ID).getValueAsString();
    }

    public String getUseremail() {
        return getParameter(USER_EMAIL_PARAMETER_ID).getValueAsString();
    }

    public String getPassword() {
        return getParameter(PASSWORD_PARAMETER_ID).getValueAsString();
    }

    public String getSpredSheet() {
        return getParameter(SPREAD_SHEET_PARAMETER_ID).getValueAsString();
    }

    public Integer getRowStart() {
        return getParameter(ROW_START_PARAMETER_ID).getValueAsInteger();
    }

    public Integer getRowEnd() {
        return getParameter(ROW_END_PARAMETER_ID).getValueAsInteger();
    }

    public EventType getEventType() {
        return getOutput().getEventType();
    }
    
    
    public String getIndexFieldName() {
        return getParameter(INDEX_PARAMETER_ID).getValueAsString();
    }

    @Override
    public GssListSourceRange newInstance() {
        UUID sourceId = UUID.randomUUID();
        return new GssListSourceRange(sourceId, this);
    }

    @Override
    public GssListSourceRange copyOf() {
        return new GssListSourceRange(this);
    }

    public static GssListSourceRange newTemplate() {
        UUID sourceId = UUID.randomUUID();
        GssListSourceRange gssSource = new GssListSourceRange(sourceId, DEFAULT_NAME, DEFAULT_DESCRIPTION);

        gssSource.addParameter(Parameter.stringParameterWithIdAndName(USER_EMAIL_PARAMETER_ID, "User email: ")
                .defaultValue("demo1@lisa-park.com")
                .required(true));
        
        gssSource.addParameter(Parameter.stringParameterWithIdAndName(PASSWORD_PARAMETER_ID, "Password: ")
                .defaultValue("isasdemo")
                .required(true));
        
        gssSource.addParameter(Parameter.stringParameterWithIdAndName(WORK_BOOK_PARAMETER_ID, "Spreadsheet name: ")
                .defaultValue("USFutures15")
                .required(true));
        
        gssSource.addParameter(Parameter.stringParameterWithIdAndName(SPREAD_SHEET_PARAMETER_ID, "Sheet name: ")
                .defaultValue("SourceData")
                .required(true));

        gssSource.addParameter(Parameter.integerParameterWithIdAndName(ROW_START_PARAMETER_ID, "Start row: ")
                //                .description("Указывает начальную строку, с которой начнется загрузка в модель. Эта строка является первой в загружаемой последовательности.")
                .defaultValue(0)
                .required(false));
        gssSource.addParameter(Parameter.integerParameterWithIdAndName(ROW_END_PARAMETER_ID, "End row: ")
                //                .description("Указывает последнюю строку, которой оканчивается загрузка в модель. Эта строка является последней в загружаемой последовательности.")
                .defaultValue(20)
                .required(false));
         
        gssSource.addParameter(Parameter.stringParameterWithIdAndName(INDEX_PARAMETER_ID, "Index field name: ")
                .defaultValue("index")
                .description("This allows you to add index to the range of selected rows. "
                + "You will need this index, if you plan to make any statistical calculations that use time window "
                + "like: Moving Average, Correlation, Regression and etc.")
                .required(true));


        gssSource.setOutput(Output.outputWithId(1).setName("Output data"));

        return gssSource;
    }

    @Override
    public CompiledExternalSource compile() throws ValidationException {
        validate();

        return new CompiledGssListSource(this.copyOf());
    }

    private static class CompiledGssListSource implements CompiledExternalSource {

        private final GssListSourceRange source;
        private volatile boolean running;

        public CompiledGssListSource(GssListSourceRange source) {
            this.source = source;
        }

        @Override
        public void startProcessingEvents(ProcessingRuntime runtime) throws ProcessingException {
            // this needs to be atomic, both the check and set
            synchronized (this) {
                checkState(!running, "Source is already processing events. Cannot call processEvents again");
                running = true;
            }

            GssSourceRangeUtils gss = new GssSourceRangeUtils(
                    source.getName(),
                    source.getWorkBook(),
                    source.getSpredSheet(),
                    source.getUseremail(),
                    source.getPassword(), 
                    source.getRowStart(), 
                    source.getRowEnd());
            try {
                List<Map<String, Object>> list = gss.loadSheet();
                processCellRange(list, runtime);

            } catch (ServiceException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        void processCellRange(List<Map<String, Object>> data, ProcessingRuntime runtime) {
            Thread thread = Thread.currentThread();
            EventType eventType = source.getEventType();

            int i = 0;
            String indexName = source.getIndexFieldName();
            for (Map<String, Object> item : data) {
                if (thread.isInterrupted()) {
                    break;
                }
                // Add index to the list of data maps
                item.put(indexName, i);
                i++;
                
                Event newEvent = createEventFromCellRange(item, eventType);
                logger.log(Level.INFO, "processCellRange: ==> {0}", newEvent);
                runtime.sendEventFromSource(newEvent, source);
            }
        }

        @Override
        public void stopProcessingEvents() {
            this.running = false;
        }

        Event createEventFromCellRange(Map<String, Object> cellRange, EventType eventType) {
            Map<String, Object> attributeValues = Maps.newHashMap();
            
            for (Attribute attribute : eventType.getAttributes()) {
                Class type = attribute.getType();
                String attributeName = attribute.getName().trim();
                Object objValue = cellRange.get(attributeName.toLowerCase().replace("_", ""));

                if (type == String.class) {
                    String value = "";
                    if (objValue != null) {
                        value = (String) objValue;
                    }
                    attributeValues.put(attributeName, value);

                } else if (type == Integer.class) {
                    int value = 0;
                    if (objValue != null && !objValue.toString().trim().isEmpty()) {
                        value = Integer.parseInt(objValue.toString());
                    }
                    attributeValues.put(attributeName, value);

                } else if (type == Short.class) {
                    Short value = 0;
                    if (objValue != null && !objValue.toString().trim().isEmpty()) {
                        value = Short.parseShort(objValue.toString());
                    }
                    
                    attributeValues.put(attributeName, value);

                } else if (type == Long.class) {
                    Long value = 0L;
                    if (objValue != null && !objValue.toString().trim().isEmpty()) {
                        value = Long.parseLong((String) objValue);
                    }

                    attributeValues.put(attributeName, value);

                } else if (type == Double.class) {
                    Double value = 0D;
                    if (objValue != null && !objValue.toString().trim().isEmpty()) {
                        value = Double.parseDouble(objValue.toString());
                    }

                    attributeValues.put(attributeName, value);

                } else if (type == Float.class) {
                    Float value = 0F;
                    if (objValue != null && !objValue.toString().trim().isEmpty()) {
                        value = Float.parseFloat(objValue.toString());
                    }

                    attributeValues.put(attributeName, value);

                } else if (type == Boolean.class) {
                    String value = (String) cellRange.get(attributeName);
                    attributeValues.put(attributeName, Booleans.parseBoolean(value));
                } else {
                    throw new IllegalArgumentException(String.format("Unknown attribute type %s", type));
                }
            }
            
            return new Event(attributeValues);
        }

        private int convert2Int(Object obj) throws NumberFormatException {
            int recNum;
            if (obj instanceof Integer) {
                recNum = (Integer) obj;
            } else if (obj instanceof Double) {
                recNum = (int) Math.floor((Double) obj);
            } else if (obj instanceof Float) {
                recNum = (int) Math.floor((Float) obj);
            } else {
                recNum = Integer.parseInt(obj.toString());
            }
            return recNum;
        }

        class GssSourceRangeUtils {

            // Parameter List
            //==========================================================================
            private final String spreadSheetName;
            private final String workSheetName;
            private final int start;
            private final int end;
            // 
            private final SpreadsheetService service;
            private final FeedURLFactory factory;
            private URL listFeedUrl;

            /**
             *
             * @param serviceId
             * @param spreadSheet
             * @param workSheet
             * @param userEmail
             * @param password
             */
            GssSourceRangeUtils(String serviceId, String spreadSheet,
                    String workSheet, String userEmail, String password,
                    int start, int end) {
                this.spreadSheetName = spreadSheet;
                this.workSheetName = workSheet;
                this.start = start;
                this.end = end;

                this.service = new SpreadsheetService(serviceId);
                try {
                    service.setUserCredentials(userEmail, password);
                } catch (AuthenticationException ex) {
                    Exceptions.printStackTrace(ex);
                }
                this.factory = FeedURLFactory.getDefault();
            }

            /**
             *
             * @return @throws ServiceException
             * @throws IOException
             */
            List<Map<String, Object>> loadSheet() throws ServiceException, IOException {
                // Get the spreadsheet to load
                SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(),
                        SpreadsheetFeed.class);
                List<SpreadsheetEntry> spreadsheets = feed.getEntries();
                int spreadsheetIndex = getSpreadsheetIndex(spreadsheets, spreadSheetName);
                SpreadsheetEntry spreadsheet = feed.getEntries().get(spreadsheetIndex);

                // Get the worksheet to load
                List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
                int worksheetIndex = getWorksheetIndex(worksheets, workSheetName);
                WorksheetEntry worksheet = (WorksheetEntry) worksheets.get(worksheetIndex);
                listFeedUrl = worksheet.getListFeedUrl();

                ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

                return getEntriyRangeFromListFeed(listFeed, start, end);
            }

            /**
             * Extracts all entries from ListFeed and converts each entry to the
             * map. Map<String, Object>.
             *
             * @param feed
             * @return List of entry maps.
             */
            List<Map<String, Object>> getEntriyRangeFromListFeed(ListFeed feed, int start, int end) {

                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

                List<ListEntry> listEntries = feed.getEntries();
                if (start > end || end == 0) {
                    // Read from start to the end of listEntries
                    end = listEntries.size();
                }

                for (int i = start; i < end; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    ListEntry entry = listEntries.get(i);
                    for (String tag : entry.getCustomElements().getTags()) {
                        map.put(tag, entry.getCustomElements().getValue(tag));
                    }
                    list.add(map);
                }
                return list;
            }

            /**
             * Returns index from worksheet list by workSheetName
             *
             * @param worksheets
             * @param workSheetName
             * @return workSheet index
             */
            private int getWorksheetIndex(List<WorksheetEntry> worksheets, String workSheetName) {
                int index = 0;

                String itemNameTrimmed = workSheetName.trim();
                for (WorksheetEntry item : worksheets) {
                    if (item.getTitle().getPlainText().trim().equalsIgnoreCase(itemNameTrimmed)) {
                        break;
                    }
                    index++;
                }
                return index;
            }

            /**
             * Returns spreadsheet index by SpreadSheet name
             *
             * @param spreadsheets
             * @param spreadSheetName
             * @return spreadsheet index
             */
            private int getSpreadsheetIndex(List<SpreadsheetEntry> spreadsheets, String spreadSheetName) {
                int index = 0;

                String itemNameTrimmed = spreadSheetName.trim();
                for (SpreadsheetEntry item : spreadsheets) {
                    if (item.getTitle().getPlainText().trim().equalsIgnoreCase(itemNameTrimmed)) {
                        break;
                    }
                    index++;
                }
                return index;
            }

        }
    }
}
