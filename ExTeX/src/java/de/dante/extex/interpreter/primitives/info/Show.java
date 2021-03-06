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

package de.dante.extex.interpreter.primitives.info;

import java.util.logging.Logger;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.Showable;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.ControlSequenceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive <code>\show</code>.
 *
 * <doc name="show">
 * <h3>The Primitive <tt>\show</tt></h3>
 * <p>
 *  The primitive <tt>\show</tt> consumes the following token and prints the
 *  definition of the token to the output stream an into the log file.
 * </p>
 * <ul>
 *  <li>If the token is a control sequence or active character and it is
 *   undefined then it is reported as <i>undefined</i>.
 *  </li>
 *  <li>If the token is a control sequence or active character and it is
 *   a primitive then it is reported with the original name of the primitive.
 *   This applies even if is redefined with <tt>\let</tt> to another name.
 *  </li>
 *  <li>If the token is a control sequence or active character and it is
 *   a macro then it is reported with the pattern and expansion text.
 *  </li>
 *  <li>Otherwise the long descriptive form of the token is reported.</li>
 * </ul>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;show&rang;
 *       &rarr; <tt>\show</tt> {@linkplain
 *           de.dante.extex.interpreter.TokenSource#getToken(Context)
 *           &lang;token&rang;} </pre>
 *
 * <h4>Examples</h4>
 *  Examples:
 *  <pre class="TeXSample">
 *    \show\abc
 *    > \abc=undefined
 *  </pre>
 *  <pre class="TeXSample">
 *    \show \def
 *    > \def=\def.
 *  </pre>
 *  <pre class="TeXSample">
 *    \let\xxx=\def\show \xxx
 *    > \xxx=\def.
 *  </pre>
 *  <pre class="TeXSample">
 *    \def\m{abc}\show \m
 *    > \m=macro:
 *    ->abc.
 *  </pre>
 *  <pre class="TeXSample">
 *    \show a
 *    > the letter a.
 *  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.29 $
 */
public class Show extends AbstractCode implements LogEnabled {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private transient Logger logger = null;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Show(final String name) {

        super(name);
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Token t = source.getToken(context);
        if (t == null) {
            throw new EofException(printableControlSequence(context));
        }
        logger.info("\n> " + meaning(t, context).toText() + ".\n");
    }

    /**
     * Get the descriptions of a token as token list.
     *
     * @param t the token to describe
     * @param context the interpreter context
     *
     * @return the token list describing the token
     *
     * @throws InterpreterException in case of an error
     */
    protected Tokens meaning(final Token t, final Context context)
            throws InterpreterException {

        if (!(t instanceof CodeToken)) {
            return new Tokens(context, t.toString());
        }
        Tokens toks;

        if (t instanceof ControlSequenceToken) {

            toks = new Tokens(context, context.esc(t));

        } else {
            try {
                Token token = context.getTokenFactory()
                        .createToken(Catcode.OTHER, t.getChar(),
                                Namespace.DEFAULT_NAMESPACE);
                toks = new Tokens(token);
            } catch (CatcodeException e) {
                throw new InterpreterException(e);
            }
        }

        toks.add(new Tokens(context, "="));
        Code code = context.getCode((CodeToken) t);
        if (code == null) {

            toks.add(new Tokens(context, //
                    getLocalizer().format("TTP.Undefined")));

        } else if ((code instanceof Showable)) {

            toks.add(((Showable) code).show(context));

        } else {

            toks.add(new Tokens(context, context.esc(code.getName())));

//        } else {
//
//            toks.add(new Tokens(context, t.getChar().getCodePoint()));
        }
        return toks;
    }

}
