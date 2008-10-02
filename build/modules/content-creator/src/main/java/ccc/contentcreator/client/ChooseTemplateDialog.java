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

import java.util.List;

import ccc.contentcreator.api.Application;
import ccc.contentcreator.api.ListControl;
import ccc.contentcreator.api.PanelControl;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.callbacks.DisposingClickListener;
import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.OptionDTO;
import ccc.contentcreator.dto.ResourceDTO;
import ccc.contentcreator.dto.TemplateDTO;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * Dialog for resource template chooser. Mostly copied from
 * UpdateOptionsDialog
 *
 * @author Civic Computing Ltd
 */
public class ChooseTemplateDialog {
    private final ApplicationDialog     _delegate;
    private final Application        _app;
    private final UIConstants     _constants;
    private final PanelControl  _gui;
    private final ResourceServiceAsync _resourceService;

    private final ResourceDTO _resource;

    private final ListControl _templates;
    private final List<OptionDTO<? extends DTO>> _options;

    /**
     * Constructor.
     *
     * @param app The Application
     * @param options The list of OptionDTOs
     * @param resource The ResourceDTO
     */
    public ChooseTemplateDialog(final Application app,
                                final List<OptionDTO<? extends DTO>> options,
                                final ResourceDTO resource) {

        _app = app;
        _constants = _app.constants();
        _delegate = _app.dialog(_constants.chooseTemplate());
        _gui = _app.verticalPanel();
        _resourceService = _app.lookupService();

        _templates = _app.listBox();
        _options = options;
        _resource = resource;

        drawGUI();
        _delegate.gui(_gui);
    }

    private void drawGUI() {

        final FeedbackPanel fPanel = new FeedbackPanel(_app);
        fPanel.setVisible(false);
        _gui.add(fPanel);

        _gui.add(
            new TwoColumnForm(_app, 1)
            .add(_constants.template(), _templates)
        );

        // populate combo box
        _templates.addItem("<none>", "<none>"); // No value.
        for (final TemplateDTO template :
            _options.get(0).<TemplateDTO>makeTypeSafe().getChoices()) {
            _templates.addItem(template.getTitle(), template.getId());
        }

        // If there is a current value set it
        final TemplateDTO currentValue =
            _options.get(0).<TemplateDTO>makeTypeSafe().getCurrentValue();
        if (null != currentValue) {
            for (int i=0; i<_templates.getItemCount(); i++) {
                if (_templates.getValue(i).equals(
                    currentValue.getId())) {
                    _templates.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Add a change listener
        _templates.addChangeListener(new ChangeListener(){
            public void onChange(final Widget arg0) {
                final ListBox lb = (ListBox) arg0;
                final int selected = lb.getSelectedIndex();
                final String templateId = lb.getValue(selected);
                if ("<none>".equals(templateId)) {
                    _options.get(0).<TemplateDTO>makeTypeSafe().
                    setCurrentValue(null);
                } else {
                    final OptionDTO<TemplateDTO> templateOpts =
                        _options.get(0).<TemplateDTO>makeTypeSafe();
                    for (final TemplateDTO template:templateOpts.getChoices()) {
                        if (template.getId().equals(templateId)) {
                            templateOpts.setCurrentValue(template);
                            return;
                        }
                    }
                    Window.alert("No template: "+templateId);
                }
            }});

        _gui.add(
            new ButtonBar(_app)
            .add(
                _constants.cancel(),
                new DisposingClickListener(_delegate))
                .add(
                    _constants.save(),
                    new ClickListener() {
                        public void onClick(final Widget sender) {
                            _resourceService
                            .updateResourceTemplate(_options,
                                _resource,
                                new DisposingCallback(_app, _delegate));
                        }})
        );
    }

    /**
     * TODO: Add a description of this method.
     *
     */
    public void center() {
        _delegate.center();
    }
}

