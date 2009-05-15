/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.binding;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ccc.api.ActionStatus;
import ccc.api.ActionSummary;
import ccc.api.ID;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * {@link ModelData} implementation for the {@link ActionSummary} class.
 *
 * @author Civic Computing Ltd.
 */
public class ActionSummaryModelData
    implements
        ModelData {

    private ActionSummary _as;

    /**
     * Constructor.
     *
     * @param as
     */
    public ActionSummaryModelData(final ActionSummary as) {
        _as = as;
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked") @Deprecated
    public <X> X get(final String property) {

        final Property p = Property.valueOf(property);

        switch (p) {

            case ACTOR:
                return (X) _as.getActor();

            case EXECUTE_AFTER:
                return (X) _as.getExecuteAfter();

            case ID:
                return (X) _as.getId();

            case PATH:
                return (X) _as.getSubjectPath();

            case STATUS:
                return (X) _as.getStatus();

            case SUBJECT_TYPE:
                return (X) _as.getSubjectType();

            case TYPE:
                return (X) _as.getType();

            default:
                throw new UnsupportedOperationException(
                    "Key not supported: "+property);
        }
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<String, Object>();
        for (final Property p : Property.values()) {
            properties.put(p.name(), get(p.name()));
        }
        return properties;
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public Collection<String> getPropertyNames() {
        final Set<String> names = new HashSet<String>();
        for (final Property p : Property.values()) {
            names.add(p.name());
        }
        return names;
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public <X> X remove(final String property) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public <X> X set(final String property, final X value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    public enum Property {
        ID, TYPE, ACTOR, EXECUTE_AFTER, PATH, SUBJECT_TYPE, STATUS;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public ActionStatus getStatus() {
        return _as.getStatus();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public ID getId() {
        return _as.getId();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     */
    public void setStatus(final ActionStatus status) {
        _as.setStatus(status);
    }
}
