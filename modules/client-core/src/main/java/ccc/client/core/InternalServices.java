/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.core;

import ccc.api.core.API;
import ccc.api.core.ActionSummary;
import ccc.api.core.Comment;
import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.core.User;
import ccc.api.types.CommandType;
import ccc.api.types.Link.Encoder;
import ccc.client.events.Bus;
import ccc.client.events.SimpleBus;
import ccc.client.validation.AbstractValidations;
import ccc.plugins.s11n.Serializers;
import ccc.plugins.s11n.json.TextParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class InternalServices {

    private InternalServices() { super(); }

    /** coreBus : Bus. */
    public static Bus<CoreEvents>  coreBus     = new SimpleBus<CoreEvents>();
    /** remotingBus : Bus. */
    public static Bus<CommandType> remotingBus = new SimpleBus<CommandType>();

    /** validator : AbstractValidations. */
    public static AbstractValidations validator;
    /** executor : RequestExecutor. */
    public static RequestExecutor     executor;
    /** serializers : Serializers. */
    public static Serializers         serializers;
    /** encoder : Encoder. */
    public static Encoder             encoder;
    /** exHandler : ExceptionHandler. */
    public static ExceptionHandler    exHandler;
    /** window : Window. */
    public static Window              window;
    /** dialogs : DialogFactory. */
    public static DialogFactory       dialogs;
    /** globals : Globals. */
    public static Globals             globals;
    /** timers : Timers. */
    public static Timers              timers;
    /** parser : TextParser. */
    public static TextParser          parser;

    /** api : API. */
    public static API                              api;
    /** actions : PagedCollection. */
    public static PagedCollection<ActionSummary>   actions;
    /** groups : PagedCollection. */
    public static PagedCollection<Group>           groups;
    /** users : PagedCollection. */
    public static PagedCollection<User>            users;
    /** comments : PagedCollection. */
    public static PagedCollection<Comment>         comments;
    /** roots : PagedCollection. */
    public static PagedCollection<ResourceSummary> roots;
}
