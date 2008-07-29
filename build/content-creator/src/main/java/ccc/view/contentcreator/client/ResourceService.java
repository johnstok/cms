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
package ccc.view.contentcreator.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Synchronous service for managing resources.
 *
 * @author Civic Computing Ltd
 */
@RemoteServiceRelativePath("resource")
public  interface ResourceService extends RemoteService {
  public String getContentRoot();
  public String getResource(String absolutePath);
}