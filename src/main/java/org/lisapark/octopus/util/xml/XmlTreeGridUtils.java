/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.util.xml;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Alex Mylnikov (alexmy@lisa-park.com)
 */

public class XmlTreeGridUtils {
    //--------------------------------------------------------------------------
    // TreeGrid JSP framework
    // Support functions for using TreeGrid in JAVA
    //--------------------------------------------------------------------------

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Functions for parsing uploaded xml
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //--------------------------------------------------------------------------
    
    /**
     * Returns xml Document from String
     * Returns null when xml is empty or not valid
     * 
     * @param xml
     * @return 
     */
    public static Document parseXML(String xml) {
        if (xml == null) {
            xml = "";
        }
        if (xml.equals("")) {
            return null;
        }
        if (xml.charAt(0) == '&') {
            xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                    .replaceAll("&amp;", "&").replaceAll("&quot;", "\"")
                    .replaceAll("&apos;", "'");
        }
        try {
            return DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(new org.xml.sax.InputSource(
                    new java.io.StringReader(xml)));
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Returns array of xml <I> elements in <Changes> tag
     * 
     * @param xml
     * @return 
     */
    public static Element[] getChanges(String xml) {
        return getChanges(parseXML(xml));
    }

    /**
     * 
     * @param xml
     * @return 
     */
    public static Element[] getChanges(Document xml) {
        if (xml == null) {
            return null;
        }
        NodeList changes = xml.getElementsByTagName("Changes");
        if (changes.getLength() == 0) {
            return null;
        }
        changes = changes.item(0).getChildNodes();
        int len = changes.getLength();
        Element[] element = new Element[len];
        for (int i = 0; i < len; i++) {
            element[i] = (Element) changes.item(i);
        }
        return element;
    }
   
    /**
     * Returns page number from input xml
     * 
     * @param xml
     * @return 
     */
    public static int getPagePos(String xml) {
        return getPagePos(parseXML(xml));
    }

    /**
     * 
     * @param xml
     * @return 
     */
    public static int getPagePos(Document xml) {
        if (xml == null) {
            return -1;
        }
        return Integer.valueOf(((Element) (xml.getElementsByTagName("B")
                .item(0))).getAttribute("Pos")).intValue();
    }
    
    /**
     * Returns page id from input xml
     * 
     * @param xml
     * @return 
     */
    public static String getPageId(String xml) {
        return getPageId(parseXML(xml));
    }

    /**
     * 
     * @param xml
     * @return 
     */
    public static String getPageId(Document xml) {
        if (xml == null) {
            return null;
        }
        return ((Element) (xml.getElementsByTagName("B").item(0))).getAttribute("id");
    }

    /**
     * Returns array of columns according to is grid sorted
     * Returns null if there are no columns
     * 
     * @param xml
     * @return 
     */
    public static String[] getSortCols(String xml) {
        return getSortCols(parseXML(xml));
    }

    /**
     * 
     * @param xml
     * @return 
     */
    public static String[] getSortCols(Document xml) {
        if (xml == null) {
            return null;
        }
        org.w3c.dom.Element Cfg = (org.w3c.dom.Element) xml.getElementsByTagName("Cfg").item(0);
        String[] s = Cfg.getAttribute("SortCols").split("\\,");
        return s.length == 0 || s[0].length() == 0 ? null : s;
    }

    /**
     * Returns array of columns according to is grid sorted
     * Returns null if there are no columns
     * 
     * @param xml
     * @return 
     */
    public static String[] getGroupCols(String xml) {
        return getGroupCols(parseXML(xml));
    }

    /**
     * 
     * @param xml
     * @return 
     */
    public static String[] getGroupCols(org.w3c.dom.Document xml) {
        if (xml == null) {
            return null;
        }
        Element Cfg = (Element) xml.getElementsByTagName("Cfg").item(0);
        String[] groupCols = Cfg.getAttribute("GroupCols").split("\\,");
        return groupCols.length == 0 || groupCols[0].length() == 0 ? null : groupCols;
    }

    /**
     * Returns array of sorting types for columns according to is grid sorted
     * Returns null if there are no columns
     * 
     * @param xml
     * @return 
     */
    public static int[] getSortTypes(String xml) {
        return getSortTypes(parseXML(xml));
    }

    /**
     * 
     * @param xml
     * @return 
     */
    public static int[] getSortTypes(org.w3c.dom.Document xml) {
        if (xml == null) {
            return null;
        }
        Element Cfg = (Element) xml.getElementsByTagName("Cfg").item(0);
        String[] sortTypes = Cfg.getAttribute("SortTypes").split("\\,");
        if (sortTypes.length == 0 || sortTypes[0].length() == 0) {
            return null;
        }
        int[] t = new int[sortTypes.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = Integer.valueOf(sortTypes[i]).intValue();
        }
        return t;
    }

    /**
     * Returns array of sorting types for columns according to is grid sorted
     * Returns null if there are no columns
     * 
     * @param xml
     * @return 
     */
    public static int[] getGroupTypes(String xml) {
        return getGroupTypes(parseXML(xml));
    }

    /**
     * 
     * @param xml
     * @return 
     */
    public static int[] getGroupTypes(Document xml) {
        if (xml == null) {
            return null;
        }
        Element Cfg = (Element) xml.getElementsByTagName("Cfg").item(0);
        String[] groupTypes = Cfg.getAttribute("GroupTypes").split("\\,");
        if (groupTypes.length == 0 || groupTypes[0].length() == 0) {
            return null;
        }
        int[] t = new int[groupTypes.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = Integer.valueOf(groupTypes[i]).intValue();
        }
        return t;
    }

    /**
     * Compares attribute value with value
     * 
     * @param element
     * @param name
     * @param value
     * @return 
     */
    public static boolean isAttribute(Element element, String name, String value) {
        return element.getAttribute(name).equals(value);
    }

    /**
     * Tests if given row has set the flag
     * 
     * @param element
     * @return 
     */
    public static boolean isDeleted(Element element) {
        return element.getAttribute("Deleted").equals("1");
    }

    /**
     * 
     * @param element
     * @return 
     */
    public static boolean isAdded(Element element) {
        return element.getAttribute("Added").equals("1");
    }

    /**
     * 
     * @param element
     * @return 
     */
    public static boolean isChanged(Element element) {
        return element.getAttribute("Changed").equals("1");
    }

    /**
     * Moved only to another parent 
     * 
     * @param element
     * @return 
     */
    public static boolean isMoved(Element element) {
        return element.getAttribute("Moved").equals("2");
    } 
    
    /**
     * Returns row's id attribute
     * 
     * @param element
     * @return 
     */
    public static String getId(Element element) {
        return element.getAttribute("id");
    }

    /**
     * 
     * @param element
     * @return 
     */
    public static String[] getIds(Element element) {
        return element.getAttribute("id").split("\\$");
    }

    /**
     * Returns request parameter value, for null returns ""
     * 
     * @param request
     * @param name
     * @return 
     */
    public static String getParameter(HttpServletRequest request, String name) {
        String P = request.getParameter(name);
        return P == null ? "" : P;
    }
    
//------------------------------------------------------------------------------------------------------------------

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Functions for generating SQL commands, from strings and xml nodes
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//------------------------------------------------------------------------------------------------------------------

    /**
     * Returns string in ' ' with doubled all '
     * 
     * @param value
     * @return 
     */
    public static String toSQL(String value) {
        return "'" + (value == null ? "" : value.replaceAll("'", "''")) + "'";
    }

    /**
     * 
     * @param element
     * @param name
     * @return 
     */
    public static String toSQL(Element element, String name) {
        return "'" + element.getAttribute(name).replaceAll("'", "''") + "'";
    }

    /**
     * Returns one string for UPDATE command, for string types
     * Returns "name='value',"
     * 
     * @param name
     * @param value
     * @return 
     */
    public static String toSQLUpdateString(String name, String value) {
        return value == null ? "" : name + "='" + value.replaceAll("'", "''") + "',";
    }

    /**
     * 
     * @param name
     * @param element
     * @param attname
     * @return 
     */
    String toSQLUpdateString(String name, Element element, String attname) {
        return element.hasAttribute(attname) ? name + "='" 
                + element.getAttribute(attname).replaceAll("'", "''") + "'," : "";
    }

    /**
     * Returns one string for UPDATE command, for number types
     * Returns "name=value,"
     * 
     * @param name
     * @param value
     * @return 
     */
    public static String toSQLUpdateNumber(String name, String value) {
        return value == null || value.length() == 0 ? "" : name + "=" + value + ",";
    }

    /**
     * 
     * @param name
     * @param element
     * @param attname
     * @return 
     */
    public static String toSQLUpdateNumber(String name, Element element, String attname) {
        return element.hasAttribute(attname) ? name + "=" + element.getAttribute(attname) + "," : "";
    }

    /**
     * Deletes all commas on the end of string
     * Useful when building comma separated list in loop, 
     * call it after list is built to strip ending comma(s)
     * @param value
     * @return 
     */
    public static String trimSQL(String value) {
        return value == null ? "" : value.replaceAll("[\\,\\s]*$", "");
    }

    /**
     * Returns attribute values in format "outName[0]=value_of_attrNames[0],
     * outName[1]=value_of_attrNames[1], ..."
     * The string never ends by comma
     * If the attribute does not exists, is not included
     * If attrIsString[x] is true, it encloses attribute value by ' '
     * 
     * @param element
     * @param attrNames
     * @param outNames
     * @param attrIsString
     * @return 
     */
    public static String toSQLUpdate(Element element, String[] attrNames, String[] outNames, boolean attrIsString[]) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < attrNames.length; i++) {
            Node N = element.getAttributeNode(attrNames[i]);
            if (N != null) {
                if (attrIsString[i]) {
                    strBuilder.append(outNames[i]).append("='").append(N.getNodeValue()
                            .replaceAll("'", "''")).append("',");
                } else {
                    strBuilder.append(outNames[i]).append("=").append(N.getNodeValue()).append(",");
                }
            }
        }
        if (strBuilder.length() > 0) {
            strBuilder.setLength(strBuilder.length() - 1);  // Last comma away
        }
        return strBuilder.toString();
    }

