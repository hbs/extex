/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package de.dante.extex.unicodeFont.format.tex.psfontmap.enc.exception;

import de.dante.extex.unicodeFont.exception.FontException;

/**
 * Exception, if the range from '[' to ']' was wrong.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class FontEncodingWrongRangeException extends FontException {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 2381759407757645948L;

    /**
     * Create a new object.
     */
    public FontEncodingWrongRangeException() {

        super();
    }

    /**
     * Create anew object.
     *
     * @param message   the message
     */
    public FontEncodingWrongRangeException(final String message) {

        super(message);
    }
}
