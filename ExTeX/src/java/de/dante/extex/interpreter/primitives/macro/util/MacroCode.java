/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.macro.util;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.primitives.typesetter.paragraph.Par;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ComparableCode;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.PrefixCode;
import de.dante.extex.interpreter.type.Showable;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.MacroParamToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.RightBraceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides an implementation for any macro code bound to a
 * control sequence or active character.
 *
 * <doc name="macros" type="howto">
 * <h3>The Macro Code</h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class MacroCode extends AbstractCode
        implements
            PrefixCode,
            ExpandableCode,
            ComparableCode,
            Showable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060405L;

    /**
     * The field <tt>body</tt> contains the tokens of the macro expansion text.
     */
    private Tokens body;

    /**
     * The field <tt>notLong</tt> contains the negated <tt>\long</tt> flag.
     * This field indicates that no macros <tt>\par</tt> are allowed in macro
     * parameter values.
     */
    private boolean notLong;

    /**
     * The field <tt>outerP</tt> contains the indicator for outer definitions.
     */
    private boolean outerP;

    /**
     * The field <tt>pattern</tt> contains the specification for the argument
     * matching.
     */
    private MacroPattern pattern;

    /**
     * Creates a new object.
     *
     * @param name the initial name of the macro
     * @param flags the flags controlling the behavior of the macro
     * @param thePattern the pattern for the acquiring of the arguments
     * @param theBody the expansion text
     */
    public MacroCode(final String name, final Flags flags,
            final MacroPattern thePattern, final Tokens theBody) {

        super(name);
        this.body = theBody;
        this.pattern = thePattern;
        this.notLong = !flags.isLong();
        this.outerP = flags.isOuter();
    }

    /**
     * @see de.dante.extex.interpreter.type.ComparableCode#compare(
     *      de.dante.extex.scanner.type.token.Token,
     *      de.dante.extex.interpreter.context.Context)
     */
    public boolean compare(final Token token, final Context context)
            throws InterpreterException {

        if (!(token instanceof CodeToken)) {
            return false;
        }

        Code code = context.getCode((CodeToken) token);

        if (!(code instanceof MacroCode)) {
            return false;
        }
        MacroCode macro = (MacroCode) code;
        if (notLong != macro.notLong //
                || outerP != macro.outerP //
                || !pattern.equals(macro.pattern)) {
            return false;
        }
        return body.equals(macro.body);
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

        Tokens[] args = matchPattern(context, source, typesetter);
        Tokens toks = new Tokens();
        int len = body.length();
        int no = 1;

        for (int i = 0; i < len; i++) {
            Token t = body.get(i);

            if (t instanceof MacroParamToken) {
                t = body.get(++i);
                if (t == null) {
                    throw new HelpingException(getLocalizer(),
                            "TTP.EOFinMatch", printableControlSequence(context));
                } else if (t instanceof MacroParamToken) {
                    toks.add(t);
                } else if (t instanceof OtherToken && t.getChar().isDigit()) {
                    no = t.getChar().getCodePoint() - '0';
                    if (args[no] == null) {
                        throw new ImpossibleException("MacroCode:NullArg");
                    }
                    toks.add(args[no]);
                } else {
                    throw new ImpossibleException("MacroCode:IllegalArg");
                }
            } else {
                toks.add(t);
            }
        }

        source.push(toks);
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

        execute(prefix, context, source, typesetter);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractCode#getLocalizer()
     */
    protected Localizer getLocalizer() {

        Localizer localizer = super.getLocalizer();
        return (localizer != null ? localizer : LocalizerFactory
                .getLocalizer(MacroCode.class));
    }

    /**
     * Get a single token or a block if the first token is a LeftBraceToken.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the tokens accumulated
     *
     * @throws InterpreterException in case of an error
     */
    private Tokens getTokenOrBlock(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Token t = source.getToken(context);

        if (t == null) {
            throw new HelpingException(getLocalizer(), "TTP.EOFinMatch",
                    printableControlSequence(context));
        } else if (t instanceof LeftBraceToken) {
            source.push(t);
            Tokens toks;
            try {
                toks = source.getTokens(context, source, typesetter);
            } catch (EofException e) {
                throw new HelpingException(getLocalizer(), "TTP.EOFinMatch",
                        printableControlSequence(context));
            }
            return toks;
        }

        return new Tokens(t);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#isOuter()
     */
    public boolean isOuter() {

        return outerP;
    }

    /**
     * Match a single parameter.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param args the array of Tokens to fill
     * @param len the length of the patterns
     * @param i the starting index
     *
     * @return the index of the character after the parameter
     *
     * @throws InterpreterException in case of an error
     */
    private int matchParameter(final Context context, final TokenSource source,
            final Typesetter typesetter, final Tokens[] args, final int len,
            final int i) throws InterpreterException {

        Token t;

        if (i >= len) {
            t = source.getToken(context);
            source.push(t);

            if (t instanceof LeftBraceToken) {
                return i;
            }
            throw new HelpingException(getLocalizer(), "TTP.UseDoesntMatch",
                    printableControlSequence(context));
        }
        Token ti = pattern.get(i);
        if (ti instanceof MacroParamToken) {
            t = source.getToken(context);
            if (!ti.equals(t)) {
                throw new HelpingException(getLocalizer(),
                        "TTP.UseDoesntMatch", printableControlSequence(context));
            }
            return i;
        } else if (ti instanceof OtherToken && ti.getChar().isDigit()) {
            int no = ti.getChar().getCodePoint() - '0';
            int i1 = i + 1;
            if (i1 < len && !(pattern.get(i1) instanceof MacroParamToken)) {
                //TODO gene: #1##
                args[no] = scanTo(context, source, pattern.get(i1));
                return i + 2;
            }
            args[no] = getTokenOrBlock(context, source, typesetter);
            return i1;
        }

        throw new HelpingException(getLocalizer(), "TTP.UseDoesntMatch",
                printableControlSequence(context));
    }

    /**
     * Match the pattern of this macro with the next tokens from the token
     * source. As a result the matching arguments are stored in an array of
     * {@link de.dante.extex.interpreter.type.tokens.Tokens Tokens}. This array
     * is returned.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return a new array of parameters which have been found during the
     *         matching. Note that some of th elements of the array might be
     *         <code>null</code>.
     *
     * @throws InterpreterException in case of an error during the matching
     */
    private Tokens[] matchPattern(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Tokens[] args = new Tokens[pattern.getArity()];
        Token ti;
        int len = pattern.length();
        int i = 0;

        while (i < len) {
            ti = pattern.get(i++);
            if (ti instanceof MacroParamToken) {
                i = matchParameter(context, source, typesetter, args, len, i);
            } else {
                Token t = source.getToken(context);
                if (!ti.equals(t)) {
                    throw new HelpingException(getLocalizer(),
                            "TTP.UseDoesntMatch",
                            printableControlSequence(context));
                } else if (notLong && t.equals(Catcode.ESCAPE, "par")) {
                    throw new HelpingException(getLocalizer(),
                            "TTP.RunawayArg", printableControlSequence(context));
                }
            }
        }

        return args;
    }

    /**
     * Collect all tokens until a given token is found.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param to the terminating token
     *
     * @return the tokens accumulated in between
     *
     * @throws InterpreterException in case of an error
     */
    private Tokens scanTo(final Context context, final TokenSource source,
            final Token to) throws InterpreterException {

        Tokens toks = new Tokens();
        int depth = 0;

        for (Token t = source.getToken(context); t != null; t = source
                .getToken(context)) {
            if (depth == 0 && t.equals(to)) {
                return toks;
            } else if (t instanceof LeftBraceToken) {
                depth++;
            } else if (t instanceof RightBraceToken) {
                depth--;
                if (depth < 0) {
                    throw new HelpingException(getLocalizer(),
                            "TTP.ExtraRightBrace",
                            printableControlSequence(context));
                }
            } else if (notLong && t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code instanceof Par) {
                    throw new HelpingException(getLocalizer(),
                            "TTP.RunawayArg", printableControlSequence(context));
                }
            }
            toks.add(t);
        }

        throw new HelpingException(getLocalizer(), "TTP.EOFinMatch",
                printableControlSequence(context));
    }

    /**
     * @see de.dante.extex.interpreter.type.Showable#show(
     *      de.dante.extex.interpreter.context.Context)
     */
    public Tokens show(final Context context) throws InterpreterException {

        try {
            StringBuffer sb = new StringBuffer();
            boolean sep = false;
            if (!notLong) {
                sb.append(context.esc("long"));
                sep = true;
            }
            if (outerP) {
                sb.append(context.esc("outer"));
                sep = true;
            }
            if (sep) {
                sb.append(" ");
            }
            sb.append(getLocalizer().format("TTP.macro"));
            sb.append(":\n");
            Tokens toks = new Tokens(context, sb);
            pattern.show(context, toks);
            toks.add(context.getTokenFactory(), "->");
            body.show(context, toks);
            return toks;
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

}
