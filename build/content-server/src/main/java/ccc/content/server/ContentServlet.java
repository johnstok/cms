/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.content.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.jee.JNDI;
import ccc.domain.Content;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;


/**
 * The ContentServlet class serves CCC content. Typically the servlet will be
 * mounted at the servlet path '/*' in the web.xml config file. Only the HTTP
 * GET method is currently supported.
 *
 * @author Civic Computing Ltd
 */
public class ContentServlet extends HttpServlet {

   /** serialVersionUID : long. */
    private static final long serialVersionUID = -5743085540949007873L;

/**
    * Get the content for the specified relative URI. This method maps the value
    * from {@link HttpServletRequest#getPathInfo()} and maps that to a
    * corresponding resource in CCC.
    *
    * TODO: Describe the mapping algorithm.
    * TODO: Handle bad resource paths
    * TODO: Handle good path, no resource
    * TODO: Character encoding
    * TODO: Mime type setting
    * TODO: Markup escaping?
    * TODO: Handle standard errors -> converting to HTML.
    * TODO: Marshal CCCException to HTML.
    * TODO: How do we handle '/'?
    *
    * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
    */
   @Override
   protected final void doGet(final HttpServletRequest req,
                              final HttpServletResponse resp)
                       throws ServletException,
                              IOException {

      final ResourcePath contentPath = new ResourcePath(req.getPathInfo());
      final Resource resource = resourceManager().lookup(contentPath);

      switch (resource.type()) {
          case CONTENT:
              final PrintWriter pw = resp.getWriter();
              pw.write("Content");
              final Content content = resource.asContent();
              for (final Paragraph paragraph : content.paragraphs().values()) {
                  pw.write(paragraph.body());
              }
              break;
          case FOLDER:
              resp.getWriter().write("Folder");
              break;
          default:
              throw new RuntimeException("Unsupported resource type!");
      }

   }

   /**
    * Accessor for the resource manager.
    *
    * @return A ResourceManager.
    */
   protected ResourceManager resourceManager() {
      return JNDI.get("ResourceManagerEJB/local");
   }

}
