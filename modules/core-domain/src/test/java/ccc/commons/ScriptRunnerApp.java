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
package ccc.commons;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;


/**
 * Simple script runner for testing.
 *
 * @author Civic Computing Ltd.
 */
public final class ScriptRunnerApp {

    private ScriptRunnerApp() { super(); }

    /**
     * Application entry point.
     *
     * @param args Arguments to the application.
     */
    public static void main(final String[] args) {

        final Charset utf8 = Charset.forName("UTF-8");

        final String script =
            Resources.readIntoString(
                ScriptRunnerApp.class.getResource("/test.javascript"),
                utf8);
        final ScriptRunner sr = new ScriptRunner();

        sr.eval(
            new Script(script, "test"),
            new Context(),
            new OutputStreamWriter(System.out, utf8));
    }

}
