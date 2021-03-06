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

package de.dante.extex.interpreter.primitives.table;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\crcr</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class CrcrTest extends NoFlagsPrimitiveTester {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public CrcrTest(final String arg) {

        super(arg, "crcr", "}", DEFINE_HASH + "\\halign{#\\cr");
    }

    /**
     * <testcase primitive="\crcr">
     *  Test case checking that <tt>\crcr</tt> outside of an alignment context
     *  produces an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLonelyCr() throws Exception {

        assertFailure(//--- input code ---
                "\\crcr" + "\\end ",
                //--- log message ---
                "Misplaced \\crcr");
    }

}