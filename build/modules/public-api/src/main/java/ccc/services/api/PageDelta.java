/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.api;

import java.util.ArrayList;
import java.util.List;



/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PageDelta
    extends
        ResourceDelta {
    public List<ParagraphDelta> _paragraphs = new ArrayList<ParagraphDelta>();
    public TemplateDelta _computedTemplate;
}
