/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.documentWriter;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;

/**
 * This is the factory to provide an instance of a document writer.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class DocumentWriterFactory {
    /** The configuration to use */
    private Configuration config;

    /**
     * Creates a new object.
     *
     * @param config the configuration to use
     */
    public DocumentWriterFactory(Configuration config) {
        super();
        this.config = config;
    }

    /**
     * Provide a new instance of a document writer.
     * 
     * @return the new instance
     */
    public DocumentWriter newInstance() throws ConfigurationException {
        DocumentWriter docWriter;

        try {
            docWriter = (DocumentWriter) Class.forName(
                    config.getAttribute("class")).newInstance();
        } catch (Exception e) {
            throw new ConfigurationInstantiationException(
                    "DocumentWriterFactory", e);
        }

        return docWriter;

    }
}
