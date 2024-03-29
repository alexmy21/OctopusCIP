/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved. 
 * http://www.lisa-park.com                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core.source.external;

import org.lisapark.octopus.core.AbstractNode;
import org.lisapark.octopus.core.Output;
import org.lisapark.octopus.core.Persistable;
import org.lisapark.octopus.core.ValidationException;
import org.lisapark.octopus.core.source.Source;

import java.util.UUID;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public abstract class ExternalSource extends AbstractNode implements Source {

    private Output output;

    protected ExternalSource(UUID id, String name, String description) {
        super(id, name, description);
    }

    protected ExternalSource(UUID id) {
        super(id);
    }

    protected ExternalSource(UUID id, ExternalSource copyFromNode) {
        super(id, copyFromNode);
        setOutput(copyFromNode.getOutput().copyOf());
    }

    protected ExternalSource(ExternalSource copyFromNode) {
        super(copyFromNode);
        setOutput(copyFromNode.getOutput().copyOf());
    }

    public abstract CompiledExternalSource compile() throws ValidationException;

    public abstract Source newInstance();

    public abstract Source copyOf();

    @Override
    public Output getOutput() {
        return output;
    }

    protected void setOutput(Output output) {
        this.output = output;
    }
}

