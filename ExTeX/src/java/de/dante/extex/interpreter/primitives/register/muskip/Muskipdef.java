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

package de.dante.extex.interpreter.primitives.register.muskip;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\muskipdef</code>.
 *
 * <doc name="muskipdef">
 * <h3>The Primitive <tt>\muskipdef</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\muskipdef</tt> {@linkplain
 *    de.dante.extex.interpreter.TokenSource#getControlSequence(Context)
 *    &lang;control sequence&rang;} {@linkplain
 *    de.dante.extex.interpreter.TokenSource#getOptionalEquals()
 *    &lang;equals&rang;} {@linkplain
 *      de.dante.extex.interpreter.TokenSource#scanNumber()
 *      &lang;8-bit&nbsp;number&rang;} </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \muskipdef\abc=45  </pre>
 *  <pre class="TeXSample">
 *    \muskipdef\abc 33  </pre>
 * </p>
 * </doc>
 *
 *
 * <h3>Possible Extension</h3>
 * Allow an expandable expression instead of the number to define real named
 * counters.
 *
 * <p>Example</p>
 * <pre>
 * \muskipdef\abc={xyz\the\count0}
 * \muskipdef\abc {def}
 * </pre>
 * To protect the buildin registers one might consider to use the key
 * "#<i>name</i>" or "muskip#<i>name</i>".
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class Muskipdef extends AbstractMuskip {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Muskipdef(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractAssignment#assign(
     *     de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        CodeToken cs = source.getControlSequence(context);
        source.getOptionalEquals(context);
        String key = getKey(source, context);
        context.setCode(cs, new MuskipParameter(key), prefix.isGlobal());
    }

}
