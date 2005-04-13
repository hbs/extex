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
 *
 */

package de.dante.extex.documentWriter;

import java.io.OutputStream;

import de.dante.extex.documentWriter.exception.DocumentWriterException;

/**
 * This is the factory for new OutputStreams.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface OutputStreamFactory {

    /**
     * Getter for the destination.
     *
     * @return the name of the dastination
     */
    String getDestination();

    /**
     * Getter for a new OutputStream of the default type.
     *
     * @return the new OutputStream
     *
     * @throws DocumentWriterException in case of an error
     */
    OutputStream getOutputStream() throws DocumentWriterException;

    /**
     * Getter for a new OutputStream.
     *
     * @param type the type of the stream to acquire. In general this should
     *  correspond to the extension of a file of this type
     *
     * @return the new OutputStream
     *
     * @throws DocumentWriterException in case of an error
     */
    OutputStream getOutputStream(String type) throws DocumentWriterException;

    /**
     * Getter for a new OutputStream.
     *
     * @param name the name segment to characterize the stream externally.
     *  This segment might be used as part of the output file. If the name is
     *  <code>null</code> then it is treated as not given at all.
     * @param type the type of the stream to acquire. In general this should
     *  correspond to the extension of a file of this type
     *
     * @return the new OutputStream
     *
     * @throws DocumentWriterException in case of an error
     */
    OutputStream getOutputStream(String name, String type)
            throws DocumentWriterException;

    /**
     * Setter for the extension.
     *
     * @param extension the default extension
     */
    void setExtension(String extension);

}