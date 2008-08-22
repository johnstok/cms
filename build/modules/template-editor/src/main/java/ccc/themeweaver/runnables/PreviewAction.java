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
package ccc.themeweaver.runnables;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;

import org.xhtmlrenderer.extend.NamespaceHandler;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.swing.BasicPanel;

import ccc.commons.VelocityProcessor;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.ResourceName;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PreviewAction extends AbstractAction {

    private BasicPanel previewPane;
    private JEditorPane editorPane;

    /**
     * Constructor.
     *
     * @param editorPane
     * @param previewPane
     */
    public PreviewAction(final JEditorPane editorPane, final XHTMLPanel previewPane) {
        super("Preview");
        this.editorPane = editorPane;
        this.previewPane = previewPane;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent arg0) {
        final Page p = new Page(new ResourceName("foo"));
        p.addParagraph("example paragraph", new Paragraph("some<br/>html"));
        final NamespaceHandler nsh = new XhtmlNamespaceHandler();
        final String previewText =
            new VelocityProcessor().render(p, editorPane.getText());
        previewPane.setDocumentFromString(previewText, null, nsh);
    }
}
