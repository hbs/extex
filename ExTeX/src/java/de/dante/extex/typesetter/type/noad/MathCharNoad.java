/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.noad;

import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class MathCharNoad extends AbstractNoad implements Noad {

    /**
     * The field <tt>family</tt> contains the font family for this character.
     */
    private int family;

    /**
     * The field <tt>uc</tt> contains the Unicode character representation.
     */
    private UnicodeChar uc;

    /**
     * Creates a new object.
     *
     * @param fam the font family for the character
     * @param character the Unicode character representation
     */
    public MathCharNoad(final int fam, final UnicodeChar character) {

        super();
        family = fam;
        uc = character;
    }

    /**
     * Getter for family.
     *
     * @return the family.
     */
    public int getFamily() {

        return this.family;
    }

    /**
     * Getter for the character.
     *
     * @return the character.
     */
    public UnicodeChar getChar() {

        return this.uc;
    }
}