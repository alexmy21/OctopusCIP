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

/**
 *
 * @author Alex Mylnikov (alexmy@lisa-park.com)
 */
public class XmlCleanUp {
    
    public static void main(String[] str){
        System.out.println(clean(DATA.replaceAll("\t", "")));
    }
    
    private static String clean(String string){
        StringBuilder cleanStr = new StringBuilder();
        
        Character lookFor = '<';
        
        for(Character ch : string.toCharArray()){
            if(ch == lookFor){
                lookFor = lookFor == '<' ? '>' : '<'; 
                cleanStr.append(ch);
            } else if(lookFor == '>'){
                cleanStr.append(ch);
            }            
        }
        
        return cleanStr.toString();
    }
    
    private static final String DATA = 
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
             "<Grid>\n"
            + "<Body>\n"
            + "<B>\n"
            + "<I prod=\"1\" machine=\"2\" unitvalue=\"12\" cost=\"10\" value=\"0\"   machine_name=\"machine2\" step=\"1\" prod_name=\"prod1\" fixed=\"0\" />\n"
            + "<I prod=\"1\" machine=\"1\" unitvalue=\"13\" cost=\"11\" value=\"0\"   machine_name=\"machine1\" step=\"2\" prod_name=\"prod1\" fixed=\"0\" />"
            + "<I prod=\"1\" machine=\"3\" unitvalue=\"14\" cost=\"12\" value=\"8\"   machine_name=\"machine3\" step=\"2\" prod_name=\"prod1\" fixed=\"1\" />"
            + "<I prod=\"1\" machine=\"2\" unitvalue=\"11\" cost=\"9\" 	value=\"0\"  machine_name=\"machine2\" step=\"3\" prod_name=\"prod1\" fixed=\"0\" />"
            + "<I prod=\"1\" machine=\"3\" unitvalue=\"14\" cost=\"12\" value=\"0\"   machine_name=\"machine3\" step=\"3\" prod_name=\"prod1\" fixed=\"0\" /> "
            + "<I prod=\"2\" machine=\"2\" unitvalue=\"12\" cost=\"10\" value=\"0\"   machine_name=\"machine2\" step=\"1\" prod_name=\"prod2\" fixed=\"0\" /> "
            + "<I prod=\"2\" machine=\"3\" unitvalue=\"14\" cost=\"12\" value=\"10\"  machine_name=\"machine3\" step=\"1\" prod_name=\"prod2\" fixed=\"1\" /> "
            + "<I prod=\"2\" machine=\"1\" unitvalue=\"14\" cost=\"12\" value=\"0\"   machine_name=\"machine1\" step=\"2\" prod_name=\"prod2\" fixed=\"0\" /> "
            + "<I prod=\"2\" machine=\"3\" unitvalue=\"13\" cost=\"11\" value=\"0\"   machine_name=\"machine3\" step=\"2\" prod_name=\"prod2\" fixed=\"0\" /> "
            + "<I prod=\"3\" machine=\"2\" unitvalue=\"13\" cost=\"11\" value=\"0\"   machine_name=\"machine2\" step=\"1\" prod_name=\"prod3\" fixed=\"0\" /> "
            + "<I prod=\"3\" machine=\"3\" unitvalue=\"12\" cost=\"10\" value=\"0\"   machine_name=\"machine3\" step=\"1\" prod_name=\"prod3\" fixed=\"0\" /> "
            + "<I prod=\"3\" machine=\"1\" unitvalue=\"11\" cost=\"9\" 	value=\"0\"   machine_name=\"machine1\" step=\"2\" prod_name=\"prod3\" fixed=\"0\" />"
            + "<I prod=\"3\" machine=\"2\" unitvalue=\"14\" cost=\"12\" value=\"0\"   machine_name=\"machine2\" step=\"2\" prod_name=\"prod3\" fixed=\"0\" />"
            + "</B>"
            + "</Body>"
            + "</Grid>";
    
}
