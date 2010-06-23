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

import ccc.api.types.CommandType;
import ccc.api.types.Link.Encoder;
import ccc.client.events.Bus;
import ccc.client.events.SimpleBus;
import ccc.client.remoting.TextParser;
import ccc.client.validation.AbstractValidations;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class InternalServices {

    public static Bus<CoreEvents>  CORE_BUS     = new SimpleBus<CoreEvents>();
    public static Bus<CommandType> REMOTING_BUS = new SimpleBus<CommandType>();

    public static AbstractValidations VALIDATOR;
    public static RequestExecutor     EXECUTOR;
    public static TextParser          PARSER;
    public static Encoder             ENCODER;
    public static ExceptionHandler    EX_HANDLER;
}
