/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import junit.framework.TestCase;
import de.dante.extex.ExTeX;
import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FontFactory;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.interpreter.ErrorHandler;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.main.errorHandler.editHandler.EditHandler;
import de.dante.extex.main.logging.LogFormatter;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.scanner.type.token.Token;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This base class for test cases handles all the nifty gritty details of
 * running an instance of <logo>ExTeX</logo>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.42 $
 */
public class ExTeXLauncher extends TestCase {

    /**
     * Inner class for the error handler.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.42 $
     */
    private class EHandler implements ErrorHandler {

        /**
         * The field <tt>logger</tt> contains the target logger.
         */
        private Logger logger;

        /**
         * Creates a new object.
         *
         * @param theLogger the target logger
         */
        public EHandler(final Logger theLogger) {

            super();
            this.logger = theLogger;
        }

        /**
         * @see de.dante.extex.interpreter.ErrorHandler#handleError(
         *      de.dante.util.GeneralException,
         *      de.dante.extex.scanner.type.token.Token,
         *      de.dante.extex.interpreter.TokenSource,
         *      de.dante.extex.interpreter.context.Context)
         */
        public boolean handleError(final GeneralException e, final Token token,
                final TokenSource source, final Context context) {

            logger.log(Level.SEVERE, e.getLocalizedMessage());
            return false;
        }

        /**
         * @see de.dante.extex.interpreter.ErrorHandler#setEditHandler(
         *       de.dante.extex.main.errorHandler.editHandler.EditHandler)
         */
        public void setEditHandler(final EditHandler editHandler) {

        }
    }

    /**
     * The constant <tt>DEFINE_BRACES</tt> contains the definition of the
     * usual category codes for braces.
     */
    public static final String DEFINE_BRACES = "\\catcode`\\{=1 "
            + "\\catcode`\\}=2 ";

    /**
     * The constant <tt>DEFINE_CATCODES</tt> contains the definition of the
     * usual category codes.
     */
    public static final String DEFINE_CATCODES = "\\catcode`\\{=1 "
            + "\\catcode`\\}=2 " + "\\catcode`\\$=3 " + "\\catcode`\\&=4 "
            + "\\catcode`\\#=6 " + "\\catcode`\\^=7 " + "\\catcode`\\_=8 "
            + "\\catcode`\\^^I=10 ";

    /**
     * The constant <tt>DEFINE_HASH</tt> contains the definition of the
     * usual category codes.
     */
    public static final String DEFINE_HASH = "\\catcode`\\#=6 ";

    /**
     * The constant <tt>DEFINE_MATH</tt> contains the definition of the
     * catcode for math shift.
     */
    public static final String DEFINE_MATH = "\\catcode`\\$=3 ";

    /**
     * The field <tt>levelMap</tt> contains the mapping for debug levels from
     * String representation to Level values.
     */
    private static final Map LEVEL_MAP = new HashMap();

    /**
     * The constant <tt>TERM</tt> contains the terminating string for output.
     */
    public static final String TERM = "\n\n";

    /**
     * Set some properties to default values. The properties set are:
     * <dl>
     * <dt><tt>extex.output</tt></dt><dd>Preset to <tt>out</tt></dd>
     * <dt><tt>extex.interaction</tt></dt><dd>Preset to <tt>batchmode</tt></dd>
     * <dt><tt>extex.fonts</tt></dt><dd>Preset to <tt>src/fonts</tt></dd>
     * </dl>
     *
     * @param properties the properties to adapt
     */
    private static void prepareProperties(final Properties properties) {

        provide(properties, "extex.output", "out");
        provide(properties, "extex.interaction", "batchmode");
        provide(properties, "extex.fonts", "src/font");
        provide(properties, "extex.nobanner", "true");

    }

    /**
     * Set a property if it has not been set yet.
     *
     * @param properties the properties to modify
     * @param name the name of the property
     * @param value the new value
     */
    private static void provide(final Properties properties, final String name,
            final String value) {

        if (properties.getProperty(name) == null) {
            properties.setProperty(name, value);
        }
    }

