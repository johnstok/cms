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
package ccc.contentcreator.client;

import ccc.contentcreator.api.Application;
import ccc.contentcreator.api.PanelControl;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.dto.AliasDTO;
import ccc.contentcreator.dto.ResourceDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class CreateAliasDialog extends Window implements ApplicationDialog {

    private final TextField<String> _targetName = new TextField<String>();
    private final TextField<String> _aliasName = new TextField<String>();
    private final ResourceDTO _target;
    private final UIConstants _constants;

    /**
     * Constructor.
     *
     * @param _app
     * @param item
     */
    public CreateAliasDialog(final Application _app, final ResourceDTO item) {
        _target = item;
        _constants = _app.constants();
        setHeading(_app.constants().updateContent());
        setWidth(640);
        setHeight(480);
        setLayout(new FitLayout());

        final FormPanel panel = new FormPanel();
        panel.setWidth("100%");
        panel.setBorders(false);
        panel.setBodyBorder(false);
        panel.setHeaderVisible(false);
        add(panel);

        _targetName.setFieldLabel(_constants.target());
        _targetName.setValue(item.getName());
        _targetName.setReadOnly(true);
        panel.add(_targetName, new FormData("100%"));

        _aliasName.setFieldLabel(_constants.name());
        _aliasName.setAllowBlank(false);
        panel.add(_aliasName, new FormData("100%"));


        addButton(
            new Button(
                _constants.cancel(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(final ButtonEvent ce) {
                        hide();
                    }
                }
            ));

        addButton(
            new Button(
                _constants.save(),
                new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {
                    final DisposingCallback callback =
                        new DisposingCallback(_app, CreateAliasDialog.this);
                    _app.lookupService().createAlias(
                        new AliasDTO(
                            null,
                            "ALIAS",
                            _aliasName.getValue(),
                            _aliasName.getValue(),
                            _target.getId()),
                        callback);
                }
                }));
    }

    /**
     * {@inheritDoc}
     */
    public void gui(final PanelControl control) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


}
