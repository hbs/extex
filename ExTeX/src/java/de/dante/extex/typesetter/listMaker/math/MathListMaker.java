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

package de.dante.extex.typesetter.listMaker.math;

import java.util.Stack;
import java.util.logging.Logger;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.group.GroupType;
import de.dante.extex.interpreter.context.tc.TypesettingContext;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingMathException;
import de.dante.extex.interpreter.primitives.register.font.NumberedFont;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.math.MathCode;
import de.dante.extex.interpreter.type.math.MathDelimiter;
import de.dante.extex.interpreter.type.muskip.Mudimen;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.ActiveCharacterToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.exception.TypesetterHelpingException;
import de.dante.extex.typesetter.listMaker.HorizontalListMaker;
import de.dante.extex.typesetter.listMaker.ListManager;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.FractionNoad;
import de.dante.extex.typesetter.type.noad.GlueNoad;
import de.dante.extex.typesetter.type.noad.KernNoad;
import de.dante.extex.typesetter.type.noad.LeftNoad;
import de.dante.extex.typesetter.type.noad.MathGlyph;
import de.dante.extex.typesetter.type.noad.MathList;
import de.dante.extex.typesetter.type.noad.MiddleNoad;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.extex.typesetter.type.noad.NoadFactory;
import de.dante.extex.typesetter.type.noad.NodeNoad;
import de.dante.extex.typesetter.type.noad.RightNoad;
import de.dante.extex.typesetter.type.noad.StyleNoad;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.node.AfterMathNode;
import de.dante.extex.typesetter.type.node.BeforeMathNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.GenericNodeList;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This is the list maker for the inline math formulae.
 *
 *
 * <doc name="mathsurround" type="register">
 * <h3>The Dimen Parameter <tt>\mathsurround</tt></h3>
 * <p>
 *  The dimen parameter <tt>\mathsurround</tt> is used to put some spacing
 *  around mathematical formulae. The value at the end of the formula is used
 *  before and after the formula. This additional space will be discarded
 *  at the end of a line.
 * </p>
 *
 * </doc>
 *
 * <doc name="everymath" type="register">
 * <h3>The Tokens Parameter <tt>\everymath</tt></h3>
 * <p>
 *  The tokens parameter <tt>\everymath</tt> contains a list of tokens which is
 *  inserted at the beginning of inline math. Those tokens take effect after the
 *  math mode has been entered but before any tokens given explicitly.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;everymath&rang;
 *      &rarr; <tt>\everymath</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \everymath={\,}  </pre>
 *
 * </doc>
 *
 * <doc name="everymathend" type="register">
 * <h3>The Tokens Parameter <tt>\everymathend</tt></h3>
 * <p>
 *  The tokens parameter <tt>\everymathend</tt> contains a list of tokens which
 *  is inserted at the end of inline math. Those tokens take effect just before
 *  the math mode is ended but after any tokens given explicitly.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;everymathend&rang;
 *      &rarr; <tt>\everymathend</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \everymathend={\,}  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.54 $
 */
