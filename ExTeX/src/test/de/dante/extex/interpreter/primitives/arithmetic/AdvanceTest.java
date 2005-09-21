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

package de.dante.extex.interpreter.primitives.arithmetic;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\advance</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class AdvanceTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(AdvanceTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public AdvanceTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\advance">
     *  Test case checking that <tt>\advance</tt> on a letter leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter1() throws Exception {

        runCode(//--- input code ---
                "\\advance a",
                //--- log message ---
                "You can\'t use `a\' after \\advance",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\advance">
     *  Test case checking that <tt>\advance</tt> on a other token leads to an
     *  error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOther1() throws Exception {

        runCode(//--- input code ---
                "\\advance 12 ",
                //--- log message ---
                "You can\'t use `1\' after \\advance",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\advance">
     *  Test case checking that <tt>\advance</tt> on a macro parameter token
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`#=6 "
                + "\\advance #2 ",
                //--- log message ---
                "You can\'t use `#\' after \\advance",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\advance">
     *  Test case checking that <tt>\advance</tt> on a non-advancable
     *  primitive (\\relax) leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRelax1() throws Exception {

        runCode(//--- input code ---
                "\\advance \\relax ",
                //--- log message ---
                "You can\'t use `\\relax\' after \\advance",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\advance">
     *  Test case checking that <tt>\advance</tt> on a count register name
     *  works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount1() throws Exception {

        runCode(//--- input code ---
                "\\count1 5 "
                +"\\advance \\count1 123 "
                + "\\the\\count1 \\end",
                //--- log message ---
                "",
                //--- output channel ---
                "128\n\n");
    }

    /**
     * <testcase primitive="\advance">
     *  Test case checking that <tt>\advance</tt> on a dimen register name
     *  works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDimen1() throws Exception {

        runCode(//--- input code ---
                "\\dimen1 5pt "
                +"\\advance \\dimen1 123pt "
                + "\\the\\dimen1 \\end",
                //--- log message ---
                "",
                //--- output channel ---
                "128.0pt\n\n");
    }
}