/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.node;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.GeneralException;

/**
 * The rule node represents a rectangular area on the page filled with some
 * color.
 *
 * @see "TeX -- The Program [138]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class RuleNode extends AbstractNode implements Node {
    /**
     * The field <tt>context</tt> the typographic context.
     */
    private TypesettingContext context;

    /**
     * Creates a new object.
     *
     * @param width the width of the rule
     * @param height the height of the rule
     * @param depth the depth of the rule
     * @param theContext the typographic context
     *
     * @see "TeX -- The Program [139]"
     */
    public RuleNode(final Dimen width, final Dimen height, final Dimen depth,
            final TypesettingContext theContext) {

        super(width, height, depth);
        this.context = theContext;
    }

    /**
     * Getter for the typographic context.
     *
     * @return the typographic context.
     */
    public TypesettingContext getContext() {

        return context;
    }

    /**
     * This method returns the printable representation.
     * This is meant to produce a exaustive form as it is used in tracing
     * output to the log file.
     *
     * @return the printable representation
     *
     * @see "TeX -- The Program [187]"
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb, "");
        return sb.toString();
    }

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a short form only as it is used in error
     * messages to the user.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     *
     * @see de.dante.extex.typesetter.type.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     * @see "TeX -- The Program [187]"
     */
    public void toString(final StringBuffer sb, final String prefix) {

        Dimen x = getHeight();
        String h = (x == null ? "*" : x.toString());
        x = getDepth();
        String d = (x == null ? "*" : x.toString());
        x = getWidth();
        String w = (x == null ? "*" : x.toString());
        sb.append(getLocalizer().format("RuleNode.Text", h, d, w));
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toText(java.lang.StringBuffer, java.lang.String)
     */
    public void toText(StringBuffer sb, String prefix) {

        // TODO gene: toText unimplemented

    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitRule(this, value);
    }

}
