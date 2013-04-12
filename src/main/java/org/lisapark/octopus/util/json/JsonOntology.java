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

import java.util.List;

/**
 *
 * @author Alex Mylnikov (alexmy@lisa-park.com)
 */
public interface JsonOntology {
    
    /**
     * Uses OpenL rules to determine a level of the node with nodeName,
     * based on the current stack of up-level observed nodes.
     * 
     * @param stack
     * @param nodeName
     * @return - next level:
     *      -1 move one level up;
     *      +1 move one level down;
     *       0 stay on the same level;
     *      any other numbers, that are greater than 0 - jump up to
     *      the (stack.size() - (return number)) level or
     *      to the level 0, if (stack.size() - (return number)) < 0.
     */
    public int nextLevel(List<String> stack, String nodeName);
    
}
