/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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
 */

package de.dante.extex.unicodeFont.format.tex.psfontmap.enc;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.format.tex.psfontmap.enc.exception.FontEncodingFileNotFoundException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.resource.ResourceFinder;

/**
 * Factory for enc-files.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class EncFactory implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -2024818780342719008L;

    /**
     * Create a new object.
     * @param afinder    finder
     */
    public EncFactory(final ResourceFinder afinder) {

        finder = afinder;
        data = new HashMap();
    }

    /**
     * The resource finder.
     */
    private transient ResourceFinder finder;

    /**
     * Map.
     */
    private Map data;

    /**
     * Returns the encoding table.
     * @param filename  the file name.
     * @return Returns the encoding table.
     * @throws FontException if an font-erorr occurred.
     * @throws ConfigurationException from the resource finder.
     */
    public String[] getEncodingTable(final String filename)
            throws FontException, ConfigurationException {

        String[] table = (String[]) data.get(filename);

        if (table == null) {
            InputStream in = finder.findResource(filename, "enc");
            if (in == null) {
                throw new FontEncodingFileNotFoundException(filename);
            }
            EncReader er = new EncReader(in);
            table = er.getTable();
            data.put(filename, table);
        }
        return table;
    }
}