public class MathListMaker extends HorizontalListMaker
        implements
            NoadConsumer,
            LogEnabled {

    /**
     * This inner class is a memento of the state of the math list maker.
     * It is used to store to the stack and restore the state from the stack.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.54 $
     */
    private class MathMemento {

        /**
         * The field <tt>block</tt> contains the indicator that this memento
         * corresponds to a block. Otherwise it corresponds to a \left-\right
         * pair.
         */
        private boolean block;

        /**
         * The field <tt>delimiter</tt> contains the left delimiter or
         * <code>null</code> for a block.
         */
        private MathDelimiter delimiter;

        /**
         * The field <tt>ip</tt> contains the insertion point.
         */
        private MathList ip;

        /**
         * The field <tt>noads</tt> contains the noads.
         */
        private Noad noads;

        /**
         * Creates a new object.
         *
         * @param ip the insertion point to be saved in this memento
         * @param noads the noads to be saved in this memento
         * @param block indicator to distinguish blocks from \left-\right
         *  constructs. a Value of <code>true</code> indicates a block.
         */
        public MathMemento(final MathList ip, final Noad noads,
                final boolean block) {

            super();
            this.ip = ip;
            this.noads = noads;
            this.block = block;
        }

        /**
         * Getter for the insertion point.
         *
         * @return the insertion point
         */
        public MathList getInsertionPoint() {

            return this.ip;
        }

        /**
         * Getter for noads.
         *
         * @return the noads
         */
        public Noad getNoads() {

            return this.noads;
        }

        /**
         * Getter for block indicator.
         *
         * @return the block
         */
        protected boolean isBlock() {

            return this.block;
        }
    }

    /**
     * The constant <tt>noadFactory</tt> contains the noad factory.
     */
    private static final NoadFactory NOAD_FACTORY = new NoadFactory();

    /**
     * This method checks that extension fonts have sufficient font dimen
     * values set.
     *
     * @param options the options
     *
     * @return <code>true</code> iff the needed font dimens are not present
     *
     * @see "[TTP 1195]"
     */
    protected static boolean insufficientExtensionFonts(
            final TypesetterOptions options) {

        Font textfont3 = options.getFont(NumberedFont.key(options, "textfont",
                "3"));
        if (textfont3.getFontDimen("8") == null) {
            return true;
        }

        Font scriptfont3 = options.getFont(NumberedFont.key(options,
                "scriptfont", "3"));
        if (scriptfont3.getFontDimen("8") == null) {
            return true;
        }

        Font scriptscriptfont3 = options.getFont(NumberedFont.key(options,
                "scriptscriptfont", "3"));
        if (scriptscriptfont3.getFontDimen("8") == null) {
            return true;
        }
        return false;
    }

    /**
     * This method checks that symbol fonts have sufficient font dimen
     * values set. In fact only the fonts itself are checked. the font dimens
     * are checked when needed.
     *
     * @param options the options
     *
     * @return <code>true</code> iff the symbol fonts have the needed font
     *  dimens
     *
     * @see "[TTP 1195]"
     */
    protected static boolean insufficientSymbolFonts(
            final TypesetterOptions options) {

        Font textfont2 = options.getFont(NumberedFont.key(options, "textfont",
                "2"));
        if (textfont2.getFontDimen("8") == null) {
            return true;
        }
        Font scriptfont2 = options.getFont(NumberedFont.key(options,
                "scriptfont", "2"));
        if (scriptfont2.getFontDimen("8") == null) {
            return true;
        }
        Font scriptscriptfont2 = options.getFont(NumberedFont.key(options,
                "scriptscriptfont", "2"));
        if (scriptscriptfont2.getFontDimen("8") == null) {
            return true;
        }
        return false;
    }

    /**
     * The field <tt>closing</tt> contains the indicator that this list maker is
     * in the mode of processing the terminal tokens.
     */
    private boolean closing = false;

    /**
     * The field <tt>insertionPoint</tt> contains the the MathList to which
     * the next noads should be added.
     */
    private MathList insertionPoint;

    /**
     * The field <tt>logger</tt> contains the logger.
     */
    private Logger logger = null;

    /**
     * The field <tt>nodes</tt> contains the list of nodes encapsulated in this
     * instance.
     */
    private Noad noads;

    /**
     * The field <tt>stack</tt> contains the stack for parsing sub-formulae.
     */
    private Stack stack = new Stack();

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     * @param locator the locator
     */
    public MathListMaker(final ListManager manager, final Locator locator) {

        super(manager, locator);
        insertionPoint = new MathList();
        noads = insertionPoint;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.interpreter.type.glue.FixedGlue)
     */
    public void add(final FixedDimen g) throws TypesetterException {

        insertionPoint.add(new NodeNoad(new GlueNode(g, true)));
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#add(
     *      de.dante.extex.interpreter.type.math.MathCode,
     *      de.dante.extex.interpreter.context.tc.TypesettingContext)
     */
    public void add(final MathCode mc, final TypesettingContext tc)
            throws TypesetterException {

        insertionPoint.add(NOAD_FACTORY.getNoad(mc, tc));
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#add(
     *      de.dante.extex.typesetter.type.math.MathDelimiter,
     *      de.dante.extex.interpreter.context.TypesettingContext)
     */
    public void add(final MathDelimiter delimiter, final TypesettingContext tc)
            throws TypesetterException {

        MathGlyph smallChar = delimiter.getSmallChar(); // TODO: gene why???
        insertionPoint.add(NOAD_FACTORY.getNoad(delimiter.getMathClass(),
                smallChar, tc));
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#add(
     *      de.dante.extex.interpreter.type.muskip.Mudimen)
     */
    public void add(final Mudimen skip) throws TypesetterException {

        insertionPoint.add(new KernNoad(skip));
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#add(
     *      de.dante.extex.interpreter.type.muskip.Muskip)
     */
    public void add(final Muskip glue) throws TypesetterException {

        insertionPoint.add(new GlueNoad(glue));
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#add(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void add(final Noad noad) throws TypesetterException {

        insertionPoint.add(noad);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.typesetter.type.Node)
     */
    public void add(final Node node)
            throws TypesetterException,
                ConfigurationException {

        if (node instanceof DiscretionaryNode) {
            NodeList postBreak = ((DiscretionaryNode) node).getPostBreak();
            if (postBreak != null && postBreak.size() != 0) {
                throw new TypesetterException(new HelpingException(
                        getLocalizer(), "TTP.IllegalMathDisc", postBreak
                                .toString()));
            }
        }

        insertionPoint.add(new NodeNoad(node));
    }

    /**
     * Spaces are ignored in math mode. Thus this method is a noop.
     *
     * @param typesettingContext the typesetting context for the space
     * @param spacefactor the space factor to use for this space or
     *  <code>null</code> to indicate that the default space factor should
     *  be used.
     *
     * @throws TypesetterException in case of an error
     * @throws ConfigurationException in case of a configuration error
     *
     * @see de.dante.extex.typesetter.ListMaker#addSpace(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.interpreter.type.count.Count)
     */
    public void addSpace(final TypesettingContext typesettingContext,
            final Count spacefactor)
            throws TypesetterException,
                ConfigurationException {

    }

    /**
     * Close the node list.
     * In the course of the closing, the Noad list is translated into a Node
     * list.
     *
     * @param context the fragment of the context accessible for the typesetter
     *
     * @return the node list enclosed in this instance
     *
     * @throws TypesetterException in case of an error
     * @throws ConfigurationException in case of a configuration error
     *
     * @see de.dante.extex.typesetter.ListMaker#complete(TypesetterOptions)
     * @see "<logo>TeX</logo> &ndash; The Program [719]"
     */
    public NodeList complete(final TypesetterOptions context)
            throws TypesetterException,
                ConfigurationException {

        if (!stack.empty()) {
            MathMemento mm = (MathMemento) stack.pop();
            throw new TypesetterException(new HelpingException(getLocalizer(),
                    "TTP.MissingInserted", //
                    (mm.isBlock() ? "}" : "\\right.")));
        }

        // see [TTP 1195]
        if (insufficientSymbolFonts(context)) {
            throw new TypesetterException(new HelpingException(getLocalizer(),
                    "TTP.InsufficientSymbolFonts"));
        }
        // see [TTP 1195]
        if (insufficientExtensionFonts(context)) {
            throw new TypesetterException(new HelpingException(getLocalizer(),
                    "TTP.InsufficientExtensionFonts"));
        }

        GenericNodeList list = new GenericNodeList();
        final FixedDimen mathsurround = context.getDimenOption("mathsurround");
        // see [TTP 1196]
        list.add(new BeforeMathNode(mathsurround));
        noads.typeset(null, null, 0, list, new MathContext(StyleNoad.TEXTSTYLE,
                context), logger);
        // see [TTP 1196]
        list.add(new AfterMathNode(mathsurround));
        // see [TTP 1196]
        getManager().setSpacefactor(Count.THOUSAND);

        return list;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#cr(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void cr(final Context context, final TypesettingContext tc,
            final UnicodeChar uc) throws TypesetterException {

    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger log) {

        this.logger = log;
    }

    /**
     * Getter for the contents of the insertion point. If the insertion point
     * does not contain an element then <code>null</code> is returned. If it
     * contains only one element then this element is returned. Otherwise the
     * complete list is returned.
     *
     * @return the contents of the insertion point
     */
    protected Noad getInsertionPoint() {

        switch (insertionPoint.size()) {
            case 0:
                return null;
            case 1:
                return insertionPoint.get(0);
            default:
                return insertionPoint;
        }
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#getLastNoad()
     */
    public Noad getLastNoad() throws TypesetterException {

        return insertionPoint.getLastNoad();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getLastNode()
     */
    public Node getLastNode() {

        return null;
    }

    /**
     * Getter for logger.
     *
     * @return the logger
     */
    public Logger getLogger() {

        return this.logger;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getMode()
     */
    public Mode getMode() {

        return Mode.MATH;
    }

    /**
     * Getter for Noads.
     *
     * @return the Noads.
     */
    protected Noad getNoads() {

        return this.noads;
    }

    /**
     * Getter for closing.
     *
     * @return the closing
     */
    protected boolean isClosing() {

        return this.closing;
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#left(
     *      de.dante.extex.typesetter.type.math.MathDelimiter)
     */
    public void left(final MathDelimiter delimiter) throws TypesetterException {

        stack.push(new MathMemento(insertionPoint, noads, false));
        insertionPoint = new MathList();
        noads = new LeftNoad(insertionPoint, delimiter);
    }

    /**
     * Notification method to deal the case that a left brace has been
     * encountered.
     *
     * @see de.dante.extex.typesetter.ListMaker#leftBrace()
     */
    public void leftBrace() {

        stack.push(new MathMemento(insertionPoint, noads, true));
        insertionPoint = new MathList();
        noads = insertionPoint;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#letter(
     *      de.dante.util.UnicodeChar,
     *      de.dante.extex.interpreter.context.tc.TypesettingContext,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.util.Locator)
     */
    public boolean letter(final UnicodeChar symbol,
            final TypesettingContext tc, final Context context,
            final TokenSource source, final Locator locator)
            throws TypesetterException {

        MathCode mcode = context.getMathcode(symbol);

        if (mcode.getMathClass() != null) {

            insertionPoint.add(NOAD_FACTORY.getNoad(mcode, tc));

        } else {
            try {
                ActiveCharacterToken t = (ActiveCharacterToken) context
                        .getTokenFactory().createToken(Catcode.ACTIVE, symbol,
                                Namespace.DEFAULT_NAMESPACE);
                source.push(t);

            } catch (CatcodeException e) {
                throw new TypesetterException(e);
            } catch (InterpreterException e) {
                throw new TypesetterException(e);
            }
        }
        return false;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#mathShift(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void mathShift(final Context context, final TokenSource source,
            final Token t) throws TypesetterException, ConfigurationException {

        if (!closing) {
            Tokens toks = context.getToks("everymathend");
            if (toks != null && toks.length() != 0) {
                try {
                    source.push(t);
                    source.push(toks);
                } catch (InterpreterException e) {
                    throw new TypesetterException(e);
                }
                closing = true;
                return;
            }
        }
        getManager().endParagraph();
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#middle(
     *      de.dante.extex.typesetter.type.math.MathDelimiter)
     */
    public void middle(final MathDelimiter delimiter)
            throws TypesetterException {

        if (stack.empty()) {
            throw new TypesetterHelpingException(getLocalizer(),
                    "TTP.ExtraOrForgotten", "$");
        }
        MathMemento memento = (MathMemento) stack.peek();
        if (memento.isBlock()) {
            throw new TypesetterHelpingException(getLocalizer(),
                    "TTP.ExtraOrForgotten", "\\right.");
        }

        insertionPoint = new MathList();
        noads = new MiddleNoad((LeftNoad) noads, delimiter, insertionPoint);
    }

    /**
     * Emitting a new paragraph is not supported in math mode.
     * Thus an exception is thrown.
     *
     * @throws TypesetterException in case of an error
     * @throws ConfigurationException in case of an configuration error
     *
     * @see de.dante.extex.typesetter.ListMaker#par()
     * @see "<logo>TeX</logo> &ndash; The Program [1047]"
     */
    public void par() throws TypesetterException, ConfigurationException {

        getManager().endParagraph();
        throw new TypesetterException(new MissingMathException("\\par"));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#removeLastNode()
     */
    public void removeLastNode() {

        throw new UnsupportedOperationException();
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#right(
     *      de.dante.extex.typesetter.type.math.MathDelimiter)
     */
    public void right(final MathDelimiter delimiter) throws TypesetterException {

        if (stack.empty()) {
            throw new TypesetterHelpingException(getLocalizer(),
                    "TTP.ExtraOrForgotten", "$");
        }
        MathMemento memento = (MathMemento) stack.pop();
        if (memento.isBlock()) {
            throw new TypesetterHelpingException(getLocalizer(),
                    "TTP.ExtraRigt", "\\right.");
        }

        LeftNoad n = (LeftNoad) noads;
        insertionPoint = memento.getInsertionPoint();
        noads = memento.getNoads();
        insertionPoint.add(new RightNoad(n, delimiter));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#rightBrace()
     */
    public void rightBrace() throws TypesetterException {

        if (stack.empty()) {
            throw new TypesetterHelpingException(getLocalizer(),
                    "TTP.ExtraOrForgotten", "$");
        }
        MathMemento memento = (MathMemento) stack.pop();
        if (!memento.isBlock()) {
            throw new TypesetterHelpingException(getLocalizer(),
                    "TTP.ExtraOrForgotten", "\\right.");
        }
        Noad n = noads;
        insertionPoint = memento.getInsertionPoint();
        noads = memento.getNoads();
        insertionPoint.add(n);
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#scanNoad(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter,
     *      java.lang.String)
     */
    public Noad scanNoad(final Flags flags, final Context context,
            final TokenSource source, final Typesetter typesetter,
            final String primitive) throws TypesetterException {

        Flags f = null;
        if (flags != null) {
            f = flags.copy();
            flags.clear();
        }
        ListManager man = getManager();
        try {
            Token t = source.getToken(context);
            if (t == null) {
                throw new EofException(primitive);
            }
            MathListMaker lm = new MathListMaker(man, source.getLocator());
            man.push(lm);
            if (t.isa(Catcode.LEFTBRACE)) {
                lm.leftBrace();
                context.openGroup(GroupType.MATH_GROUP, source.getLocator(), t);
                source.executeGroup();
            } else {
                source.execute(t, context, typesetter);
            }
        } catch (TypesetterException e) {
            throw e;
        } catch (InterpreterException e) {
            throw new TypesetterException(e);
        } catch (ConfigurationException e) {
            throw new TypesetterException(e);
        }
        if (flags != null) {
            flags.set(f);
        }
        return (((MathListMaker) man.pop())).getInsertionPoint();
    }

    /**
     * Setter for closing.
     *
     * @param closing the closing to set
     */
    protected void setClosing(final boolean closing) {

        this.closing = closing;
    }

    /**
     * Setter for insertionPoint.
     *
     * @param insertionPoint the insertionPoint to set
     */
    protected void setInsertionPoint(final MathList insertionPoint) {

        this.insertionPoint = insertionPoint;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#showlist(
     *      java.lang.StringBuffer, long, long)
     */
    public void showlist(final StringBuffer sb, final long l, final long m) {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#subscriptMark(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      Typesetter, de.dante.extex.scanner.type.token.Token)
     */
    public void subscriptMark(final Context context, final TokenSource source,
            final Typesetter typesetter, final Token token)
            throws TypesetterException {

        Noad sub = scanNoad(null, context, source, typesetter, token.toString());
        if (insertionPoint.size() == 0) {
            add(new MathList());
        }
        Noad noad = insertionPoint.get(insertionPoint.size() - 1);
        if (noad.getSubscript() != null) {
            throw new TypesetterException(new HelpingException(getLocalizer(),
                    "TTP.DoubleSubscript"));
        }

        noad.setSubscript(sub);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#superscriptMark(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      Typesetter, de.dante.extex.scanner.type.token.Token)
     */
    public void superscriptMark(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final Token token) throws TypesetterException {

        Noad sup = scanNoad(null, context, source, typesetter, token.toString());
        if (insertionPoint.size() == 0) {
            add(new MathList());
        }
        Noad noad = insertionPoint.get(insertionPoint.size() - 1);
        if (noad.getSuperscript() != null) {
            throw new TypesetterException(new HelpingException(getLocalizer(),
                    "TTP.DoubleSuperscript"));
        }

        noad.setSuperscript(sup);
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#switchToFraction(
     *      de.dante.extex.typesetter.type.math.MathDelimiter,
     *      de.dante.extex.typesetter.type.math.MathDelimiter,
     *      de.dante.extex.interpreter.type.dimen.FixedDimen,
     *      de.dante.extex.interpreter.context.TypesettingContext)
     */
    public void switchToFraction(final MathDelimiter leftDelimiter,
            final MathDelimiter rightDelimiter, final FixedDimen ruleWidth,
            final TypesettingContext tc) throws TypesetterException {

        // see [TTP 1183]
        if (!(noads instanceof MathList)) {
            throw new TypesetterException(new HelpingException(getLocalizer(),
                    "TTP.AmbiguousFraction"));
        }
        insertionPoint = new MathList();
        noads = new FractionNoad((MathList) noads, insertionPoint,
                leftDelimiter, rightDelimiter, ruleWidth, tc);
    }

}
