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

package de.dante.extex.typesetter.type;

import java.io.Serializable;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.WideGlue;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.util.exception.GeneralException;

/**
 * A node is the basic data structure for the typesetter. It has a reference
 * point and three dimensions, namely width, height, and depth (see figure).
 * <img src="doc-files/Node.png" alt="" align="right"/>
 * <p>
 * Note that those dimensions are sometimes used for different purposes.
 * For instance a KernNode does use the width to denote the actual size which
 * can be applied in horizontal or in vertical direction, depending on the
 * context.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.21 $
 */
public interface Node extends Serializable {

    /**
     * Add the flexible depth of the current node to the given glue.
     *
     * @param glue the glue to add to.
     */
    void addDepthTo(WideGlue glue);

    /**
     * Add the flexible height of the current node to the given glue.
     *
     * @param glue the glue to add to.
     */
    void addHeightTo(WideGlue glue);

    /**
     * Add the flexible width of the current node to the given glue.
     *
     * @param glue the glue to add to.
     */
    void addWidthTo(WideGlue glue);

    /**
     * This method performs any action which are required to executed at the
     * time of shipping the node to the DocumentWriter. It is a NOOP in the
     * abstract base class and should be overwritten by sub-classes if
     * required.
     *
     * @param context the interpreter context
     * @param typesetter the typesetter
     * @param visitor the node visitor to be invoked when the node is hit. Note
     *  that each node in the output page is visited this way. Thus there is no
     *  need to implement a node traversal for the NodeList types
     * @param inHMode <code>true</code> iff the container is a horizontal list.
     *  Otherwise the container is a vertical list
     *
     * @return the node to be used instead of the current one in the output
     *  list. If the value is <code>null</code> then the node is deleted. If
     *  the value is the node itself then it is preserved.
     *
     * @throws GeneralException in case of an error
     */
    Node atShipping(Context context, Typesetter typesetter,
            NodeVisitor visitor, boolean inHMode) throws GeneralException;

    /**
     * This method determines the number of characters contained in a node.
     * <ul>
     * <li>A CharNode has a single character</li>
     * <li>A LigatureNde has the number of characters which made up the
     *  ligature</li>
     * <li>A NodeList has the number of characters given by the sum of the
     *  number of characters of its elements</li>
     * <li>Any other node types has no character</li>
     * </ul>
     *
     * @return the number of characters contained
     */
    int countChars();

    /**
     * Getter for the array of characters enclosed in this node.
     *
     * @return the array of characters
     */
    CharNode[] getChars();

    /**
     * Getter for the depth of the node.
     *
     * @return the depth
     */
    FixedDimen getDepth();

    /**
     * Getter for the height of the node.
     *
     * @return the height
     */
    FixedDimen getHeight();

    /**
     * Compute the vertical size of a node.
     * The vertical size is the size of the box enclosing the bounding
     * box and containing the base line (see figure).
     * <img src="doc-files/verticalSize.png" alt="" align="right"/>
     * <ul>
     *  <li>
     *   The vertical size is normally the sum of height and depth.
     *   This normal case applies if both height and depth are not
     *   negative.
     *  </li>
     *  <li>
     *   If height is positive and the depth is negative then the
     *   maximum of the height and the negated depth is used.
     *  </li>
     *  <li>
     *   If the height is negative and the depth is positive then the
     *   maximum of the negated height and the depth is used.
     *  </li>
     *  <li>
     *   If both height and depth are negative then the negated sum of
     *   both values is used.
     *  </li>
     * </ul>
     *
     * @return the vertical size
     */
    FixedDimen getVerticalSize();

    /**
     * Getter for the width of the node.
     *
     * @return the width
     */
    FixedDimen getWidth();

    /**
     * Setter for the depth of the node.
     *
     * @param depth the node depth
     */
    void setDepth(FixedDimen depth);

    /**
     * Setter for the height of the node.
     *
     * @param height the new height
     */
    void setHeight(FixedDimen height);

    /**
     * Setter for the width of the node.
     *
     * @param width the new width
     */
    void setWidth(FixedDimen width);

    /**
     * Adjust the height of a flexible node. This method is a noop for any but
     * the flexible nodes.
     *
     * @param height the desired height
     * @param sum the total sum of the glues
     */
    void spreadHeight(FixedDimen height, FixedGlueComponent sum);

    /**
     * Adjust the width of a flexible node. This method is a noop for any but
     * the flexible nodes.
     *
     * @param width the desired width
     * @param sum the total sum of the glues
     */
    void spreadWidth(FixedDimen width, FixedGlueComponent sum);

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a exhaustive form as it is used in tracing
     * output to the log file.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     * @param breadth the breadth of the nodes to display
     * @param depth the depth of the nodes to display
     */
    void toString(StringBuffer sb, String prefix, int breadth, int depth);

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a short form only as it is used in error
     * messages to the user.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     */
    void toText(StringBuffer sb, String prefix);

    /**
     * This method provides an entry point for the visitor pattern.
     *
     * @param visitor the visitor to apply
     * @param value the argument for the visitor
     *
     * @return the result of the method invocation of the visitor
     *
     * @throws GeneralException in case of an error
     */
    Object visit(NodeVisitor visitor, Object value) throws GeneralException;

}
