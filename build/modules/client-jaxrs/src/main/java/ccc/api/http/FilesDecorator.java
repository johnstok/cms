/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.http;

import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;

import ccc.api.Files;
import ccc.api.dto.FileDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.jaxrs.FilesImpl;
import ccc.api.types.DBC;


/**
 * Decorator implementation of the files API for HTTP specific behaviour.
 *
 * @author Civic Computing Ltd.
 */
public class FilesDecorator
    extends
        FilesImpl {

    private final HttpClient _httpClient;
    private final String _hostUrl;

    /**
     * Constructor.
     *
     * @param files The files implementation to decorate.
     * @param hostUrl The URL for the host's API implementation.
     * @param httpClient The HTTP client used to contact the host.
     */
    public FilesDecorator(final Files files,
                          final String hostUrl,
                          final HttpClient httpClient) {
        super(files);
        _hostUrl    = DBC.require().notEmpty(hostUrl);
        _httpClient = DBC.require().notNull(httpClient);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFile(final FileDto file) {
        final FileUploader fu = new FileUploader(_httpClient, _hostUrl);
        return fu.createFile(file);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary updateFile(final UUID fileId, final FileDto file) {
        final FileUploader fu = new FileUploader(_httpClient, _hostUrl);
        return fu.updateFile(file);
    }
}
