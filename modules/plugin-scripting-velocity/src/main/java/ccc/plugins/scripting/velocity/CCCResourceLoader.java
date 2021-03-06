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
package ccc.plugins.scripting.velocity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.log4j.Logger;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import ccc.api.synchronous.Resources;


/**
 * A Velocity resource loader that delegates to a {@link StatefulReader}.
 *
 * @author Civic Computing Ltd.
 */
public class CCCResourceLoader
    extends
        ResourceLoader {
    private static final Logger LOG =
        Logger.getLogger(VelocityProcessor.TEMPLATE_LOG_CATEGORY);

    /** {@inheritDoc} */
    @Override
    public long getLastModified(final Resource arg0) {
        return new Date().getTime();
    }


    /** {@inheritDoc} */
    @Override
    public InputStream getResourceStream(final String absolutePath) {
        final Resources resources =
            (Resources) rsvc.getApplicationAttribute("ccc-reader");

        if (null==resources) {
//            LOG.error("Resource reader is missing!");
            return new ByteArrayInputStream(new byte[0]);
        }

        final String contents =
            resources.fileContentsFromPath(absolutePath, "UTF8");

        if (null==contents) {
            LOG.warn("Template refers to missing resource: "+absolutePath);
            return new ByteArrayInputStream(new byte[0]);
        }

        try {
            return new ByteArrayInputStream(contents.getBytes("UTF8"));
        } catch (final UnsupportedEncodingException e) {
            LOG.warn("Failed to read resource as UTF-8: "+absolutePath);
            return new ByteArrayInputStream(new byte[0]);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void init(final ExtendedProperties arg0) {
        /* NO-OP */
    }


    /** {@inheritDoc} */
    @Override
    public boolean isSourceModified(final Resource arg0) {
        return true;
    }
}
