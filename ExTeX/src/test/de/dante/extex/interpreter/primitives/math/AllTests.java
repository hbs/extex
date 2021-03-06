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

package de.dante.extex.interpreter.primitives.math;

import de.dante.extex.interpreter.primitives.math.delimiter.RadicalTest;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class AllTests {

    /**
     * TODO gene: missing JavaDoc
     *
     * @param args
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(AllTests.suite());
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @return
     */
    public static Test suite() {

        TestSuite suite = new TestSuite(
                "Test for de.dante.extex.interpreter.primitives.math");
        //$JUnit-BEGIN$
        suite.addTestSuite(MathchoiceTest.class);
        suite.addTestSuite(MathchardefTest.class);
        suite.addTestSuite(MathcodeTest.class);
        suite.addTestSuite(OverlineTest.class);
        suite.addTestSuite(MathaccentTest.class);
        suite.addTestSuite(UnderlineTest.class);
        suite.addTestSuite(MathTest.class);
        //$JUnit-END$
        return suite;
    }

}
