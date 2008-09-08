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

import ccc.contentcreator.api.Application;
import ccc.contentcreator.api.CompositeControl;
import ccc.contentcreator.api.Constants;
import ccc.contentcreator.api.Control;
import ccc.contentcreator.api.FileControl;
import ccc.contentcreator.api.GridControl;
import ccc.contentcreator.api.ListControl;
import ccc.contentcreator.api.PanelControl;
import ccc.contentcreator.api.ResourceService;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.StringControl;
import ccc.contentcreator.dialogs.ApplicationDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;


/**
 * GWT implementation of {@link Application}.
 *
 * @author Civic Computing Ltd.
 */
public class GwtApplication implements Application {

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public ResourceServiceAsync lookupService() {
        return GWT.create(ResourceService.class);
    }

    /** {@inheritDoc} */
    public void alert(final String message) {
        Window.alert(message);
    }

    /** {@inheritDoc} */
    public Constants constants() {
        return GWT.create(Constants.class);
    }

    /** {@inheritDoc} */
    public String hostURL() {
        return GWT.getHostPageBaseURL()
            .substring(
                0,
                GWT.getHostPageBaseURL()
                   .lastIndexOf("content-creator/")); //$NON-NLS-1$
    }

    /** {@inheritDoc} */
    @SuppressWarnings("synthetic-access")
    public PanelControl verticalPanel() {
        return new GwtVerticalPanelControl();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("synthetic-access")
    public PanelControl horizontalPanel() {
        return new GwtHorizontalPanelControl();
    }

    /** {@inheritDoc} */
    public ApplicationDialog dialog(final String title) {
        return new GwtAppDialog(title);
    }

    /** {@inheritDoc} */
    public Control frame(final String url) {
        return new GwtFrameControl(url);
    }


    /** {@inheritDoc} */
    public Control button(final String buttonTitle,
                          final ClickListener clickListener) {
        return new GwtButtonControl(buttonTitle, clickListener);
    }

    /** {@inheritDoc} */
    public Control label(final String title) {
        return new GwtLabelControl(title);
    }


    /** {@inheritDoc} */
    public GridControl grid(final int numRows, final int i) {
        return new GwtGridControl(numRows, i);
    }

    /**
     * GwtLabelControl.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtLabelControl
                 extends Label
              implements Control {

        /**
         * Constructor.
         */
        public GwtLabelControl(final String title) {
            super(title);
        }
    }

    /**
     * GwtTextBoxControl.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtTextBoxControl
                 extends TextBox
              implements StringControl {

        /** {@inheritDoc} */
        public String model() {
            return getText();
        }

