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

import java.util.logging.Logger;

import de.dante.extex.backend.outputStream.OutputStreamFactory;
import de.dante.extex.backend.outputStream.TrivialOutputFactory;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.OutputStreamConsumer;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.interpreter.unit.LoadUnit;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationNotFoundException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This primitive initiates the loading of native code and implements the
 * primitive <tt>\ensureloaded</tt>
 *
 * <doc name="ensureloaded">
 * <h3>The Primitive <tt>\ensureloaded</tt></h3>
 * <p>
 *  The primitive <tt>\ensureloaded</tt> dynamically requests that a unit of
 *  <logo>ExTeX</logo> is loaded.
 * </p>
 * <p>
 *  A unit consists of primitives and some initializing actions.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The general form of this primitive is
 * <pre class="syntax">
 *   &lang;ensureloaded&rang;
 *       &rarr; <tt>\ensureloaded</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;} </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \ensureloaded{etex}  </pre>
 *  <pre class="TeXSample">
 *    \ensureloaded\toks0  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class EnsureLoaded extends AbstractCode
        implements
            OutputStreamConsumer,
            LogEnabled {

    /**
     * The constant <tt>CONFIG_UNIT</tt> contains the prefix for the
     * path of the configuration.
     */
    private static final String CONFIG_UNIT = "config/unit/";

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>logger</tt> contains the logger to use.
     */
    private transient Logger logger = null;

    /**
     * The field <tt>outFactory</tt> contains the output factory.
     */
    private transient OutputStreamFactory outFactory;

    /**
     * Creates a new object.
     *
     * @param codeName the name of the primitive
     */
    public EnsureLoaded(final String codeName) {

        super(codeName);
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *       de.dante.extex.interpreter.Flags,
     *       de.dante.extex.interpreter.context.Context,
     *       de.dante.extex.interpreter.TokenSource,
     *       de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Tokens tokens = source.scanTokens(context, false, false, getName());

        if (tokens == null) {
            throw new EofException(getName());
        }
        String configName = tokens.toText();
        try {
            Configuration configuration = new ConfigurationFactory()
                    .newInstance(CONFIG_UNIT + configName);
            LoadUnit.loadUnit(configuration, context, source, typesetter,
                    logger, outFactory);
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        } catch (ConfigurationNotFoundException e) {
            throw new HelpingException(getLocalizer(), "UnknownUnit",
                    configName);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * Getter for logger.
     *
     * @return the logger
     */
    protected Logger getLogger() {

        return this.logger;
    }

    /**
     * @see de.dante.extex.interpreter.type.OutputStreamConsumer#setOutputStreamFactory(
     *      de.dante.extex.backend.outputStream.OutputStreamFactory)
     */
    public void setOutputStreamFactory(final OutputStreamFactory factory) {

        this.outFactory = factory;
    }

}
