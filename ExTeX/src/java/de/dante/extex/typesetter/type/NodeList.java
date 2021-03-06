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

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;

/**
 * This interface describes the features of a linear collection of nodes.
 * <p>
 * The following picture describes the attributes and relations:
 * </p>
 * <p>
 * <img src="doc-files/NodeList.png" />
 * </p>
 * <p>
 * Note that the NodeList does not provide an automatic relation of the contents
 * to the attributes. This means that adding a node does not update width,
 * height, or depth.
 * </p>
 *
 * @see de.dante.extex.interpreter.type.box.Box
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public interface NodeList extends Node, Cloneable {

    /**
     * Add a node to the node list at a given position.
     *
     * @param index the position of insertion
     * @param node the node to add
     */
    void add(int index, Node node);

    /**
     * Add a node to the node list.
     * The other attributes (width, height, depth) are not modified.
     *
     * @param node the node to add
     */
    void add(Node node);

    /**
     * Add some glue to the node list.
     * The other attributes (width, height, depth) are not modified.
     *
     * @param glue the glue to add
     */
    void addSkip(FixedGlue glue);

    /**
     * Remove all nodes from the list. The list is empty afterwards.
     * The dimensions are reset to zero unless target sizes are specified.
     * In this case the target sizes are used.
     */
    void clear();

    /**
     * Clone the current object.
     *
     * @return the copy
     */
    NodeList copy();

    /**
     * Getter for a node at a given position.
     *
     * @param index the position
     *
     * @return the node at position <i>index</i> of <code>null</code> if index
     * is out of bounds
     */
    Node get(int index);

    /**
     * Getter for the move value of the node list.
     * The move parameter describes how far from its original position the box
     * is moved leftwards or rightwards. Positive values indicate a move
     * rightwards.
     *
     * @return the move value
     */
    Dimen getMove();

    /**
     * Getter for the shift value of the node list.
     * The shift parameter describes how far from its original position the box
     * is shifted up or down. Positive values indicate a shift downwards.
     *
     * @return the shift value
     */
    Dimen getShift();

    /**
     * Get a new iterator for all nodes in the list.
     * This method is just provided for completeness. Consider a conventional
     * loop because of performance issues.
     *
     * @return the iterator for all nodes in the list
     */
    NodeIterator iterator();

    /**
     * Remove an element at a given position.
     * The other attributes (width, height, depth) are not modified.
     *
     * @param index the position
     *
     * @return the element previously located at position <i>index</i>
     */
    Node remove(int index);

    /**
     * Setter for the move value of the node list.
     * The move parameter describes how far from its original position the box
     * is moved leftwards or rightwards. Positive values indicate a move
     * rightwards.
     *
     * @param d the move value
     */
    void setMove(FixedDimen d);

    /**
     * Setter for the shift value of the node list.
     * The shift parameter describes how far from its original position the box
     * is shifted up or down. Positive values indicate a shift downwards.
     *
     * @param d the amount to be shifted
     */
    void setShift(FixedDimen d);

    /**
     * Getter for the number of elements of the list.
     *
     * @return the length of the list
     */
    int size();

    /**
     * Print the node possibly truncated in breadth and depth.
     *
     * @param sb the target string buffer
     * @param prefix the string inserted at the beginning of each line
     * @param depth the depth limit for the pretty printing
     * @param width the width limit for the pretty printing
     */
    void toString(StringBuffer sb, String prefix, int depth, int width);

}
