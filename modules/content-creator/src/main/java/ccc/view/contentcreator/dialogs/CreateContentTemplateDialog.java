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

import ccc.view.contentcreator.client.Constants;
import ccc.view.contentcreator.client.ResourceService;
import ccc.view.contentcreator.client.ResourceServiceAsync;
import ccc.view.contentcreator.dto.TemplateDTO;
import ccc.view.contentcreator.widgets.ButtonBar;
import ccc.view.contentcreator.widgets.FeedbackPanel;
import ccc.view.contentcreator.widgets.TwoColumnForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class CreateContentTemplateDialog extends DialogBox {

    private final Constants _constants = GWT.create(Constants.class);
    private final String    _title     = _constants.createDisplayTemplate();
    private final VerticalPanel _widget = new VerticalPanel();

    private final ResourceServiceAsync _resourceService =
        (ResourceServiceAsync) GWT.create(ResourceService.class);
    private final TextBox _templateTitle = new TextBox();
    private final TextBox _description = new TextBox();
    private final TextArea _body = new TextArea();

    /**
     * Constructor.
     */
    public CreateContentTemplateDialog() {
        super(false, true);
        setText(_title);
        setWidget(_widget);
        drawGUI();
    }

    private void drawGUI() {

        final FeedbackPanel fPanel = new FeedbackPanel();
        fPanel.setVisible(false);
        _widget.add(fPanel);

        _widget.add(
            new TwoColumnForm(3)
                .add(_constants.title(), _templateTitle)
                .add(_constants.description(), _description)
                .add(_constants.body(), _body)
            );

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
                            final TemplateDTO dto = model();
                            if (dto.isValid()) {
                                _resourceService.createTemplate(
                                    dto,
                                    new DisposingCallback());
                            } else {
                                final FeedbackPanel fPanel =
                                    (FeedbackPanel) _widget.getWidget(0);
                                fPanel.displayErrors(dto.validate());
                                fPanel.setVisible(true);
                            }
                        }})
            );
    }

    private TemplateDTO model() {
        return new TemplateDTO(
            _templateTitle.getText(),
            _description.getText(),
            _body.getText());
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
