/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.omega.mode;

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\DefaultInputMode</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class DefaultInputModeTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * The command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(DefaultInputModeTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public DefaultInputModeTest(final String arg) {

        super(arg, "DefaultInputMode", " onebyte ");
        setConfig("omega");
    }

    /**
     * <testcase primitive="\DefaultInputMode">
     *  Test case checking that <tt>\DefaultInputMode</tt> works ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErrEof1() throws Exception {

        assertFailure(//--- input code ---
                "\\DefaultInputMode",
                //--- output channel ---
                "Unexpected end of file");
    }

    /**
     * <testcase primitive="\DefaultInputMode">
     *  Test case checking that <tt>\DefaultInputMode</tt> works ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        assertFailure(//--- input code ---
                "\\DefaultInputMode xxx",
                //--- output channel ---
                "Bad input mode");
    }

    //TODO implement more primitive specific test cases

}
