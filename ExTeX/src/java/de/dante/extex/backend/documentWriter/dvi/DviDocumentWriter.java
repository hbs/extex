/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.documentWriter.dvi;

import java.io.IOException;
import java.io.OutputStream;

import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.backend.documentWriter.DocumentWriterOptions;
import de.dante.extex.backend.documentWriter.SingleDocumentStream;
import de.dante.extex.backend.documentWriter.exception.NoOutputStreamException;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.type.InspectableNodeVisitor;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeIterator;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.extex.typesetter.type.node.AdjustNode;
import de.dante.extex.typesetter.type.node.AfterMathNode;
import de.dante.extex.typesetter.type.node.AlignedLeadersNode;
import de.dante.extex.typesetter.type.node.BeforeMathNode;
import de.dante.extex.typesetter.type.node.CenteredLeadersNode;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.ExpandedLeadersNode;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.InsertionNode;
import de.dante.extex.typesetter.type.node.KernNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.extex.typesetter.type.node.MarkNode;
import de.dante.extex.typesetter.type.node.PenaltyNode;
import de.dante.extex.typesetter.type.node.RuleNode;
import de.dante.extex.typesetter.type.node.SpaceNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.extex.typesetter.type.node.VirtualCharNode;
import de.dante.extex.typesetter.type.node.WhatsItNode;
import de.dante.extex.typesetter.type.page.Page;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;

/**
 * This is a implementation of a dvi document writer.
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.2 $
 */
public class DviDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            Localizable {

