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

package de.dante.extex.interpreter.primitives.font;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.DimenConvertible;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\fontcharwd</code>.
 *
 * <doc name="fontcharwd">
 * <h3>The Primitive <tt>\fontcharwd</tt></h3>
 * <p>
 *  The primitive <tt>\fontcharwd</tt> is a read-only dimen register which
 *  corresponds to the width of a character in a given font. If the character
 *  is not defined in the font then 0pt is returned.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;fontcharwd&rang;
 *       &rarr; <tt>\fontcharwd</tt> {@linkplain
 *          de.dante.extex.interpreter.TokenSource#getFont(Context,String)
 *          &lang;font&rang;} {@linkplain
 *          de.dante.extex.interpreter.TokenSource#scanCharacterCode(Context,Typesetter,String)
 *          &lang;character code&rang;} </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \fontcharwd\tenrm `a  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.18 $
 */
public class Fontcharwd extends AbstractCode
        implements
            ExpandableCode,
            CountConvertible,
            DimenConvertible,
            Theable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Fontcharwd(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return convertDimen(context, source, typesetter);
    }

    /**
     * @see de.dante.extex.interpreter.type.dimen.DimenConvertible#convertDimen(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertDimen(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return get(context, source, typesetter).getValue();
    }

    /**
     * @see de.dante.extex.interpreter.type.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        source.push(the(context, source, typesetter));
    }

    /**
     * Get the dimen value of the width. If the character is not defined in the
     * font then ZERO_PT is returned.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the dimen value of the width
     *
     * @throws InterpreterException in case of an error
     */
    private FixedDimen get(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Font fnt = source.getFont(context, getName());
        try {
            UnicodeChar uc = source
                    .scanCharacterCode(context, typesetter, null);
            FixedGlue d = fnt.getWidth(uc);
            return (d != null ? d.getLength() : Dimen.ZERO_PT);

        } catch (EofException e) {
            throw new EofException(printableControlSequence(context));
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        try {
            FixedDimen width = get(context, source, typesetter);
            return width.toToks(context.getTokenFactory());
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

}
