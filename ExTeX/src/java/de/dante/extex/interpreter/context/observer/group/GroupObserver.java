/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context.observer.group;

import de.dante.extex.interpreter.context.ContextInternals;

/**
 * This interface describes the ability to receive a notification about the
 * opening and closing of groups.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface GroupObserver {

    /**
     * Receive a notification that a group has been closed.
     *
     * @param context the interpreter context
     *
     * @throws Exception in case of an error
     */
    void receiveCloseGroup(ContextInternals context) throws Exception;

    /**
     * Receive a notification that a group has been opened.
     *
     * @param context the interpreter context
     *
     * @throws Exception in case of an error
     */
    void receiveOpenGroup(ContextInternals context) throws Exception;

}