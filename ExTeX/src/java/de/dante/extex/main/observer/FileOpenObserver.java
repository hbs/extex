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

package de.dante.extex.main.observer;

import java.io.InputStream;
import java.util.logging.Logger;

import de.dante.extex.scanner.stream.observer.file.OpenFileObserver;

/**
 * This observer reports that a certain file has been opened.
 * According to the behavior of <logo>TeX</logo> it logs an open brace and the
 * name of the file.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class FileOpenObserver implements OpenFileObserver {

    /**
     * The field <tt>logger</tt> contains the current logger
     */
    private Logger logger;

    /**
     * Creates a new object.
     *
     * @param theLogger the logger to use
     */
    public FileOpenObserver(final Logger theLogger) {

        super();
        this.logger = theLogger;
    }

    /**
     * @see de.dante.extex.scanner.stream.observer.file.OpenFileObserver#update(
     *      java.lang.String,
     *      java.lang.String,
     *      java.io.InputStream)
     */
    public void update(final String filename, final String filetype,
            final InputStream stream) {

        logger.info("(" + filename);
    }

}
