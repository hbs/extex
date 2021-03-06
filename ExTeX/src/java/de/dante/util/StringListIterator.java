/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.util;

import java.util.Iterator;
import java.util.List;

/**
 * This class provides an iterator to traverse all elements of a
 * {@link StringList StringList}.
 * This is a type-safe incarnation of an {@link Iterator Iterator}.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class StringListIterator {

    /**
     * The field <tt>iterator</tt> contains the encapsulated iterator.
     */
    private Iterator iterator;

    /**
     * Creates a new object and fills it with some values.
     *
     * @param val a {@link java.util.Vector Vector} of the elements to traverse
     */
    public StringListIterator(final List val) {

        super();
        iterator = val.iterator();
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {

        return iterator.hasNext();
    }

    /**
     * @see java.util.Iterator#next()
     */
    public String next() {

        return (String) (iterator.next());
    }

}