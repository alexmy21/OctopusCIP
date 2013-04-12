/**************************************************************************************
 * Copyright (C) 2012 Lisa park, Inc. All rights reserved.                            *  
 * http://www.lisa-park.com                                                           *
 * E-Mail: alexmy@lisa-park.com                                                       *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt                               *
 **************************************************************************************/

package org.lisapark.octopus.core;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.awt.*;
import java.util.Set;
import java.util.UUID;
import javax.swing.*;
import org.lisapark.octopus.core.parameter.Parameter;

/**
 * Abstract Base class implementation of the {@link Node} interface that contains a {@link #name}, {@link #description}
 * and {@link #location} along with the corresponding setter/getter implementations from the {@link Node} interface.
 * <p/>
 * This class also provides method to manipulate the {@link Parameter}s for this node.
 *
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public abstract class AbstractNode implements Node {

    private final UUID id;
    private String name;
    private String description;
    private Point location;
    private Icon icon;
    private Set<Parameter> parameters = Sets.newHashSet();

    protected AbstractNode(UUID id) {
        this.id = id;
    }

    protected AbstractNode(UUID id, String name, String description) {
        this.id = id;
        setName(name);
        setDescription(description);
    }

    protected AbstractNode(UUID id, AbstractNode copyFromNode) {
        this.id = id;
        setName(copyFromNode.name);
        setDescription(copyFromNode.description);
        for (Parameter parameter : copyFromNode.getParameters()) {
            this.addParameter(parameter.copyOf());
        }
    }

    protected AbstractNode(AbstractNode copyFromNode) {
        this.id = copyFromNode.id;
        setName(copyFromNode.name);
        setDescription(copyFromNode.description);

        for (Parameter parameter : copyFromNode.getParameters()) {
            this.addParameter(parameter.copyOf());
        }
    }

    public final UUID getId() {
        return id;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public AbstractNode setName(String name) {
        checkArgument(name != null, "name cannot be null");
        this.name = name;

        return this;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    @Override
    public AbstractNode setDescription(String description) {
        checkArgument(description != null, "description cannot be null");
        this.description = description;

        return this;
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public AbstractNode setLocation(Point location) {
        checkArgument(location != null, "location cannot be null");
        this.location = location;

        return this;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public Node setIcon(Icon icon) {
        checkArgument(icon != null, "Icon cannot be null");
        this.icon = icon;

        return this;
    }

    protected void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

    protected void addParameter(Parameter.Builder parameter) {
        this.parameters.add(parameter.build());
    }

    protected Parameter getParameter(int parameterId) {
        return AbstractComponent.getComponentById(parameters, parameterId);
    }

    protected String getParameterValueAsString(int parameterId) {
        return AbstractComponent.getComponentById(parameters, parameterId).getValueAsString();
    }

    @Override
    public Set<Parameter> getParameters() {
        return ImmutableSet.copyOf(this.parameters);
    }

    /**
     * This node will check the validity of it's {@link #parameters}
     *
     * @throws ValidationException if a parameter is not valid
     */
    @Override
    public void validate() throws ValidationException {
        for (Parameter parameter : parameters) {
            parameter.validate();
        }
    }

    /**
     * This implementation of equals will check that the specified otherObject is an instance of a {@link Node}
     * and the {@link #getId()} are equivalent.
     *
     * @param otherObject the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the otherObject; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof Node)) {
            return false;
        }

        AbstractNode that = (AbstractNode) otherObject;

        return this.getId().equals(that.getId());
    }

    /**
     * This implementation of hashCode returns the {@link java.util.UUID#hashCode()} of the {@link #getId()}
     *
     * @return a hash code value for this node.
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
