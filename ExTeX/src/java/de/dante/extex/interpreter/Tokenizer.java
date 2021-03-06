/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter;

import de.dante.extex.scanner.type.Catcode;
import de.dante.util.UnicodeChar;

/**
 * A tokenizer is a class which is able to categorize characters according to
 * the category codes.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public interface Tokenizer {

    /**
     * Getter for the category code of a character.
     *
     * @param c the Unicode character to analyze
     *
     * @return the category code of a character
     */
    Catcode getCatcode(UnicodeChar c);

    /**
     * Getter for the name space.
     *
     * @return the name space
     */
    String getNamespace();

}
