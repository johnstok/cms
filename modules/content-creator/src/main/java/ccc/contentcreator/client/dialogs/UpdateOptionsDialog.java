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

import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.OptionDTO;
import ccc.contentcreator.dto.TemplateDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;


/**
 * TODO Add Description for this type.
 * TODO: Move null handling into option class.
 *
 * @author Civic Computing Ltd
 */
public class UpdateOptionsDialog extends AbstractEditDialog {

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
                        "",
                        "");

    /**
     * Constructor.
     *
     * @param options A list of options to display on this panel.
     */
    public UpdateOptionsDialog(final List<OptionDTO<? extends DTO>> options) {
        super(Globals.uiConstants().options());

        _options = options;

        // TODO: Refactor defaults for combo-box - esp' ID setting for Selenium.
        _defaultTemplate.setFieldLabel(constants().defaultTemplate());
        _defaultTemplate.setTemplate(
            "<tpl for=\".\">"
            + "<div class=x-combo-list-item id={name}>{name}</div>"
            + "</tpl>");
        _defaultTemplate.setId("default-template");
        _defaultTemplate.setDisplayField("name");
        _defaultTemplate.setForceSelection(true);
        _defaultTemplate.setAllowBlank(false);
        addField(_defaultTemplate);

        setPanelId("UserPanel");

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

                resourceService()
                    .updateOptions(_options,
                                   new DisposingCallback(
                                       UpdateOptionsDialog.this
                                   )
                    );
            }
        };
    }
}
