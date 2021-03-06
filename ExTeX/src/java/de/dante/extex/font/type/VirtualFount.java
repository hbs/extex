/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type;

import de.dante.extex.interpreter.context.tc.TypesettingContext;
import de.dante.extex.typesetter.type.node.VirtualCharNode;
import de.dante.util.UnicodeChar;

/**
 * Virtual Fount Interface
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public interface VirtualFount {

    /**
     * Returns the virt. char node.
     * @param context   the typesetting context
     * @param uc        the character
     * @return Returns the virt. char node.
     */
    VirtualCharNode getVirtualCharNode(TypesettingContext context,
            UnicodeChar uc);

}