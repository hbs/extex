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

package de.dante.extex.interpreter.context;

import de.dante.extex.interpreter.context.observer.CodeObserver;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This interface describes the container for all data of an interpreter
 * context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public interface ContextCode {

    /**
     * Expand some tokens.
     *
     * @param tokens the tokens to expand
     * @param typesetter the typesetter to use
     *
     * @return the expanded tokens
     *
     * @throws GeneralException in case of an error
     */
    Tokens expand(Tokens tokens, Typesetter typesetter) throws GeneralException;

    /**
     * Convenience method to get the code assigned to a Token.
     * If the Token is a ControlSequenceToken then the macro is returned.
     * If the Token is a ActiveCharacterToken then the active value is returned.
     * Otherwise <code>null</code> is returned.
     *
     * @param t the Token to differentiate on
     *
     * @return the code for the token
     *
     * @throws InterpreterException in case of an error
     */
    Code getCode(CodeToken t) throws InterpreterException;

    /**
     * Register an observer for code change events.
     * Code change events are triggered when the assignment of a macro or
     * active character changes. In this case the appropriate method in the
     * observer is invoked.
     * @param token the token to be observed. This should be a macro or
     * active character token.
     * @param observer the observer to receive the events
     */
    void registerCodeChangeObserver(Token token, CodeObserver observer);

    /**
     * Setter for the code assigned to a Token.
     * The Token has to be either a
     * {@link de.dante.extex.scanner.type.ActiveCharacterToken ActiveCharacterToken}
     * or a
     * {@link de.dante.extex.scanner.type.ControlSequenceToken ControlSequenceToken}.
     *
     * @param t the Token to set the code for
     * @param code the code for the token
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     * @throws InterpreterException In case of an error
     */
    void setCode(CodeToken t, Code code, boolean global)
            throws InterpreterException;

    /**
     * Remove a registered observer for code change events.
     * Code change events are triggered when the assignment of a macro or
     * active character changes. In this case the appropriate method in the
     * observer is invoked.
     *
     * @param name the token to be observed. This should be a macro or
     * active character token.
     * @param observer the observer to receive the events
     */
    void unregisterCodeChangeObserver(Token name, CodeObserver observer);

}