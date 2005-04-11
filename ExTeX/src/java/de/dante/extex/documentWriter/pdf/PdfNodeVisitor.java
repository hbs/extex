/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.documentWriter.pdf;

import java.awt.Color;
import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeIterator;
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
import de.dante.extex.typesetter.type.node.WhatsItNode;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.Unit;

/**
 * PDF NodeVisitor.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class PdfNodeVisitor implements NodeVisitor {

    /**
     * the pdf content
     */
    private PdfContentByte cb;

    /**
     * current x position
     */
    private Dimen currentX;

    /**
     * current y postition
     */
    private Dimen currentY;

    /**
     * Create a new object.
     *
     * @param pdfcb     the pdf contentbyte
     * @param cx        the currentx
     * @param cy        the currenty
     */
    public PdfNodeVisitor(final PdfContentByte pdfcb, final Dimen cx,
            final Dimen cy) {

        cb = pdfcb;
        currentX = cx;
        currentY = cy;
    }

    /**
     * Draw a box around the node (only for test).
     *
     * @param node  the node
     */
    private void drawNode(final Node node) {

        cb.setLineWidth(0.1f);
        if (node instanceof VerticalListNode) {
            cb.setColorStroke(Color.RED);
        } else if (node instanceof HorizontalListNode) {
            cb.setColorStroke(Color.YELLOW);
        } else {
            cb.setColorStroke(Color.GREEN);
        }
        float cx = (float) Unit.getDimenAsBP(currentX);
        float cy = (float) Unit.getDimenAsBP(currentY);
        float w = (float) Unit.getDimenAsBP(node.getWidth());
        float h = (float) Unit.getDimenAsBP(node.getHeight());
        float d = (float) Unit.getDimenAsBP(node.getDepth());
        cb.moveTo(cx, cy);
        cb.lineTo(cx + w, cy);
        cb.stroke();
        cb.moveTo(cx, cy);
        cb.lineTo(cx, cy - h);
        cb.stroke();
        cb.moveTo(cx + w, cy);
        cb.lineTo(cx + w, cy - h);
        cb.stroke();
        cb.moveTo(cx + w, cy - h);
        cb.lineTo(cx, cy - h);
        cb.stroke();

    }

    // -----------------------------------------
    // -----------------------------------------
    // -----------------------------------------
    // -----------------------------------------
    // -----------------------------------------
    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(AdjustNode,
     * java.lang.Object)
     */
    public Object visitAdjust(final AdjustNode node, final Object value2) {

        //        Element element = new Element("adjust");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(AfterMathNode,
     * java.lang.Object)
     */
    public Object visitAfterMath(final AfterMathNode node, final Object value2) {

        //        Element element = new Element("aftermath");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(AlignedLeadersNode,
     * java.lang.Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode node,
            final Object value2) {

        //        Element element = new Element("alignedleaders");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(BeforeMathNode,
     * java.lang.Object)
     */
    public Object visitBeforeMath(final BeforeMathNode node, final Object value2) {

        //        Element element = new Element("beforemath");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(CenteredLeadersNode,
     * java.lang.Object)
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object value) {

        //        Element element = new Element("centeredleaders");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(CharNode,
     * java.lang.Object)
     */
    public Object visitChar(final CharNode node, final Object value)
            throws GeneralException {

        try {
            UnicodeChar uc = node.getCharacter();
            Font font = node.getTypesettingContext().getFont();
            //        de.dante.extex.interpreter.context.Color color = node
            //                .getTypesettingContext().getColor();

            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.beginText();
            cb.setColorFill(Color.BLACK);
            cb.setFontAndSize(bf, (float) Unit.getDimenAsPT(font
                    .getActualSize()));
            float cy = (float) (Unit.getDimenAsBP(currentY) - Unit
                    .getDimenAsBP(node.getWidth()));
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, uc.toString(),
                    (float) Unit.getDimenAsBP(currentX), cy, 0);
            cb.endText();

            drawNode(node);

            currentX.add(node.getWidth());
        } catch (DocumentException e) {
            throw new GeneralException(e.getMessage());
        } catch (IOException e) {
            throw new GeneralException(e.getMessage());
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(DiscretionaryNode,
     * java.lang.Object)
     */
    public Object visitDiscretionary(final DiscretionaryNode node,
            final Object value) {

        //        Element element = new Element("discretionary");
        //        addNodeAttributes(nod
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(ExpandedLeadersNode,
     * java.lang.Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object value) {

        //        Element element = new Element("expandedleaders");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(GlueNode,
     * java.lang.Object)
     */
    public Object visitGlue(final GlueNode node, final Object value) {

        currentX.add(node.getWidth());
        currentY.add(node.getHeight());
        currentY.add(node.getDepth());
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(HorizontalListNode,
     * java.lang.Object)
     */
    public Object visitHorizontalList(final HorizontalListNode node,
            final Object value) throws GeneralException {

        Dimen saveX = new Dimen(currentX);
        Dimen saveY = new Dimen(currentY);

        NodeIterator it = node.iterator();
        while (it.hasNext()) {
            Node newnode = it.next();
            newnode.visit(this, node);
        }
        currentX.set(saveX);
        currentY.set(saveY);

        drawNode(node);

        currentX.add(node.getWidth());
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(InsertionNode,
     * java.lang.Object)
     */
    public Object visitInsertion(final InsertionNode node, final Object value) {

        //        Element element = new Element("insertion");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(KernNode,
     * java.lang.Object)
     */
    public Object visitKern(final KernNode node, final Object value) {

        //        Element element = new Element("kern");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(LigatureNode,
     * java.lang.Object)
     */
    public Object visitLigature(final LigatureNode node, final Object value) {

        //        Element element = new Element("ligature");
        //        addNodeAttributes(node, element);
        //        Node first = node.getLeft();
        //        Node second = node.getRight();
        //        if (first != null) {
        //            Element e = getNodeElement(first);
        //            if (e != null) {
        //                element.addContent(e);
        //            }
        //        }
        //        if (second != null) {
        //            Element e = getNodeElement(second);
        //            if (e != null) {
        //                element.addContent(e);
        //            }
        //        }
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(MarkNode,
     * java.lang.Object)
     */
    public Object visitMark(final MarkNode node, final Object value) {

        //        Element element = new Element("mark");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(PenaltyNode,
     * java.lang.Object)
     */
    public Object visitPenalty(final PenaltyNode node, final Object value) {

        //        Element element = new Element("penalty");
        //        addNodeAttributes(node, element);
        //        element.setAttribute("penalty", String.valueOf(node.getPenalty()));
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(RuleNode,
     * java.lang.Object)
     */
    public Object visitRule(final RuleNode node, final Object value) {

        //        Element element = new Element("rule");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode,
     * java.lang.Object)
     */
    public Object visitSpace(final SpaceNode node, final Object value) {

        //        Element element = new Element("space");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(VerticalListNode,
     * java.lang.Object)
     */
    public Object visitVerticalList(final VerticalListNode node,
            final Object value) throws GeneralException {

        Dimen saveX = new Dimen(currentX);
        Dimen saveY = new Dimen(currentY);

        NodeIterator it = node.iterator();
        while (it.hasNext()) {
            Node newnode = it.next();
            newnode.visit(this, node);
        }
        currentX.set(saveX);
        currentY.set(saveY);

        drawNode(node);

        currentY.add(node.getDepth());
        currentY.add(node.getHeight());
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(WhatsItNode,
     * java.lang.Object)
     */
    public Object visitWhatsIt(final WhatsItNode node, final Object value) {

        //        Element element = new Element("whatsit");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

}