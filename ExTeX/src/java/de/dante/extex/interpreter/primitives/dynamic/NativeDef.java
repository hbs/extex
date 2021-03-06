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

package de.dante.extex.interpreter.primitives.dynamic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This primitive provides a binding of a macro or active character to
 * code in some programming language.
 * This code implements the primitive <tt>\nativedef</tt>.
 *
 * <doc name="nativedef">
 * <h3>The Primitive <tt>\nativedef</tt></h3>
 * <p>
 *  The primitive <tt>\nativedef</tt> assigns a definition to a macro or
 *  active character. This is done in a similar way as <tt>\def</tt>
 *  works. The difference is that the definition has to be provided in
 *  form of a Java class which glues in native code.
 * </p>
 *
 * <h4>Syntax</h4>
 * The general form of this primitive is
 * <pre class="syntax">
 *   &lang;nativedef&rang;
 *       &rarr; <tt>\nativedef</tt> &lang;type&rang; {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getControlSequence(Context)
 *       &lang;control sequence&rang;} &lang;name&rang;  </pre>
 * <p>
 *  The <tt>&lang;type&rang;</tt> is any specification of a list of
 *  tokens like a constant list enclosed in braces or a token register.
 *  The value of these tokens are taken and resolved via the configuration.
 *  This appropriate class is loaded if needed and instantiated. The
 *  instance is bound as code to the <i>&lang;control sequence&rang;</i>.
 * </p>
 * <p>
 *  The <tt>&lang;control sequence&rang;</tt> is any macro or active
 *  character. If this token is missing or of the wrong type then an
 *  error is raised.
 * </p>
 * <p>
 *  The <tt>&lang;name&rang;</tt> is any specification of a list of
 *  tokens like a constant list enclosed in braces or a token register.
 *  The value of these tokens are passed to the binding class to specify the
 *  target. For instance the Java binding requires this to be name of the
 *  Java class implementing the functionality.
 * </p>
 * <p>
 *  The primitive <tt>\nativedef</tt> is local to the enclosing group as
 *  is <tt>\def</tt>. And similar to <tt>\def</tt> the modifier
 *  <tt>\global</tt> can be used to make the definition in all groups
 *  instead of the current group only.
 * </p>
 * <p>
 *  The primitive <tt>\nativedef</tt> also respects the count register
 *  <tt>\globaldefs</tt> to enable general global assignment.
 * </p>
 * <p>
 *  Since the primitive is classified as assignment the value of
 *  <tt>\afterassignment</tt> is applied.
 * </p>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \nativedef{java}\x{my.primitive.MyPrimitive}  </pre>
 * <p>
 *  This example shows how the control sequence <tt>\x</tt> is bound to the
 *  Java class <tt>my.primitive.MyPrimitive</tt>. This definition is local to
 *  the current group.
 * </p>
 *  <pre class="TeXSample">
 *    \global\nativedef{java}\x{my.primitive.MyPrimitive}  </pre>
 * <p>
 *  This example shows how the control sequence <tt>\x</tt> is bound to the
 *  Java class <tt>my.primitive.MyPrimitive</tt>. This definition is performed
 *  globally.
 * </p>
 *
 * <h4>Configuration</h4>
 * <p>
 *  The supported types are determined in the configuration of the unit which
 *  defines the primitive. Here a
 *  mapping is specified assigning a binding class for each supported type.
 *  Thus it is possible to configure in the support for several extension types.
 *  Currently a binding for Java is provided. In the future other languages can
 *  be added easily.
 * </p>
 *
 * <pre class="Configuration">
 *  &lt;define name="nativedef"
 *          class="de.dante.extex.interpreter.primitives.dynamic.NativeDef"&gt;
 *    &lt;load name="java"
 *          class="de.dante.extex.interpreter.primitives.dynamic.java.JavaDef"/&gt;
 *  &lt;/define&gt;
 * </pre>
 *
 * <p>
 *  The body of the define tag for the primitive may contain an arbitrary number
 *  of load sections. Each load has the attribute name and class. The attribute
 *  name determines the type. This corresponds to the type given in the first
 *  argument of the primitive invocation.
 * </p>
 * <p>
 *  The class attribute names the class which provides the binding to the
 *  target programming language.
 * </p>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class NativeDef extends AbstractAssignment
        implements
            Configurable,
            LogEnabled {

    /**
     * This inner class provides access to the functionality of an abstract
     * factory. It is here to overcome the deficiency of a missing multiple
     * inheritance in Java.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.12 $
     */
    protected class Factory extends AbstractFactory {

        /**
         * Create a new instance of the class given by the attribute
         * <tt>class</tt> of the configuration.
         *
         * @return the Code loaded
         * @throws ConfigurationException in case of an error
         */
        public Definer createLoad() throws ConfigurationException {

            return (Definer) createInstanceForConfiguration(getConfiguration(),
                    Definer.class);
        }
    }

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2006L;

    /**
     * The field <tt>logger</tt> contains the logger to use.
     */
    private transient Logger logger = null;

    /**
     * The field <tt>map</tt> contains the mapping from a symbolic name to a
     * configuration.
     */
    private Map map = new HashMap();

    /**
     * Creates a new object.
     *
     * @param codeName the name of the primitive
     */
    public NativeDef(final String codeName) {

        super(codeName);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractAssignment#assign(
     *       de.dante.extex.interpreter.Flags,
     *       de.dante.extex.interpreter.context.Context,
     *       de.dante.extex.interpreter.TokenSource,
     *       de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        String name = source.getTokens(context, source, typesetter).toText();
        Configuration cfg = (Configuration) map.get(name);
        if (cfg == null) {
            throw new InterpreterException(getLocalizer().format("UnknownType",
                    name, getName()));
        }

        Factory factory = new Factory();
        factory.enableLogging(logger);
        try {
            factory.configure(cfg);
            factory.createLoad().define(prefix, context, source, typesetter);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.framework.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        Iterator iterator = config.iterator("load");
        while (iterator.hasNext()) {
            Configuration cfg = (Configuration) iterator.next();
            map.put(cfg.getAttribute("name"), cfg);
        }
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * Getter for logger.
     *
     * @return the logger
     */
    protected Logger getLogger() {

        return this.logger;
    }

}