    /**
     * Internal <code>NodeVisitor</code> of this class.
     *
     */
    private final class DviVisitor
            implements
                NodeVisitor,
                InspectableNodeVisitor {

        /**
         * Writer for the dvi code.  The writer knows the dvi format.
         *
         */
        private DviWriter dviWriter = null;

        /**
         * Visitor for nested nodes.  This is normally
         * <code>this</code>.  It changes during debugging.
         *
         */
        private NodeVisitor visitor = this;

        /**
         * Creates a new instance.
         *
         * @param theDviWriter writer for the dvi output
         */
        public DviVisitor(final DviWriter theDviWriter) {

            dviWriter = theDviWriter;
        }


        /**
         * Get wrong node.
         *
         * @param node the wrong node
         * @return Exception for this confusion.
         *
         * @exception GeneralException if an error occurs
         */
        private GeneralException confusion(final String node)
                throws GeneralException {

            final String argument;

            if (localizer == null) {
                argument = "ExTeX.DocumentWriterWrongNode: " + node;
            } else {
                argument = localizer.format("ExTeX.DocumentWriterWrongNode",
                        node);
            }

            // TODO: return new PanicException(localizer, "TTP.Confusion", argument); (TE)
            return new PanicException();

        }

        /**
         * Set the visitor for recursive inspection of Nodes (NodeLists).
         *
         * @param theVisitor the new <code>NodeVisitor</code>
         */
        public void setVisitor(final NodeVisitor theVisitor) {

            // this method is needed for debugging

            visitor = theVisitor;
        }


        /**
         * Inspect Adjust for dvi file.
         *
         * @param node the <code>AdjustNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(AdjustNode,
         *     java.lang.Object)
         */
        public Object visitAdjust(final AdjustNode node, final Object value)
                throws GeneralException {

            // TODO unimplemented
            throw new GeneralException("unimplemented");
        }

        /**
         * Inspect AfterMathNode for dvi file.
         *
         * @param node the <code>AfterMathNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(AfterMathNode,
         *     java.lang.Object)
         */
        public Object visitAfterMath(final AfterMathNode node,
                final Object value) throws GeneralException {

            // TODO unimplemented
            throw new GeneralException("unimplemented");
        }

        /**
         * Inspect AlignedLeadersNode for dvi file.
         *
         * @param node the <code>AlignedLeadersNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(AlignedLeadersNode,
         *     java.lang.Object)
         */
        public Object visitAlignedLeaders(final AlignedLeadersNode node,
                final Object value) throws GeneralException {

            // TODO unimplemented
            throw new GeneralException("unimplemented");
        }

        /**
         * Inspect BeforeMathNode for dvi file.
         *
         * @param node the <code>BeforeMathNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(BeforeMathNode,
         *     java.lang.Object)
         */
        public Object visitBeforeMath(final BeforeMathNode node,
                final Object value) throws GeneralException {

            // TODO unimplemented
            throw new GeneralException("unimplemented");
        }


        /**
         * Inspect CenteredLeadersNode for dvi file.
         *
         * @param node the <code>CenteredLeadersNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(CenteredLeadersNode,
         *     java.lang.Object)
         */
        public Object visitCenteredLeaders(final CenteredLeadersNode node,
                final Object value) throws GeneralException {

            // TODO unimplemented
            throw new GeneralException("unimplemented");
        }

        /**
         * Write an CharNode to dvi file.
         *
         * @param node the <code>CharNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(AfterMathNode,
         *     java.lang.Object)
         */
        public Object visitChar(final CharNode node, final Object value)
                throws GeneralException {

            Font font = node.getTypesettingContext().getFont();

            if (currentFont != font) {
                dviWriter.selectFont(font);
                currentFont = font;
            }

            dviWriter.writeNode(node);

            return null;
        }


        /**
         * Inspect DiscretionaryNode for dvi file.
         *
         * @param node the <code>DiscretionaryNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(DiscretionaryNode,
         *     java.lang.Object)
         */
        public Object visitDiscretionary(final DiscretionaryNode node,
                final Object value) throws GeneralException {

            // TODO unimplemented
            throw new GeneralException("unimplemented");
        }


        /**
         * Inspect ExpandedLeadersNode for dvi file.
         *
         * @param node the <code>ExpandedLeadersNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(ExpandedLeadersNode,
         *     java.lang.Object)
         */
        public Object visitExpandedLeaders(final ExpandedLeadersNode node,
                final Object value) throws GeneralException {

            // TODO unimplemented
            throw new GeneralException("unimplemented");
        }


        /**
         * Write an GlueNode to dvi file.
         *
         * @param node the <code>GlueNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(GlueNode,
         *     java.lang.Object)
         */
        public Object visitGlue(final GlueNode node, final Object value)
                throws GeneralException {

            dviWriter.writeSpace(node.getWidth(), mode);

            return null;
        }


        /**
         * Write horizontal list to dvi file.
         *
         * @param nodes the <code>VerticalListNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(HorizontalListNode,
         *     java.lang.Object)
         */
        public Object visitHorizontalList(final HorizontalListNode nodes,
                final Object value) throws GeneralException {

            Mode oldMode = mode;

            mode = Mode.HORIZONTAL;

            writeNodes(nodes);

            mode = oldMode;
            return null;
        }


        /**
         * Inspect insertion for dvi file.
         *
         * @param node the <code>InsertionNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(InsertionNode,
         *     java.lang.Object)
         */
        public Object visitInsertion(final InsertionNode node,
                final Object value) throws GeneralException {

            throw confusion("insertion");
        }


        /**
         * Write an KernNode to dvi file.
         *
         * @param node the <code>KernNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(KernNode,
         *     java.lang.Object)
         */
        public Object visitKern(final KernNode node, final Object value)
                throws GeneralException {

            dviWriter.writeSpace(node.getWidth(), mode);

            return null;
        }


        /**
         * Write an LigatureNode to dvi file.
         *
         * @param node the <code>LigatureNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(LigatureNode,
         *     java.lang.Object)
         */
        public Object visitLigature(final LigatureNode node, final Object value)
                throws GeneralException {

            visitChar(node, value);

            return null;
        }


        /**
         * Inspect mark for dvi file.
         *
         * @param node the <code>MarkNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(MarkNode,
         *     java.lang.Object)
         */
        public Object visitMark(final MarkNode node, final Object value)
                throws GeneralException {

            throw confusion("mark");
        }


        /**
         * Inspect PenaltyNode for dvi file.
         *
         * @param node the <code>PenaltyNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(PenaltyNode,
         *     java.lang.Object)
         */
        public Object visitPenalty(final PenaltyNode node, final Object value)
                throws GeneralException {

            throw confusion("penalty");
        }


        /**
         * Write a RuleNode to dvi file.
         *
         * @param node the <code>RuleNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(RuleNode,
         *     java.lang.Object)
         */
        public Object visitRule(final RuleNode node, final Object value)
                throws GeneralException {

            dviWriter.writeNode(node);

            return null;
        }


        /**
         * Write a SpaceNode to dvi file.
         *
         * @param node the <code>SpaceNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode,
         *     java.lang.Object)
         */
        public Object visitSpace(final SpaceNode node, final Object value)
                throws GeneralException {

            dviWriter.writeSpace(node.getWidth(), mode);

            return null;
        }


        /**
         * Write a vertical list to dvi file.
         *
         * @param nodes the <code>VerticalListNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(VerticalListNode,
         *     java.lang.Object)
         */
        public Object visitVerticalList(final VerticalListNode nodes,
                final Object value) throws GeneralException {

            Mode oldMode = mode;

            mode = Mode.VERTICAL;

            writeNodes(nodes);

            mode = oldMode;
            return null;
        }


        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVirtualChar(de.dante.extex.typesetter.type.node.VirtualCharNode, java.lang.Object)
         */
        public Object visitVirtualChar(final VirtualCharNode node,
                final Object value) throws GeneralException {

            // TODO visitVirtualChar unimplemented
            return null;
        }

        /**
         * Write a WhatsItNode to dvi file.
         *
         * @param node the <code>WhatsItNode</code> value
         * @param value ignored
         * @return null
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(WhatsItNode,
         *     java.lang.Object)
         */
        public Object visitWhatsIt(final WhatsItNode node, final Object value)
                throws GeneralException {

            dviWriter.writeNode(node);
            return null;
        }

        /**
         * Write nodelist to dvi file.
         *
         * @param nodes <code>NodeList</code> for writing.
         * @exception GeneralException if an error occurs
         */
        private void writeNodes(final NodeList nodes) throws GeneralException {

            NodeIterator iterator = nodes.iterator();

            dviWriter.saveCurrentPositions();

            dviWriter.writeHorizontalSpace(nodes.getMove());
            dviWriter.writeVerticalSpace(nodes.getShift());
            while (iterator.hasNext()) {
                Node node = iterator.next();
                node.visit(visitor, null);

                // write next Nodes after this node in vertical list
                if (mode == Mode.VERTICAL) {
                    dviWriter.writeSpace(node.getHeight(), Mode.VERTICAL);
                }
            }
            dviWriter.restoreCurrentPositions();
        }

    } // end class DviVisitor

