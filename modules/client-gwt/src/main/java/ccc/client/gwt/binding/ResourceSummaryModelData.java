/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.binding;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.Alias;
import ccc.api.core.Folder;
import ccc.api.core.Page;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.types.DBC;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.api.types.Username;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.core.ResponseHandlerAdapter;
import ccc.client.gwt.events.AliasCreated;
import ccc.client.gwt.events.FolderCreated;
import ccc.client.gwt.events.PageCreated;
import ccc.client.gwt.events.ResourceRenamed;
import ccc.client.gwt.events.WorkingCopyApplied;
import ccc.client.gwt.events.WorkingCopyCleared;
import ccc.client.gwt.widgets.ContentCreator;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;


/**
 * {@link ModelData} implementation for the {@link ResourceSummary} class.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSummaryModelData
    implements
        ModelData {
    /** DISPLAY_PROPERTY : String. */
    public static final String DISPLAY_PROPERTY = Property.NAME.name();

    private ResourceSummary _rs;
    final Globals g = new GlobalsImpl();

    /**
     * Constructor.
     *
     * @param rs The resource summary to wrap.
     */
    public ResourceSummaryModelData(final ResourceSummary rs) {
        _rs = rs;
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked") @Deprecated
    public <X> X get(final String property) {

        final Property p = Property.valueOf(property);

        switch (p) {

            case CHILD_COUNT:
                return (X) Integer.valueOf(_rs.getChildCount());

            case DATE_CHANGED:
                return (X) _rs.getDateChanged();

            case DATE_CREATED:
                return (X) _rs.getDateCreated();

            case FOLDER_COUNT:
                return (X) Integer.valueOf(_rs.getFolderCount());

            case UUID:
                return (X) _rs.getId();

            case LOCKED:
                return (X) _rs.getLockedBy();

            case MM_INCLUDE:
                return (X) Boolean.valueOf(_rs.isIncludeInMainMenu());

            case NAME:
                return (X) _rs.getName();

            case PARENT:
                return (X) _rs.getParent();

            case PUBLISHED:
                return (X) _rs.getPublishedBy();

            case SORT_ORDER:
                return (X) _rs.getSortOrder();

            case TITLE:
                return (X) _rs.getTitle();

            case TYPE:
                return (X) _rs.getType();

            case WORKING_COPY:
                return (X) Boolean.valueOf(_rs.isHasWorkingCopy());

            case ABSOLUTE_PATH:
                return (X) _rs.getAbsolutePath();

            case INDEX_PAGE_ID:
                return (X) _rs.getIndexPageId();

            case DESCRIPTION:
                return (X) _rs.getDescription();

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

    /**
     * Property values form model data.
     *
     * @author Civic Computing Ltd.
     */
    public enum Property {
        /** UUID : Property. */
        UUID,
        /** PARENT : Property. */
        PARENT,
        /** NAME : Property. */
        NAME,
        /** PUBLISHED : Property. */
        PUBLISHED,
        /** TITLE : Property. */
        TITLE,
        /** LOCKED : Property. */
        LOCKED,
        /** TYPE : Property. */
        TYPE,
        /** CHILD_COUNT : Property. */
        CHILD_COUNT,
        /** FOLDER_COUNT : Property. */
        FOLDER_COUNT,
        /** MM_INCLUDE : Property. */
        MM_INCLUDE,
        /** SORT_ORDER : Property. */
        SORT_ORDER,
        /** WORKING_COPY : Property. */
        WORKING_COPY,
        /** DATE_CHANGED : Property. */
        DATE_CHANGED,
        /** DATE_CREATED : Property. */
        DATE_CREATED,
        /** ABSOLUTE_PATH : Property. */
        ABSOLUTE_PATH,
        /** INDEX_PAGE_ID : Property. */
        INDEX_PAGE_ID,
        /** DESCRIPTION : Property. */
        DESCRIPTION;
    }

    /**
     * Merge the specified resource summary into this model data.
     *
     * @param rs The new resource summary.
     */
    public void merge(final ResourceSummary rs) {
        _rs = rs;
    }

    /**
     * Accessor.
     *
     * @return The  UUID of the resource.
     */
    public UUID getId() {
        return _rs.getId();
    }

    /**
     * Mutator.
     *
     * @param b The boolean value to set.
     */
    public void setWorkingCopy(final boolean b) {
        _rs.setHasWorkingCopy(b);
    }

    /**
     * Accessor.
     *
     * @return The name.
     */
    public String getName() {
        return _rs.getName();
    }

    /**
     * Accessor.
     *
     * @return The resource type.
     */
    public ResourceType getType() {
        return _rs.getType();
    }

    /**
     * Accessor.
     *
     * @return The folder count.
     */
    public int getFolderCount() {
        return _rs.getFolderCount();
    }

    /**
     * Accessor.
     *
     * @return The child count.
     */
    public int getChildCount() {
        return _rs.getChildCount();
    }

    /**
     * Accessor.
     *
     * @return The user name of the resource locker.
     */
    public Username getLocked() {
        return _rs.getLockedBy();
    }

    /**
     * Mutator.
     *
     * @param order The sort order to set.
     */
    public void setSortOrder(final String order) {
        _rs.setSortOrder(order);
    }

    /**
     * Mutator.
     *
     * @param title The resource title to set.
     */
    public void setTitle(final String title) {
        _rs.setTitle(title);
    }

    /**
     * Accessor.
     *
     * @return The UUID of the parent.
     */
    public UUID getParent() {
        return _rs.getParent();
    }

    /**
     * Mutator.
     *
     * @param value The name to set.
     */
    public void setName(final String value) {
        _rs.setName(value);
    }

    /**
     * Mutator.
     *
     * @param b The boolean value to set.
     */
    public void setIncludeInMainMenu(final boolean b) {
        _rs.setIncludeInMainMenu(b);
    }

    /**
     * Accessor.
     *
     * @return The title of the resource.
     */
    public String getTitle() {
        return _rs.getTitle();
    }

    /**
     * Accessor.
     *
     * @return The sort order of the resource.
     */
    public String getSortOrder() {
        return _rs.getSortOrder();
    }

    /**
     * Accessor.
     *
     * @return Username of the publisher.
     */
    public Username getPublished() {
        return _rs.getPublishedBy();
    }

    /**
     * Mutator.
     *
     * @return true if has a working copy.
     */
    public boolean hasWorkingCopy() {
        return _rs.isHasWorkingCopy();
    }

    /**
     * Accessor.
     *
     * @return true if included in the main menu.
     */
    public boolean isIncludedInMainMenu() {
        return _rs.isIncludeInMainMenu();
    }

    /**
     * Mutator.
     *
     * @param absolutePath The absolute path to set.
     */
    public void setAbsolutePath(final String absolutePath) {
        _rs.setAbsolutePath(absolutePath);
    }

    /**
     * Accessor.
     *
     * @return The absolute path.
     */
    public String getAbsolutePath() {
        return _rs.getAbsolutePath();
    }

    /**
     * Accessor.
     *
     * @return Template UUID.
     */
    public UUID getTemplateId() {
        return _rs.getTemplateId();
    }

    /**
     * Accessor.
     *
     * @return The tags.
     */
    public String getTags() {
        final StringBuilder sb = new StringBuilder();
        for (final String tag : _rs.getTags()) {
            sb.append(tag);
            sb.append(',');
        }

        String tagString = sb.toString();
        if (tagString.endsWith(",")) {
            tagString = tagString.substring(0, tagString.length()-1);
        }

        return tagString;
    }

    /**
     * Mutator.
     *
     * @param user The user to set.
     */
    public void setLocked(final Username user) {
        _rs.setLockedBy(user);
    }

    /**
     * Mutator.
     *
     * @param user The user to set.
     */
    public void setPublished(final Username user) {
        _rs.setPublishedBy(user);
    }

    /**
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setTemplateId(final UUID id) {
        _rs.setTemplateId(id);
    }

    /**
     * Mutator.
     *
     * @param tags The tags to set.
     */
    public void setTags(final String tags) {
        _rs.setTags(parseTagString(tags));
    }

    /**
     * Parse a comma separated string to produce a set of tags.
     *
     * @param tags The string to parse.
     *
     * @return The set of tags.
     */
    public static Set<String> parseTagString(final String tags) {
        DBC.require().notNull(tags);
        DBC.require().containsNoBrackets(tags);

        final String[] tagArray = tags.split(",");
        final Set<String> parsed = new HashSet<String>();
        for(final String tag : tagArray) {
            if (tag.trim().length() < 1) {
                continue;
            }
            parsed.add(tag.trim());
        }
        return parsed;
    }

    /**
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setIndexPageId(final UUID id) {
        _rs.setIndexPageId(id);
    }

    /**
     * Accessor.
     *
     * @return The index page.
     */
    public UUID getIndexPageId() {
        return _rs.getIndexPageId();
    }

    /**
     * Accessor.
     *
     * @return Returns the description.
     */
    public String getDescription() {

        return _rs.getDescription();
    }

    /**
     * Mutator.
     *
     * @param description The description to set.
     */
    public void setDescription(final String description) {
        _rs.setDescription(description);
    }

    /**
     * Retrieve the relative path to a resource's revision data.
     *
     * @return The path as a string.
     */
    public String revisionsPath() {
        return _rs.revisions().build(new GWTTemplateEncoder());
    }

    /**
     * Increase the folder count by 1.
     */
    public void incrementFolderCount() {
        _rs.setFolderCount(_rs.getFolderCount()+1);
    }

    /**
     * Decrease the folder count by 1.
     */
    public void decrementFolderCount() {
        _rs.setFolderCount(_rs.getFolderCount()-1);
    }


    /**
     * Parse the response as a resource summary.
     *
     * @param response The response to parse.
     *
     * @return The resource summary.
     */
    @Deprecated
    // FIXME Remove this method.
    protected static ResourceSummary parseResourceSummary(final Response response) {
        return new ResourceSummary(
            new GwtJson(JSONParser.parse(response.getText()).isObject()));
    }


    /**
     * Create a request to apply the resource's working copy.
     *
     * @return A HTTP request.
     */
    public Request applyWorkingCopy() {
        return new Request(
            RequestBuilder.POST,
            Globals.API_URL + _rs.applyWc().build(new GWTTemplateEncoder()),
            "",
            new WCAppliedCallback(g.uiConstants().applyWorkingCopy(), this));
    }

    /**
     * Create a request to clear the resource's working copy.
     *
     * @return A HTTP request.
     */
    public Request clearWorkingCopy() {
        return new Request(
            RequestBuilder.POST,
            Globals.API_URL + _rs.clearWc().build(new GWTTemplateEncoder()),
            "",
            new WCClearedCallback(g.uiConstants().deleteWorkingCopy(), this));
    }


    /**
     * Create a new alias.
     *
     * @param alias The alias to create.
     *
     * @return The HTTP request to create an alias.
     */
    public static Request createAlias(final Alias alias) {
        final String path = Globals.API_URL+Alias.list();

        final GwtJson json = new GwtJson();
        alias.toJson(json);

        return
            new Request(
                RequestBuilder.POST,
                path,
                json.toString(),
                new AliasCreatedCallback(
                    new GlobalsImpl().uiConstants().createAlias()));
    }


    /**
     * Create a new folder.
     *
     * @param name The new folder's name.
     * @param parentFolder The parent folder.
     *
     * @return The HTTP request to create a folder.
     */
    // FIXME: Should pass a folder here.
    public static Request createFolder(final String name,
                                       final UUID parentFolder) {
        final String path = Globals.API_URL+Folder.list();

        final GwtJson json = new GwtJson();
        final Folder f = new Folder();
        f.setParent(parentFolder);
        f.setName(new ResourceName(name));
        f.toJson(json);

        return
            new Request(
                RequestBuilder.POST,
                path,
                json.toString(),
                new FolderCreatedCallback(
                    new GlobalsImpl().uiConstants().createFolder()));
    }


    /**
     * Create a new page.
     *
     * @param page The page to create.
     *
     * @return The HTTP request to create a folder.
     */
    public static Request createPage(final Page page) {
        final String path =  Globals.API_URL+Page.list();

        final GwtJson json = new GwtJson(); // FIXME: Broken.
        page.toJson(json);

        return
            new Request(
                RequestBuilder.POST,
                path,
                json.toString(),
                new PageCreatedCallback(
                    new GlobalsImpl().uiConstants().createPage()));
    }


    /**
     * Rename a resource.
     *
     * @param name
     * @param id
     * @param newPath
     *
     * @return The HTTP request to rename a resource.
     */
    public Request rename(final String name,
                                 final ResourcePath newPath) {
        return
            new Request(
                RequestBuilder.POST,
                Globals.API_URL + _rs.rename().build(new GWTTemplateEncoder()),
                name,
                new ResourceRenamedCallback(
                    new GlobalsImpl().uiConstants().rename(),
                    name,
                    getId(),
                    newPath));
    }


    /**
     * Callback handler for renaming a resource.
     *
     * @author Civic Computing Ltd.
     */
    public static class ResourceRenamedCallback extends ResponseHandlerAdapter {

        private final ResourceRenamed _event;

        /**
         * Constructor.
         *
         * @param name The action name.
         * @param newPath The resource's new path.
         * @param id The resource's ID.
         * @param rName The resource's new name.
         */
        public ResourceRenamedCallback(final String name,
                                       final String rName,
                                       final UUID id,
                                       final ResourcePath newPath) {
            super(name);
            _event = new ResourceRenamed(rName, newPath.toString(), id);
        }

        /** {@inheritDoc} */
        @Override
        public void onNoContent(final Response response) {
            ContentCreator.EVENT_BUS.fireEvent(_event);
        }
    }


    /**
     * Callback handler for creating a page.
     *
     * @author Civic Computing Ltd.
     */
    public static class PageCreatedCallback extends ResponseHandlerAdapter {

        /**
         * Constructor.
         *
         * @param name The action name.
         */
        public PageCreatedCallback(final String name) {
            super(name);
        }

        /** {@inheritDoc} */
        @Override
        public void onOK(final Response response) {
            final ResourceSummaryModelData rs =
                new ResourceSummaryModelData(parseResourceSummary(response));
            ContentCreator.EVENT_BUS.fireEvent(new PageCreated(rs));
        }
    }


    /**
     * Callback handler for creating a folder.
     *
     * @author Civic Computing Ltd.
     */
    public static class FolderCreatedCallback extends ResponseHandlerAdapter {

        /**
         * Constructor.
         *
         * @param name The action name.
         */
        public FolderCreatedCallback(final String name) {
            super(name);
        }

        /** {@inheritDoc} */
        @Override
        public void onOK(final Response response) {
            final ResourceSummaryModelData rs =
                new ResourceSummaryModelData(parseResourceSummary(response));
            ContentCreator.EVENT_BUS.fireEvent(new FolderCreated(rs));
        }
    }


    /**
     * Callback handler for creating an alias.
     *
     * @author Civic Computing Ltd.
     */
    public static class AliasCreatedCallback extends ResponseHandlerAdapter {

        /**
         * Constructor.
         *
         * @param name The action name.
         */
        public AliasCreatedCallback(final String name) {
            super(name);
        }

        /** {@inheritDoc} */
        @Override
        public void onOK(final Response response) {
            final ResourceSummaryModelData newAlias =
                new ResourceSummaryModelData(parseResourceSummary(response));
            ContentCreator.EVENT_BUS.fireEvent(new AliasCreated(newAlias));
        }
    }


    /**
     * Callback handler for applying a working copy.
     *
     * @author Civic Computing Ltd.
     */
    public static class WCAppliedCallback extends ResponseHandlerAdapter {

        private final WorkingCopyApplied _event;

        /**
         * Constructor.
         *
         * @param name The action name.
         * @param resource The resource whose WC has been applied.
         */
        public WCAppliedCallback(final String name,
                                 final ResourceSummaryModelData resource) {
            super(name);
            _event = new WorkingCopyApplied(resource);
        }

        /** {@inheritDoc} */
        @Override
        public void onNoContent(final Response response) {
            ContentCreator.EVENT_BUS.fireEvent(_event);
        }
    }


    /**
     * Callback handler for clearing a working copy.
     *
     * @author Civic Computing Ltd.
     */
    private static class WCClearedCallback extends ResponseHandlerAdapter {

        private final WorkingCopyCleared _event;

        /**
         * Constructor.
         *
         * @param name The action name.
         * @param resource The resource whose WC has been applied.
         */
        public WCClearedCallback(final String name,
                                 final ResourceSummaryModelData resource) {
            super(name);
            _event = new WorkingCopyCleared(resource);
        }

        /** {@inheritDoc} */
        @Override
        public void onNoContent(final Response response) {
            ContentCreator.EVENT_BUS.fireEvent(_event);
        }
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public ResourceSummary getDelegate() { return _rs; }
}
