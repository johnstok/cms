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

import ccc.plugins.mail.Mailer;
import ccc.plugins.markup.XHTML;
import ccc.plugins.multipart.MultipartFormData;
import ccc.plugins.s11n.Serializers;
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

    /**
     * Create a new XHTML plugin.
     *
     * @return The XHTML plugin.
     */
    public XHTML html() {
        return
            construct(
                XHTML.class,
                "ccc.plugins.markup.tagsoup.TagSoupXHTML");
    }


    /**
     * Create a new serializers plugin.
     *
     * @return The serializers plugin.
     */
    public Serializers serializers() {
        try {
            final Class<?>[] types = new Class<?>[] {
                Class.forName("ccc.plugins.s11n.json.TextParser")
            };

            final Object[] values = new Object[] {
                Class
                    .forName("ccc.plugins.s11n.json.ServerTextParser")
                    .newInstance()
            };

            return
                construct(
                    Serializers.class,
                    "ccc.plugins.s11n.json.SerializerFactory",
                    types,
                    values);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Create a new mailer plugin.
     *
     * @return The mailer plugin.
     */
    public Mailer createMailer() {
        return
            construct(
                Mailer.class,
                "ccc.plugins.mail.javamail.JavaMailMailer");
    }


    /**
     * Create a new templating plugin.
     *
     * @return The templating plugin.
     */
    public TextProcessor createTemplating() {
        return
            construct(
                TextProcessor.class,
                "ccc.plugins.scripting.velocity.VelocityProcessor");
    }


    /**
     * Create a new scripting plugin.
     *
     * @return The scripting plugin.
     */
    public TextProcessor createScripting() {
        return
            construct(
                TextProcessor.class,
                "ccc.plugins.scripting.rhino.ScriptRunner");
    }


    /**
     * Create a new index plugin.
     *
     * @param theArguments Arguments required by the plugin.
     *
     * @return The index plugin.
     */
    public Index createIndex(final Object... theArguments) {
        return
            construct(
                Index.class,
                "ccc.plugins.search.lucene.SimpleLuceneFS",
                theArguments);
    }


    /**
     * Create a new indexer plugin.
     *
     * @param theArguments Arguments required by the plugin.
     *
     * @return The indexer plugin.
     */
    public Indexer createIndexer(final Object... theArguments) {
        return
            construct(
                Indexer.class,
                "ccc.plugins.search.lucene.SimpleLuceneFS",
                theArguments);
    }


    /**
     * Create a new sessions plugin.
     *
     * @return The sessions plugin.
     */
    public Sessions createSessions() {
        return
            construct(
                Sessions.class,
                "ccc.plugins.security.jboss.JbossSession");
    }


    /**
     * Create a new multipart form data plugin.
     *
     * @param charEncoding  The character encoding for the content.
     * @param contentLength The length of the multipart content.
     * @param contentType   The mime-type for the content.
     * @param inputStream   The input stream for reading the content.
     *
     * @return The multipart form data plugin.
     */
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