    /**
     * Configuration of ExTeX.
     */
    private Configuration configuration = null;


    /**
     * Saves the current font.  Need the check if there is a font
     * change needed.
     */
    private Font currentFont = null;

    // TODO: docu (TE)
    /*
     * TODO:
     * - perhaps it is better to put the mode in the visitor-argument
     * - handle first vertical box special
     * (TE)
     */

    /**
     * The constant <code>DEBUG</code> turn debug on or off.
     *
     */
    private final boolean DEBUG = false;

    /**
     * Options of the document writer.
     *
     */
    private DocumentWriterOptions documentWriterOptions = null;

    /**
     * DviWriter used to write the DviFile.
     *
     */
    private DviWriter dviWriter = null;

    /**
     * Set iff we are at the beginning of the dvi-file.
     *
     */
    private boolean isBeginDviFile = true;

    /**
     * Object for localize strings messages.
     *
     */
    private Localizer localizer = null;

    /**
     * Current mode (<code>{@link
     * de.dante.extex.typesetter.Mode#VERTICAL Mode.VERTICAL}</code>
     * or <code>{@link de.dante.extex.typesetter.Mode#HORIZONTAL
     * Mode.HORIZONTAL}</code>).
     *
     */
    private Mode mode = Mode.VERTICAL;

    /**
     * Visitor for the nodes.
     *
     */
    private InspectableNodeVisitor visitor = null;

    /**
     * Creates a new instance.
     *
     * @param theCfg configuration of ExTeX
     * @param options options for <code>DviDocumentWriter</code>
     */
    public DviDocumentWriter(final Configuration theCfg,
            final DocumentWriterOptions options) {

        super();
        this.configuration = theCfg;
        documentWriterOptions = options;
    }

    /**
     * This method is invoked upon the end of the processing.  End of
     * dvi file is written.
     *
     * @exception GeneralException if an error occurs
     * @exception IOException if an error occurs
     */
    public void close() throws GeneralException, IOException {

        dviWriter.endDviFile();
    }

    /**
     * Set the <code>Localizer</code> method here.
     *
     * @param theLocalizer a <code>Localizer</code> value
     * @see de.dante.util.framework.i18n.Localizable#enableLocalization(
     *      de.dante.util.framework.i18n.Localizer)
     */
    public void enableLocalization(final Localizer theLocalizer) {

        localizer = theLocalizer;
    }

    /**
     * Getter for the extension associated with dvi output.
     *
     * @return normally "dvi"
     */
    public String getExtension() {

        return "dvi";
    }

    /**
     * Get the number of written pages until now.
     *
     * @return the number of written pages
     */
    public int getPages() {

        return dviWriter.getPages();
    }

    /**
     * Setter for the output stream.  This method throws no exception.
     * If somethings goes wrong {@link
     * #shipout(de.dante.extex.typesetter.type.NodeList) shipout(NodeList)}
     * informs the caller.
     *
     * @param writer an <code>OutputStream</code> value
     */
    public void setOutputStream(final OutputStream writer) {

        dviWriter = new DviWriter(writer, documentWriterOptions);
        visitor = new DviVisitor(dviWriter);

        if (DEBUG) {
            visitor = new DebugNodeVisitor(visitor);
        }
    }

    /**
     * Setter of an named parameter.  This Documentwriter supports no
     * parameters yet.
     *
     * @param name a <code>String</code> value
     * @param value a <code>String</code> value
     */
    public void setParameter(final String name, final String value) {

        // there no paramters yet
    }

    /**
     * This is the entry point for the document writer.  Exceptions of
     * the initialisation of the class will be thrown here.
     *
     * @param page the <code>Page</code> to send
     * @return number of pages
     * @exception GeneralException if an error occurs
     * @exception IOException if an error occurs
     */
    public int shipout(final Page page)
            throws GeneralException,
                IOException {

        NodeList nodes = page.getNodes();
        GeneralException error;

        if (dviWriter == null) {
            throw new NoOutputStreamException();
        }

        if (isBeginDviFile) {
            isBeginDviFile = false;
            dviWriter.beginDviFile();
        }

        currentFont = null;

        mode = Mode.VERTICAL;
        dviWriter.beginPage();

        nodes.visit(visitor, null);

        dviWriter.endPage();

        error = dviWriter.getError();
        if (error != null) {
            throw new GeneralException(error);
        }
        return 1;
    }
}