/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.listMaker;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;

/**
 * This interface describes a list for alignments with the associated methods.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public interface AlignmentList {

    /**
     * This method is invoked when a row in the alignment is complete and the
     * cells can be integrated. If some cells are not filled jet then they
     * are treated as empty.
     *
     * @param context the interpreter context
     * @param source the token source
     * @param noalign the tokens to be inserted or <code>null</code>
     *
     * @throws TypesetterException in case of an error
     */
    void cr(Context context, TokenSource source, NodeList noalign)
            throws TypesetterException;

    /**
     * This method is invoked when a row in the alignment is complete and the
     * cells can be integrated. If some cells are not filled jet then they
     * are treated as empty.
     * In contrast to the method {@link #cr(Context, TokenSource, boolean) cr()}
     * this method is a noop when the alignment is at the beginning of a row.
     *
     * @param context the interpreter context
     * @param source the token source
     *
     * @throws TypesetterException in case of an error
     */
    void crcr(Context context, TokenSource source, Typesetter typesetter)
            throws TypesetterException;

    /**
     * The invocation of this method indicates that the pattern for the current
     * cell should not be taken from the preamble but the default should be used
     * instead.
     *
     * @throws TypesetterException in case of an error
     */
    void omit() throws TypesetterException;

    /**
     * This method is invoked when a cell is complete which should be
     * continued in the next cell.
     *
     * @param context the interpreter context
     * @param source the token source
     *
     * @throws TypesetterException in case of an error
     */
    void span(Context context, TokenSource source) throws TypesetterException;

}
