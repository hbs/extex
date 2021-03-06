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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.macro.Csname;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\ifcsname</code>.
 *
 * <doc name="ifcsname">
 * <h3>The Primitive <tt>\ifcsname</tt></h3>
 * <p>
 *  The primitive <tt>\ifcsname</tt> checks if a certain control sequence is
 *  defined. For this purpose it collects tokens in the same way as
 *  <tt>\csname</tt> does until <tt>\endcsname</tt> is encountered.
 *  It does not define the control sequence if it is not defined.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;ifcsname&rang;
 *      &rarr; <tt>\ifcsname</tt> ... <tt>\endcsname</tt> &lang;true text&rang; <tt>\fi</tt>
 *       |  <tt>\ifcsname</tt> ... <tt>\endcsname</tt> &lang;true text&rang; <tt>\else</tt> &lang;false text&rang; <tt>\fi</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *     \ifcsname def\endcsname ok\fi  </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class Ifcsname extends AbstractIf {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Ifcsname(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.conditional.AbstractIf#conditional(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    protected boolean conditional(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Tokens toks = Csname.scanToEndCsname(context, source, typesetter,
                getLocalizer());
        String csname = toks.toText();
        try {
            return context.getCode((CodeToken) context.getTokenFactory()
                    .createToken(Catcode.ESCAPE, null, csname,
                            context.getNamespace())) != null;
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
    }

}
