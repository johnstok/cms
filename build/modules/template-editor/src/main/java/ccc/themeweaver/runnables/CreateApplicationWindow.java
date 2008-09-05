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

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

import jsyntaxpane.SyntaxKit;

import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.swing.NaiveUserAgent;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CreateApplicationWindow implements Runnable {

    /** {@inheritDoc} */
    @Override
    public void run() {


        final XHTMLPanel previewPane = new XHTMLPanel(
            new NaiveUserAgent(){
                /* No overrides */
                });
        final JScrollPane previewScrollPane = new JScrollPane(previewPane);
        previewScrollPane.setVerticalScrollBarPolicy(
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        final JEditorPane editorPane = new JEditorPane();
        editorPane.setEditorKit(new SyntaxKit("xml"));
        editorPane.setText("<html><body><h1>Hello world!</h1></body></html>");
        final JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        final JSplitPane pane =
            new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                           editorScrollPane,
                           previewScrollPane);
        pane.setDividerLocation(400);

        final JButton preview = new JButton();
        preview.setAction(new PreviewAction(editorPane, previewPane));

        final JFrame frame = new JFrame("Civic Themeweaver (Beta)\u2122");
        frame.setIconImage(new ImageIcon(getClass().getResource("/ccc/themeweaver/civic.gif")).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(preview, BorderLayout.SOUTH);
        frame.getContentPane().add(pane);
        frame.setSize(800, 600);

        frame.setVisible(true);
    }

}
