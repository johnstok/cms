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

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;


/**
 * A folder that can contain other resources.
 *
 * @author Civic Computing Ltd
 */
public final class Folder extends Resource {

    /**
     * Constructor.
     *
     * @param name
     */
    public Folder(final ResourceName name) {
        super(name);
    }

    private final List<Resource> entries = new ArrayList<Resource>();

    /**
     * @see ccc.domain.Resource#type()
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
     * @param path
     * @return
     */
    public <T extends Resource> T navigateTo(final ResourcePath path) {

        Resource currentPosition = this;

        for (ResourceName name : path.elements()) {

            if (ResourceType.FOLDER != currentPosition.type()) {
                throw new CCCException(
                    currentPosition.name()+" in path "+path+" is not a folder.");
            }

            currentPosition = currentPosition.asFolder().findEntryByName(name);

        }
        return (T)currentPosition;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param name
     * @return
     */
    public Resource findEntryByName(final ResourceName name) {
        for (Resource entry : entries) {
            if (entry.name().equals(name)) {
                return entry;
            }
        }
        throw new CCCException("No entry '"+name+"' in folder '"+name()+"'");
    }

}
