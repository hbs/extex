/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.interpreter.primitives.prefix;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\immediate</code>.
 * It does simply nothing, but as a side effect the prefix <i>IMMEDIATE</i> is
 * added to the prefixes.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Immediate extends AbstractCode {
    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Immediate(String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#expand(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource, de.dante.extex.typesetter.Typesetter)
     */
    public void expand(Flags prefix, Context context, TokenSource source,
                      Typesetter typesetter) {
        prefix.setImmediate();
    }
}
