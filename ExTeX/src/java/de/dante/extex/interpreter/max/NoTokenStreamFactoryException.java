/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.max;

import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This Exception is thrown when a TokenStreamFactory is needed but none is
 * found.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class NoTokenStreamFactoryException extends ConfigurationException {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * The field <tt>message</tt> contains the message of this exception.
     */
    private String message;

    /**
     * Create a new object.
     *
     * @param aMessage the message string
     */
    public NoTokenStreamFactoryException(final String aMessage) {

        super(aMessage, (String) null);
        this.message = aMessage;
    }

    /**
     * Getter for the text prefix of this ConfigException.
     *
     * @return the text
     */
    public String getLocalizedMessage() {

        return getLocalizer().format(
                "ConfigurationNoTokenStreamFactoryException.Text", message);
    }

}
