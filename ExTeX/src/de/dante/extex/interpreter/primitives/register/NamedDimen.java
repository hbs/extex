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
package de.dante.extex.interpreter.primitives.register;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Advanceable;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.typesetter.Typesetter;

import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\dimen</code>.
 * It sets the named dimen register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * Example
 * <pre>
 * \day=345
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class NamedDimen extends AbstractCode implements Advanceable {
    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NamedDimen(String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Advanceable#advance(int, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource)
     */
    public void advance(Flags prefix, Context context,
                        TokenSource source) throws GeneralException {
        advance(prefix, context, source, getName());
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(Flags prefix, Context context, TokenSource source,
            Typesetter typesetter) throws GeneralException {
        expand(prefix, context, source, getName());
    }

    /**
     * ...
     *
     * @param prefix ...
     * @param context ...
     * @param source ...
     * @param key ...
     *
     * @throws GeneralException ...
     */
    protected void advance(Flags prefix, Context context,
                           TokenSource source, String key)
                    throws GeneralException {
        source.scanKeyword("by");

        long val     = source.scanInteger();//TODO: incorrect
        Dimen dimen = context.getDimen(key);
        dimen.set(dimen.getValue() + val);

        if (prefix.isGlobal()) {
            context.setCount(key, dimen.getValue() + val, true);
        }
    }

    /**
     * ...
     *
     * @param prefix ...
     * @param context ...
     * @param source ...
     * @param key ...
     *
     * @throws GeneralException ...
     */
    protected void expand(Flags prefix, Context context,
                          TokenSource source, String key)
                   throws GeneralException {
        source.scanOptionalEquals();

        Dimen dimen = new Dimen(source, context);
        context.setDimen(key, dimen, prefix.isGlobal());
        prefix.clear();
        doAfterAssignment(context, source);
    }
    
    /**
     * ...
     *
     * @param context the interpreter context
     * @param value ...
     */
    public void set(Context context, String value)
             throws GeneralException {
                 //TODO
//        context.setDimen(getName(),
//                         (value.equals("") ? 0 : ...));
    }

}
