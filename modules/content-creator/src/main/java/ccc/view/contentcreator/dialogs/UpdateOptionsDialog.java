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

package ccc.view.contentcreator.dialogs;

import java.util.List;

import ccc.view.contentcreator.client.Constants;
import ccc.view.contentcreator.client.ResourceService;
import ccc.view.contentcreator.client.ResourceServiceAsync;
import ccc.view.contentcreator.dto.DTO;
import ccc.view.contentcreator.dto.OptionDTO;
import ccc.view.contentcreator.dto.TemplateDTO;
import ccc.view.contentcreator.widgets.ButtonBar;
import ccc.view.contentcreator.widgets.FeedbackPanel;
import ccc.view.contentcreator.widgets.TwoColumnForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class UpdateOptionsDialog extends DialogBox {

    private final Constants _constants = GWT.create(Constants.class);
    private final String    _title     = _constants.options();
    private final VerticalPanel _widget = new VerticalPanel();

    private final ResourceServiceAsync _resourceService =
        (ResourceServiceAsync) GWT.create(ResourceService.class);
    private final ListBox _defaultTemplate = new ListBox();
    private final List<OptionDTO<? extends DTO>> _options;

    /**
     * Constructor.
     * @param templates
     */
    public UpdateOptionsDialog(final List<OptionDTO<? extends DTO>> options) {
        super(false, true);
        _options = options;
        setText(_title);
        setWidget(_widget);
        drawGUI();
    }

    private void drawGUI() {

        final FeedbackPanel fPanel = new FeedbackPanel();
        fPanel.setVisible(false);
        _widget.add(fPanel);

        _widget.add(
            new TwoColumnForm(1)
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

        _widget.add(
            new ButtonBar()
                .add(
                    _constants.cancel(),
                    new ClickListener() {
                        public void onClick(final Widget sender) {
                            hide();
                        }})
                .add(
                    _constants.save(),
                    new ClickListener() {
                        public void onClick(final Widget sender) {
                            _resourceService
                                .updateOptions(_options,
                                               new DisposingCallback());
                        }})
            );
    }

    /**
     * A simple call-back that displays an error or disposes the dialog.
     *
     * @author Civic Computing Ltd.
     */
    private final class DisposingCallback implements AsyncCallback<Void> {
        public void onFailure(final Throwable arg0) {
            Window.alert(_constants.error());
        }
        public void onSuccess(final Void arg0) {
            hide();
        }
    }
}
