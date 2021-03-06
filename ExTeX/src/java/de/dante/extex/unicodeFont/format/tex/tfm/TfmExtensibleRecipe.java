/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.tex.tfm;

import java.io.IOException;
import java.io.Serializable;

import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Class for extensible recipe.
 *
 * <p>
 * Extensible characters are specified by an extensible_recipe,
 * which consists of four bytes called top, mid, bot, and
 * rep (in this order). These bytes are the character codes of
 * individual pieces used to build up a large symbol.
 * </p>
 *
 * <p>
 * Information from:
 * The DVI Driver Standard, Level 0
 * The TUG DVI Driver Standards Committee
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class TfmExtensibleRecipe implements XMLWriterConvertible, Serializable {

    /**
     * the id.
     */
    private int etid;

    /**
     * top.
     */
    private short top;

    /**
     * mid.
     */
    private short mid;

    /**
     * bot.
     */
    private short bot;

    /**
     * rep.
     */
    private short rep;

    /**
     * Create a new object.
     * @param rar   the input
     * @param id    the id
     * @throws IOException if an IO-error occurs.
     */
    public TfmExtensibleRecipe(final RandomAccessR rar, final int id)
            throws IOException {

        etid = id;
        top = (short) rar.readByteAsInt();
        mid = (short) rar.readByteAsInt();
        bot = (short) rar.readByteAsInt();
        rep = (short) rar.readByteAsInt();
    }

    /**
     * Returns the bot.
     * @return Returns the bot.
     */
    public short getBot() {

        return bot;
    }

    /**
     * Returns the mid.
     * @return Returns the mid.
     */
    public short getMid() {

        return mid;
    }

    /**
     * Returns the rep.
     * @return Returns the rep.
     */
    public short getRep() {

        return rep;
    }

    /**
     * Returns the top.
     * @return Returns the top.
     */
    public short getTop() {

        return top;
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("extensiblerecipe");
        writer.writeAttribute("id", String.valueOf(etid));
        writer.writeAttribute("top", String.valueOf(top));
        writer.writeAttribute("mid", String.valueOf(mid));
        writer.writeAttribute("bot", String.valueOf(bot));
        writer.writeAttribute("rep", String.valueOf(rep));
        writer.writeEndElement();
    }
}
