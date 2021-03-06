/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\if</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class IfTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IfTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IfTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\if">
     *  Test case checking that <tt>\if</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\if aa true\\else false\\fi\\end",
                //--- output channel ---
                "true" + TERM);
    }

    /**
     * <testcase primitive="\if">
     *  Test case checking that <tt>\if</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                "\\if ab true\\else false\\fi\\end",
                //--- output channel ---
                "false" + TERM);
    }

    /**
     * <testcase primitive="\if">
     *  Test case checking that <tt>\if</tt> ...
     * </testcase>
     * The TeXbook
     *
     * @throws Exception in case of an error
     */
    public void test10() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES +
                "\\def\\a{*}\\let\\b=*\\def\\c{/}" +
                "\\if *\\a true\\else false\\fi\\end",
                //--- output channel ---
                "true" + TERM);
    }

    /**
     * <testcase primitive="\if">
     *  Test case checking that <tt>\if</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test11() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES +
                "\\def\\a{*}\\let\\b=*\\def\\c{/}" +
                "\\if \\a* true\\else false\\fi\\end",
                //--- output channel ---
                "true" + TERM);
    }

    /**
     * <testcase primitive="\if">
     *  Test case checking that <tt>\if</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test12() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES +
                "\\def\\a{*}\\let\\b=*\\def\\c{/}" +
                "\\if \\a\\b true\\else false\\fi\\end",
                //--- output channel ---
                "true" + TERM);
    }

    /**
     * <testcase primitive="\if">
     *  Test case checking that <tt>\if</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test13() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES +
                "\\def\\a{*}\\let\\b=*\\def\\c{/}" +
                "\\if \\b\\a true\\else false\\fi\\end",
                //--- output channel ---
                "true" + TERM);
    }

    /**
     * <testcase primitive="\if">
     *  Test case checking that <tt>\if</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test14() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES +
                "\\def\\a{*}\\let\\b=*\\def\\c{/}" +
                "\\if \\a\\c true\\else false\\fi\\end",
                //--- output channel ---
                "false" + TERM);
    }

    /**
     * <testcase primitive="\if">
     *  Test case checking that <tt>\if</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test15() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES +
                "\\def\\a{*}\\let\\b=*\\def\\c{/}" +
                "\\if \\a\\par true\\else false\\fi\\end",
                //--- output channel ---
                "false" + TERM);
    }

    /**
     * <testcase primitive="\if">
     *  Test case checking that <tt>\if</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test16() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES +
                "\\def\\a{*}\\let\\b=*\\def\\c{/}" +
                "\\if \\par\\let true\\else false\\fi\\end",
                //--- output channel ---
                "true" + TERM);
    }

}
