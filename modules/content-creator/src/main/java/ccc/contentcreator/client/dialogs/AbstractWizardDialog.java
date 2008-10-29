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
package ccc.contentcreator.client.dialogs;

import java.util.ArrayList;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractWizardDialog extends
AbstractBaseDialog {

    /** _layout : CardLayout. */
    private final CardLayout _layout = new CardLayout();

    private ArrayList<Component> _cards = new ArrayList<Component>();

    private final Button _save = new Button(
        _constants.save(),
        saveAction());

    private final Button _next = new Button(
        _constants.next(),
        nextAction());

    private final Button _prev = new Button(
        _constants.previous(),
        prevAction());

    private ContentPanel _cp = new ContentPanel();


    /**
     * Constructor.
     *
     * @param title Title of the dialog.
     */
    public AbstractWizardDialog(final String title) {

        super(title);

        _cp.setLayout(_layout);
        _cp.setWidth("100%");
        _cp.setBorders(false);
        _cp.setBodyBorder(false);
        _cp.setHeaderVisible(false);

        setLayout(new FitLayout());
        add(_cp);

        _next.setVisible(false);
        _prev.setVisible(false);
        _save.setVisible(false);

        addButton(new Button(
            _constants.cancel(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {
                    hide();
                }
            }));

        addButton(_prev);
        addButton(_save);
        addButton(_next);
    }


    /**
     * Refreshes content panel containing cards of the wizard.
     *
     */
    public void refresh() {
        _cp.removeAll();
        for (final Component c : _cards) {
            _cp.add(c);
        }

        if (!_cards.isEmpty()) {
            _layout.setActiveItem(_cards.get(0));
        }
        if (_cards.size() == 1) {
            _save.setVisible(true);
        }
        if (_cards.size() > 1) {
            _next.setVisible(true);
        }
    }

    /**
     * Adds new component to the cards collection.
     *
     * @param component New component.
     */
    public void addCard(final Component component) {
        _cards.add(component);
    }

    /**
     * Removes component from the cards collection.
     *
     * @param component The component to remove.
     */
    public void removeCard(final Component component) {
        _cards.remove(component);
    }

    private int currentIndex(final Component current) {
        if (current != null &&_cards.size() > 0) {
            return _cards.indexOf(current);
        }
        return -1;
    }

    private SelectionListener<ButtonEvent> nextAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final int currentIndex = currentIndex(_layout.getActiveItem());

                if (_cards.size() == 2) {
                    if (currentIndex == 0) {
                        _prev.setVisible(true);
                        _next.setVisible(false);
                        _save.setVisible(true);
                        _layout.setActiveItem(_cards.get(1));
                    }
                } else if (_cards.size() > 2) {
                    if (currentIndex == 0) {
                        _prev.setVisible(true);
                        _layout.setActiveItem(_cards.get(currentIndex+1));
                    } else if (currentIndex > 0
                            && currentIndex < _cards.size()-2) {
                        _layout.setActiveItem(_cards.get(currentIndex+1));
                    } else if (currentIndex == _cards.size()-2) {
                        _next.setVisible(false);
                        _save.setVisible(true);
                        _layout.setActiveItem(_cards.get(currentIndex+1));
                    }
                }

            }
        };
    }

    private SelectionListener<ButtonEvent> prevAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final int currentIndex = currentIndex(_layout.getActiveItem());

                if (_cards.size() == 2) {
                    if (currentIndex == _cards.size()-1) {
                        _prev.setVisible(false);
                        _next.setVisible(true);
                        _save.setVisible(false);
                        _layout.setActiveItem(_cards.get(0));
                    }
                } else if (_cards.size() > 2) {
                    if (currentIndex == 1) {
                        _prev.setVisible(false);
                        _layout.setActiveItem(_cards.get(0));
                    } else if (currentIndex > 1
                            && currentIndex < _cards.size()-1) {
                        _layout.setActiveItem(_cards.get(currentIndex-1));
                    } else if (currentIndex == _cards.size()-1) {
                        _next.setVisible(true);
                        _save.setVisible(false);
                        _layout.setActiveItem(_cards.get(currentIndex-1));
                    }
                }
            }
        };
    }

    /**
     * Factory for save actions.
     *
     * @return A selection listener for use by the save button.
     */
    protected abstract SelectionListener<ButtonEvent> saveAction();

}
