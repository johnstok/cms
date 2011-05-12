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

import java.lang.reflect.Method;

import org.jboss.resteasy.client.core.extractors.DefaultEntityExtractorFactory;
import org.jboss.resteasy.client.core.extractors.EntityExtractor;


/**
 * Entity extractor factory subclass used to fix a RestEasy bug.
 * <br>See: https://jira.jboss.org/browse/RESTEASY-435 for details.
 *
 * @author Civic Computing Ltd.
 */
final class EnhancedEntityExtractorFactory
    extends
        DefaultEntityExtractorFactory {

    /**
     * Create an entity extractor for methods returning VOID.
     *
     * @return An entity extractor.
     */
    public EntityExtractor<?> createVoidExtractor2() {
        return new VoidEntityExtractor();
    }


    /** {@inheritDoc} */
    @Override
    public EntityExtractor<?> createExtractor(final Method method) {
        final Class<?> returnType = method.getReturnType();
        if (isVoidReturnType(returnType)) {
            return createVoidExtractor2();
        }
        return super.createExtractor(method);
    }
}
