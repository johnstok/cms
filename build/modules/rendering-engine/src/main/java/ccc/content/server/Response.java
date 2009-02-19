/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.content.server;

import ccc.commons.DBC;


/**
 * A CCC response.
 *
 * @author Civic Computing Ltd.
 */
public class Response {

    private String  _description;
    private String  _disposition;
    private String  _mimeType;
    private Long    _length;
    private String  _charset;
    private Long    _expiry;
    private Body    _body;

    /**
     * Mutator.
     *
     * @param description The new description.
     */
    public void setDescription(final String description) {
        _description = description;
    }

    /**
     * Accessor.
     *
     * @return The body's description.
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Mutator.
     *
     * @param length The size of the response's body, in bytes.
     */
    public void setLength(final Long length) {
        _length = length;
    }

    /**
     * Accessor.
     *
     * @return The size of the response's body, in bytes.
     */
    public Long getLength() {
        return _length;
    }

    /**
     * Mutator.
     *
     * @param charset The new character set.
     */
    public void setCharSet(final String charset) {
        _charset = charset;
    }

    /**
     * Accessor.
     *
     * @return The character set for this response.
     */
    public String getCharSet() {
        return _charset;
    }

    /**
     * Mutator.
     *
     * @param primary The primary part of the mime type.
     * @param secondary The secondary part of the mime type.
     */
    public void setMimeType(final String primary, final String secondary) {
        _mimeType = primary+"/"+secondary;
    }

    /**
     * Accessor.
     *
     * @return The response's mime type, as a string.
     */
    public String getMimeType() {
        return _mimeType;
    }

    /**
     * Mutator.
     *
     * @param expiry The response's expiry.
     */
    public void setExpiry(final Long expiry) {
        _expiry = expiry;
    }

    /**
     * Accessor.
     *
     * @return The response's expiry.
     */
    public Long getExpiry() {
        return _expiry;
    }

    /**
     * Mutator.
     *
     * @param disposition The new disposition for this response.
     */
    public void setDisposition(final String disposition) {
        _disposition = disposition;
    }

    /**
     * Accessor.
     *
     * @return The response's disposition.
     */
    public String getDisposition() {
        return _disposition;
    }

    /**
     * Mutator.
     *
     * @param body The new body for this response.
     */
    public void setBody(final Body body) {
        DBC.require().notNull(body);
        _body = body;
    }

    /**
     * Accessor.
     *
     * @return The response's body.
     */
    public Body getBody() {
        return _body;
    }

}
