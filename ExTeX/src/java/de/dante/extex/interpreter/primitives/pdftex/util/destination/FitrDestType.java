/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.pdftex.util.destination;

import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.typesetter.type.node.RuleNode;

/**
 * This is the fitr destination type for PDF.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class FitrDestType extends DestType {

    /**
     * The field <tt>rule</tt> contains the rule specification.
     */
    private RuleNode rule;

    /**
     * Creates a new object.
     *
     * @param rule the rule
     */
    public FitrDestType(final RuleNode rule) {

        super();
        this.rule = rule;
    }

    /**
     * Getter for rule.
     * The rule carries the width height, and depth. Nothing else.
     * And even those parameters are optional; they might be <code>null</code>.
     *
     * @return the rule
     */
    public RuleNode getRule() {

        return this.rule;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("fitr ");
        FixedDimen x = rule.getWidth();
        if (x != null) {
            sb.append("width ");
            x.toString(sb);
        }
        x = rule.getHeight();
        if (x != null) {
            sb.append("height ");
            x.toString(sb);
        }
        x = rule.getDepth();
        if (x != null) {
            sb.append("depth ");
            x.toString(sb);
        }
        return sb.toString();
    }

    /**
     * @see de.dante.extex.interpreter.primitives.pdftex.util.destType.DestType#visit(
     *      de.dante.extex.interpreter.primitives.pdftex.util.destType.DestTypeVisitor)
     */
    public Object visit(final DestinationVisitor visitor) {

        return visitor.visitFitr(this);
    }

}