    /**
     * Returns attribute values separated by comma
     * The string never ends by comma
     * If the attribute does not exists, is not included
     * If attrIsString[x] is true, it encloses attribute value by ' '
     * 
     * @param element
     * @param attrNames
     * @param attrIsString
     * @return 
     */
    public static String toSQLInsert(org.w3c.dom.Element element, String[] attrNames, boolean attrIsString[]) {
        StringBuilder S = new StringBuilder();
        for (int i = 0; i < attrNames.length; i++) {
            Node N = element.getAttributeNode(attrNames[i]);
            if (N != null) {
                if (attrIsString[i]) {
                    S.append("'").append(N.getNodeValue().replaceAll("'", "''")).append("',");
                } else {
                    S.append(N.getNodeValue()).append(",");
                }
            }
        }
        if (S.length() > 0) {
            S.setLength(S.length() - 1);  // Last comma away
        }
        return S.toString();
    }
//------------------------------------------------------------------------------------------------------------------

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Advanced functions for updating changes in xml to database
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//------------------------------------------------------------------------------------------------------------------

    /**
     * Helper function for saveTree
     * Returns value in or without ' '
     * If name is found in names array, it checks types for string type and if found returns in ' '
     * 
     * @param names
     * @param types
     * @param name
     * @param value
     * @return 
     */
    public static String getValue(String[] names, int[] types, String name, String value) {
        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(name)) {
                int t = types[i];
                if (value.equals("") && (t == Types.DATE || t == java.sql.Types.TIME)) {
                    return "NULL";
                }
                if (t == Types.VARCHAR || t == Types.CHAR || t == Types.LONGVARCHAR 
                        || t == Types.DATE || t == Types.TIME) {
                    return "'" + value.replaceAll("'", "''") + "'";
                }
                return value.equals("") ? "NULL" : value;
            }
        }
        return "";
    }

    /**
     * Saves changes in xml to database table
     * idCol is database table column name where is stored id attribute
     * parentCol is database table column name where is stored Parent attribute, 
     * for Parent<-Child relation.
     * In Parent column the row has value of id column of parent row => All parent's 
     * children have its id in their Parent column
     * bodyParent is value of Parent for new added root rows
     * 
     * @param xml
     * @param Cmd
     * @param table
     * @param idCol
     * @param parentCol
     * @param bodyParent
     * @return
     * @throws java.sql.SQLException
     * @throws java.io.IOException 
     */
    public static boolean saveTree(String xml, Statement stmt, String table, String idCol, String parentCol, 
            String bodyParent) throws SQLException, IOException {

// --- Gets column names and types ---
        ResultSet R = stmt.executeQuery("SELECT TOP 1 * FROM " + table);
        ResultSetMetaData M = R.getMetaData();
        int cnt = M.getColumnCount();
        String[] colNames = new String[cnt];
        int[] types = new int[cnt];
        for (int i = 1; i <= cnt; i++) {
            colNames[i - 1] = M.getColumnName(i);
            types[i - 1] = M.getColumnType(i);
        }
        String[] names = colNames;

// --- saves data to database ---
        Element[] Ch = getChanges(xml);
        if (Ch == null) {
            return false;
        }
        for (int i = 0; i < Ch.length; i++) {
            Element element = Ch[i];
            NamedNodeMap A = element.getAttributes();
            String id = getId(element);
            if (id.equals("")) {
                continue; // Error
            }
            if (isDeleted(element)) { // Deleting
                stmt.executeUpdate("DELETE FROM " + table + " WHERE " 
                        + idCol + "=" + getValue(names, types, idCol, id));
            } else if (isAdded(element)) { // Adding
                StringBuilder Cols = new StringBuilder();
                StringBuilder Vals = new StringBuilder();
                Cols.append("INSERT INTO ").append(table).append("(");
                Vals.append(") VALUES (");
                for (int a = 0; a < A.getLength(); a++) {
                    Node N = A.item(a);
                    String name = N.getNodeName();
                    if (!name.equals("Added") && !name.equals("Changed") 
                            && !name.equals("Moved") && !name.equals("Next") 
                            && !name.equals("Prev") && !name.equals("Parent")) {
                        Cols.append("").append(name).append(",");
                        Vals.append(getValue(names, types, name, N.getNodeValue())).append(",");
                    }
                }
                Cols.append(parentCol);
                Vals.append("'").append(!element.getAttribute("Parent").equals("") 
                        ? element.getAttribute("Parent") : bodyParent).append("'");
                stmt.executeUpdate(Cols.toString() + Vals.toString() + ")");
            } else if (isChanged(element) || isMoved(element)) { // Updating
                StringBuilder S = new StringBuilder();
                S.append("UPDATE ").append(table).append(" SET ");
                for (int a = 0; a < A.getLength(); a++) {
                    Node N = A.item(a);
                    String name = N.getNodeName();
                    if ("Parent".equals(name)) {
                        name = parentCol;
                    }
                    if (!name.equals("Added") && !name.equals("Changed") 
                            && !name.equals("Moved") && !name.equals("Next") 
                            && !name.equals("Prev") && !name.equals("id")) {
                        S.append(name).append("=")
                                .append(getValue(names, types, name, N.getNodeValue()))
                                .append(",");
                    }
                }
                S.setLength(S.length() - 1);
                S.append(" WHERE ").append(idCol).append("=").append(getValue(names, types, idCol, id));
                stmt.executeUpdate(S.toString());
            }
        }
        return true;
    }
