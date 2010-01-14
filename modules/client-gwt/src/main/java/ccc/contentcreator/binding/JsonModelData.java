/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.binding;

import java.util.Collection;
import java.util.Map;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * Abstract base class for overlay types that can be used by GXT a Store.
 *
 * @author Civic Computing Ltd.
 */
abstract class JsonModelData
    implements
        ModelData {

    private final JsonObject _delegate;


    /**
     * Constructor.
     */
    public JsonModelData() {
        this("{}");
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of the model.
     */
    public JsonModelData(final String json) {
        this(JsonObject.create(json));
    }


    /**
     * Constructor.
     *
     * @param delegate The JSON delegate to use.
     */
    protected JsonModelData(final JsonObject delegate) {
        _delegate = delegate;
    }


    /** {@inheritDoc} */
    @Override
    public <X> X get(final String property) {
        return (X) _delegate.get(property);
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getProperties() {
        return _delegate.getProperties();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> getPropertyNames() {
        return _delegate.getPropertyNames();
    }


    /** {@inheritDoc} */
    @Override
    public <X> X remove(final String property) {
        return (X) _delegate.remove(property);
    }


    /** {@inheritDoc} */
    @Override
    public <X> X set(final String property, final X value) {
        return _delegate.set(property, value);
    }
}
