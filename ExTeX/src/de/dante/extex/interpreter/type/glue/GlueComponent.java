/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.glue;

import java.io.Serializable;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.i18n.GeneralPanicException;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.DimenConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.util.GeneralException;

/**
 * This class provides a means to store floating numbers with an order.
 *
 * <p>Examples</p>
 * <pre>
 * 123 pt
 * -123 pt
 * 123.456 pt
 * 123.pt
 * .465 pt
 * -.456pt
 * +456pt
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class GlueComponent implements Serializable, FixedGlueComponent {

    /**
     * The constant <tt>FLOAT_DIGITS</tt> contains the number of digits to
     * consider when producing a string representation of this type.
     *
     * Attention: Do not change this value unless you have read and understood
     * TeX the program!
     */
    private static final int FLOAT_DIGITS = 17;

    /**
     * The constant <tt>ONE</tt> contains the internal representation for 1pt.
     * @see "TeX -- The Program [101]"
     */
    public static final long ONE = 1 << 16;

    /**
     * The field <tt>POINT_PER_100_IN</tt> contains the conversion factor from
     * inch to point. The value contained is the number of points in 100 inch.
     */
    private static final int POINT_PER_100_IN = 7227;

    /**
     * The field <tt>order</tt> contains the order of infinity.
     * In case of an order 0 the value holds the absolute value; otherwise
     * value holds the factor of the order.
     */
    private int order = 0;

    /**
     * The field <tt>value</tt> contains the integer representatation of the
     * dimen register in sp if the order is 0.
     * If the order is not 0 then the value holds the factor to the order in
     * units of 2<sup>16</sup>.
     */
    private long value = 0;

    /**
     * Creates a new object.
     */
    public GlueComponent() {

        super();
    }

    /**
     * Creates a new object.
     *
     * @param context the interpreter context
     * @param source the source for the tokens to be read
     * @param fixed if <code>true</code> then no glue order is allowed
     *
     * @throws GeneralException in case of an error
     */
    public GlueComponent(final Context context, final TokenSource source,
            final boolean fixed) throws GeneralException {

        super();
        set(context, source, fixed);
    }

    /**
     * Creates a new object with a fixed width.
     *
     * @param theValue the fixed value
     */
    public GlueComponent(final long theValue) {

        super();
        this.value = theValue;
    }

    /**
     * Creates a new object with a width with a possibly higher order.
     *
     * @param theValue the fixed width or the factor
     * @param theOrder the order
     */
    public GlueComponent(final long theValue, final int theOrder) {

        super();
        this.value = theValue;
        this.order = theOrder;
    }

    /**
     * Creates a new object from a TokenStream.
     *
     * @param source the source for new tokens
     * @param context the interpreter context
     *
     * @throws GeneralException in case of an error
     */
    public GlueComponent(final TokenSource source, final Context context)
            throws GeneralException {

        this(context, source, false);
    }

    /**
     * Parses a token stream for a float and returns it as fixed point number.
     *
     * @param source the source for new tokens
     * @param start the initial token to start with
     *
     * @return the fixed point representation of the floating number in units
     * of 2<sup>16</sup>.
     *
     * @throws GeneralException in case of an error
     */
    public static long scanFloat(final TokenSource source, final Token start)
            throws GeneralException {

        boolean neg = false;
        long val = 0;
        int post = 0;
        Token t = start;
        if (t == null) {
            return 0;
        } else if (t.equals(Catcode.OTHER, "-")) {
            neg = true;
            t = source.getNonSpace();
        } else if (t.equals(Catcode.OTHER, "+")) {
            t = source.getNonSpace();
        }
        if (t != null && !t.equals(Catcode.OTHER, ".")
            && !t.equals(Catcode.OTHER, ",")) {
            val = source.scanNumber(t);
            t = source.getToken();
        }
        if (t != null
            && (t.equals(Catcode.OTHER, ".") || t.equals(Catcode.OTHER, ","))) {
            // @see "TeX -- The Program [102]"
            int[] dig = new int[FLOAT_DIGITS];
            int k = 0;
            for (t = source.getToken(); t != null && t.isa(Catcode.OTHER)
                                        && t.getValue().matches("[0-9]"); t = source
                    .getToken()) {
                if (k < FLOAT_DIGITS) {
                    dig[k++] = t.getValue().charAt(0) - '0';
                }
            }
            if (k < FLOAT_DIGITS) {
                k = FLOAT_DIGITS;
            }
            post = 0;
            while (k-- > 0) {
                post = (post + dig[k] * (1 << FLOAT_DIGITS)) / 10;
            }
            post = (post + 1) / 2;
        }
        source.push(t);
        val = val << 16 | post;
        return (neg ? -val : val);
    }

    /**
     * Add another GlueCoponent g to this instance.
     * If the order of g is greater than the order of this instance then this
     * operation does not change the value or order at all.
     * If the order of g is less than the order of this instance then the value
     * and order of g are stored in this instance.
     * If the orders agree then the sum of both values is stored in this
     * instance.
     *
     * @param g the GlueCoponent to add
     */
    public void add(final FixedGlueComponent g) {

        int o = g.getOrder();
        if (order == o) {
            value += g.getValue();
        } else if (order < o) {
            order = o;
            value = g.getValue();
        }
    }

    /**
     * Create a copy of this instance with the same order and value.
     *
     * @return a new copy of this instance
     */
    public GlueComponent copy() {

        return new GlueComponent(value, order);
    }

    /**
     * Compares the current instance with another GlueComponent for equality.
     *
     * @param d the other GlueComponent to compare to. If this parameter is
     * <code>null</code> then the comparison fails.
     *
     * @return <code>true</code> iff <i>|this| == |d| and ord(this) == ord(d)</i>
     */
    public boolean eq(final FixedGlueComponent d) {

        return (d != null && //
                value == d.getValue() && //
        order == d.getOrder());
    }

    /**
     * Compares the current instance with another GlueComponent.
     *
     * @param d the other GlueComponent to compare to
     *
     * @return <code>true</code> iff this is greater or equal to d
     *
     * @throws NullPointerException in case that the argument is
     * <code>null</code>.
     */
    public boolean ge(final FixedGlueComponent d) {

        return (!lt(d));
    }

    /**
     * Getter for the value in scaled points (sp).
     *
     * @return the value in internal units of scaled points (sp)
     */
    public long getValue() {

        return this.value;
    }

    /**
     * Compares the current instance with another GlueComponent.
     *
     * @param d the other GlueComponent to compare to
     *
     * @return <code>true</code> iff <i>ord(this) == ord(d) && |this| &gt; |d|</i>
     * or <i>ord(this) &gt; ord(d)</i>
     *
     * @throws NullPointerException in case that the argument is
     * <code>null</code>.
     */
    public boolean gt(final FixedGlueComponent d) {

        return ((order == d.getOrder() && value > d.getValue()) || //
        order > d.getOrder());
    }

    /**
     * Compares the current instance with another GlueComponent.
     *
     * @param d the other GlueComponent to compare to
     *
     * @return <code>true</code> iff this is less or equal to d
     *
     * @throws NullPointerException in case that the argument is
     * <code>null</code>.
     */
    public boolean le(final FixedGlueComponent d) {

        return (!gt(d));
    }

    /**
     * Compares the current instance with another GlueComponent.
     *
     * @param d the other GlueComponent to compare to
     *
     * @return <code>true</code> iff <i>ord(this) == ord(d) && |this| &lt; |d|</i>
     * or <i>ord(this) &lt; ord(d)</i>
     *
     * @throws NullPointerException in case that the argument is
     * <code>null</code>.
     */
    public boolean lt(final FixedGlueComponent d) {

        return ((order == d.getOrder() && value < d.getValue()) || //
        order < d.getOrder());
    }

    /**
     * Multiply the value by an integer fraction.
     * <p>
     *  <i>length</i> = <i>length</i> * <i>nom</i> / <i>denom</i>
     * </p>
     *
     * @param nom nominator
     * @param denom denominator
     */
    public void multiply(final long nom, final long denom) {

        this.value = this.value * nom / denom;
    }

    /**
     * Set the value and order from the data gathered by parsing a token source.
     *
     * @param context the interpreter context
     * @param source the source for next tokens
     *
     * @throws GeneralException in case of an error
     */
    public void set(final Context context, final TokenSource source)
            throws GeneralException {

        set(context, source, true);
    }

    /**
     * Setter for the value and order.
     *
     * @param d the new value
     */
    public void set(final FixedGlueComponent d) {

        this.value = d.getValue();
        this.order = d.getOrder();
    }

    /**
     * Setter for the value in terms of the internal representation.
     *
     * @param theValue the new value
     */
    public void set(final long theValue) {

        this.value = theValue;
    }

    /**
     * Setter for the value.
     *
     * @param val the new value
     */
    public void setValue(final long val) {

        this.value = val;
    }

    /**
     * Determine the printable representation of the object.
     *
     * @return the printable representation
     *
     * @see #toString(StringBuffer)
     * @see #toToks(TokenFactory)
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * Determine the printable representation of the object and append it to
     * the given StringBuffer.
     *
     * @param sb the output string buffer
     *
     * @see #toString()
     */
    public void toString(final StringBuffer sb) {

        long val = getValue();

        if (val < 0) {
            sb.append('-');
            val = -val;
        }

        long v = val / ONE;
        if (v == 0) {
            sb.append('0');
        } else {
            long m = 1;
            while (m <= v) {
                m *= 10;
            }
            m /= 10;
            while (m > 0) {
                sb.append((char) ('0' + (v / m)));
                v = v % m;
                m /= 10;
            }
        }

        sb.append('.');

        val = 10 * (val % ONE) + 5;
        long delta = 10;
        do {
            if (delta > ONE) {
                val = val + 0100000 - 50000; // round the last digit
            }
            int i = (int) (val / ONE);
            sb.append((char) ('0' + i));
            val = 10 * (val % ONE);
            delta *= 10;
        } while (val > delta);

        if (order == 0) {
            sb.append('p');
            sb.append('t');
        } else if (order > 0) {
            sb.append('f');
            sb.append('i');
            for (int i = order; i > 0; i--) {
                sb.append('l');
            }
        } else {
            throw new RuntimeException("This can't happen."); //TODO incomplete
        }
    }

    /**
     * Determine the printable representation of the object and return it as a
     * list of Tokens.
     * The value returned is exactely the string which would be produced by
     * TeX to print the Dimen. This means the result is expressed in pt and
     * properly rounded to be read back in again without loss of information.
     *
     * @param factory the token factory to get the required tokens from
     *
     * @return the printable representation
     *
     * @throws GeneralException in case of an error
     *
     * @see "TeX -- The Program [103]"
     * @see #toToks(TokenFactory)
     * @see #toString()
     * @see #toString(StringBuffer)
     */
    public Tokens toToks(final TokenFactory factory) throws GeneralException {

        Tokens toks = new Tokens();
        toToks(toks, factory);
        return toks;
    }

    /**
     * Determine the printable representation of the object and return it as a
     * list of Tokens.
     * The value returned is exactely the string which would be produced by
     * TeX to print the Dimen. This means the result is expressed in pt and
     * properly rounded to be read back in again without loss of information.
     *
     * @param toks the tokens to append to
     * @param factory the token factory to get the required tokens from
     *
     * @throws GeneralException in case of an error
     *
     * @see "TeX -- The Program [103]"
     * @see #toToks(TokenFactory)
     * @see #toString()
     * @see #toString(StringBuffer)
     */
    public void toToks(final Tokens toks, final TokenFactory factory)
            throws GeneralException {

        long val = getValue();

        if (val < 0) {
            toks.add(factory.newInstance(Catcode.OTHER, '-'));
            val = -val;
        }

        long v = val / ONE;
        if (v == 0) {
            toks.add(factory.newInstance(Catcode.OTHER, '0'));
        } else {
            long m = 1;
            while (m <= v) {
                m *= 10;
            }
            m /= 10;
            while (m > 0) {
                toks.add(factory.newInstance(Catcode.OTHER,
                                             (char) ('0' + (v / m))));
                v = v % m;
                m /= 10;
            }
        }

        toks.add(factory.newInstance(Catcode.OTHER, '.'));

        val = 10 * (val % ONE) + 5;
        long delta = 10;
        do {
            if (delta > ONE) {
                val = val + 0100000 - 50000; // round the last digit
            }
            int i = (int) (val / ONE);
            toks.add(factory.newInstance(Catcode.OTHER, (char) ('0' + i)));
            val = 10 * (val % ONE);
            delta *= 10;
        } while (val > delta);

        if (order == 0) {
            toks.add(factory.newInstance(Catcode.LETTER, 'p'));
            toks.add(factory.newInstance(Catcode.LETTER, 't'));
        } else if (order > 0) {
            toks.add(factory.newInstance(Catcode.LETTER, 'f'));
            toks.add(factory.newInstance(Catcode.LETTER, 'i'));
            for (int i = order; i > 0; i--) {
                toks.add(factory.newInstance(Catcode.LETTER, 'l'));
            }
        } else {
            throw new GeneralPanicException("TTP.Confusion");
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.glue.FixedGlueComponent#getOrder()
     */
    public int getOrder() {

        return order;
    }

    /**
     * Set the value and order from the data gathered by parsing a token source.
     *
     * @param context the interpreter context
     * @param source the source for next tokens
     * @param fixed this argument indicates that no fil parts of the object
     * should be filled. This means that the component is in fact a fixed
     * Dimen value.
     *
     * @throws GeneralException in case of an error
     */
    protected void set(final Context context, final TokenSource source,
            final boolean fixed) throws GeneralException {

        Token t = source.scanNonSpace();
        if (t == null) {
            throw new GeneralHelpingException("TTP.IllegalUnit");
        }

        value = scanFloat(source, t);

        t = source.getNonSpace();
        if (t == null) {
            throw new GeneralHelpingException("TTP.IllegalUnit");
        }

        source.push(t);
        long mag = 1000;
        if (source.getKeyword("true")) { // cf. TTP[453], TTP[457]
            mag = context.getMagnification();
            source.push(source.scanNonSpace());
        }
        // cf. TTP[458]
        if (source.getKeyword("pt")) {
            // nothing to do
        } else if (source.getKeyword("sp")) {
            value = value / ONE;
        } else if (source.getKeyword("mm")) {
            value = value * POINT_PER_100_IN / 2540;
        } else if (source.getKeyword("cm")) {
            value = value * POINT_PER_100_IN / 254;
        } else if (source.getKeyword("in")) {
            value = value * POINT_PER_100_IN / 100;
        } else if (source.getKeyword("pc")) {
            value = value * 12;
        } else if (source.getKeyword("bp")) {
            value = value * POINT_PER_100_IN / 7200;
        } else if (source.getKeyword("dd")) {
            value = value * 1238 / 1157;
        } else if (source.getKeyword("cc")) {
            value = value * 14856 / 1157;
        } else if (source.getKeyword("ex")) {
            Dimen ex = context.getTypesettingContext().getFont().getEm();
            value = value * ex.getValue() / ONE;
        } else if (source.getKeyword("em")) {
            Dimen em = context.getTypesettingContext().getFont().getEm();
            value = value * em.getValue() / ONE;
        } else if (fixed && source.getKeyword("fil")) {
            order = 1;
            for (t = source.getToken(); //
            (t != null && (t.equals('l') || t.equals('L'))); //
            t = source.getToken()) {
                order++;
            }
            source.push(t);
        } else if ((t = source.getToken()) != null) {
            if (t instanceof CodeToken) {
                Code code = context.getCode(t);
                if (code instanceof DimenConvertible) {
                    value = value
                            * ((DimenConvertible) code).convertDimen(context,
                                                                     source)
                            / ONE;
                } else {
                    throw new GeneralHelpingException("TTP.IllegalUnit");
                }
            } else {
                throw new GeneralHelpingException("TTP.IllegalUnit");
            }
        } else { // cf. TTP [459]
            throw new GeneralHelpingException("TTP.IllegalUnit");
        }

        if (mag != 1000) {
            value = value * mag / 1000;
        }
    }

}