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

package de.dante.extex.interpreter.primitives.hyphen;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.MissingLeftBraceException;
import de.dante.extex.interpreter.primitives.register.CharCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.language.Language;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.LetterToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.RightBraceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.unicode.Unicode;
import de.dante.util.UnicodeChar;
import de.dante.util.UnicodeCharList;

/**
 * This class provides an implementation for the primitive
 * <code>\hyphenation</code>.
 *
 * <doc name="hyphenation">
 * <h3>The Primitive <tt>\hyphenation</tt></h3>
 * <p>
 *  The primitive <tt>\hyphenation</tt> can be used to add hyphenation
 *  exceptions to the current language. The argument is a list of whitespace
 *  separated words enclosed in braces. The hyphenation points are indicated
 *  by including a hyphen character (-) at the appropriate places.
 * </p>
 * <p>
 *  When paragraph breaking needs to insert additional break points these
 *  hyphenation points are translated into discretionaries. The exceptions
 *  specified with the primitive <tt>\hyphenation</tt> have precedence before
 *  the hyphenation points found with the help of hyphenation patterns.
 * </p>
 * <p>
 *  One example which make use of this precedence is the hyphenation
 *  exception without any hyphen characters. This can be used to suppress any
 *  hyphenation in a single word.
 * </p>
 *
 * <h4>Syntax</h4>
 *  <pre class="syntax">
 *    &lang;hyphenation&rang;
 *     &rarr; <tt>\hyphenation</tt> {...} </pre>
 *
 * <h4>Example:</h4>
 *  <pre class="TeXSample">
 *   \hyphenation{as-so-ciate as-so-ciates}  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class HyphenationPrimitive extends AbstractHyphenationCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060228L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public HyphenationPrimitive(final String name) {

        super(name);
    }

    /**
     * Collect all characters that make up a word.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param token the first token already read
     *
     * @return the first character not included into the word
     *
     * @throws InterpreterException in case of an error
     * @throws CatcodeException in case of an exception in token creation
     */
    protected UnicodeCharList collectWord(final Context context,
            final TokenSource source, final Token token)
            throws InterpreterException,
                CatcodeException {

        UnicodeCharList word = new UnicodeCharList();
        UnicodeChar uc, lc;

        for (Token t = token; t != null; t = source.getToken(context)) {

            if (t instanceof LetterToken || t instanceof OtherToken) {
                uc = t.getChar();
            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                uc = ((CharCode) code).getCharacter();
            } else {
                source.push(t);
                return word;
            }

            if (t.equals(Catcode.OTHER, '-')) {
                word.add(Unicode.SHY);
            } else {
                uc = t.getChar();
                lc = context.getLccode(uc);
                word.add(lc == null ? uc : lc);
            }
        }

        return word;
    }

    /**
     * Scan for hyphenation values and store these values in the
     * <code>HyphernationTable</code>.
     * The index for the <code>HyphernationTable</code> is based on the
     * value from the count register <code>\language</code>.
     *
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Language table = getHyphenationTable(context);
        Token t = source.getNonSpace(context);
        if (!(t instanceof LeftBraceToken)) {
            throw new MissingLeftBraceException(
                    printableControlSequence(context));
        }

        try {
            UnicodeCharList word;

            for (t = source.getNonSpace(context); //
            !(t instanceof RightBraceToken); //
            t = source.getNonSpace(context)) {

                if (!isWordConstituent(t, context)) {
                    throw new InterpreterException(getLocalizer().format(
                            "TTP.ImproperHyphen",
                            printableControlSequence(context)));
                }
                word = collectWord(context, source, t);
                table.addHyphenation(word, (TypesetterOptions) context);
            }
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        } catch (HyphenationException e) {
            throw new InterpreterException(e);
        }

    }

    /**
     * This method checks that the given token is a word constituent.
     * This means that the token is either
     * <ul>
     * <li>a letter token, or</li>
     * <li>a other token, or</li>
     * <li>a code token defined with <tt>\chardef</tt>.</li>
     * </ul>
     *
     * @param t the token to analyze
     * @param context the interpreter context
     *
     * @return <code>true</code> iff the token is
     *
     * @throws InterpreterException in case of an error
     */
    protected boolean isWordConstituent(final Token t, final Context context)
            throws InterpreterException {

        if (t == null) {
            return false;
        } else if (t instanceof LetterToken || t instanceof OtherToken) {
            return true;
        } else if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            return (code instanceof CharCode);
        }

        return false;
    }

}
