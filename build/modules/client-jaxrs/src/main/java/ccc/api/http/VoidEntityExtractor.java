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

import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.client.core.extractors.ClientRequestContext;
import org.jboss.resteasy.client.core.extractors.EntityExtractor;


/**
 * Entity extractor for methods returning VOID.
 *
 * @author Civic Computing Ltd.
 */
final class VoidEntityExtractor
    implements
        EntityExtractor<Void> {


    /** {@inheritDoc} */
    @Override
    public Void extractEntity(final ClientRequestContext context,
                              final Object... args) {

        final BaseClientResponse<?> response =
            context.getClientResponse();
        try {
            response.checkFailureStatus();
        } catch (final RuntimeException e) {
            context
                .getErrorHandler()
                .clientErrorHandling(response, e);
        }
        return null;
    }
}
