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

package de.dante.extex.interpreter.primitives.info;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\meaning</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class MeaningTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for JobnameTest.
     *
     * @param arg the name
     */
    public MeaningTest(final String arg) {

        super(arg, "meaning", "\\count1 ");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a letter is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter1() throws Exception {

        assertSuccess(//--- input code ---
                "\\meaning a\\end",
                //--- output channel ---
                "the letter a" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a digit is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOther1() throws Exception {

        assertSuccess(//--- input code ---
                "\\meaning 1\\end",
                //--- output channel ---
                "the character 1" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a open-group character
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLeft1() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`{=1" + "\\meaning {\\end",
                //--- output channel ---
                "begin-group character {" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a close group character
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRight1() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`}=2" + "\\meaning }\\end",
                //--- output channel ---
                "end-group character }" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a begin-mat character
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMath1() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`$=3" + "\\meaning $\\end",
                //--- output channel ---
                "math shift character $" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a alignment tab
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testTab1() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`&=4" + "\\meaning &\\end",
                //--- output channel ---
                "alignment tab character &" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a macro parameter
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHash1() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`#=6" + "\\meaning #\\end",
                //--- output channel ---
                "macro parameter character #" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a superscript
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSuper1() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`^=7" + "\\meaning ^\\end",
                //--- output channel ---
                "superscript character ^" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a subscript
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSub1() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`_=8" + "\\meaning _\\end",
                //--- output channel ---
                "subscript character _" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of an undefined active
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testActive1() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`~=13" + "\\meaning ~\\end",
                //--- output channel ---
                "~=undefined" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a defined active
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testActive2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "\\catcode`~=13" + "\\def~{}" + "\\meaning ~\\end",
                //--- output channel ---
                "~=macro:\n->" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a defined active
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testActive3() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "\\catcode`~=13" + "\\def~{abc}" + "\\meaning ~\\end",
                //--- output channel ---
                "~=macro:\n->abc" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of an undefined macro
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test0() throws Exception {

        assertSuccess(//--- input code ---
                "\\meaning \\undef \\end",
                //--- output channel ---
                "\\undef=undefined" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of \relax is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\meaning \\relax \\end",
                //--- output channel ---
                "\\relax=\\relax" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of \meaning is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                "\\meaning \\meaning \\end",
                //--- output channel ---
                "\\meaning=\\meaning" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a defined macro
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro0() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "\\def\\x{abc}" + "\\meaning \\x \\end",
                //--- output channel ---
                "\\x=macro:\n->abc" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a defined macro with
     *  one parameter is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "\\def\\x#1{ab#1cd}" + "\\meaning \\x \\end",
                //--- output channel ---
                "\\x=macro:\n#1->ab#1cd" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a defined macro with
     *  a complex parameter is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "\\def\\x A#1B#2{ab#1cd}" + "\\meaning \\x \\end",
                //--- output channel ---
                "\\x=macro:\nA#1B#2->ab#1cd" + TERM);
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of an integer parameter
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRegister1() throws Exception {

        assertSuccess(//--- input code ---
                "\\meaning \\day \\end",
                //--- output channel ---
                "\\day=\\day" + TERM);
    }

}
