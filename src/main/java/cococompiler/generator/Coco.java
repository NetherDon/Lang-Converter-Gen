/*-------------------------------------------------------------------------
Compiler Generator Coco/R,
Copyright (c) 1990, 2004 Hanspeter Moessenboeck, University of Linz
extended by M. Loeberbauer & A. Woess, Univ. of Linz
ported from C# to Java by Wolfgang Ahorner
with improvements by Pat Terry, Rhodes University

This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2, or (at your option) any
later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

As an exception, it is allowed to write an extension of Coco/R that is
used as a plugin in non-free software.

If not otherwise stated, any source code generated by Coco/R (other than
Coco/R itself) does not fall under the GNU General Public License.
-------------------------------------------------------------------------*/
/*-------------------------------------------------------------------------
  Trace output options
  0 | A: prints the states of the scanner automaton
  1 | F: prints the First and Follow sets of all nonterminals
  2 | G: prints the syntax graph of the productions
  3 | I: traces the computation of the First sets
  4 | J: prints the sets associated with ANYs and synchronisation sets
  6 | S: prints the symbol table (terminals, nonterminals, pragmas)
  7 | X: prints a cross reference list of all syntax symbols
  8 | P: prints statistics about the Coco run

  Trace output can be switched on by the pragma
    $ { digit | letter }
  in the attributed grammar or as a command-line option
  -------------------------------------------------------------------------*/

package cococompiler.generator;

import java.io.File;

import cococompiler.frames.FramePath;

public class Coco 
{

  /*
  public static void main(String[] arg) 
  {
    System.out.println("Coco/R (Apr 15, 2013)");
    String sourceName = null, packageName = null, frameDirectory = null, ddtString = null, outputDirectory = null;
    int returnValue = 1;
    for (int i = 0; i < arg.length; i++) 
    {
      if (arg[i].equals("-package") && i < arg.length - 1) packageName = arg[++i].trim();
      else if (arg[i].equals("-frames") && i < arg.length - 1) frameDirectory = arg[++i].trim();
      else if (arg[i].equals("-trace") && i < arg.length - 1) ddtString = arg[++i].trim();
      else if (arg[i].equals("-o") && i < arg.length - 1) outputDirectory = arg[++i].trim();
      else sourceName = arg[i];
    }

    if (arg.length > 0 && sourceName != null) 
    {
      try 
      {
        if (generateParser(sourceName, packageName, frameDirectory, outputDirectory, ddtString))
        {
          returnValue = 0;
        }
      } 
      catch (FatalError e) 
      {
        e.printStackTrace();
      }
    } 
    else 
    {
      printHelp();
    }
    
    System.exit(returnValue);
  }
  */

  public static CocoParseResult generateParser(
    String sourceName, 
    String packageName, 
    String outputDirectory, 
    String ddtString,
    FramePath scannerPath,
    FramePath parserPath
  )
  {
    String sourceDirectory = new File(sourceName).getAbsoluteFile().getParent();

    Scanner scanner = new Scanner(sourceName);
    Parser parser = new Parser(scanner);

    parser.trace = new Trace(sourceDirectory);
    parser.tab = new Tab(parser);
    parser.dfa = new DFA(parser, scannerPath);
    parser.pgen = new ParserGen(parser, parserPath);

    parser.tab.sourceName = sourceName;
    parser.tab.sourceDirectory = sourceDirectory;
    parser.tab.packageName = packageName;
    parser.tab.outputDirectory = outputDirectory != null ? outputDirectory : sourceDirectory;

    if (ddtString != null)
    {
      parser.tab.SetDDT(ddtString);
    }

    CocoParseResult result = parser.Parse();

    parser.trace.Close();

    //System.out.println(parser.errors.getErrorCount() + " errors detected");

    for (String message : parser.errors.getAllMessages())
    {
      System.out.println(message);
    }

    if (parser.errors.getErrorCount() != 0)
    {
      return new CocoParseResult();
    }

    return result;
  }
}