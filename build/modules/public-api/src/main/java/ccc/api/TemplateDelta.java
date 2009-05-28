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
package ccc.api;

import java.io.Serializable;


/**
 * A delta, for updating templates.
 *
 * @author Civic Computing Ltd.
 */
public final class TemplateDelta implements Serializable, Jsonable {
    private String _title;
    private String _description;
    private String _body;
    private String _definition;
    private MimeType _mimeType;

    @SuppressWarnings("unused") private TemplateDelta() { super(); }

    /**
     * Constructor.
     *
     * @param title The template's title.
     * @param description The template's description.
     * @param body The template's body.
     * @param definition The template's definition.
     * @param mimeType The template's mime type.
     */
    public TemplateDelta(final String title,
                         final String description,
                         final String body,
                         final String definition,
                         final MimeType mimeType) {
        _title = title;
        _description = description;
        _body = body;
        _definition = definition;
        _mimeType = mimeType;
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
     * @return Returns the description.
     */
    public String getDescription() {
        return _description;
    }


    /**
     * Mutator.
     *
     * @param description The description to set.
     */
    public void setDescription(final String description) {
        _description = description;
    }


    /**
     * Accessor.
     *
     * @return Returns the body.
     */
    public String getBody() {
        return _body;
    }


    /**
     * Mutator.
     *
     * @param body The body to set.
     */
    public void setBody(final String body) {
        _body = body;
    }


    /**
     * Accessor.
     *
     * @return Returns the definition.
     */
    public String getDefinition() {
        return _definition;
    }


    /**
     * Mutator.
     *
     * @param definition The definition to set.
     */
    public void setDefinition(final String definition) {
        _definition = definition;
    }


    /**
     * Accessor.
     *
     * @return Returns the mimeType.
     */
    public MimeType getMimeType() {
        return _mimeType;
    }


    /**
     * Mutator.
     *
     * @param mimeType The mimeType to set.
     */
    public void setMimeType(final MimeType mimeType) {
        _mimeType = mimeType;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) { // TODO: Use JsonKeys
        json.set("title",       getTitle());
        json.set("description", getDescription());
        json.set("definition",  getDefinition());
        json.set("body",        getBody());
        json.set("mime-type",   getMimeType());
    }
}
