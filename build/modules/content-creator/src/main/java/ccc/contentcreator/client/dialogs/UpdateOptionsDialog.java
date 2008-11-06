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

package ccc.contentcreator.client.dialogs;

import java.util.List;

import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.OptionDTO;
import ccc.contentcreator.dto.TemplateDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FormData;


/**
 * TODO Add Description for this type.
 * TODO: Move null handling into option class.
 *
 * @author Civic Computing Ltd
 */
public class UpdateOptionsDialog extends AbstractEditDialog {

    private final ResourceServiceAsync _resourceService =
        Globals.resourceService();
    private final ComboBox<TemplateDTO> _defaultTemplate =
        new ComboBox<TemplateDTO>();
    private final List<OptionDTO<? extends DTO>> _options;
    private final TemplateDTO _none =
        new TemplateDTO(null,
                        -1,
                        "[none]",
                        "[none]",
                        "[none]",
                        "[none]",
                        "<fields/>",
                        "");

    /**
     * Constructor.
     *
     * @param options
     */
    public UpdateOptionsDialog(final List<OptionDTO<? extends DTO>> options) {
        super(Globals.uiConstants().options());

        _options = options;

        _defaultTemplate.setFieldLabel(_constants.defaultTemplate());
        _defaultTemplate.setId(_constants.defaultTemplate());
        _defaultTemplate.setDisplayField("name");
        _defaultTemplate.setForceSelection(true);
        _panel.add(_defaultTemplate, new FormData("100%"));

        _panel.setId("UserPanel");
        _save.setId("userSave");
        _cancel.setId("userCancel");

        drawGUI();
    }

    private void drawGUI() {

        // Populate combo-box
        final ListStore<TemplateDTO> store = new ListStore<TemplateDTO>();
        for (final TemplateDTO template
            : _options.get(0).<TemplateDTO> makeTypeSafe().getChoices()) {
            store.add(template);
        }
        store.add(_none);
        _defaultTemplate.setStore(store);


        // Set the current value
        final TemplateDTO currentValue =
            _options.get(0).<TemplateDTO>makeTypeSafe().getCurrentValue();

        if (null == currentValue) {
            _defaultTemplate.setValue(_none);
        } else {
            _defaultTemplate.setValue(currentValue);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override public void componentSelected(final ButtonEvent ce) {

                final TemplateDTO selected = _defaultTemplate.getValue();
                // TODO: NULL should cause error.
                if (_none.equals(selected)) {
                    _options
                        .get(0)
                        .<TemplateDTO>makeTypeSafe()
                        .setCurrentValue(null);
                } else {
                    _options
                        .get(0)
                        .<TemplateDTO>makeTypeSafe()
                        .setCurrentValue(selected);
                }

                _resourceService
                    .updateOptions(_options,
                                   new DisposingCallback(
                                       UpdateOptionsDialog.this
                                   )
                    );
            }
        };
    }
}
