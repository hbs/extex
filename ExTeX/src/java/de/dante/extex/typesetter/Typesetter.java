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
package de.dante.extex.typesetter;


import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configurable;

/**
 * ...
 * 
 * 
 * @see "TeX -- The Program [211]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface Typesetter extends ListMaker, Configurable {
    
    public abstract void setDocumentWriter(DocumentWriter doc);

    public abstract CharNodeFactory getCharNodeFactory();

    public void finish() throws GeneralException;
    
}