//------------------------------------------------------------------------------------------------------------------

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Functions for creating xml from strings and Recordset
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Returns string with replaced &,',< by xml entities
     * 
     * @param value
     * @return 
     */
    public static String toXMLString(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("&", "&amp;").replaceAll("'", "&apos;")
                .replaceAll("<", "&lt;").replaceAll("\n", "&#x0a;")
                .replaceAll("\r", "&#x0d;");
    }

    /**
     * Returns string with replaced xml entities by &,',<,",> by characters
     * 
     * @param value
     * @return 
     */
    public static String fromXMLString(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"").replaceAll("&apos;", "'")
                .replaceAll("&#x0a;", "\n").replaceAll("&#x0d;", "\r")
                .replaceAll("&amp;", "&");
    }

    /**
     * Returns string with replaced &," by xml entities
     * 
     * @param value
     * @return 
     */
    public static String toHTMLString(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("&", "&amp;").replaceAll("\\\"", "&quot;");
    }

    /**
     * Returns value as xml string quoted by single quote and replaced <,&,' by xml entities
     * 
     * @param value
     * @return 
     */
    public static String toXML(short value) {
        return "'" + String.valueOf(value) + "'";
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static String toXML(int value) {
        return "'" + String.valueOf(value) + "'";
    }

    public static String toXML(long value) {
        return "'" + String.valueOf(value) + "'";
    }

    public static String toXML(float value) {
        return "'" + String.valueOf(value) + "'";
    }

    public static String toXML(double value) {
        return "'" + String.valueOf(value) + "'";
    }

    public static String toXML(boolean value) {
        return "'" + (value ? "1" : "0") + "'";
    }

    public static void toXML(StringBuffer S, String value) {
        S.append(toXML(value));
    }

    public static String toXML(String value) {
        if (value == null) {
            return "''";
        }
        return "'" + value.replaceAll("&", "&amp;").replaceAll("'", "&apos;")
                .replaceAll("<", "&lt;").replaceAll("\n", "&#x0a;")
                .replaceAll("\r", "&#x0d;") + "'";
    }

    public static void toXML(StringBuffer S, String name, String value) {
        S.append(toXML(name, value));
    }

    public static String toXML(String name, String value) {
        if (value == null) {
            return "";
        }
        return " " + name + "='" + value.replaceAll("&", "&amp;")
                .replaceAll("'", "&apos;").replaceAll("<", "&lt;")
                .replaceAll("\n", "&#x0a;").replaceAll("\r", "&#x0d;") + "'";
    }

    public static String toXML(String name, ResultSet R, String colName) 
            throws SQLException {
        return toXML(name, R.getString(colName));
    }

    public static void toXML(StringBuffer S, String name, ResultSet R, String colName) 
            throws SQLException {
        S.append(toXML(name, R.getString(colName)));
    }
    
//------------------------------------------------------------------------------------------------------------------

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Advanced functions for creating xml
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//------------------------------------------------------------------------------------------------------------------

    /**
     * xml representation of one row with given attribute names and their values
     * end is the string appended after row, for rows with children use ">", 
     * without children "/>", or you can set own attributes
     * 
     * @param names
     * @param values
     * @return 
     */
    public static String getRowXML(String[] names, String[] values) {
        return getRowXML(names, values, "/>");
    }

    public static String getRowXML(String[] names, String[] values, String end) {
        StringBuffer S = new StringBuffer();
        S.append("<I");
        for (int i = 0; i < names.length; i++) {
            toXML(S, names[i], values[i]);
        }
        S.append(end);
        return S.toString();
    }

    /**
     * xml representation of one row with given attribute names 
     * and their values from ResultSet in columns colNames
     * end is the string appended after row, for rows with children use ">", 
     * without children "/>", or you can set own attributes
     * Does not move cursor in ResultSet
     *      * 
     * @param R
     * @param names
     * @param colNames
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getRowXML(ResultSet R, String[] names, String[] colNames) 
            throws SQLException {
        return getRowXML(R, names, colNames, "/>");
    }

    /**
     * 
     * @param R
     * @param names
     * @param colNames
     * @param end
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getRowXML(ResultSet R, String[] names, String[] colNames, String end) 
            throws SQLException {
        StringBuffer S = new StringBuffer();
        S.append("<I");
        for (int i = 0; i < names.length; i++) {
            toXML(S, names[i], R.getString(colNames[i]));
        }
        S.append(end);
        return S.toString();
    }

    /**
     * xml representation of one row with given attribute names and their 
     * values from ResultSet in columns colNames
     * end is the string appended after row, for rows with children use ">", 
     * without children "/>", or you can set own attributes
     * Does not move cursor in ResultSet
     * 
     * @param R
     * @param names
     * @param colIndexes
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getRowXML(ResultSet R, String[] names, int[] colIndexes) 
            throws SQLException {
        return getRowXML(R, names, colIndexes, "/>");
    }

    /**
     * 
     * @param R
     * @param names
     * @param colIndexes
     * @param end
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getRowXML(ResultSet R, String[] names, int[] colIndexes, String end) 
            throws SQLException {
        StringBuffer S = new StringBuffer();
        S.append("<I");
        for (int i = 0; i < names.length; i++) {
            toXML(S, names[i], R.getString(colIndexes[i]));
        }
        S.append(end);
        return S.toString();
    }

    /**
     * xml representation of one row with given attribute names and their 
     * values from ResultSet in columns indexed as attribute names
     * end is the string appended after row, for rows with children use ">", 
     * without children "/>", or you can set own attributes
     * Does not move cursor in ResultSet
     * 
     * @param R
     * @param names
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getRowXML(ResultSet R, String[] names) throws SQLException {
        return getRowXML(R, names, "/>");
    }

    /**
     * 
     * @param R
     * @param names
     * @param end
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getRowXML(ResultSet R, String[] names, String end) 
            throws SQLException {
        StringBuffer S = new StringBuffer();
        S.append("<I");
        for (int i = 0; i < names.length; i++) {
            toXML(S, names[i], R.getString(i + 1));
        }
        S.append(end);
        return S.toString();
    }

    /**
     * Returns complete xml representation of all rows with given attribute names 
     * and their values from ResultSet in columns indexed as attribute names
     * @param R
     * @param names
     * @param colNames
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getTableXML(ResultSet R, String[] names, String[] colNames) 
            throws SQLException {
        StringBuffer S = new StringBuffer();
        S.append("<Grid><Body><B>");
        while (R.next()) {
            S.append("<I");
            for (int i = 0; i < names.length; i++) {
                toXML(S, names[i], R.getString(colNames[i]));
            }
            S.append("/>");
        }
        S.append("</B></Body></Grid>");
        return S.toString();
    }

    /**
     * Returns complete xml representation of all rows with given attribute 
     * names and their values from ResultSet 
     * in columns indexed as attribute names
     * 
     * @param R
     * @param names
     * @param colIndexes
     * @return
     * @throws SQLException 
     */
    public static String getTableXML(ResultSet R, String[] names, int[] colIndexes) 
            throws SQLException {
        StringBuffer S = new StringBuffer();
        S.append("<Grid><Body><B>");
        while (R.next()) {
            S.append("<I");
            for (int i = 0; i < names.length; i++) {
                toXML(S, names[i], R.getString(colIndexes[i]));
            }
            S.append("/>");
        }
        S.append("</B></Body></Grid>");
        return S.toString();
    }

    /**
     * Returns complete xml representation of all rows with given attribute
     * names and their values from ResultSet in columns indexed as attribute
     * names
     *
     * @param R
     * @param names
     * @return
     * @throws SQLException
     */
    public static String getTableXML(ResultSet R, String[] names) throws SQLException {
        StringBuffer S = new StringBuffer();
        S.append("<Grid><Body><B>");
        while (R.next()) {
            S.append("<I");
            for (int i = 0; i < names.length; i++) {
                toXML(S, names[i], R.getString(i + 1));
            }
            S.append("/>");
        }
        S.append("</B></Body></Grid>");
        return S.toString();
    }

    /**
     * Returns complete xml representation from database resultset. 
     * Uses the same TreeGrid column names as in resultset
     * idCol is name of database column that will be stored to id attribute
     * 
     * @param R
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getTableXML(ResultSet R) throws SQLException {
        return getTableXML(R, "id");
    }

    /**
     
     * 
     * @param R
     * @param idCol
     * @return
     * @throws SQLException 
     */
    public static String getTableXML(ResultSet R, String idCol) throws SQLException {
        java.sql.ResultSetMetaData M = R.getMetaData();
        int cnt = M.getColumnCount();
        String[] names = new String[cnt];
        for (int i = 1; i <= cnt; i++) {
            String Name = M.getColumnName(i);
            if (Name.equalsIgnoreCase(idCol)) {
                Name = "id";
            }
            names[i - 1] = Name;
        }
        return getTableXML(R, names);
    }

    /**
     * Returns complete xml representation of all rows with given attribute names and 
     * their values from ResultSet in columns indexed as attribute names
     * table is name of database table
     * names are TreeGrid attribute names in order in that are filled from columns from database table
     * names must contain name "id", this is identify attribute
     *    In Parent column the row has value of id column of parent row => All parent's 
     * children have its id in their Parent column
     * For deep==false reads only one level of tree, for server side child paging
     * 
     * @param Cmd
     * @param table
     * @param names
     * @param colNames
     * @param bodyParent
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getTreeXML(Statement Cmd, String table, String[] names, String[] colNames, String bodyParent) 
            throws SQLException {
        return getTreeXML(Cmd, table, orderNames(Cmd, table, names, colNames), bodyParent);
    }

    /**
     * 
     * @param Cmd
     * @param table
     * @param names
     * @param colNames
     * @param bodyParent
     * @param headParent
     * @param footParent
     * @param deep
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getTreeXML(Statement stmt, String table, String[] names, String[] colNames, 
            String bodyParent, String headParent, String footParent, boolean deep) 
            throws SQLException {
        return getTreeXML(stmt, table, orderNames(stmt, table, names, colNames), 
                bodyParent, headParent, footParent, deep);
    }

    /**
     * 
     * @param Cmd
     * @param table
     * @param names
     * @param bodyParent
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getTreeXML(Statement stmt, String table, String[] names, 
            String bodyParent) throws SQLException {
        return getTreeXML(stmt, table, names, bodyParent, null, null, true);
    }

    /**
     * 
     * @param Cmd
     * @param table
     * @param names
     * @param bodyParent
     * @param headParent
     * @param footParent
     * @param deep
     * @return
     * @throws java.sql.SQLException 
     */
    public static String getTreeXML(Statement stmt, String table, String[] names, 
            String bodyParent, String headParent, String footParent, boolean deep) throws SQLException {
        StringBuilder S = new StringBuilder();
        S.append("<Grid>");
        if (headParent != null) {
            S.append("<Head>").append(getTreeXML(stmt, table, names, headParent, deep)).append("</Head>");
        }
        if (bodyParent != null) {
            S.append("<Foot>").append(getTreeXML(stmt, table, names, footParent, deep)).append("</Foot>");
        }
        S.append("<Body><B>").append(getTreeXML(stmt, table, names, bodyParent, deep)).append("</B></Body>");
        S.append("</Grid>");
//out.print(Str.toString().replaceAll("\\&","&amp;").replaceAll("\\<","&lt;").replaceAll("\\>","&gt;").replaceAll("\\\"","&quot;"));
        return S.toString();
    }

    /**
     * Helper function for getTreeXML()
     * orders names (attribute names) to match indexes in table according to colNames
     * 
     * @param stmt
     * @param table
     * @param names
     * @param colNames
     * @return
     * @throws SQLException 
     */
    public static String[] orderNames(Statement stmt, String table, String[] names, String[] colNames) 
            throws SQLException {
        java.sql.ResultSet R = stmt.executeQuery("SELECT TOP 1 * FROM " + table);
        java.sql.ResultSetMetaData M = R.getMetaData();
        int cnt = M.getColumnCount();
        String[] newnames = new String[cnt];
        for (int i = 1; i <= cnt; i++) {
            String Name = M.getColumnName(i);
            for (int j = 0; j < colNames.length; j++) {
                if (Name.equalsIgnoreCase(colNames[j])) {
                    newnames[i - 1] = names[j];
                }
            }
        }
        return newnames;
    }

    /**
     * Returns children of row with id parentVal => returns all rows that have in their 
     * Parent column value parentVal
     * Other parameters are the same as in previous function getTreeXML
     * 
     * @param Cmd
     * @param table
     * @param names
     * @param colNames
     * @param parentVal
     * @param deep
     * @return
     * @throws SQLException 
     */
    public static String getTreeXML(java.sql.Statement Cmd, String table, String[] names, 
            String[] colNames, String parentVal, boolean deep) throws SQLException {
        return getTreeXML(Cmd, table, orderNames(Cmd, table, names, colNames), parentVal, deep);
    }

    public static String getTreeXML(Statement stmt, String table, String[] names, String parentVal, boolean deep) 
            throws SQLException {
        StringBuilder S = new StringBuilder();
        int cnt = names.length, id = 0, parent = 0;
        for (int i = 0; i < cnt; i++) {
            if (names[i] == null) {
                continue;
            }
            if (names[i].equalsIgnoreCase("id")) {
                id = i + 1;
            } else if (names[i].equalsIgnoreCase("parent")) {
                parent = i + 1;
            }
        }
        ResultSet R = stmt.executeQuery("SELECT * FROM " + table + (parent > 0 ? " WHERE " 
                + names[parent - 1] + "='" + parentVal + "'" : ""));
        if (R == null) {
            return "";
        }
        while (R.next()) {
            S.append("<I");
            for (int i = 1; i <= cnt; i++) {
                String Name = names[i - 1];
                String Value = R.getString(i);
                if (i != parent && Value != null && Value.length() > 0) {
                    S.append(" ").append(Name).append("='").append(Value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll("'", "&apos;").replaceAll("\n", "&#x0a;").replaceAll("\r", "&#x0d;")).append("'");
                }
            }
            S.append(">");
            if (parent > 0 && deep) {
                S.append(getTreeXML(stmt, table, names, R.getString(id), deep)).append("\r\n");
            }
            S.append("</I>");
        }
        return S.toString();
    }

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Examples support
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//------------------------------------------------------------------------------------------------------------------
// Returns statemenet to HSQLDB database. This database provider is used for examples
    public static Statement getHsqlStatement(HttpServletRequest request, JspWriter out, String dbPath, 
            String user, String pass) throws Exception {
        return getHsqlStatement(request, out, dbPath, user, pass, false);
    }

    public static Statement getHsqlStatement(HttpServletRequest request, JspWriter out, String dbPath, 
            String user, String pass, boolean fromHTML) throws Exception {
        String Path = request.getServletPath().replaceAll("[^\\/\\\\]*$", ""); // Relative path to script directory ending with "/"
        Connection conn;
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            conn = DriverManager.getConnection("jdbc:hsqldb:file:" + request.getRealPath(Path + dbPath), "sa", "");
            return conn.createStatement();
        } catch (Exception e) {
            //String Err = "! Failed to load HSQLDB JDBC driver.<br>You need to copy <b>hsqldb.jar</b> file to your shared lib directory and <b>restart</b> your http server.";
            String Err = "! Failed to load HSQLDB JDBC driver.\n\nYou need to copy \"hsqldb.jar\" file to your shared lib directory and RESTART your http server.";
            try {
                out.print(fromHTML ? "<font color=red>" + Err + "</font>" : "<Grid><IO Result='-1' Message='" + Err + "'/></Grid>");
                out.close();
            } catch (Exception e2) {
            }
            throw new Exception(Err);
        }
    }
}
