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
package de.dante.util.configuration;

import java.util.Iterator;

/**
 * This class provides an Iterator over multiple Configurations.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class MultiConfigurationIterator implements Iterator {

    /**
     * The field <tt>iter</tt> contains the internal iterator in config[ptr].
     */
    private Iterator iter = null;

    /**
     * The field <tt>key</tt> contains the symbolic key for this Iterator.
     */
    private String key;

    /**
     * The field <tt>config</tt> contains the list of configurations to iterate
     * over.
     */
    private Configuration[] configs;

    /**
     * The field <tt>ptr</tt> contains the index of configurations to be treated
     * next.
     */
    private int ptr = 0;

    /**
     * Creates a new object.
     *
     * @param theConfigs the array of configurations to combine
     * @param theKey ...
     */
    public MultiConfigurationIterator(final Configuration[] theConfigs,
        final String theKey) {

        super();
        this.configs = theConfigs;
        this.key = theKey;

        if (theConfigs.length > 0) {
            iter = theConfigs[0].iterator(theKey);
        }
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {

        if (iter == null) {
            return false;
        }

        if (iter.hasNext()) {
            return true;
        }

        while (++ptr < configs.length) {
            iter = configs[ptr].iterator(key);

            if (iter.hasNext()) {
                return true;
            }
        }

        iter = null;
        return false;
    }

    /**
     * @see java.util.Iterator#next()
     */
    public Object next() {

        if (iter == null) {
            return null;
        }

        if (iter.hasNext()) {
            return iter.next();
        }

        while (++ptr < configs.length) {
            iter = configs[ptr].iterator(key);

            if (iter.hasNext()) {
                return iter.next();
            }
        }

        return iter.next();
    }

    /**
     * @see java.util.Iterator#remove()
     */
    public void remove() {

        //todo unimplemented because not needed yet
        throw new RuntimeException("unimplemented");
    }

}
