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

package ccc.contentcreator.dialogs;

import java.util.List;

import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.callbacks.DisposingClickListener;
import ccc.contentcreator.client.Application;
import ccc.contentcreator.client.Constants;
import ccc.contentcreator.client.ResourceServiceAsync;
import ccc.contentcreator.controls.ListControl;
import ccc.contentcreator.controls.PanelControl;
import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.OptionDTO;
import ccc.contentcreator.dto.TemplateDTO;
import ccc.contentcreator.widgets.ButtonBar;
import ccc.contentcreator.widgets.FeedbackPanel;
import ccc.contentcreator.widgets.TwoColumnForm;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class UpdateOptionsDialog {

    private final ApplicationDialog     _delegate;
    private final Application        _app;
    private final Constants     _constants;
    private final PanelControl  _gui;
    private final ResourceServiceAsync _resourceService;

    private final ListControl _defaultTemplate;
    private final List<OptionDTO<? extends DTO>> _options;

    /**
     * Constructor.
     * @param templates
     */
    public UpdateOptionsDialog(final Application app,
                               final List<OptionDTO<? extends DTO>> options) {

        _app = app;
        _constants = _app.constants();
        _delegate = _app.dialog(_constants.options());
        _gui = _app.verticalPanel();
        _resourceService = _app.lookupService();

        _defaultTemplate = _app.listBox();
        _options = options;

        drawGUI();
        _delegate.gui(_gui);
    }

    private void drawGUI() {

        final FeedbackPanel fPanel = new FeedbackPanel(_app);
        fPanel.setVisible(false);
        _gui.add(fPanel);

        _gui.add(
            new TwoColumnForm(_app, 1)
                .add(_constants.defaultTemplate(), _defaultTemplate)
            );

        // populate combo box
        _defaultTemplate.addItem("<none>", "<none>"); // No value.
        for (final TemplateDTO template :
                    _options.get(0).<TemplateDTO>makeTypeSafe().getChoices()) {
            _defaultTemplate.addItem(template.getTitle(), template.getId());
        }

        // If there is a current value set it
        final TemplateDTO currentValue = _options.get(0).<TemplateDTO>makeTypeSafe().getCurrentValue();
        if (null != currentValue) {
            for (int i=0; i<_defaultTemplate.getItemCount(); i++) {
                if (_defaultTemplate.getValue(i).equals(
                    currentValue.getId())) {
                    _defaultTemplate.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Add a change listener
        _defaultTemplate.addChangeListener(new ChangeListener(){
            public void onChange(final Widget arg0) {
                final ListBox lb = (ListBox)arg0;
                final int selected = lb.getSelectedIndex();
                final String templateId = lb.getValue(selected);
                if ("<none>".equals(templateId)) {
                    _options.get(0).<TemplateDTO>makeTypeSafe().setCurrentValue(null);
                } else {
                    for (final TemplateDTO template :
                        _options.get(0).<TemplateDTO>makeTypeSafe().getChoices()) {
                        if (template.getId().equals(templateId)) {
                            _options.get(0).<TemplateDTO>makeTypeSafe().setCurrentValue(template);
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
                                .updateOptions(_options,
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
