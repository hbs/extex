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

package de.dante.extex.typesetter.paragraphBuilder.impl;

import junit.framework.TestCase;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ParagraphBuilderImplTest extends TestCase {

    /**
     * ...
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.3 $
     */
    private class MockOptions implements TypesetterOptions {
        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getCountOption(java.lang.String)
         */
        public FixedCount getCountOption(final String name) {

            if (name.equals("tracingparagraphs")) {
                return new Count(0);
            } else if (name.equals("pretolerance")) {
                return new Count(300);
            } else if (name.equals("tolerance")) {
                return new Count(200);
            } else if (name.equals("hyphenpenalty")) {
                return new Count(20);
            } else if (name.equals("exhyphenpenalty")) {
                return new Count(30);
            }
            return new Count(0);
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getDimenOption(java.lang.String)
         */
        public FixedDimen getDimenOption(final String name) {

            // TODO Auto-generated method stub
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getGlueOption(java.lang.String)
         */
        public FixedGlue getGlueOption(final String name) {

            if (name.equals("parfillskip")) {
                return new Glue(1000);
            }
            return new Glue(0);
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getParshape()
         */
        public ParagraphShape getParshape() {

            // TODO Auto-generated method stub
            return null;
        }
        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#setParshape(de.dante.extex.typesetter.paragraphBuilder.ParagraphShape)
         */
        public void setParshape(ParagraphShape shape) {

            // TODO Auto-generated method stub

        }
    }

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ParagraphBuilderImplTest.class);
    }

    /**
     * ...
     */
    public void test1() {

        ParagraphBuilder builder = new ParagraphBuilderImpl();
        builder.setOptions(new MockOptions());
        HorizontalListNode nodes = new HorizontalListNode();
        assertEquals(0, builder.build(nodes).size());
    }
}