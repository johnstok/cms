/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.api.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.api.types.ResourceName;
import ccc.api.types.URIBuilder;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * A new alias.
 *
 * @author Civic Computing Ltd.
 */
public class Alias
    extends
        Resource {

    private UUID _targetId;
    private String _targetPath;


    /**
     * Constructor.
     *
     * @param parentId The alias' parent.
     * @param name The alias' name.
     * @param targetId The alias' target.
     */
    public Alias(final UUID parentId,
                    final ResourceName name,
                    final UUID targetId) {
        setParent(parentId);
        setName(name);
        _targetId = targetId;
    }


    /**
     * Constructor.
     *
     * @param targetId The alias' target's id.
     */
    public Alias(final UUID targetId) {
        _targetId = targetId;
    }


    /**
     * Constructor.
     */
    public Alias() { super(); }


    /**
     * Accessor.
     *
     * @return Returns the targetId.
     */
    public final UUID getTargetId() {
        return _targetId;
    }


    /**
     * Mutator.
     *
     * @param targetId The targetId to set.
     */
    public void setTargetId(final UUID targetId) {
        _targetId = targetId;
    }


    /**
     * Mutator.
     *
     * @param targetPath The targetPath to set.
     */
    public void setTargetPath(final String targetPath) {
        _targetPath = targetPath;
    }


    /**
     * Accessor.
     *
     * @return Returns the targetPath.
     */
    public String getTargetPath() {
        return _targetPath;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);

        final Map<String, String> links = new HashMap<String, String>();
        links.put("create", ccc.api.core.ResourceIdentifiers.Alias.COLLECTION);
        if (null!=getId()) {
            links.put(
                "update",
                new URIBuilder(ccc.api.core.ResourceIdentifiers.Alias.ELEMENT)
                    .replace("id", getId().toString())
                    .toString());
        }

        json.set(JsonKeys.TARGET_ID, _targetId);
        json.set("links", links);
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        super.fromJson(json);

        setTargetId(json.getId(JsonKeys.TARGET_ID));
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public static String list() {
        return ccc.api.core.ResourceIdentifiers.Alias.COLLECTION;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param id
     * @return
     */
    public static String targetName(final UUID id) {
        return
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Alias.TARGET_NAME)
            .replace("id", id.toString())
            .toString();
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String self() {
        return
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Alias.ELEMENT)
            .replace("id", getId().toString())
            .toString();
    }
}
