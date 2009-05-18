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
package ccc.contentcreator.client;

import ccc.contentcreator.binding.FileSummaryModelData;

import com.extjs.gxt.ui.client.widget.form.TriggerField;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ImageTriggerField extends TriggerField<String> {

    private FileSummaryModelData _md;

    /**
     * TODO: Add a description of this method.
     *
     * @param md
     */
    public void setFSModel(FileSummaryModelData md) {
        _md = md;
    }

    public FileSummaryModelData getFSModel() {
        return _md;
    }

}
