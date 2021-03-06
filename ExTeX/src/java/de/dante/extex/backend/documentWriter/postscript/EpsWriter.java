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

package de.dante.extex.backend.documentWriter.postscript;

import java.io.IOException;
import java.io.OutputStream;

import de.dante.extex.backend.documentWriter.DocumentWriterOptions;
import de.dante.extex.backend.documentWriter.postscript.util.FontManager;
import de.dante.extex.backend.documentWriter.postscript.util.HeaderManager;
import de.dante.extex.backend.documentWriter.postscript.util.PsConverter;
import de.dante.extex.backend.documentWriter.postscript.util.PsUnit;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.page.Page;
import de.dante.util.exception.GeneralException;

/**
 * This document writer produces Encapsulated Postscript documents.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class EpsWriter extends AbstractPostscriptWriter {

    /**
     * The field <tt>fontManager</tt> contains the font manager.
     */
    private FontManager fontManager;

    /**
     * The field <tt>headerManager</tt> contains the header manager.
     */
    private HeaderManager headerManager = new HeaderManager();

    /**
     * Creates a new object.
     *
     * @param options the options for the document writer
     */
    public EpsWriter(final DocumentWriterOptions options) {

        super();
        fontManager = new FontManager();
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#close()
     */
    public void close() {

    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "eps";
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.page.Page)
     */
    public int shipout(final Page p) throws GeneralException, IOException {

        PsConverter converter = getConverter(headerManager);

        OutputStream stream = newOutputStream("eps");

        byte[] bytes = converter.toPostScript(p, fontManager, headerManager);

        stream.write("%!PS-Adobe-2.0 EPSF-2.0\n".getBytes());
        writeDsc(stream, "Creator", getParameter("Creator"));
        writeDsc(stream, "Title", getParameter("Title"));
        writeBB(stream, "BoundingBox", p.getNodes());
        writeHRBB(stream, "HiResBoundingBox", p.getNodes());
        writeFonts(stream, fontManager);
        writeDsc(stream, "EndComments");
        fontManager.write(stream);
        fontManager.clear();
        headerManager.write(stream);
        stream.write(bytes);
        writeDsc(stream, "EOF");
        stream.close();
        stream = null;
        return 1;
    }

    /**
     * Write a BoundingBox DSC to an output stream.
     *
     * @param stream the target stream to write to
     * @param name the name of the DSC comment
     * @param nodes the nodes to extract the dimensions from
     *
     * @throws IOException in case of an error during writing
     */
    private void writeBB(final OutputStream stream, final String name,
            final NodeList nodes) throws IOException {

        StringBuffer sb = new StringBuffer();
        stream.write('%');
        stream.write('%');
        stream.write(name.getBytes());
        stream.write(':');
        stream.write(' ');
        stream.write("0 0 ".getBytes());
        PsUnit.toPoint(nodes.getWidth(), sb, true);
        stream.write(sb.toString().getBytes());
        sb.delete(0, sb.length() - 1);
        stream.write(' ');
        Dimen d = new Dimen(nodes.getHeight());
        d.add(nodes.getDepth());
        PsUnit.toPoint(d, sb, true);
        stream.write(sb.toString().getBytes());
        stream.write('\n');
    }

    /**
     * Write a HiResBoundingBox DSC to an output stream.
     *
     * @param stream the target stream to write to
     * @param name the name of the DSC comment
     * @param nodes the nodes to extract the dimensions from
     *
     * @throws IOException in case of an error during writing
     */
    private void writeHRBB(final OutputStream stream, final String name,
            final NodeList nodes) throws IOException {

        StringBuffer sb = new StringBuffer();
        stream.write('%');
        stream.write('%');
        stream.write(name.getBytes());
        stream.write(':');
        stream.write(' ');
        stream.write("0 0 ".getBytes());
        PsUnit.toPoint(nodes.getWidth(), sb, false);
        stream.write(sb.toString().getBytes());
        sb.delete(0, sb.length() - 1);
        stream.write(' ');
        Dimen d = new Dimen(nodes.getHeight());
        d.add(nodes.getDepth());
        PsUnit.toPoint(d, sb, false);
        stream.write(sb.toString().getBytes());
        stream.write('\n');
    }

}
