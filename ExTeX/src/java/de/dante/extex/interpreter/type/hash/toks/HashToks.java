/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.hash.toks;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.hash.toks.exception.InterpreterMissingHashKeyException;
import de.dante.extex.interpreter.type.hash.toks.exception.InterpreterMissingHashValueException;
import de.dante.extex.interpreter.type.hash.toks.exception.InterpreterMissingLeftBraceException;
import de.dante.extex.interpreter.type.hash.toks.exception.InterpreterMissingRightBraceException;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * A Hash for Tokens.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class HashToks implements Serializable {

    /**
     * The hash
     */
    private HashMap map = null;

    /**
     * Creates a new object.
     */
    public HashToks() {

        super();
        map = new HashMap();
    }

    /**
     * Creates a new object.
     * get the <code>TokenSource</code> for a <code>HashToks</code> (noexpand).
     * @param context   the context
     * @param source    the token source
     * @throws InterpreterException if an error occurs.
     */
    public HashToks(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        super();
        map = new HashMap();

        // { {key1}{value1} {key2}{value2} }
        Token token = source.getNonSpace(context);

        if (token == null) {
            throw new InterpreterMissingLeftBraceException();
        } else if (!token.isa(Catcode.LEFTBRACE)) {
            throw new InterpreterMissingLeftBraceException();
        }

        while (true) {
            String key = source.scanTokensAsString(context, "???");
            if (key.trim().length() == 0) {
                throw new InterpreterMissingHashKeyException();
            }
            Tokens toks = source.getTokens(context, source, typesetter);
            if (toks == null) {
                throw new InterpreterMissingHashValueException();
            }
            put(key, toks);

            // next ?
            token = source.getNonSpace(context);
            if (token == null) {
                throw new InterpreterMissingRightBraceException();
            } else if (token.isa(Catcode.RIGHTBRACE)) {
                break;
            }
            source.push(token);
        }
    }

    /**
     * Put the tokens on the hash with the key.
     * @param key       the key
     * @param toks      the tokens
     */
    public void put(final String key, final Tokens toks) {

        map.put(key, toks);
    }

    /**
     * Return the tokens for a key.
     * @param key   the key
     * @return  the token for this key
     */
    public Tokens get(final String key) {

        Tokens toks = (Tokens) map.get(key);
        if (toks == null) {
            toks = new Tokens();
        }
        return toks;
    }

    /**
     * Return the size of the hash.
     * @return  the size of the hash
     */
    public int size() {

        return map.size();
    }

    /**
     * Contains the key
     * @param key   the key
     * @return  <code>true</code> if the key exists, otherwise <code>false</code>
     */
    public boolean containsKey(final String key) {

        return map.containsKey(key);
    }

    /**
     * Return the value as <code>String</code>
     * @return the value as <code>String</code>
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        Iterator it = map.keySet().iterator();
        buf.append("{\n");
        while (it.hasNext()) {
            String key = (String) it.next();
            buf.append('{' + key + '}');
            buf.append('{' + get(key).toText() + "}\n");
        }
        buf.append("}\n");
        return buf.toString();
    }
}