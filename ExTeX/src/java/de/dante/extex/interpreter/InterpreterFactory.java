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

package de.dante.extex.interpreter;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.framework.AbstractFactory;

/**
 * Factory for the Interpreter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.16 $
 */
public class InterpreterFactory extends AbstractFactory {

    /**
     * Creates a new object.
     */
    public InterpreterFactory() {

        super();
    }

    /**
     * Creates a new object.
     *
     * @param configuration the configuration object to use
     *
     * @throws ConfigurationException in case that the attribute
     *             <tt>classname</tt> is missing
     * @deprecated use the constructor without arguments and configure()
     */
    public InterpreterFactory(final Configuration configuration)
            throws ConfigurationException {

        super();
        configure(configuration);
    }

    /**
     * Get a instance for the interface
     * <tt>{@link de.dante.extex.interpreter.Interpreter Interpreter}</tt>.
     *
     * @return a new instance for the interface Interpreter
     *
     * @throws ConfigurationException in case of an error in the configuration
     */
    public Interpreter newInstance() throws ConfigurationException {

        Interpreter interpreter = (Interpreter) createInstance(Interpreter.class);

        return interpreter;
    }
}