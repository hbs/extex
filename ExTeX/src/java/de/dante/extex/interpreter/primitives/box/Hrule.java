/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
package de.dante.extex.interpreter.primitives.box;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.typesetter.Typesetter;

import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\hrule</code>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class Hrule extends AbstractCode {
    /**
     * The constant <tt>DEFAULT_RULE</tt> contains the equivalent to 0.4pt.
     */
    private static final long DEFAULT_RULE = 26214; //0.4pt

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Hrule(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     * @see "TeX -- The Program [463]"
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {
        Dimen width = new Dimen(0);
        Dimen height = new Dimen(DEFAULT_RULE);
        Dimen depth = new Dimen(0);

        for (;;) {
            if (source.scanKeyword("width")) {
                width.set(context, source);
            } else if (source.scanKeyword("height")) {
                height.set(context, source);
            } else if (source.scanKeyword("depth")) {
                depth.set(context, source);
            } else {
                break;
            }
        }

        //TODO: typesetter.add();
        prefix.clear();
    }
}
