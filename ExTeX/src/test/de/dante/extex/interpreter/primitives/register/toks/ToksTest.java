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

package de.dante.extex.interpreter.primitives.register.toks;

/**
 * This is a test suite for the primitive <tt>\toks</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ToksTest extends AbstractToksRegisterTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ToksTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public ToksTest(final String arg) {

        super(arg, "toks", "42", "");
    }

    /**
     * <testcase primitive="\toks">
     *   Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAssign1() throws Exception {

        assertSuccess(//--- input code ---
                "\\toks12=\\toks23 "
                + "\\end",
                //--- output channel ---
                "");
    }


}
