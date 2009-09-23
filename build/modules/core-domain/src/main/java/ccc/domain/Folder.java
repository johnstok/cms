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

import java.util.ArrayList;
import java.util.List;

import ccc.domain.sorting.Sorter;
import ccc.entities.IFolder;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.snapshots.FolderSnapshot;
import ccc.snapshots.PageSnapshot;
import ccc.types.DBC;
import ccc.types.ResourceName;
import ccc.types.ResourceOrder;
import ccc.types.ResourcePath;
import ccc.types.ResourceType;


/**
 * A folder that can contain other resources.
 *
 * @author Civic Computing Ltd
 */
public final class Folder extends Resource implements IFolder {

    private List<Resource> _entries = new ArrayList<Resource>();
    private ResourceOrder  _order = ResourceOrder.MANUAL;
    private Page _indexPage = null;

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
     *
     * @throws ResourceExistsException If a resource already exists in this
     *  folder with the same name.
     * @throws CycleDetectedException If the specified resource is a parent of
     *  this resource.
     */
    public void add(final Resource resource) throws ResourceExistsException,
                                                    CycleDetectedException {
        DBC.require().notNull(resource);
        final Resource existingEntry = entryWithName(resource.name());
        if (null!=existingEntry) {
            throw new ResourceExistsException(this, existingEntry);
        }
        if (resource instanceof Folder) {
            final Folder folder = (Folder) resource;
            if (folder.isAncestorOf(this)) {
                throw new CycleDetectedException();
            }
        }

        _entries.add(resource);
        resource.parent(this);
    }

    /**
     * Determine if this folder is an ancestor of the specified resource.
     * <ul>
     * <li>Returns false if resource is null.
     * <li>Returns true if this equals the resource.
     * <li>Returns true if this equals a parent of the resource.
     * <li>Otherwise return false.
     * </ul>
     *
     * @param resource The potential child.
     * @return Returns true if the resource is a child of this folder.
     */
    public boolean isAncestorOf(final Resource resource) {
        if (null==resource) {
            return false;
        } else if (equals(resource)) {
            return true;
        } else if (isAncestorOf(resource.parent())) {
            return true;
        }
        return false;
    }

    /**
     * Re-order the folder's children.
     *
     * @param resources The children of this folder in a new order.
     */
    public void reorder(final List<Resource> resources) {
        DBC.require().notNull(resources);
        DBC.require().toBeTrue(_entries.size() == resources.size());

        _entries = resources;
    }

    /** {@inheritDoc} */
    @Override
    public List<Resource> entries() {
        final List<Resource> entries = new ArrayList<Resource>(_entries);
        Sorter.sort(entries, _order);
        return entries;
    }

    /** {@inheritDoc} */
    @Override
    public List<Resource> entries(final int count,
                                  final int page,
                                  final String sortOrder) {
        DBC.require().greaterThan(0, count);
        DBC.require().greaterThan(0, page);

        final int from = count * (page-1);
        int to = from + count;
        to = (to>_entries.size()) ? _entries.size() : to;

        final List<Resource> entries = new ArrayList<Resource>(_entries);
        Sorter.sort(entries, ResourceOrder.valueOf(sortOrder));

        return entries.subList(from, to);
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
     * Query whether this folder has an entry with the specified name.
     *
     * @param resourceName The name of the resource.
     * @return True if an entry exists, false otherwise.
     */
    public boolean hasEntryWithName(final ResourceName resourceName) {
        for (final Resource entry : _entries) {
            if (entry.name().equals(resourceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieve an entry from this folder.
     *
     * @param resourceName The name of the resource to retrieve.
     *
     * @return The corresponding resource of NULL if no such resource exists.
     */
    public Resource entryWithName(final ResourceName resourceName) {
        for (final Resource entry : _entries) {
            if (entry.name().equals(resourceName)) {
                return entry;
            }
        }
        return null;
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
        for (final Resource entry : entries()) {
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
     * TODO: Add a description for this method.
     *
     * @return
     */
    public List<Page> pages() {
        final List<Page> entries = new ArrayList<Page>();
        for (final Resource entry : entries()) {
            if (entry.type()==ResourceType.PAGE) {
                entries.add(entry.as(Page.class));
            }
        }
        return entries;
    }

    /** {@inheritDoc} */
    @Override
    public List<PageSnapshot> pagesAsSnapshots() {
        final List<PageSnapshot> entries = new ArrayList<PageSnapshot>();
        for (final Resource entry : entries()) {
            if (entry.type()==ResourceType.PAGE) {
                entries.add(entry.as(Page.class).forCurrentRevision());
            }
        }
        return entries;
    }

    /** {@inheritDoc} */
    @Override
    public List<FolderSnapshot> foldersAsSnapshots() {
        final List<FolderSnapshot> entries = new ArrayList<FolderSnapshot>();
        for (final Resource entry : _entries) {
            if (entry.type()==ResourceType.FOLDER) {
                entries.add(entry.as(Folder.class).forCurrentRevision());
            }
        }
        return entries;
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
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

    /**
     * Accessor for the sort order property.
     *
     * @return The folder sort order.
     */
    public ResourceOrder sortOrder() {
        return _order;
    }

    /**
     * Mutator for the sort order property.
     *
     * @param order The new sort order.
     */
    public void sortOrder(final ResourceOrder order) {
        DBC.require().notNull(order);
        _order = order;
    }

    /** {@inheritDoc} */
    @Override
    public Jsonable createSnapshot() {
        return new Jsonable(){
            /** {@inheritDoc} */
            @Override public void toJson(final Json json) {
                json.set(JsonKeys.TITLE, title());
            }
        };
    }

    /**
     * Accessor for the index page.
     *
     * @return The index page of this folder.
     */
    public Page indexPage() {
        return _indexPage;
    }

    /**
     * Mutator for the index page.
     *
     * @param indexPage The index page to set.
     */
    public void indexPage(final Page indexPage) {
        _indexPage = indexPage;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);
        json.set(JsonKeys.SORT_ORDER, sortOrder().name());
        json.set(
            JsonKeys.INDEX_PAGE_ID,
            (null==indexPage()) ? null : indexPage().id().toString());
        // Index folder entries?
    }




    /* ====================================================================
     * Snapshot support.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public FolderSnapshot forWorkingCopy() {
        return new FolderSnapshot(this);
    }

    /** {@inheritDoc} */
    @Override
    public FolderSnapshot forCurrentRevision() {
        return new FolderSnapshot(this);
    }

    /** {@inheritDoc} */
    @Override
    public FolderSnapshot forSpecificRevision(final int revNo) {
        return new FolderSnapshot(this);
    }
}
