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
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * A helper action that executes the specified actions in serial.
 *
 * @author Civic Computing Ltd.
 */
public class SerialAction
    implements
        ServletAction {

    private final List<ServletAction> _actions;


    /**
     * Constructor.
     *
     * @param actions The actions to perform.
     */
    public SerialAction(final ServletAction... actions) {
        _actions = Arrays.asList(actions);
    }



    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest req,
                        final HttpServletResponse resp)
                                        throws ServletException, IOException {
        for (final ServletAction a : _actions) {
            a.execute(req, resp);
        }
    }

}
