/*
 * Copyright (C) 2003  Gerd Neugebauer
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */
package de.dante.extex.scanner;



/**
 * This is the abstract base class for all Tokens.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractToken implements Token {
    /** The value of the token */
    protected String value;

    /**
     * Creates a new object.
     *
     * @param value the value of the token
     */
    protected AbstractToken(String value) {
        super();
        this.value = value;
    }

    /**
     * This abstract class forces a derived class to overwrite this definition.
     *
     * @see de.dante.extex.scanner.Token#getCatcode()
     */
    public abstract Catcode getCatcode();

    /**
     * Return the printable representation of this object
     *
     * @return the printable representation
     */
    public abstract String toString();

    /**
     * @see de.dante.extex.scanner.Token#getValue()
     */
    public String getValue() {
        return value;
    }

    /**
     * @see de.dante.extex.scanner.Token#equals(de.dante.extex.scanner.Token)
     */
    public boolean equals(Token t) {
        return this == t ||
               (getCatcode() == t.getCatcode() &&
               value.equals(t.getValue()));
    }

    /**
     * @see de.dante.extex.scanner.Token#equals(Catcode,String)
     */
    public boolean equals(Catcode cc, String s) {
        return getCatcode() == cc && value.equals(s);
    }

    /**
     * @see de.dante.extex.scanner.Token#equals(de.dante.extex.scanner.Catcode, char)
     */
    public boolean equals(Catcode cc, char c) {
        return getCatcode() == cc && value.length() == 1 &&
               value.charAt(0) == c;
    }

    /**
     * @see de.dante.extex.scanner.Token#isa(Catcode)
     */
    public boolean isa(Catcode cc) {
        return getCatcode() == cc;
    }
}
