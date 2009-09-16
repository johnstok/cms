/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;


/**
 * Basic API for MVP Views.
 *
 * @param <T> The type of presenter for the view.
 *
 * @author Civic Computing Ltd.
 */
public interface View<T> {


    /**
     * Hide the view.
     */
    void hide();


    /**
     * Show the view.
     */
    void show();


    /**
     * Set the presenter for the view.
     *
     * @param presenter The presenter to set.
     */
    void setPresenter(T presenter);
}
