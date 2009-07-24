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

package ccc.api.template;

import java.util.Set;

import ccc.api.Paragraph;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface IPageSnapshot extends IResourceSnapshot {

    Paragraph paragraph(final String name);

    Set<Paragraph> paragraphs();
}