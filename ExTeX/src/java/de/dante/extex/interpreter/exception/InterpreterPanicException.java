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

import de.dante.util.framework.i18n.Localizer;

/**
 * This exception signals the error handler that a continuation is not
 * desirable.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class InterpreterPanicException extends InterpreterException {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * The field <tt>arg</tt> contains the additional argument for printing.
     */
    private String arg;

    /**
     * The field <tt>tag</tt> contains the tag for the localizer.
     */
    private String tag;

    /**
     * Creates a new object.
     *
     * @param localizer the localizer
     * @param tag the name of the format for the localizer
     */
    public InterpreterPanicException(final Localizer localizer, final String tag) {

        super(localizer);
        this.tag = tag;
        this.arg = "?";
    }

    /**
     * Creates a new object.
     *
     * @param localizer the localizer
     * @param tag the name of the format for the localizer
     * @param arg the argument
     */
    public InterpreterPanicException(final Localizer localizer,
            final String tag, final String arg) {

        super(localizer);
        this.tag = tag;
        this.arg = arg;
    }

    /**
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    public String getLocalizedMessage() {

        return getLocalizer().format(tag, arg);
    }

}
