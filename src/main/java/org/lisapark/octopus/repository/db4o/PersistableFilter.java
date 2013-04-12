/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.repository.db4o;

import com.db4o.instrumentation.core.ClassFilter;
import org.lisapark.octopus.core.Persistable;

import java.lang.annotation.Annotation;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public class PersistableFilter implements ClassFilter {
    @Override
    public boolean accept(Class<?> aClass) {
        if (null == aClass || aClass.equals(Object.class)) {
            return false;
        }
        return hasAnnotation(aClass)
                || accept(aClass.getSuperclass());
    }

    private boolean hasAnnotation(Class<?> aClass) {
        // We compare by name, to be class-loader independent
        Annotation[] annotations = aClass.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName()
                    .equals(Persistable.class.getName())) {
                return true;
            }
        }
        return false;
    }

}