        /** {@inheritDoc} */
        public void model(final String text) {
            setText(text);
        }
    }

    /**
     * GwtTextAreaControl.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtTextAreaControl
                 extends TextArea
              implements StringControl {

        /** {@inheritDoc} */
        public String model() {
            return getText();
        }

        /** {@inheritDoc} */
        public void model(final String text) {
            setText(text);
        }
    }

    /**
     * GwtButtonControl.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtButtonControl
                 extends Button
              implements Control {

        /**
         * Constructor.
         *
         * @param buttonTitle
         * @param clickListener
         */
        public GwtButtonControl(final String buttonTitle,
                                final ClickListener clickListener) {
            super(buttonTitle, clickListener);
        }
    }

    /**
     * GwtListBoxControl.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtListBoxControl
                 extends ListBox
              implements ListControl {
        /* No overrides */
    }

    /**
     * GwtFileUploadControl.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtFileUploadControl
                 extends FileUpload
              implements FileControl {
        /* No overrides */
    }

    /**
     * GwtFormPanelControl.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtFormPanelControl
                 extends FormPanel
              implements PanelControl {

        /**
         * Constructor.
         *
         * @param url
         * @param encoding
         * @param httpMethod
         * @param formHandler
         * @param gui
         */
        public GwtFormPanelControl(final String url,
                                   final String encoding,
                                   final String httpMethod,
                                   final FormHandler formHandler,
                                   final PanelControl gui) {

            setAction(url);
            setEncoding(encoding);
            setMethod(httpMethod);
            addFormHandler(formHandler);
            setWidget((Widget) gui);
        }

        /** {@inheritDoc} */
        public void add(final Control frame) {
            setWidget((Widget) frame);
        }

        /** {@inheritDoc} */
        public Control child(final int i) {
            throw new UnsupportedOperationException("Method not implemented.");
        }

        /** {@inheritDoc} */
        public void setHorizontalAlignment(
                               final HorizontalAlignmentConstant alignRight) {
            throw new UnsupportedOperationException("Method not implemented.");
        }

        /** {@inheritDoc} */
        public void setVerticalAlignment(
                                final VerticalAlignmentConstant alignBottom) {
            throw new UnsupportedOperationException("Method not implemented.");
        }
    }

    /**
     * GwtHiddenControl.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtHiddenControl
                 extends Hidden
              implements StringControl {

        /** {@inheritDoc} */
        public String model() {
            return getValue();
        }

        /** {@inheritDoc} */
        public void model(final String text) {
            setValue(text);
        }
    }

    /**
     * GwtFrameControl.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtFrameControl
                 extends Frame
              implements Control {

        /**
         * Constructor.
         *
         * @param url
         */
        public GwtFrameControl(final String url) {
            super(url);
            setStyleName("ccc-Frame");
            DOM.setElementPropertyInt(getElement(), "frameBorder", 0);
        }

    }

    /**
     * GwtVerticalPanelControl.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtVerticalPanelControl
                 extends VerticalPanel
              implements PanelControl {

        /** {@inheritDoc} */
        public void add(final Control frame) {
            if (frame instanceof CompositeControl) {
                add((Widget) ((CompositeControl) frame).rootWidget());
            } else {
                add((Widget) frame);
            }
        }

        /** {@inheritDoc} */
        public Control child(final int i) {
            return (Control) getWidget(i);
        }

        /** {@inheritDoc} */
        public void submit() {
            throw new UnsupportedOperationException("Method not implemented.");
        }
    }

    /**
     * GwtHorizontalPanelControl.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtHorizontalPanelControl
                 extends HorizontalPanel
              implements PanelControl {

        /** {@inheritDoc} */
        public void add(final Control frame) {
            if (frame instanceof CompositeControl) {
                add((Widget) ((CompositeControl) frame).rootWidget());
            } else {
                add((Widget) frame);
            }
        }

        /** {@inheritDoc} */
        public Control child(final int i) {
            return (Control) getWidget(i);
        }

        /** {@inheritDoc} */
        public void submit() {
            throw new UnsupportedOperationException("Method not implemented.");
        }
    }

    /**
     * GwtAppDialog.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtAppDialog
                 extends DialogBox
              implements ApplicationDialog {

        /**
         * Constructor.
         *
         * @param autoHide
         * @param modal
         */
        GwtAppDialog(final String title) {
            super(false, true);
            setText(title);
        }

        /** {@inheritDoc} */
        public void gui(final PanelControl control) {
            setWidget((Widget) control);
        }
    }

    /**
     * GwtAppDialog.
     *
     * @author Civic Computing Ltd.
     */
    private static class GwtGridControl
                 extends Grid
              implements GridControl {

        /**
         * Constructor.
         *
         * @param rows
         * @param columns
         */
        public GwtGridControl(final int rows, final int columns) {
            super(rows, columns);
        }

        /** {@inheritDoc} */
        public void setWidget(final int row,
                              final int i,
                              final Control widget) {
            if (widget instanceof CompositeControl) {
                setWidget(
                    row,
                    i,
                    (Widget) ((CompositeControl) widget).rootWidget());
            } else {
                setWidget(row, i, (Widget) widget);
            }
        }
    }

    /** {@inheritDoc} */
    public StringControl textBox() {
        return new GwtTextBoxControl();
    }

    /** {@inheritDoc} */
    public StringControl textArea() {
        return new GwtTextAreaControl();
    }

    /** {@inheritDoc} */
    public ListControl listBox() {
        return new GwtListBoxControl();
    }

    /** {@inheritDoc} */
    public FileControl fileUpload() {
        return new GwtFileUploadControl();
    }

    /** {@inheritDoc} */
    public StringControl hidden() {
        return new GwtHiddenControl();
    }

    /** {@inheritDoc} */
    public PanelControl formPanel(final String url,
                                  final String encoding,
                                  final String httpMethod,
                                  final FormHandler formHandler,
                                  final PanelControl gui) {

        return new GwtFormPanelControl(url,
                                       encoding,
                                       httpMethod,
                                       formHandler,
                                       gui);
    }
}
