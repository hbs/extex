/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */
package de.dante.extex.scanner;

import de.dante.extex.i18n.Messages;
import de.dante.util.UnicodeChar;


/**
 * This class represents a sub mark token.
 * <p>
 * This class has a protected constructor only. Use the factory
 * {@link de.dante.extex.scanner.TokenFactory TokenFactory}
 * to get an instance of this class.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class SubMarkToken extends AbstractToken implements Token {

    /**
     * Creates a new object.
     *
     * @param uc the actual value
     */
    protected SubMarkToken(final UnicodeChar uc) {
        super(uc);
    }

    /**
     * @see de.dante.extex.scanner.Token#getCatcode()
     */
    public Catcode getCatcode() {
        return Catcode.SUBMARK;
    }

    /**
     * Get the string representation of this object for debugging purposes.
     *
     * @return the string representation
     *
     * @see "TeX -- The Program [298]"
     */
    public String toString() {

        return Messages.format("SubMarkToken.Text", getValue());
    }

}
