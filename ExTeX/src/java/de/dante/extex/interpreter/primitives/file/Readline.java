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

package de.dante.extex.interpreter.primitives.file;

import java.util.logging.Logger;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.interaction.Interaction;
import de.dante.extex.interpreter.primitives.macro.util.MacroCode;
import de.dante.extex.interpreter.primitives.macro.util.MacroPattern;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.file.InFile;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive <code>\readline</code>.
 *
 * <doc name="readline">
 * <h3>The Primitive <tt>\readline</tt></h3>
 * <p>
 *  The primitive <tt>\readline</tt> read characters from an input stream until
 *  the end of line is encountered. The characters are translated to tokens
 *  with the category code OTHER except the white-space characters which receive
 *  the category code SPACE. This mapping is performed ignoring the setting of
 *  {@link de.dante.extex.interpreter.primitives.register.CatcodePrimitive <tt>\catcode</tt>}.
 *  The resulting token list is bound to the control sequence given.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;readline&rang;
 *      &rarr; &lang;optional prefix&rang; <tt>\readline</tt> {@linkplain
 *        de.dante.extex.interpreter.primitives.file.AbstractFileCode#scanInFileKey(Context,TokenSource,Typesetter)
 *        &lang;infile&nbsp;name&rang;} <tt>to</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getControlSequence(Context)
 *        &lang;control sequence&rang;}
 *
 *    &lang;optional prefix&rang;
 *      &rarr;
 *       |  <tt>\global</tt> &lang;optional prefix&rang; </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 * \openin3= abc.def
 * \readline3 to \line
 * \closein3 </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
 */
public class Readline extends AbstractAssignment implements LogEnabled {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060411L;

    /**
     * The field <tt>TOKENIZER</tt> contains the tokenizer to use for this
     * primitive.
     */
    private static final Tokenizer TOKENIZER = new Tokenizer() {

        /**
         * @see de.dante.extex.interpreter.Tokenizer#getCatcode(
         *      de.dante.util.UnicodeChar)
         */
        public Catcode getCatcode(final UnicodeChar c) {

            return (c.getCodePoint() == ' ' ? Catcode.SPACE : Catcode.OTHER);
        }

        /**
         * @see de.dante.extex.interpreter.Tokenizer#getNamespace()
         */
        public String getNamespace() {

            return Namespace.DEFAULT_NAMESPACE;
        }

    };

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private transient Logger logger = null;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Readline(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractAssignment#assign(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        String key = AbstractFileCode
                .scanInFileKey(context, source, typesetter);

        if (!source.getKeyword(context, "to")) {
            throw new HelpingException(getLocalizer(), "TTP.MissingToForRead",
                    printableControlSequence(context));
        }
        CodeToken cs = source.getControlSequence(context);

        InFile file = context.getInFile(key);

        if (!file.isOpen()) {
            file = context.getInFile(null);
            if (!file.isOpen()) {
                throw new HelpingException(getLocalizer(), "TTP.EOFinRead",
                        printableControlSequence(context));
            }
        }
        if (!file.isFileStream()) {
            Interaction interaction = context.getInteraction();
            if (interaction != Interaction.ERRORSTOPMODE) {
                throw new HelpingException(getLocalizer(), "TTP.NoTermRead",
                        printableControlSequence(context));
            }
        }

        if (file.isStandardStream()) {
            logger.severe(printable(context, cs) + "=");
        }

        Tokens toks = file.read(context.getTokenFactory(), TOKENIZER);

        if (toks == null) {
            throw new HelpingException(getLocalizer(), "TTP.EOFinRead");
        }

        context.setCode(cs, new MacroCode(cs.getName(), prefix,
                MacroPattern.EMPTY, toks), prefix.clearGlobal());
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

}
