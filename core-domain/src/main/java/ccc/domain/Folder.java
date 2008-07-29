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

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.List;

import ccc.commons.jee.DBC;
import ccc.commons.jee.JSON;


/**
 * A folder that can contain other resources.
 *
 * @author Civic Computing Ltd
 */
public final class Folder extends Resource implements JSONable {

    private List<Resource> entries = new ArrayList<Resource>();

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    private Folder() { super(); }

    /**
     * Constructor.
     *
     * @param name The unique name for this resource.
     */
    public Folder(final ResourceName name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType type() {
        return ResourceType.FOLDER;
    }

    /**
     * Accessor for size.
     *
     * @return The number of resources contained by this folder.
     */
    public int size() {
        return entries.size();
    }

    /**
     * Add a resource to this folder.
     *
     * @param resource The resource to add.
     */
    public void add(final Resource resource) {
        DBC.require().notNull(resource);
        entries.add(resource);
    }

    /**
     * Accessor for entries.
     *
     * @return A list of all the resources in this folder.
     */
    public List<Resource> entries() {
        return unmodifiableList(entries);
    }

    /**
     * TODO: Add a description of this method.
     * TODO: Work out why we need a cast here...
     *
     * @param <T> The type of the resource at the specified path.
     * @param path The path to a resource, relative to this folder.
     * @return The resource at the specified path.
     */
    public <T extends Resource> T navigateTo(final ResourcePath path) {

        Resource currentPosition = this;

        for (final ResourceName name : path.elements()) {

            if (ResourceType.FOLDER != currentPosition.type()) {
                throw new CCCException(
                    currentPosition.name()
                        +" in path "+path
                        +" is not a folder.");
            }

            currentPosition = currentPosition.asFolder().findEntryByName(name);

        }
        return (T)currentPosition;
    }

    /**
     * Find the entry in this folder with the specified name.
     * Throws a CCCException if no resource exists with the specified name.
     *
     * @param name The name of the resource.
     * @return The resource with the specified name.
     */
    public Resource findEntryByName(final ResourceName name) {
        for (final Resource entry : entries) {
            if (entry.name().equals(name)) {
                return entry;
            }
        }
        throw new CCCException("No entry '"+name+"' in folder '"+name()+"'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toJSON() {
        return
            JSON.object()
                .add("name", name().toString())
                .add("entries", entryReferences())
                .toString();
    }

    /**
     * Retrieve a list of references, one for each entry in this folder.
     *
     * @return
     */
    public List<ResourceRef> entryReferences() {

        final List<ResourceRef> resourceRefs = new ArrayList<ResourceRef>();
        for (final Resource entry : entries) {
            resourceRefs.add(new ResourceRef(entry.name(), entry.id(), entry.type()));
        }
        return resourceRefs;
    }

}