    {
        LEVEL_MAP.put("config", Level.CONFIG);
        LEVEL_MAP.put("info", Level.INFO);
        LEVEL_MAP.put("warning", Level.WARNING);
        LEVEL_MAP.put("severe", Level.SEVERE);
        LEVEL_MAP.put("fine", Level.FINE);
        LEVEL_MAP.put("finer", Level.FINER);
        LEVEL_MAP.put("finest", Level.FINEST);
    }

    /**
     * The field <tt>props</tt> contains the merged properties from the
     * system properties and the properties loaded from <tt>.extex-test</tt>.
     */
    private Properties props = null;

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public ExTeXLauncher(final String arg) {

        super(arg);
    }

    /**
     * Run some code through <tt>Interpreter</tt> which is expected to fail.
     *
     * @param properties the properties to modify
     * @param code the code to expand
     * @param log the expected output on the log stream
     *
     * @return a new instance of the <tt>Interpreter</tt> class which has been
     *  used for the test run. This object can be inspected in additional
     *  asserts.
     *
     * @throws Exception in case of an error
     */
    public Interpreter assertFailure(final Properties properties,
            final String code, final String log) throws Exception {

        return assertOutput(properties, code, log, "");
    }

    /**
     * Run some code through <tt>Interpreter</tt> which is expected to fail.
     *
     * @param code the code to expand
     * @param log the expected output on the log stream
     *
     * @return a new instance of the <tt>Interpreter</tt> class which has been
     *  used for the test run. This object can be inspected in additional
     *  asserts.
     *
     * @throws Exception in case of an error
     */
    public Interpreter assertFailure(final String code, final String log)
            throws Exception {

        return assertOutput(getProps(), code, log, "");
    }

