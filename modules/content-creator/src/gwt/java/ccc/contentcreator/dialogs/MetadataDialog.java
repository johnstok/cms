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
package ccc.contentcreator.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ccc.contentcreator.binding.DataBinding;

import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class MetadataDialog
    extends
        TableDataDisplayDialog<Map.Entry<String, String>> {

    /**
     * Constructor.
     *
     * @param title
     * @param data
     */
    public MetadataDialog(final String title,
                         final Collection<Map.Entry<String, String>> data) {
        super(title, data);
        _dataStore.add(DataBinding.bindMetadata(_data));
    }


    /** {@inheritDoc} */
    @Override
    protected TableColumnModel defineColumnModel() {

        final List<TableColumn> columns = new ArrayList<TableColumn>();

        columns.add(new TableColumn("key", "Key", PERCENT_50));
        columns.add(new TableColumn("value", "Value", PERCENT_50));

        return new TableColumnModel(columns);
    }
}
