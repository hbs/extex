/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.string;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.ExpandableCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\string</code>.
 *
 * <doc name="string">
 * <h3>The Primitive <tt>\string</tt></h3>
 * <p>
 *  This primitive takes the next unexpanded token. If this token is a control
 *  sequence -- and no active character -- then the value of <tt>escapechar</tt>
 *  followed by the characters from the name of the control sequence.
 *  Otherwise it is a single character token containing the character code of
 *  the token.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\string</tt> &lang;token&rang; </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \string ...  </pre>
 * </p>
 * </doc>
 *
 *
 * @see "TeX -- the Program [69]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class StringPrimitive extends AbstractCode implements ExpandableCode {

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public StringPrimitive(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        expand(prefix, context, source, typesetter);
        return true;
    }

    /**
     * @see de.dante.extex.interpreter.ExpandableCode#expand(
     *     de.dante.extex.interpreter.Flags,
     *     de.dante.extex.interpreter.context.Context,
     *     de.dante.extex.interpreter.TokenSource,
     *     de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Token t = source.getToken();
        if (t instanceof ControlSequenceToken) {
            char esc = (char) (context.getCount("escapechar").getValue());
            source.push(new Tokens(context, Character.toString(esc)
                                            + t.getValue()));
        } else {
            source.push(new Tokens(context, t.getValue()));
        }
    }
}
