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

package de.dante.extex.interpreter.type.box;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.group.GroupType;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.MissingLeftBraceException;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.listMaker.InnerVerticalListMaker;
import de.dante.extex.typesetter.listMaker.RestrictedHorizontalListMaker;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class is used to represent box registers.
 * A box register can either be void or be a horizontal or vertical list.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.42 $
 */
public class Box implements BoxOrRule, Serializable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>nodes</tt> contains the node list stored in this box.
     * Thus is either a
     * {@link de.dante.extex.typesetter.type.node.HorizontalListNode HorizontalListNode}
     * or a
     * {@link de.dante.extex.typesetter.type.node.VerticalListNode VerticalListNode}
     * or it is <code>null</code>.
     * In case of a <code>null</code> value the box is void.
     */
    private NodeList nodes = null;

    /**
     * Creates a new object.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter stack
     * @param isHorizontal indicator whether a <tt>\hbox</tt> should be
     *  constructed. The alternative is a <tt>\vbox</tt>.
     * @param insert tokens to insert at the beginning or <code>null</code>
     *  for none
     * @param groupType the type of the group just entered
     * @param startToken the token which started the group
     *
     * @throws InterpreterException in case of an error
     */
    public Box(final Context context, final TokenSource source,
            final Typesetter typesetter, final boolean isHorizontal,
            final Tokens insert, final GroupType groupType,
            final Token startToken) throws InterpreterException {

        super();

        Token t = source.getToken(context);

        if (t == null) {
            throw new EofException();
        } else if (!t.isa(Catcode.LEFTBRACE)) {
            throw new MissingLeftBraceException(null);
        }

        ListMaker lm;

        if (isHorizontal) {
            lm = new RestrictedHorizontalListMaker(typesetter.getManager(),
                    source.getLocator());
        } else {
            lm = new InnerVerticalListMaker(typesetter.getManager(), source
                    .getLocator());
        }
        typesetter.push(lm);

        try {
            context.openGroup(groupType, source.getLocator(), startToken);
            source.push(insert);
            source.executeGroup();
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        } finally {
            try {
                while (typesetter.getListMaker() != lm) {
                    typesetter.add(typesetter
                            .complete((TypesetterOptions) context));
                }

                nodes = typesetter.complete((TypesetterOptions) context);
            } catch (ConfigurationException e) {
                throw new InterpreterException(e);
            }
        }
    }

    /**
     * Creates a new object.
     *
     * @param list the node list
     */
    public Box(final NodeList list) {

        nodes = list;
    }

    /**
     * Creates a new object.
     *
     * @param box the box to copy (shallow)
     */
    public Box(final Box box) {

        nodes = (box == null ? null : box.getNodes());
    }

    /**
     * Clear the contents of the box. Afterwards the box is void.
     */
    public void clear() {

        nodes = null;
    }

    /**
     * Getter for the depth of this box.
     *
     * @return the depth of this box or 0pt in case of a void box
     */
    public FixedDimen getDepth() {

        return (nodes == null ? Dimen.ZERO_PT : nodes.getDepth());
    }

    /**
     * Getter for the height of this box.
     *
     * @return the height of this box or 0pt in case of a void box
     */
    public FixedDimen getHeight() {

        return (nodes == null ? Dimen.ZERO_PT : nodes.getHeight());
    }

    /**
     * Getter for the localizer.
     *
     * @return the localizer
     */
    protected Localizer getLocalizer() {

        return LocalizerFactory.getLocalizer(Box.class);
    }

    /**
     * Getter for the move parameter.
     * The move parameter describes how far from its original position the box
     * is moved leftwards or rightwards. Positive values indicate a move
     * rightwards.
     *
     * @return the move parameter
     */
    public FixedDimen getMove() {

        return this.nodes.getMove();
    }

    /**
     * Getter for nodes.
     *
     * @return the nodes.
     */
    public NodeList getNodes() {

        return nodes;
    }

    /**
     * Getter for the shift parameter.
     * The shift parameter describes how far from its original position the box
     * is shifted up or down. Positive values indicate a move upwards.
     *
     * @return the shift parameter
     */
    public FixedDimen getShift() {

        return this.nodes.getShift();
    }

    /**
     * Getter for the width of this box.
     *
     * @return the width of this box or 0pt in case of a void box
     */
    public FixedDimen getWidth() {

        return (nodes == null ? Dimen.ZERO_PT : nodes.getWidth());
    }

    /**
     * Checks whether the box is a horizontal box.
     *
     * @return <tt>true</tt> iff the box is a horizontal box.
     */
    public boolean isHbox() {

        return (nodes != null && nodes instanceof HorizontalListNode);
    }

    /**
     * Checks whether the box is a vertical box.
     *
     * @return <tt>true</tt> iff the box is a vertical box.
     */
    public boolean isVbox() {

        return (nodes != null && nodes instanceof VerticalListNode);
    }

    /**
     * Checks whether the box is void.
     *
     * @return <tt>true</tt> iff the box is void.
     */
    public boolean isVoid() {

        return nodes == null;
    }

    /**
     * Setter for the depth of the box.
     * If the box is void then this method simply does nothing.
     *
     * @param depth the new width
     */
    public void setDepth(final Dimen depth) {

        if (nodes != null) {
            nodes.setDepth(depth);
        }
    }

    /**
     * Setter for the height of the box.
     * If the box is void then this method simply does nothing.
     *
     * @param height the new width
     */
    public void setHeight(final FixedDimen height) {

        if (nodes != null) {
            if (nodes instanceof VerticalListNode) {
                ((VerticalListNode) nodes).vpack(height);
            } else {
                nodes.setHeight(height);
            }
        }
    }

    /**
     * Setter for the move parameter.
     * The move parameter describes hpw far from its original position the box
     * is moved leftwards or rightwards. Positive values indicate a move
     * rightwards.
     *
     * @param d the new move parameter
     */
    public void setMove(final Dimen d) {

        this.nodes.setMove(d);
    }

    /**
     * Setter for the shift parameter.
     * The shift parameter describes hpw far from its original position the box
     * is shifted up or down. Positive values indicate a move upwards.
     *
     * @param d the new shift parameter
     */
    public void setShift(final Dimen d) {

        this.nodes.setShift(d);
    }

    /**
     * Setter for the width of the box.
     * If the box is void then this method simply does nothing.
     *
     * @param width the new width
     */
    public void setWidth(final FixedDimen width) {

        if (nodes != null) {
            nodes.setWidth(width);
            if (nodes instanceof HorizontalListNode) {
                ((HorizontalListNode) nodes).hpack(width);
            }
        }
    }

    /**
     * Adjust the height of the box if it is not void.
     *
     * @param spread the length to add to the height
     */
    public void spreadHeight(final FixedDimen spread) {

        if (nodes != null) {
            Dimen x = new Dimen(nodes.getHeight());
            x.add(spread);
            setHeight(x);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        if (nodes == null) {
            return "void";
        }
        return nodes.toString();
    }

    /**
     * Adjust the width of the box if it is not void.
     *
     * @param spread the length to add to the width
     */
    public void spreadWidth(final FixedDimen spread) {

        if (nodes != null) {
            Dimen x = new Dimen(nodes.getWidth());
            x.add(spread);
            setWidth(x);
        }
    }

    /**
     * Split off material from a vlist of a certain height.
     *
     * @param height the height of the material to cut off
     * @param logger the logger or <code>null</code>
     *
     * @return a new vertical node list with the material
     *
     * @throws OperationNotSupportedException in case that the Box is not a vlist
     *
     * @see "TTP [977]"
     */
    public VerticalListNode vsplit(final Dimen height, final Logger logger)
            throws OperationNotSupportedException {

        if (!isVbox()) {
            throw new OperationNotSupportedException("vsplit");
        }

        return ((VerticalListNode) nodes).split(height, logger, logger);
    }

}