    /**
     * Run some code through <logo>ExTeX</logo>.
     *
     * @param properties the properties to start with
     * @param code the code to expand
     * @param log the expected output on the log stream
     * @param expect the expected output on the output stream
     *
     * @return a new instance of the <tt>Interpreter</tt> class which has been
     *  used for the test run. This object can be inspected in additional
     *  asserts.
     *
     * @throws InterpreterException in case of an error
     */
    public Interpreter assertOutput(final Properties properties,
            final String code, final String log, final String expect)
            throws InterpreterException {

        boolean errorP = false;

        prepareProperties(properties);
        properties.setProperty("extex.code", code);
        properties.setProperty("extex.file", "");
        properties.setProperty("extex.nobanner", "true");
        properties.setProperty("extex.config", getConfig());

        ExTeX extex = new ExTeX(properties) {

            /**
             * @see de.dante.extex.ExTeX#makeInterpreter(de.dante.util.framework.configuration.Configuration, de.dante.extex.scanner.stream.TokenStreamFactory, de.dante.extex.font.FontFactory)
             */
            protected Interpreter makeInterpreter(final Configuration config,
                    final TokenStreamFactory factory,
                    final FontFactory fontFactory)
                    throws ConfigurationException,
                        GeneralException,
                        FontException,
                        IOException {

                Interpreter interpreter = super.makeInterpreter(config,
                        factory, fontFactory);
                interpreter.getContext().set(new Font() {

                    public UnicodeChar getHyphenChar() {

                        return null;
                    }

                    public UnicodeChar getSkewChar() {

                        return null;
                    }

                    public void setHyphenChar(final UnicodeChar hyphen) {

                    }

                    public void setSkewChar(final UnicodeChar skew) {

                    }

                    public void setFontDimen(final String key, final Dimen value) {

                    }

                    public Glyph getGlyph(final UnicodeChar c) {

                        return new Glyph() {

                            public Dimen getDepth() {

                                return Dimen.ONE_PT;
                            }

                            public void setDepth(final Dimen d) {

                            }

                            public Dimen getHeight() {

                                return Dimen.ONE_PT;
                            }

                            public void setHeight(final Dimen h) {

                            }

                            public Dimen getItalicCorrection() {

                                return Dimen.ZERO_PT;
                            }

                            public void setItalicCorrection(final Dimen d) {

                            }

                            public Dimen getWidth() {

                                return Dimen.ONE_PT;
                            }

                            public void setWidth(final Dimen w) {

                            }

                            public String getName() {

                                return "?";
                            }

                            public void setName(final String n) {

                            }

                            public String getNumber() {

                                return c.toString();
                            }

                            public void setNumber(final String nr) {

                            }

                            public void addKerning(final Kerning kern) {

                            }

                            public Dimen getKerning(final UnicodeChar uc) {

                                return Dimen.ZERO_PT;
                            }

                            public void addLigature(final Ligature lig) {

                            }

                            public UnicodeChar getLigature(final UnicodeChar uc) {

                                return null;
                            }

                            public FontByteArray getExternalFile() {

                                return null;
                            }

                            public void setExternalFile(final FontByteArray file) {

                            }

                            public Dimen getLeftSpace() {

                                return Dimen.ZERO_PT;
                            }

                            public void setLeftSpace(final Dimen ls) {

                            }

                            public Dimen getRightSpace() {

                                return Dimen.ZERO_PT;
                            }

                            public void setRightSpace(Dimen rs) {

                            }};
                    }

                    public Glue getSpace() {

                        return new Glue(Dimen.ONE_PT.getValue() * 10);
                    }

                    public Dimen getEm() {

                        return new Dimen(Dimen.ONE_PT.getValue() * 10);
                    }

                    public Dimen getEx() {

                        return new Dimen(Dimen.ONE_PT.getValue() * 5);
                    }

                    public Dimen getFontDimen(final String key) {

                        return new Dimen(Dimen.ZERO_PT.getValue());
                    }

                    public String getProperty(final String key) {

                        return null;
                    }

                    public String getFontName() {

                        return "testfont";
                    }

                    public int getCheckSum() {

                        return 0;
                    }

                    public BoundingBox getBoundingBox() {

                        return null;
                    }

                    public Glue getLetterSpacing() {

                        return new Glue(Dimen.ZERO_PT.getValue());
                    }

                    public Dimen getDesignSize() {

                        return new Dimen(Dimen.ONE_PT.getValue() * 10);
                  }

                    public Dimen getActualSize() {

                        return new Dimen(Dimen.ONE_PT.getValue() * 10);
                    }

                    public FountKey getFontKey() {

                        return null;
                    }

                    public FontByteArray getFontByteArray() {

                        return null;
                    }
                }, true);
                return interpreter;
            }

        };

        Level level = getLogLevel(properties);
        Logger logger = Logger.getLogger("test");
        logger.setUseParentHandlers(false);
        logger.setLevel(level);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Handler handler = new StreamHandler(byteStream, new LogFormatter());
        handler.setLevel(level);
        logger.addHandler(handler);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        extex.setOutStream(stream);
        extex.setErrorHandler(new EHandler(logger));
        extex.setLogger(logger);

        if (Boolean.valueOf(
                properties.getProperty("extex.launcher.verbose", "false"))
                .booleanValue()) {
            System.err.println(code);
        }

        init(extex);

        Interpreter interpreter = null;
        try {
            interpreter = extex.run();
        } catch (CharacterCodingException e) {
            errorP = true;
        } catch (ConfigurationException e) {
            errorP = true;
        } catch (IOException e) {
            errorP = true;
        } catch (GeneralException e) {
            errorP = true;
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
        }

        handler.close();
        logger.removeHandler(handler);
        if (log != null) {
            assertEquals(log, byteStream.toString());
        } else {
            assertFalse("No error expected", errorP);
        }
        if (expect != null) {
            assertEquals(expect, stream.toString());
        }
        return interpreter;
    }

