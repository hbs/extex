/*
 * Copyright (C) 2003-2004 The ExTeX Group
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
package de.dante.extex.scanner.stream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;

import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationWrapperException;
import de.dante.util.file.FileFinder;
import de.dante.util.file.FileFinderConfigImpl;
import de.dante.util.observer.NotObservableException;
import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;
import de.dante.util.observer.ObserverList;

/**
 * This is the factory to provide an instance of a TokenStream.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.14 $
 */
public class TokenStreamFactory implements FileFinder, Observable {

	/**
	 * The field <tt>openFileObservers</tt> contains the observers registered
	 * for the "file" event
	 */
	private ObserverList openFileObservers = new ObserverList();

	/**
	 * The field <tt>openStreamObservers</tt> contains the observers registered
	 * for the "stream" event.
	 */
	private ObserverList openStreamObservers = new ObserverList();

	/**
	 * The field <tt>openStringObservers</tt> contains the observers registered
	 * for the "string" event.
	 */
	private ObserverList openStringObservers = new ObserverList();

	/**
	 * The field <tt>classname_string</tt> contains the StringTokenizer
	 */
	private String classname_string;

	/**
	 * The field <tt>classname_file</tt> contains the FileTokenizer
	 */
	private String classname_file;

	/**
	 * The field <tt>classname_reader</tt> contains the ReaderTokenizer
	 */
	private String classname_reader;

	/**
	 * The field <tt>fileFinder</tt> contains the ...
	 */
	private FileFinder fileFinder;

	/**
	 * Creates a new object.
	 *
	 * @param config the configuration to use
	 *
	 * @throws ConfigurationException ...
	 */
	public TokenStreamFactory(final Configuration config) throws ConfigurationException {
		super();

		Configuration cfg_string = config.findConfiguration("string");
		if (cfg_string == null) {
			throw new ConfigurationMissingAttributeException("string", config);
		}
		this.classname_string = cfg_string.getAttribute("class");

		Configuration cfg_file = config.findConfiguration("file");
		if (cfg_file == null) {
			throw new ConfigurationMissingAttributeException("file", config);
		}
		this.classname_file = cfg_file.getAttribute("class");

		Configuration cfg_reader = config.findConfiguration("reader");
		if (cfg_reader == null) {
			throw new ConfigurationMissingAttributeException("reader", config);
		}
		this.classname_reader = cfg_reader.getAttribute("class");

		this.fileFinder = new FileFinderConfigImpl(config);
	}

	/**
	 * Provide a new instance of a token stream.
	 *
	 * @param line ...
	 * mgn: kill encoding: not neccessary for a string
	 * @return the new instance
	 *
	 * @throws ConfigurationException ...
	 */
	public TokenStream newInstance(final String line) throws ConfigurationException {
		TokenStream stream;

		try {
			stream = (TokenStream) Class.forName(classname_string).getConstructor(new Class[] { String.class }).newInstance(new Object[] { line });
		} catch (Exception e) {
			throw new ConfigurationInstantiationException(e);
		}

		try {
			openStringObservers.update(this, line);
		} catch (GeneralException e) {
			throw new ConfigurationWrapperException(e);
		}
		return stream;
	}

	/**
	 * Provide a new instance of a token stream.
	 *
	 * @param file ...
	 * @param type ...
	 * @param encoding the name of the encoding to use
	 *
	 * @return the new instance
	 *
	 * @throws ConfigurationException ...
	 * @throws FileNotFoundException ...
	 * @throws IOException ...
	 */
	public TokenStream newInstance(final String file, final String type, final String encoding)
		throws ConfigurationException, IOException, FileNotFoundException {

		TokenStream stream;
		File theFile = fileFinder.findFile(file, type);
		if (theFile == null) {
			throw new FileNotFoundException(file);
		}
		try {
			stream =
				(TokenStream) Class.forName(classname_file).getConstructor(new Class[] { File.class, String.class }).newInstance(
					new Object[] { theFile, encoding });
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();

			if (cause == null) {
				throw new ConfigurationInstantiationException(e);
			} else if (cause instanceof FileNotFoundException) {
				throw (FileNotFoundException) (e.getCause());
			} else if (cause instanceof IOException) {
				throw (IOException) (e.getCause());
			}

			throw new ConfigurationInstantiationException(e);
		} catch (Exception e) {
			throw new ConfigurationInstantiationException(e);
		}

		try {
			openFileObservers.update(this, file);
		} catch (GeneralException e) {
			throw new ConfigurationWrapperException(e);
		}
		return stream;
	}

	/**
	 * Provide a new instance of a token stream.
	 *
	 * @param reader ...
	 * mgn: delete encoding: reader already has an encoding
	 *
	 * @return the new instance
	 *
	 * @throws ConfigurationException ...
	 */
	public TokenStream newInstance(final InputStreamReader reader) throws ConfigurationException {

		TokenStream stream;

		try {
			stream = (TokenStream) Class.forName(classname_reader).getConstructor(new Class[] { Reader.class }).newInstance(new Object[] { reader });
		} catch (Exception e) {
			throw new ConfigurationInstantiationException(e);
		}

		try {
			openStreamObservers.update(this, reader);
		} catch (GeneralException e) {
			throw new ConfigurationWrapperException(e);
		}
		return stream;
	}

	/**
	 * @see de.dante.util.observer.Observable#registerObserver(java.lang.String,
	 *      de.dante.util.Observer)
	 */
	public void registerObserver(final String name, final Observer observer) throws NotObservableException {

		if ("file".equals(name)) {
			openFileObservers.add(observer);
		} else if ("stream".equals(name)) {
			openStreamObservers.add(observer);
		} else if ("string".equals(name)) {
			openStringObservers.add(observer);
		} else {
			throw new NotObservableException(name);
		}
	}

	/**
	 * Getter for the file finder.
	 *
	 * @return the file finder
	 */
	public FileFinder getFileFinder() {
		return fileFinder;
	}

	/**
	 * Setter for the file finder
	 *
	 * @param finder the new file finder
	 */
	public void setFileFinder(final FileFinder finder) {
		fileFinder = finder;
	}

	/**
	 * @see de.dante.util.file.FileFinder#findFile(java.lang.String,
	 *      java.lang.String)
	 */
	public File findFile(final String name, final String type) throws ConfigurationException {

		try {
			openFileObservers.update(this, name);
		} catch (GeneralException e) {
			throw new ConfigurationWrapperException(e);
		}
		return fileFinder.findFile(name, type);
	}

}
