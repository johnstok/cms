/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */

package ccc.remoting.gwt;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import ccc.services.ResourceManager;
import ccc.view.contentcreator.client.ResourceService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * Implementation of {@link ResourceService}.
 * 
 * @author Civic Computing Ltd
 */
public class ResourceServiceImpl extends RemoteServiceServlet implements ResourceService {

   /** serialVersionUID : long */
   private static final long serialVersionUID = 4907235349044174242L;

   /**
    * @see ccc.view.contentcreator.client.ResourceService#save()
    */
   public String save() {
      try {
         ((ResourceManager)new InitialContext().lookup("ResourceManagerEJB/local")).create();
      } catch (NamingException e) {
         // TODO Auto-generated catch block
         throw new RuntimeException(e);
      }
      return "Saved";
   }
}