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

package de.dante.extex.interpreter.primitives.register.count;

import java.util.Calendar;

import de.dante.test.IntegerParameterTester;

/**
 * This is a test suite for the primitive <tt>\time</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class TimeTest extends IntegerParameterTester {

    /**
     * Provide the current time as default value for the comparison.
     *
     * @return the current time as string
     */
    private static String getDefaultValue() {

        Calendar cal = Calendar.getInstance();
        int x = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
        return Integer.toString(x);
    }

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(TimeTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public TimeTest(final String arg) {

        super(arg, "time", "", getDefaultValue());
    }

}