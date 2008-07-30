/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.commons.jee.JSON;


/**
 * This class models a reference to a resource.
 *
 * @author Civic Computing Ltd
 */
public class ResourceRef implements JSONable {

    private ResourceName name;
    private UUID         id;
    private ResourceType type;
    private Map<String, String> metadata = new HashMap<String, String>();

    /**
     * Constructor.
     *
     * @param name The name of the resource.
     * @param id The unique ID for the resource.
     * @param type The type of the resource.
     */
    public ResourceRef(final ResourceName name,
                       final UUID id,
                       final ResourceType type) {

        this.name = name;
        this.id = id;
        this.type = type;
    }

    /**
     * Accessor for the name field.
     *
     * @return The name as a {@link ResourceName}.
     */
    public final ResourceName name() {
        return name;
    }

    /**
     * Accessor for the ID field.
     *
     * @return The ID as a {@link UUID}.
     */
    public final UUID id() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toJSON() {

        final ccc.commons.jee.JSON.Object jsonObject = JSON.object();

        jsonObject
            .add("name", name.toString())
            .add("id", id.toString())
            .add("type", type.toString());

        for (final Map.Entry<String, String> metadatum : metadata.entrySet()) {
            jsonObject.add(metadatum.getKey(), metadatum.getValue());
        }

        return jsonObject.toString();
    }

    /**
     * Add an item of metadata.
     *
     * @param key The name of this metadata item.
     * @param value The value for this metadata item.
     */
    public final void addMetadata(final String key, final String value) {
        metadata.put(key, value);
    }

    /**
     * Accessor for metadata.
     *
     * @return This reference's metadata as a hashmap.
     */
    public final Map<String, String> metadata() {
        return Collections.unmodifiableMap(metadata);
    }

}
