/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.dante.util.GeneralException;

/**
 * This class provides an Exception with the possibility to provide additional
 * help on the error encoutered. Thus it has two levels of information: the
 * first level is the message and the second level is the additional help.
 * <p>
 * Both information strings are mapped via the
 * {@link de.dante.extex.i18n.Messages Messages} apparatus. The key provided
 * to this Exception is used as a key to find the format in the Messages.
 * For the message it is used plain and for the help the string ".help" is
 * appended.
 * </p>
 * <h3>Example</h3>
 * <p>
 * Consider the following lines in the messages file:
 * </p>
 * <pre>
 * abc.def = This is the message
 * abc.def.help = This is the help text. \
 *               It can even span several lines.
 * </pre>
 * Then the following instruction can savely be used:
 * <pre>
 *     throw new HelpingException("abc.def");
 * </pre>
 * <p>
 * With this exception up to three arguments can be used. The String value of
 * those arguments are inserted into the message string for the placeholders
 * {0}, {1}, and {2}.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class HelpingException extends GeneralException {

    /**
     * The field <tt>defaultBundle</tt> contains the default resource bundle.
     */
    private static ResourceBundle defaultBundle = null;

    /**
     * Setter for the resource bundle to use.
     *
     * @param resource the ResourceBundle to use
     */
    public static void setResource(final ResourceBundle resource) {

        defaultBundle = resource;
    }

    /**
     * The field <tt>arg1</tt> contains the first argument.
     */
    private String arg1 = "?";

    /**
     * The field <tt>arg2</tt> contains the second argument.
     */
    private String arg2 = "?";

    /**
     * The field <tt>arg3</tt> contains the third argument.
     */
    private String arg3 = "?";

    /**
     * The field <tt>bundle</tt> contains the resource bundle to use.
     */
    private ResourceBundle bundle = null;

    /**
     * The field <tt>tag</tt> contains the name of the message to show.
     */
    private String tag = "GeneralDetailedException.help";

    /**
     * Creates a new object without variable arguments.
     *
     * @param messageTag the message
     *
     * deprecated use the method with the explicit resource bundle instead.
     */
    public HelpingException(final String messageTag) {

        super();
        tag = messageTag;
        bundle = defaultBundle;
    }

    /**
     * Creates a new object without variable arguments.
     *
     * @param messageTag the message
     * @param resource the resource bundle to use
     */
    public HelpingException(final ResourceBundle resource,
            final String messageTag) {

        super();
        tag = messageTag;
        bundle = resource;
    }

    /**
     * Creates a new object with one variable argument.
     *
     * @param messageTag the message
     * @param a1 the first argument
     *
     * deprecated use the method with the explicit resource bundle instead.
     */
    public HelpingException(final String messageTag, final String a1) {

        super();
        tag = messageTag;
        this.arg1 = a1;
        bundle = defaultBundle;
    }

    /**
     * Creates a new object with one variable argument.
     *
     * @param messageTag the message
     * @param a1 the first argument
     * @param resource the resource bundle to use
     */
    public HelpingException(final ResourceBundle resource,
            final String messageTag, final String a1) {

        super();
        tag = messageTag;
        this.arg1 = a1;
        bundle = resource;
    }

    /**
     * Creates a new object with two variable arguments.
     *
     * @param messageTag the message
     * @param a1 the first argument
     * @param a2 the second argument
     *
     * deprecated use the method with the explicit resource bundle instead.
     */
    public HelpingException(final String messageTag, final String a1,
            final String a2) {

        super();
        tag = messageTag;
        this.arg1 = a1;
        this.arg2 = a2;
        bundle = defaultBundle;
    }

    /**
     * Creates a new object with two variable arguments.
     *
     * @param messageTag the message
     * @param a1 the first argument
     * @param a2 the second argument
     * @param resource the resource bundle to use
     */
    public HelpingException(final ResourceBundle resource,
            final String messageTag, final String a1, final String a2) {

        super();
        tag = messageTag;
        this.arg1 = a1;
        this.arg2 = a2;
        bundle = resource;
    }

    /**
     * Creates a new object with three variable arguments.
     *
     * @param messageTag the message
     * @param a1 the first argument
     * @param a2 the second argument
     * @param a3 the third argument
     *
     * deprecated use the method with the explicit resource bundle instead.
     */

    public HelpingException(final String messageTag, final String a1,
            final String a2, final String a3) {

        super();
        tag = messageTag;
        this.arg1 = a1;
        this.arg2 = a2;
        this.arg3 = a3;
        bundle = defaultBundle;
    }

    /**
     * Creates a new object with three variable arguments.
     *
     * @param messageTag the message
     * @param a1 the first argument
     * @param a2 the second argument
     * @param a3 the third argument
     * @param resource the resource bundle to use
     */
    public HelpingException(final ResourceBundle resource,
            final String messageTag, final String a1, final String a2,
            final String a3) {

        super();
        tag = messageTag;
        this.arg1 = a1;
        this.arg2 = a2;
        this.arg3 = a3;
        bundle = resource;
    }

    /**
     * Getter for further help information.
     *
     * @return the help information
     */
    public String getHelp() {

        return MessageFormat.format(determineFormat(tag + ".help"),
                new Object[]{arg1, arg2, arg3});
    }

    /**
     * Getter for further help information.
     *
     * @return the help information
     */
    public String getMessage() {

        return MessageFormat.format(determineFormat(tag), new Object[]{arg1,
                arg2, arg3});
    }

    /**
     * Unfailing getter for a format string.
     *
     * @param name the key string for the format
     *
     * @return ...
     */
    protected String determineFormat(final String name) {

        String format;
        try {
            return bundle.getString(name);
        } catch (MissingResourceException e) {
            format = "???" + name + "???";
        } catch (NullPointerException e) {
            format = "???" + name + "???";
        }
        return format;
    }

}