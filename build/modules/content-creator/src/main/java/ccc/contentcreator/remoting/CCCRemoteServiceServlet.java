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
package ccc.contentcreator.remoting;

import ccc.commons.CCCProperties;
import ccc.services.ServiceLookup;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CCCRemoteServiceServlet
    extends
        RemoteServiceServlet {

    protected ServiceLookup _services =
        new ServiceLookup(CCCProperties.get("application.name"));

//    /** {@inheritDoc} */
//    @Override
//    protected SerializationPolicy doGetSerializationPolicy(final HttpServletRequest arg0,
//                                                           final String arg1,
//                                                           final String arg2) {
//        String context = arg0.getContextPath();
//        if (context.startsWith("/")) {
//            context = context.substring(1);
//        }
//        return super.doGetSerializationPolicy(arg0, arg1+context, arg2);
//    }
}
