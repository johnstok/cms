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
package ccc.plugins;

import static ccc.commons.Reflection.*;

import java.io.InputStream;

import ccc.plugins.multipart.MultipartFormData;
import ccc.plugins.scripting.TextProcessor;
import ccc.plugins.search.Index;
import ccc.plugins.search.Indexer;
import ccc.plugins.security.Sessions;


/**
 * Factory for plugins.
 *
 * @author Civic Computing Ltd.
 */
public class PluginFactory {


    public TextProcessor createTemplating() {
        return
            construct(
                TextProcessor.class,
                "ccc.plugins.scripting.velocity.VelocityProcessor");
    }


    public TextProcessor createScripting() {
        return
            construct(
                TextProcessor.class,
                "ccc.plugins.scripting.rhino.ScriptRunner");
    }


    public Index createIndex(final Object... theArguments) {
        return
            construct(
                Index.class,
                "ccc.plugins.search.lucene.SimpleLuceneFS",
                theArguments);
    }


    public Indexer createIndexer(final Object... theArguments) {
        return
            construct(
                Indexer.class,
                "ccc.plugins.search.lucene.SimpleLuceneFS",
                theArguments);
    }


    public Sessions createSessions() {
        return
            construct(
                Sessions.class,
                "ccc.plugins.security.jboss.JbossSession");
    }


    public MultipartFormData createFormData(final String charEncoding,
                                            final int contentLength,
                                            final String contentType,
                                            final InputStream inputStream) {

        final Class<?>[] types = new Class<?>[] {
            String.class,
            int.class,
            String.class,
            InputStream.class
        };

        final Object[] values = new Object[] {
            charEncoding,
            Integer.valueOf(contentLength),
            contentType,
            inputStream
        };

        return
            construct(
                MultipartFormData.class,
                "ccc.plugins.multipart.apache.MultipartForm",
                types,
                values);
    }
}
