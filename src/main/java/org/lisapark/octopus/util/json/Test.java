/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.util.json;

/**
 *
 * @author Alex Mylnikov (alexmy@lisa-park.com)
 */
public class Test {
    
    public static void main(String[] args){
        String date = convertDate("09.01.2009");
        System.out.println(date);
    }
    
    private static String convertDate(String string) {
            String istring = string.replace('.', '-');
            String[] dateparts = istring.split("-");
            
//            logger.log(Level.INFO, "convertDate: ====> {0}", istring);
            
            if(dateparts.length == 3){
                return dateparts[2] + "-" + dateparts[1] + "-" + dateparts[0];
            } else {
                return "2012" + "-" + "06" + "-" + "13";
            }
        }
    
}
