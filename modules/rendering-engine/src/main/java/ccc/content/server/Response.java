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
 * TODO: Add Description for this type.
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
     * TODO: Add a description of this method.
     *
     * @param string
     */
    public void setDescription(final String string) {
        _description = string;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getDescription() {
        return _description;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param l
     */
    public void setLength(final Long l) {
        _length = l;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Long getLength() {
        return _length;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     */
    public void setCharSet(final String string) {
        _charset = string;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getCharSet() {
        return _charset;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     * @param string2
     */
    public void setMimeType(final String string, final String string2) {
        _mimeType = string+"/"+string2;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getMimeType() {
        return _mimeType;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param valueOf
     */
    public void setExpiry(final Long valueOf) {
        _expiry = valueOf;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Long getExpiry() {
        return _expiry;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     */
    public void setDisposition(final String string) {
        _disposition = string;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getDisposition() {
        return _disposition;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param body
     */
    public void setBody(final Body body) {
        DBC.require().notNull(body);
        _body = body;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Body getBody() {
        return _body;
    }

}
