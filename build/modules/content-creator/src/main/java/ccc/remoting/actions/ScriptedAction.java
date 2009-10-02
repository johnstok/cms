/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.remoting.actions;

import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.domain.File;
import ccc.domain.Resource;
import ccc.persistence.FileRepository;
import ccc.remoting.RequestScopeServiceLocator;
import ccc.rendering.ReadContentToStringAction;


/**
 * A servlet action that invokes a provided script.
 *
 * @author Civic Computing Ltd.
 */
public class ScriptedAction
    extends
        AbstractServletAction {

    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest req,
                        final HttpServletResponse resp) {

        // FIXME: Don't cache executing resources.
        // r.setExpiry(null);

        final FileRepository data = getDataManager(req);
        final Resource rs = getResource(req);

        if (rs instanceof File) {
            final File f = (File) rs;

            if (f.isText() /*&& f.isExecutable()*/) {
                final String fileContent =
                    ReadContentToStringAction.read(data, f);

                invokeScript(req, resp, fileContent);
            } // } else { throw new UnsupportedHttpMethod(); }
        } // } else { throw new UnsupportedHttpMethod(); }
    }


    private void invokeScript(final HttpServletRequest req,
                              final HttpServletResponse resp,
                              final String script) {
        try {
            final ScriptEngineManager factory = new ScriptEngineManager();
            final ScriptEngine engine = factory.getEngineByName("JavaScript");
            engine.getContext().setWriter(resp.getWriter());

            engine.put("request",  req);
            engine.put("response", resp);
            engine.put("services", new RequestScopeServiceLocator(req));
            engine.put("user", getCurrentUser(req));
            // What else to add?

            engine.eval(script);

        } catch (final ScriptException e) {
            throw new RuntimeException("Error invoking script.", e);
        } catch (final IOException e) {
            throw new RuntimeException("Error invoking script.", e);
        }
    }
}
