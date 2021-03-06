/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context.impl.extension;

import java.io.Serializable;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.impl.Group;
import de.dante.extex.interpreter.type.bool.Bool;
import de.dante.extex.interpreter.type.hash.toks.HashToks;
import de.dante.extex.interpreter.type.pair.Pair;
import de.dante.extex.interpreter.type.real.Real;
import de.dante.extex.interpreter.type.transform.Transform;

/**
 * This is the implementation of a group object with ExTeX-functions.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public interface GroupExtension extends Group, Tokenizer, Serializable {

    /**
     * Setter for the real register in the current group.
     *
     * @param name the name of the register
     * @param value the value of the register
     */
    void setReal(String name, Real value);

    /**
     * Setter for a real register in the requested groups.
     *
     * @param name the name of the real register
     * @param value the value of the real register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setReal(String name, Real value, boolean global);

    /**
     * Getter for the named real register in the current group. The name can
     * either be a string representing a number or an arbitrary string. In the
     * first case the behavior of the numbered real registers is emulated. The
     * other case can be used to store special real values.
     *
     * As a default value 0 is returned.
     *
     * @param name the name of the real register
     *
     * @return the value of the real register or its default
     */
    Real getReal(String name);

    /**
     * Setter for the bool register in the current group.
     *
     * @param name the name of the register
     * @param value the value of the register
     */
    void setBool(String name, Bool value);

    /**
     * Setter for a bool register in the requested groups.
     *
     * @param name the name of the register
     * @param value the value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setBool(String name, Bool value, boolean global);

    /**
     * Getter for the named bool register in the current group. The name can
     * either be a string representing a number or an arbitrary string. In the
     * first case the behavior of the numbered real registers is emulated. The
     * other case can be used to store special real values.
     *
     * As a default value <code>null</code> is returned.
     *
     * @param name the name of the register
     *
     * @return the value of the register or its default
     */
    Bool getBool(String name);

    /**
     * Setter for the pair register in the current group.
     *
     * @param name the name of the register
     * @param value the value of the register
     */
    void setPair(String name, Pair value);

    /**
     * Setter for a pair register in the requested groups.
     *
     * @param name the name of the register
     * @param value the value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setPair(String name, Pair value, boolean global);

    /**
     * Getter for the named pair register in the current group. The name can
     * either be a string representing a number or an arbitrary string. In the
     * first case the behavior of the numbered real registers is emulated. The
     * other case can be used to store special real values.
     *
     * As a default value <code>null</code> is returned.
     *
     * @param name the name of the register
     * @return the value of the register or its default
     */
    Pair getPair(String name);

    /**
     * Setter for the transform register in the current group.
     *
     * @param name the name of the register
     * @param value the value of the register
     */
    void setTransform(String name, Transform value);

    /**
     * Setter for a transform register in the requested groups.
     *
     * @param name the name of the register
     * @param value the value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setTransform(String name, Transform value, boolean global);

    /**
     * Getter for the named transform register in the current group. The name can
     * either be a string representing a number or an arbitrary string. In the
     * first case the behavior of the numbered real registers is emulated. The
     * other case can be used to store special real values.
     *
     * As a default value <code>null</code> is returned.
     *
     * @param name the name of the register
     * @return the value of the register or its default
     */
    Transform getTransform(String name);

    /**
     * Setter for the hash-toks register in the current group.
     *
     * @param name the name of the register
     * @param value the value of the register
     */
    void setHashToks(String name, HashToks value);

    /**
     * Setter for a hash-toks register in the requested groups.
     *
     * @param name the name of the register
     * @param value the value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setHashToks(String name, HashToks value, boolean global);

    /**
     * Getter for the named hash-toks register in the current group. The name can
     * either be a string representing a number or an arbitrary string. In the
     * first case the behavior of the numbered hash-toks registers is emulated. The
     * other case can be used to store special hash-toks values.
     *
     * As a default value <code>null</code> is returned.
     *
     * @param name the name of the register
     * @return the value of the register or its default
     */
    HashToks getHashToks(String name);

}