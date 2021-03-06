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

package de.dante.extex.interpreter.primitives.typesetter.box;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.group.GroupType;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.MissingLeftBraceException;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\hbox</code>.
 *
 * <doc name="hbox">
 * <h3>The Primitive <tt>\hbox</tt></h3>
 * <p>
 *  The primitive <tt>\hbox</tt> takes arguments enclosed in braces and
 *  typesets this contents in horizontal mode. If a width is given then the
 *  horizontal box is spread to this width. Otherwise the horizontal box has
 *  the natural width of its contents.
 * </p>
 * <p>
 *  The contents of the tokens register <tt>\everyhbox</tt> is inserted at the
 *  beginning of the horizontal material of the box.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;hbox&rang;
 *      &rarr; <tt>\hbox</tt> &lang;box specification&rang; <tt>{</tt> &lang;horizontal material&rang; <tt>}</tt>
 *
 *    &lang;box specification&rang;
 *      &rarr;
 *       |  <tt>to</tt> &lang;rule dimension&rang;
 *       |  <tt>spread</tt> &lang;rule dimension&rang;  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \hbox{abc}  </pre>
 *  <pre class="TeXSample">
 *    \hbox to 120pt{abc}  </pre>
 *  <pre class="TeXSample">
 *    \hbox spread 12pt{abc}  </pre>
 *
 * </doc>
 *
 *
 * <doc type="parameter" name="everyhbox">
 * <h3>The Tokens Parameter <tt>\everyhbox</tt></h3>
 * <p>
 *  The tokens parameter is used in <tt>/hbox</tt>. The tokens contained are
 *  inserted at the beginning of the horizontal material of the horizontal box.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;everyhbox&rang;
 *      &rarr; <tt>\everyhbox</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \everyhbox{\message{Hi there}}  </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.23 $
 */
public class Hbox extends AbstractBoxPrimitive {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Hbox(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.box.Boxable#getBox(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Token startToken = source.getLastToken();
        Box box;
        try {
            if (source.getKeyword(context, "to")) {
                Dimen d = Dimen.parse(context, source, typesetter);
                box = acquireBox(context, source, typesetter, startToken,
                        GroupType.ADJUSTED_HBOX_GROUP);
                box.setWidth(d);
            } else if (source.getKeyword(context, "spread")) {
                Dimen d = Dimen.parse(context, source, typesetter);
                box = acquireBox(context, source, typesetter, startToken,
                        GroupType.ADJUSTED_HBOX_GROUP);
                box.spreadWidth(d);
            } else {
                box = acquireBox(context, source, typesetter, startToken,
                        GroupType.HBOX_GROUP);
            }
        } catch (EofException e) {
            throw new EofException(printableControlSequence(context));
        } catch (MissingLeftBraceException e) {
            throw new MissingLeftBraceException(
                    printableControlSequence(context));
        }
        return box;
    }

    /**
     * Acquire a complete Box taking into account the tokens in
     * <tt>\afterassignment</tt> and <tt>\everyhbox</tt>.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param startToken the token which started the group
     *
     * @return the complete Box
     *
     * @throws InterpreterException in case of an error
     */
    private Box acquireBox(final Context context, final TokenSource source,
            final Typesetter typesetter, final Token startToken,
            final GroupType groupType) throws InterpreterException {

        Tokens toks = context.getToks("everyhbox");
        Token t = context.getAfterassignment();
        Tokens insert;

        if (t == null) {
            insert = toks;
        } else {
            insert = new Tokens(t);
            if (toks != null) {
                insert.add(toks);
            }
        }

        return new Box(context, source, typesetter, true, insert, groupType,
                startToken);
    }

}
