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

package de.dante.extex.interpreter.context;

import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.tokens.Tokens;

/**
 * This interface describes the container for all data of an interpreter
 * context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public interface ContextTokens {

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.tokens.Tokens Tokens}
     * register. Tokens registers are named, either with a number or an
     * arbitrary string. The numbered registers where limited to 256 in
     * <logo>TeX</logo>.
     * This restriction does no longer hold for <logo>ExTeX</logo>.
     *
     * @param name the name or number of the token register
     *
     * @return the token register or a new one if it is not defined yet
     *
     * @see #getToksOrNull(String)
     * @see #setToks(String, Tokens, boolean)
     */
    Tokens getToks(String name);

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.tokens.Tokens toks}
     * register. Tokens registers are named, either with a number or an
     * arbitrary string. The numbered registers where limited to 256 in
     * <logo>TeX</logo>.
     * This restriction does no longer hold for <logo>ExTeX</logo>.
     *
     * @param name the name or number of the token register
     *
     * @return the token register or <code>null</code> if it is not defined
     *
     * @see #getToks(String)
     * @see #setToks(String, Tokens, boolean)
     */
    Tokens getToksOrNull(String name);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.tokens.Tokens Tokens}
     * register in the specified groups. Tokens registers are named, either with
     * a number or an arbitrary string. The numbered registers where limited to
     * 256 in <logo>TeX</logo>. This restriction does no longer hold for
     * <logo>ExTeX</logo>.
     *
     * @param name the name or the number of the register
     * @param toks the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws InterpreterException in case of a problem in an observer
     *
     * @see #getToks(String)
     * @see #getToksOrNull(String)
     */
    void setToks(String name, Tokens toks, boolean global)
            throws InterpreterException;

}
