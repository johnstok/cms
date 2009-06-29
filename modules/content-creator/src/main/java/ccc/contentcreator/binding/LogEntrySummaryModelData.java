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
import java.util.MissingResourceException;
import java.util.Set;

import ccc.api.CommandType;
import ccc.api.ID;
import ccc.api.LogEntrySummary;
import ccc.contentcreator.api.CommandTypeConstants;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.core.client.GWT;


/**
 * {@link ModelData} implementation for the {@link LogEntrySummary} class.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntrySummaryModelData
    implements
        ModelData {
    public static final String EXPAND_PROPERTY = Property.COMMENT.name();

    private final LogEntrySummary _les;

    /**
     * Constructor.
     *
     * @param les The log entry summary to wrap.
     */
    public LogEntrySummaryModelData(final LogEntrySummary les) {
        _les = les;
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked") @Deprecated
    public <X> X get(final String property) {

        final Property p = Property.valueOf(property);

        switch (p) {

            case ACTOR:
                return (X) _les.getActor();

            case ACTION:
                return (X) _les.getAction();

            case LOCALISED_ACTION:
                return (X) getLocalisedAction();

            case COMMENT:
                return (X) "";

            case HAPPENED_ON:
                return (X) _les.getHappenedOn();

            case INDEX:
                return (X) Long.valueOf(_les.getIndex());

            case IS_MAJOR_EDIT:
                return (X) Boolean.FALSE;

            case SUBJECT_ID:
                return (X) _les.getSubject();

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
        SUBJECT_ID, ACTION, LOCALISED_ACTION, ACTOR, HAPPENED_ON, COMMENT, IS_MAJOR_EDIT, INDEX;
    }

    /**
     * Accessor.
     *
     * @return The action.
     */
    public CommandType getAction() {
        return _les.getAction();
    }

    /**
     * Looks up for localised string for the {@link CommandType}.
     *
     * @return The localised string or name of the enum if nothing found.
     */
    public String getLocalisedAction() {
        final CommandTypeConstants types =
            GWT.create(CommandTypeConstants.class);

        String local = null;
        try {
            local = types.getString(_les.getAction().name());
        } catch (final MissingResourceException e) {
            local = _les.getAction().name();
        }
        return local;
    }

    /**
     * Accessor.
     *
     * @return The Id.
     */
    public ID getId() {
        return _les.getSubject();
    }

    /**
     * Accessor.
     *
     * @return The Index.
     */
    public long getIndex() {
        return _les.getIndex();
    }
}
