/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;

import java.util.ArrayList;

import ccc.client.core.Globals;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.CardPanel;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.ui.Widget;


/**
 * Abstract class for wizard type card layout.
 * Contains logic and event handling for next, previous and cancel buttons.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractWizardDialog
    extends
        AbstractBaseDialog {

    private final ArrayList<Component> _cards =
        new ArrayList<Component>();

    private final Button _save = new Button(
        constants().save(),
        saveAction());

    private final Button _next = new Button(
        constants().next(),
        nextAction());

    private final Button _prev = new Button(
        constants().previous(),
        prevAction());

    private final CardPanel _cp = new CardPanel();

    /**
     * Constructor.
     *
     * @param title Title of the dialog.
     * @param globals The globals for this dialog.
     */
    public AbstractWizardDialog(final String title, final Globals globals) {

        super(title, globals);

        _cp.setWidth("100%");
        _cp.setBorders(false);

        add(_cp);

        _next.setVisible(false);
        _prev.setVisible(false);
        _save.setVisible(false);

        _next.setId("next");
        _prev.setId("previous");
        _save.setId("save");

        addButton(getCancel());
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
        for (final Widget c : _cards) {
            _cp.add(c);
        }

        if (!_cards.isEmpty()) {
            _cp.setActiveItem(_cards.get(0));
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

    /**
     * Replaces card.
     *
     * @param original The component to replace.
     * @param replace The new component.
     */
    public void replaceCard(final Component original, final Component replace) {
        final int index = _cards.indexOf(original);
        if (index > 0) {
            _cards.set(index, replace);
        }
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
                final int currentIndex = currentIndex(_cp.getActiveItem());
                Component card = null;

                if (_cards.size() == 2) {
                    if (currentIndex == 0) {
                        _prev.setVisible(true);
                        _next.setVisible(false);
                        _save.setVisible(true);
                        card = _cards.get(1);
                    }
                } else if (_cards.size() > 2) {
                    if (currentIndex == 0) {
                        _prev.setVisible(true);
                        card = _cards.get(currentIndex+1);
                    } else if (currentIndex > 0
                            && currentIndex < _cards.size()-2) {
                        card = _cards.get(currentIndex+1);
                    } else if (currentIndex == _cards.size()-2) {
                        _next.setVisible(false);
                        _save.setVisible(true);
                        card = _cards.get(currentIndex+1);
                    }
                }
                if (card != null) {
                    _cp.setActiveItem(card);
                }

            }
        };
    }

    private SelectionListener<ButtonEvent> prevAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final int currentIndex = currentIndex(_cp.getActiveItem());
                Component card = null;

                if (_cards.size() == 2) {
                    if (currentIndex == _cards.size()-1) {
                        _prev.setVisible(false);
                        _next.setVisible(true);
                        _save.setVisible(false);
                        card = _cards.get(0);
                    }
                } else if (_cards.size() > 2) {
                    if (currentIndex == 1) {
                        _prev.setVisible(false);
                        card = _cards.get(0);
                    } else if (currentIndex > 1
                            && currentIndex < _cards.size()-1) {
                        card = _cards.get(currentIndex-1);
                    } else if (currentIndex == _cards.size()-1) {
                        _next.setVisible(true);
                        _save.setVisible(false);
                        card = _cards.get(currentIndex-1);
                    }
                }
                if (card != null) {
                    _cp.setActiveItem(card);
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