    /**
     * Run some code through <logo>ExTeX</logo>.
     *
     * @param code the code to expand
     * @param log the expected output on the log stream
     * @param expect the expected output on the output stream
     *
     * @return a new instance of the <tt>Interpreter</tt> class which has been
     *  used for the test run. This object can be inspected in additional
     *  asserts.
     *
     * @throws Exception in case of an error
     */
    public Interpreter assertOutput(final String code, final String log,
            final String expect) throws Exception {

        return assertOutput(getProps(), code, log, expect);
    }

    /**
     * Run some code through <logo>ExTeX</logo>.
     *
     * @param properties the properties to modify
     * @param code the code to expand
     * @param expect the expected output on the output stream
     *
     * @return a new instance of the <tt>Interpreter</tt> class which has been
     *  used for the test run. This object can be inspected in additional
     *  asserts.
     *
     * @throws Exception in case of an error
     */
    public Interpreter assertSuccess(final Properties properties,
            final String code, final String expect) throws Exception {

        return assertOutput(properties, code, "", expect);
    }

    /**
     * Run some code through <logo>ExTeX</logo>.
     *
     * @param code the code to expand
     * @param expect the expected output on the output stream
     *
     * @return a new instance of the <tt>Interpreter</tt> class which has been
     *  used for the test run. This object can be inspected in additional
     *  asserts.
     *
     * @throws Exception in case of an error
     */
    public Interpreter assertSuccess(final String code, final String expect)
            throws Exception {

        return assertOutput(getProps(), code, "", expect);
    }

    /**
     * Getter for the configuration name.
     *
     * @return the name of the configuration
     */
    protected String getConfig() {

        return "extex.xml";
    }

    /**
     * Determine the log level.
     *
     * @param properties the properties
     *
     * @return the log level
     */
    private Level getLogLevel(final Properties properties) {

        Level level = (Level) LEVEL_MAP.get(properties.getProperty(
                "extex.launcher.loglevel", "info"));

        return level == null ? Level.INFO : level;
    }

    /**
     * Getter for properties.
     *
     * @return the properties
     */
    public Properties getProps() {

        if (props == null) {
            props = System.getProperties();

            File file = new File(".extex-test");
            if (file.canRead()) {
                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    props.load(inputStream);
                    inputStream.close();
                    //} catch (FileNotFoundException e) {
                    // ignored on purpose
                } catch (IOException e) {
                    // ignored on purpose
                }
            }
        }
        return (Properties) this.props.clone();
    }

    /**
     * Initialize the ExTeX object just before the code is run.
     *
     * @param extex the ExTeX object
     */
    protected void init(final ExTeX extex) {

    }

    /**
     * Run <logo>ExTeX</logo> on a file.
     *
     * @param file the name of the file to read from
     *
     * @return the contents of the log file
     *
     * @throws Exception in case of an error
     */
    public String runFile(final String file) throws Exception {

        return runFile1(file, System.getProperties());
    }

    /**
     * Run <logo>ExTeX</logo> on a file.
     *
     * @param file the name of the file to read from
     * @param properties properties to start with
     *
     * @return the contents of the log file
     *
     * @throws Exception in case of an error
     */
    public String runFile1(final String file, final Properties properties)
            throws Exception {

        prepareProperties(properties);
        properties.setProperty("extex.code", "\\errorstopmode ");
        properties.setProperty("extex.file", file);
        properties.setProperty("extex.jobname", file);

        ExTeX extex = new ExTeX(properties);

        Logger logger = Logger.getLogger("test");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Handler handler = new StreamHandler(bytes, new LogFormatter());
        handler.setLevel(Level.WARNING);
        logger.addHandler(handler);
        extex.setLogger(logger);
        extex.setErrorHandler(new EHandler(logger));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        extex.setOutStream(stream);

        extex.run();

        handler.close();
        logger.removeHandler(handler);
        return bytes.toString();
    }

    /**
     * Getter for properties.
     *
     * @return the properties
     */
    public Properties showNodesProperties() {

        Properties p = getProps();
        p.put("extex.output", "dump");
        return p;
    }

}
