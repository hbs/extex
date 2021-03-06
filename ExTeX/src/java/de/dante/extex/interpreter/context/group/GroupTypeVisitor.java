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

package de.dante.extex.interpreter.context.group;

/**
 * This interface describes a visitor for group types.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface GroupTypeVisitor {

    /**
     * This method is invoked when an adjusted hbox group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitAdjustedHboxGroup(Object arg);

    /**
     * This method is invoked when an align group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitAlignGroup(Object arg);

    /**
     * This method is invoked when a bottom level group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitBottomLevelGroup(Object arg);

    /**
     * This method is invoked when a disc group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitDiscGroup(Object arg);

    /**
     * This method is invoked when a hbox group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitHboxGroup(Object arg);

    /**
     * This method is invoked when an insert group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitInsertGroup(Object arg);

    /**
     * This method is invoked when a math choice group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitMathChoiceGroup(Object arg);

    /**
     * This method is invoked when a math group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitMathGroup(Object arg);

    /**
     * This method is invoked when a math left group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitMathLeftGroup(Object arg);

    /**
     * This method is invoked when a math shift group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitMathShiftGroup(Object arg);

    /**
     * This method is invoked when a no align group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitNoAlignGroup(Object arg);

    /**
     * This method is invoked when a output group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitOutputGroup(Object arg);

    /**
     * This method is invoked when a semi simple group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitSemiSimpleGroup(Object arg);

    /**
     * This method is invoked when a simple group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitSimpleGroup(Object arg);

    /**
     * This method is invoked when a vbox group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitVboxGroup(Object arg);

    /**
     * This method is invoked when a vcenter group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitVcenterGroup(Object arg);

    /**
     * This method is invoked when a vtop group has been encountered.
     *
     * @param arg the argument
     *
     * @return some object
     */
    Object visitVtopGroup(Object arg);

}
