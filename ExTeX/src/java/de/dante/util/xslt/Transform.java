/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.util.xslt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.transform.JDOMSource;

/**
 * Tranforam a xml-file with a xslt-file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public final class Transform {

    /**
     * private: no instance
     */
    private Transform() {

    }

    /**
     * parameter
     */
    private static final int PARAMETER = 3;

    /**
     * main
     * @param args      the comandlinearguments
     * @throws Exception  in case of an error
     */
    public static void main(final String[] args) throws Exception {

        if (args.length != PARAMETER) {
            System.err.println("java de.dante.util.xslt.Transform "
                    + "<xml-file> <xsl-file> <out-file>");
            System.exit(1);
        }

        SAXBuilder builder = new SAXBuilder(false);
        Document xmldoc = builder.build(new BufferedInputStream(
                new FileInputStream(args[0])));

        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer(new StreamSource(args[1]));

        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(args[2]));

        StreamResult result = new StreamResult(out);
        transformer.transform(new JDOMSource(xmldoc), result);
        out.close();
    }
}