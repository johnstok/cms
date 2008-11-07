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
package ccc.contentcreator.client;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.dto.PageDTO;
import ccc.contentcreator.dto.ParagraphDTO;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class EditPagePanel extends FormPanel {
    private ContentPanel _upperPanel;
    private final TextField<String> _title = new TextField<String>();
    private final TextField<String> _name = new TextField<String>();
    private DefinitionPanel _dp = new DefinitionPanel();

    /** _constants : UIConstants. */
    private final UIConstants _constants = Globals.uiConstants();

    /**
     * Constructor.
     *
     */
    public EditPagePanel() {
        super();
        drawGUI();
    }

    private void drawGUI() {
        _upperPanel = new ContentPanel();
        _upperPanel.setWidth("100%");
        _upperPanel.setBorders(false);
        _upperPanel.setBodyBorder(false);
        _upperPanel.setHeaderVisible(false);
        _upperPanel.setLayout(new FormLayout());

        _name.setFieldLabel(_constants.name());
        _name.setAllowBlank(false);
        _name.setId(_constants.name());
        _upperPanel.add(_name, new FormData("90%"));

        _title.setFieldLabel(_constants.title());
        _title.setAllowBlank(false);
        _title.setId(_constants.title());
        _upperPanel.add(_title, new FormData("90%"));

        add(_upperPanel);
        add(_dp);
        setLayout(new RowLayout());
        setBorders(false);
        setBodyBorder(false);
        setHeaderVisible(false);
    }

    /**
     * Create fields for the page creation/editing.
     *
     * @param definition Definition XML.
     */
    public void createFields(final String definition) {
        _dp = new DefinitionPanel();
        _dp.renderFields(definition);
        removeAll(); // in order to avoid zombie field labels
        add(_upperPanel);
        add(_dp);
    }

    /**
     * Populates fields for editing.
     *
     * @param page PageDTO of the original page.
     */
    public void populateFields(final PageDTO page) {
        _name.setValue(page.getName());
        _name.setReadOnly(true);
        _title.setValue(page.getTitle());

        for (final Entry<String,ParagraphDTO> para : page.getParagraphs().entrySet()) {

            for (final Component c : _dp.getItems()) {
                if (c.getId().equals(para.getKey())) {
                    if ("TEXT".equals(c.getData("type"))) {
                        final Field<String> f = (Field<String>) c;
                        f.setValue(para.getValue().getValue());
                    } else if ("DATE".equals(c.getData("type"))) {
                        final DateField f = (DateField) c;
                        f.setValue(new Date(new Long(para.getValue().getValue())));
                    }
                }
            }
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return TextField for name.
     */
    public TextField<String> name() {
        return _name;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return TextField for title.
     */
    public TextField<String> title() {
        return _title;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return List of components in definition panel.
     */
    public List<Component> definitionItems() {
        return _dp.getItems();
    }

}
