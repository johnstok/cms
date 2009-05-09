/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.api;

import java.io.Serializable;


/**
 * Represents an update to a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceDelta
    implements
        Serializable {
    private ID     _id;
    private String _title;
    private ID _templateId;
    private String _tags;
    private boolean _published;

    protected ResourceDelta() { super(); }

    /**
     * Constructor.
     *
     * @param id
     * @param name
     * @param title
     * @param templateId
     * @param tags
     * @param published
     */
    public ResourceDelta(final ID     id,
                         final String title,
                         final ID     templateId,
                         final String tags,
                         final boolean published) {
        _id = id;
        _title = title;
        _templateId = templateId;
        _tags = tags;
        _published = published;
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public ID getId() {
        return _id;
    }


    /**
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setId(final ID id) {
        _id = id;
    }


    /**
     * Accessor.
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return _title;
    }


    /**
     * Mutator.
     *
     * @param title The title to set.
     */
    public void setTitle(final String title) {
        _title = title;
    }


    /**
     * Accessor.
     *
     * @return Returns the templateId.
     */
    public ID getTemplateId() {
        return _templateId;
    }


    /**
     * Mutator.
     *
     * @param templateId The templateId to set.
     */
    public void setTemplateId(final ID templateId) {
        _templateId = templateId;
    }


    /**
     * Accessor.
     *
     * @return Returns the tags.
     */
    public String getTags() {
        return _tags;
    }


    /**
     * Mutator.
     *
     * @param tags The tags to set.
     */
    public void setTags(final String tags) {
        _tags = tags;
    }


    /**
     * Accessor.
     *
     * @return Returns the published.
     */
    public boolean isPublished() {
        return _published;
    }


    /**
     * Mutator.
     *
     * @param published The published to set.
     */
    public void setPublished(final boolean published) {
        _published = published;
    }
}
