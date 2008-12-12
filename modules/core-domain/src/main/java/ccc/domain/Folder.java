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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ccc.commons.DBC;


/**
 * A folder that can contain other resources.
 *
 * @author Civic Computing Ltd
 */
public final class Folder extends Resource {

    private final List<Resource> _entries = new ArrayList<Resource>();

    /** Constructor: for persistence only. */
    protected Folder() { super(); }

    /**
     * Constructor.
     *
     * @param title The title for this resource.
     */
    public Folder(final String title) {
        super(title);
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
        return _entries.size();
    }

    /**
     * Add a resource to this folder.
     *
     * @param resource The resource to add.
     */
    public void add(final Resource resource) {
        DBC.require().notNull(resource);
        if (hasEntryWithName(resource.name())) {
            throw new CCCException(
                "Folder already contains a resource with name '"
                + resource.name()
                + "'.");
        }
        _entries.add(resource);
        resource.parent(this);
    }

    /**
     * Accessor for entries.
     *
     * @return A list of all the resources in this folder.
     */
    public List<Resource> entries() {
        return unmodifiableList(_entries);
    }

    /**
     * Navigate from this folder to another resource described by the
     * specified path.
     *
     * @param path The path to a resource, relative to this folder.
     * @return The resource at the specified path.
     */
    public Resource navigateTo(final ResourcePath path) {

        Resource currentPosition = this;

        for (final ResourceName name : path.elements()) {

            if (ResourceType.FOLDER != currentPosition.type()) {
                throw new CCCException(
                    currentPosition.name()
                        +" in path "+path
                        +" is not a folder.");
            }

            currentPosition =
                currentPosition.as(Folder.class).findEntryByName(name);

        }
        return currentPosition;
    }

    /**
     * Find the entry in this folder with the specified name.
     * Throws a CCCException if no resource exists with the specified name.
     *
     * @param name The name of the resource.
     * @return The resource with the specified name.
     */
    public Resource findEntryByName(final ResourceName name) {
        for (final Resource entry : _entries) {
            if (entry.name().equals(name)) {
                return entry;
            }
        }
        throw new CCCException("No entry '"+name+"' in folder '"+name()+"'");
    }

    /**
     * Query method - returns the number of folders that are children of this
     * folder.
     *
     * @return The number of folders that are children of this folder, as an
     *      integer.
     */
    public int folderCount() {
        int count = 0;
        for (final Resource entry : _entries) {
            if (entry.type()==ResourceType.FOLDER) { count++; }
        }
        return count;
    }

    /**
     * Pretty print this folder to the specified print writer.
     *
     * @param pw The writer to print to.
     */
    public void prettyPrint(final PrintWriter pw) {
        final int indent = 0;
        pw.println(name());
        prettyPrint(entries(), pw, indent + 2);
    }

    private void prettyPrint(final List<Resource> entries,
                             final PrintWriter pw,
                             final int indent) {

        for (final Resource entry : entries) {
            for (int i = 0; i < indent; i++) { // TODO: Move to commons.
                pw.print(" ");
            }
            pw.println(entry.name());
            if (entry.type() == ResourceType.FOLDER) {
                prettyPrint(entry.as(Folder.class).entries(), pw, indent + 2);
            }
        }
    }

    /**
     * Query whether this folder has an entry with the specified name.
     *
     * @param resourceName The name of the resource.
     * @return True if an entry exists, false otherwise.
     */
    public boolean hasEntryWithName(final ResourceName resourceName) {
        for (final Resource entry : entries()) {
            if (entry.name().equals(resourceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieve the entries in this folder in a type-safe list.
     *
     * @param resourceType A class representing the type of resources in this
     *      folder.
     * @param <T> The type of the resources in this folder.
     * @return The entries in this folder as a type-safe list.
     */
    public <T extends Resource> List<T> entries(final Class<T> resourceType) {
        final List<T> entries = new ArrayList<T>();
        for (final Resource entry : _entries) {
            entries.add(entry.as(resourceType));
        }
        return entries;
    }

    /**
     * Query method to determine if this folder contains any pages.
     *
     * @return true if the folder contains any pages, false otherwise.
     */
    public boolean hasPages() {
        for (final Resource r : _entries) {
            if (r.type().equals(ResourceType.PAGE)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Accessor method for the first page in this folder.
     *
     * @return The first page in the list of entries.
     */
    public Page firstPage() {
        for (final Resource r : _entries) {
            if (r.type().equals(ResourceType.PAGE)) {
                return r.as(Page.class);
            }
        }
        throw new CCCException("No pages in this folder.");
    }

    /**
     * Remove a resource from this folder.
     *
     * @param resource The resource to remove.
     */
    public void remove(final Resource resource) {
        DBC.require().notNull(resource);
        _entries.remove(resource);
        resource.parent(null);
    }

    /**
     * Retrieve a list of all the pages in this folder.
     *
     * @return A list of pages.
     */
    public List<Page> pages() {
        final List<Page> entries = new ArrayList<Page>();
        for (final Resource entry : _entries) {
            if (entry.type()==ResourceType.PAGE) {
                entries.add(entry.as(Page.class));
            }
        }
        return entries;
    }

    /**
     * Retrieve a list of all the folders in this folder.
     *
     * @return A list of folders.
     */
    public List<Folder> folders() {
        final List<Folder> entries = new ArrayList<Folder>();
        for (final Resource entry : _entries) {
            if (entry.type()==ResourceType.FOLDER) {
                entries.add(entry.as(Folder.class));
            }
        }
        return entries;
    }

    /**
     * Query method to determine if this folder contains any aliases.
     *
     * @return true if the folder contains any aliases, false otherwise.
     */
    public boolean hasAliases() {
        for (final Resource r : _entries) {
            if (r.type().equals(ResourceType.ALIAS)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Accessor method for the first alias in this folder.
     *
     * @return The first alias in the list of entries.
     */
    public Alias firstAlias() {
        for (final Resource r : _entries) {
            if (r.type().equals(ResourceType.ALIAS)) {
                return r.as(Alias.class);
            }
        }
        throw new CCCException("No aliases in this folder.");
    }
}
