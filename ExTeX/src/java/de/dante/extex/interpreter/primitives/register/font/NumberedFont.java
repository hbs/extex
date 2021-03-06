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

package de.dante.extex.interpreter.primitives.register.font;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;

/**
 * This class provides an implementation for a font stored under a name and a
 * number in the context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class NumberedFont extends NamedFont {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Construct the reference key for a numbered font.
     *
     * @param context the interpreter context
     * @param theName the base name of the font
     * @param theNumber the number of the font
     *
     * @return the key
     */
    public static String key(final Context context, final String theName,
            final String theNumber) {

        if (Namespace.SUPPORT_NAMESPACE_FONT) {
            return context.getNamespace() + "\b" + theName + "#" + theNumber;
        } else {
            return theName + "#" + theNumber;
        }
    }

    /**
     * Construct the reference key for a numbered font.
     *
     * @param context the interpreter context
     * @param theName the base name of the font
     * @param theNumber the number of the font
     *
     * @return the key
     */
    public static String key(final TypesetterOptions context,
            final String theName, final String theNumber) {

        if (Namespace.SUPPORT_NAMESPACE_FONT) {
            return context.getNamespace() + "\b" + theName + "#" + theNumber;
        } else {
            return theName + "#" + theNumber;
        }
    }

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NumberedFont(final String name) {

        super(name);
    }

    /**
     * Return the key (the name of the primitive) for the numbered font
     * register.
     *
     * @param context the interpreter context to use
     * @param source the source for new tokens
     *
     * @return the key for the current register
     *
     * @throws InterpreterException in case that a derived class need to throw an
     *  Exception this one is declared.
     *
     * @see de.dante.extex.interpreter.primitives.register.font.NamedFont#getKey(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    protected String getKey(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return key(context, getName(), //
                Long.toString(Count.scanNumber(context, source, typesetter)));
    }

}
