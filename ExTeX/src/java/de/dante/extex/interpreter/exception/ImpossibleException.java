/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.exception;


/**
 * This exception indicates that a situation has been detected which should not
 * happen. This results from conservative programming. For instance if all
 * possible values of a variable are processed in a switch statement and then
 * this exceptiopn is thrown in the default clause.
 * <p>
 * This exception is not applicable for i18n.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ImpossibleException extends RuntimeException {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param message the description of the error
     */
    public ImpossibleException(final String message) {

        super(message);
    }

    /**
     * Creates a new object.
     *
     * @param cause the cause of the error
     */
    public ImpossibleException(final Throwable cause) {

        super(cause);
    }

    /**
     * Creates a new object.
     *
     * @param message the description of the error
     * @param cause the cause of the error
     */
    public ImpossibleException(final String message, final Throwable cause) {

        super(message, cause);
    }

}
