/*
 *  $Id: PackInfo.java,v 1.2 2005/05/30 16:35:15 gene Exp $
 *  IzPack
 *  Copyright (C) 2001-2004 Julien Ponge
 *
 *  File :               PackagerPack.java
 *  Description :        Packs a "Pack's" files into the installer
 *  Author's email :     mchenryc@acm.org
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.izforge.izpack.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.izforge.izpack.ExecutableFile;
import com.izforge.izpack.Pack;
import com.izforge.izpack.PackFile;
import com.izforge.izpack.ParsableFile;
import com.izforge.izpack.UpdateCheck;

/**
 * Temporary holding place for Pack information as the Packager is built. The
 * packager is used by the compiler to collect info about an installer, and
 * finally create the actual installer files.
 *
 * @author     Chadwick McHenry
 */
public class PackInfo
{
  /** The pack object serialized in the installer. */
  private Pack pack;

  /** The color of the node. This is used for the dependency graph algorithms */
  public int colour;

  /** white colour*/
  public final static int WHITE = 0;
  /** grey colour*/
  public final static int GREY  = 1;
  /** black colour*/
  public final static int BLACK = 2;

  /** Files of the Pack. */
  private Map files = new HashMap();

  /** Parsables files in this Pack. */
  private List parsables = new ArrayList();

  /** Executable files in this Pack. */
  private List executables = new ArrayList();

  /** Update check specifications in this Pack. */
  private List updateChecks = new ArrayList();

  /** Constructor with required info. */
  protected PackInfo(String name, String id, String description, boolean required, boolean loose)
  {
    pack = new Pack( name, id, description, null,null, required, true, loose);
    colour = PackInfo.WHITE;
  }
  
  /* **********************************************************************
   * Attributes of the Pack
   * **********************************************************************/

  public void setDependencies(List dependencies)
  {
    pack.dependencies = dependencies;
  }
  public void setOsConstraints(List osConstraints)
  {
    pack.osConstraints = osConstraints;
  }
  
  public List getOsConstraints(List osConstraints)
  {
    return pack.osConstraints;
  }

  public void setPreselected(boolean preselected)
  {
    pack.preselected = preselected;
  }

  public boolean isPreselected()
  {
    return pack.preselected;
  }

  public Pack getPack()
  {
    return pack;
  }

  /* **********************************************************************
   * Public methods to add data to the Installer being packed
   * **********************************************************************/

  /**
   * Add a file or directory to be installed.
   *
   * @param file       the file or basedir to be installed.
   * @param targetfile path file will be installed to.
   * @param osList     the target operation system(s) of this pack.
   * @param override   what to do if the file already exists when installing
   *
   * @throws FileNotFoundException if the file specified does not exist. The
   * file is not read until the {@link Packager#createInstaller} is invoked,
   * thus a FileNotFoundEception will occur then, if the file is deleted
   * in between.
   */
/*  public void addFile(File file, String targetfile, List osList, int override)
    throws FileNotFoundException
  {
    addFile( file,targetfile, osList, override, null);
  }


  /**
   * Add a file or directory to be installed.
   *
   * @param file       the file or basedir to be installed.
   * @param targetfile path file will be installed to.
   * @param osList     the target operation system(s) of this pack.
   * @param override   what to do if the file already exists when installing
   * @param  additionals    Map which contains additional data
   *
   * @throws FileNotFoundException if the file specified does not exist. The
   * file is not read until the {@link Packager#createInstaller} is invoked,
   * thus a FileNotFoundEception will occur then, if the file is deleted
   * in between.
   */
  public void addFile(File file, String targetfile, List osList, 
    int override, Map additionals) throws FileNotFoundException
  {
    if (! file.exists())
      throw new FileNotFoundException(file.toString());

    PackFile packFile = new PackFile(file, targetfile, osList, 
      override, additionals);
    files.put(packFile, file);
  }

  /** Set of PackFile objects for this Pack. */
  public Set getPackFiles()
  {
    return files.keySet();
  }

  /**
   * The file described by the specified PackFile. Returns <tt>null</tt> if the
   * PackFile did not come from the set returned by {@link #getPackFiles()}.
   */
  public File getFile(PackFile packFile)
  {
    return (File) files.get(packFile);
  }

  /**
   * Parsable files have variables substituted after installation.
   */
  public void addParsable(ParsableFile parsable)
  {
    parsables.add(parsable);
  }

  /** List of parsables for this Pack. */
  public List getParsables()
  {
    return parsables;
  }
  
  /**
   * Executables files have their executable flag set, may be executed, and
   * optionally, deleted when finished executing.
   */
  public void addExecutable(ExecutableFile executable)
  {
    executables.add(executable);
  }

  /** List of parsables for this Pack. */
  public List getExecutables()
  {
    return executables;
  }

  /**
   * Executables files have their executable flag set, may be executed, and
   * optionally, deleted when finished executing.
   */
  public void addUpdateCheck(UpdateCheck updateCheck)
  {
    updateChecks.add(updateCheck);
  }

  /** List of update checks for this Pack. */
  public List getUpdateChecks()
  {
    return updateChecks;
  }
  /**
   * The packs that this file depends on
   */
  public void addDependency(String dependency)
  {
    if(pack.dependencies == null)
    {
      pack.dependencies = new ArrayList();
    }
    pack.dependencies.add(dependency);
  }
  public List getDependencies()
  {
    return pack.dependencies;
  }
  public String toString()
  {
    return pack.name;
  }
}
