/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.type.node;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.util.UnicodeChar;

import java.util.HashMap;
import java.util.Map;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class CharNodeFactory {

    /**
     * The field <tt>cache</tt> contains the ...
     */
    private Map cache = new HashMap();

    /**
     * Creates a new object.
     */
    public CharNodeFactory() {
        super();
    }

    /**
     * ...
     *
     * @param typesettingContext ...
     * @param c ...
     *
     * @return ...
     */
    public CharNode newInstance(final TypesettingContext typesettingContext,
            final UnicodeChar c) {
        CharNode n = (CharNode) cache.get(c);

        if (n == null) {
            n = new CharNode(typesettingContext, c);
            cache.put(c, n);
        }

        return n;
    }

}
