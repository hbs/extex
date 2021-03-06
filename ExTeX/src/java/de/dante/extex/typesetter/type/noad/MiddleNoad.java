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

package de.dante.extex.typesetter.type.noad;

import java.util.logging.Logger;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.math.MathDelimiter;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This Noad carries a delimiter which is set in the middle between math
 * material surrounding it. This delimiter adjusts its height to the height of
 * the surrounding material.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
 */
public class MiddleNoad extends LeftNoad {

    /**
     * The field <tt>delimiter</tt> contains the middle delimiter.
     */
    private MathDelimiter delimiter;

    /**
     * The field <tt>noad</tt> contains the material before this noad.
     */
    private LeftNoad noadPre;

    /**
     * The field <tt>noadPost</tt> contains the material after this noad.
     */
    private Noad noadPost;

    /**
     * Creates a new object.
     *
     * @param noadPre the material before this noad
     * @param delimiter the delimiter
     * @param noadPost the material after this noad
     */
    public MiddleNoad(final LeftNoad noadPre, final MathDelimiter delimiter,
            final Noad noadPost) {

        super(noadPre, delimiter);
        this.delimiter = delimiter;
        this.noadPre = noadPre;
        this.noadPost = noadPost;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.AbstractNoad#toStringAdd(
     *      java.lang.StringBuffer,
     *      int)
     */
    public void toStringAdd(final StringBuffer sb, final int depth) {

        sb.append("middle");
        delimiter.toString(sb);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.LeftNoad#typeset(
     *      de.dante.extex.typesetter.type.noad.Noad,
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger,
     *      de.dante.extex.interpreter.type.dimen.Dimen,
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void typeset(final Noad previousNoad, final NoadList noads,
            final int index, final NodeList list,
            final MathContext mathContext, final Logger logger,
            final Dimen height, final Dimen depth)
            throws TypesetterException,
                ConfigurationException {

        getSpacingClass().addClearance(
                (previousNoad != null ? previousNoad.getSpacingClass() : null),
                list, mathContext);

        HorizontalListNode hlist = new HorizontalListNode();

        noadPost
                .typeset(previousNoad, noads, index, hlist, mathContext, logger);
        height.max(hlist.getHeight());
        depth.max(hlist.getDepth());

        noadPre.typeset(previousNoad, noads, index, list, mathContext, logger,
                height, depth);

        delimiter
                .typeset(previousNoad, noads, index, list, mathContext, logger);
        list.add(hlist);
    }

}
