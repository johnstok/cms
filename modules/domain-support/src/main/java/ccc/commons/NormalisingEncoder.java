/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Revision      $Rev: 2757 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-05-06 14:48:25 +0100 (Thu, 06 May 2010) $
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.text.Normalizer.Form;

import ccc.api.types.Link.Encoder;

/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class NormalisingEncoder implements Encoder {
    /** {@inheritDoc} */
    @Override public String encode(final String string) {
        try {
            return
                URLEncoder.encode(
                    Normalizer.normalize(string, Form.NFKC),
                    "UTF-8")
                .replace("+", "%20")
                .replace("%3A", ":"); // FIXME: Replace remaining reserved chars.
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
