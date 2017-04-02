/*
 * Copyright 2017 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package guru.mmp.application.web.servlets;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DumpNameSpaceServlet</code> servlet dumps the JNDI namespace.
 *
 * @author Marcus Portmann
 */
public class DumpNamespaceServlet extends HttpServlet
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(DumpNamespaceServlet.class);
  private static final long serialVersionUID = 1000000;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    PrintWriter pw = response.getWriter();

    printHtmlHeader(pw);

    InitialContext ic = null;

    try
    {
      ic = new InitialContext();

      TreeDumpStatus treeDumpStatus = new TreeDumpStatus();

      pw.println("<br><div class=\"section\">[]</div><br>");
      dumpNameSpace(treeDumpStatus, ic, "", pw, 1);

      pw.println("<div class=\"section\">[java:app]</div><br>");
      dumpNameSpace(treeDumpStatus, ic, "java:app", pw, 1);

      pw.println("<div class=\"section\">[java:global]</div><br>");
      dumpNameSpace(treeDumpStatus, ic, "java:global", pw, 1);

      pw.println("<div class=\"section\">[java:comp]</div><br>");
      dumpNameSpace(treeDumpStatus, ic, "java:comp", pw, 1);

      pw.println("<br><div class=\"section\">[java:comp/env]</div><br>");
      dumpNameSpace(treeDumpStatus, ic, "java:comp/env", pw, 1);

      pw.println("<div class=\"section\">[java:appserver]</div><br>");
      dumpNameSpace(treeDumpStatus, ic, "java:appserver", pw, 1);
    }
    catch (Throwable e)
    {
      pw.println(
          "<span style=\"color: red; thirdparty-weight: bold; padding-left: 10px; padding-top: "
          + "10px; padding-bottom: 10px;\">[" + e.getClass().getName() + "] " + e.getMessage()
          + "</span>");
      logger.error("Failed to dump the JNDI tree: " + e.getMessage(), e);
    }
    finally
    {
      try
      {
        if (ic != null)
        {
          ic.close();
        }
      }
      catch (Throwable ignored) {}
    }

    printHtmlFooter(pw);
  }

  private void dumpNameSpace(TreeDumpStatus treeDumpStatus, Context context, String path,
      PrintWriter pw, int depth)
    throws NamingException
  {
    NamingEnumeration<NameClassPair> nameClassPairs;

    try
    {
      nameClassPairs = context.list(path);
    }
    catch (NameNotFoundException e)
    {
      return;
    }
    catch (Throwable e)
    {
      System.err.println("[ERROR] Failed to list the JNDI nodes for the context (" + path + "): "
          + e.getMessage());

      return;
    }

    while (nameClassPairs.hasMoreElements())
    {
      NameClassPair nameClassPair = nameClassPairs.nextElement();

      // Process the child node
      try
      {
        Object object = InitialContext.doLookup(path + "/" + nameClassPair.getName());

        if (!(object instanceof javax.naming.Context))
        {
          pw.println("<div style=\"padding-top: 2px; padding-bottom: 2px; padding-left: " + ((depth
              * 20)) + "px;\">" + nameClassPair.getName() + " <span class=\"className\">["
              + nameClassPair.getClassName() + "] = " + object + "</span></div><br>");
        }
        else
        {
          boolean processChildren = true;

          if (nameClassPair.getName().equals("thisNode") && (depth > 1))
          {
            processChildren = false;
          }
          else if (nameClassPair.getName().equals("cell"))
          {
            if (treeDumpStatus.foundCell)
            {
              processChildren = false;
            }
            else
            {
              treeDumpStatus.foundCell = true;
            }
          }
          else if (nameClassPair.getName().equals("nodes"))
          {
            if (treeDumpStatus.foundNodes)
            {
              processChildren = false;
            }
            else
            {
              treeDumpStatus.foundNodes = true;
            }
          }
          else if (nameClassPair.getName().equals("node"))
          {
            if (treeDumpStatus.foundNode)
            {
              processChildren = false;
            }
            else
            {
              treeDumpStatus.foundNode = true;
            }
          }
          else if (nameClassPair.getName().equals("servers"))
          {
            if (treeDumpStatus.foundServers)
            {
              processChildren = false;
            }
            else
            {
              treeDumpStatus.foundServers = true;
            }
          }
          else if (nameClassPair.getName().equals("clusters"))
          {
            if (treeDumpStatus.foundClusters)
            {
              processChildren = false;
            }
            else
            {
              treeDumpStatus.foundClusters = true;
            }
          }
          else if (nameClassPair.getName().equals("persistent"))
          {
            if (treeDumpStatus.foundPersistent)
            {
              processChildren = false;
            }
            else
            {
              treeDumpStatus.foundPersistent = true;
            }
          }
          else if (nameClassPair.getName().equals("domain"))
          {
            if (treeDumpStatus.foundDomain)
            {
              processChildren = false;
            }
            else
            {
              treeDumpStatus.foundDomain = true;
            }
          }

          if (processChildren)
          {
            pw.println(
                "<div style=\"thirdparty-weight: bold; padding-top: 2px; padding-bottom: 2px;"
                + " padding-left: " + ((depth * 20)) + "px;\">" + nameClassPair.getName() + " "
                + "<span class=\"className\">[" + nameClassPair.getClassName()
                + "]</span></div><br>");

            if (path.length() > 0)
            {
              dumpNameSpace(treeDumpStatus, context, path + "/" + nameClassPair.getName(), pw,
                  depth + 1);
            }
            else
            {
              dumpNameSpace(treeDumpStatus, context, nameClassPair.getName(), pw, depth + 1);
            }
          }
          else
          {
            pw.println("<div style=\"color: #9f9f9f; padding-top: 2px; padding-bottom: 2px; "
                + "padding-left: " + ((depth * 20)) + "px;\">" + nameClassPair.getName() + " "
                + "(Duplicate Reference)" + "</div><br>");
          }
        }
      }
      catch (Throwable e)
      {
        pw.println("<div style=\"padding-top: 2px; padding-bottom: 2px; padding-left: " + ((depth
            * 20)) + "px;\">" + nameClassPair.getName() + " <span class=\"className\">["
            + nameClassPair.getClassName() + "]</span></div><br>");
      }
    }
  }

  private void printHtmlFooter(PrintWriter pw)
    throws IOException
  {
    pw.println("  </body>");
    pw.println("</html>");
  }

  private void printHtmlHeader(PrintWriter pw)
    throws IOException
  {
    pw.println("<html>");
    pw.println("<head>");
    pw.println("  <style>");
    pw.println(
        "    body {thirdparty-family: Tahoma, Verdana, Arial, Helvetica; thirdparty-size: 10pt;}");
    pw.println("    .section {padding-top: 10px; padding-bottom: 2px; color: green; "
        + "thirdparty-weight: bold; thirdparty-size: 9pt;}");
    pw.println("    .className {color: 808080;}");
    pw.println("  </style>");
    pw.println("</head>");
    pw.println("<body>");
  }

  private class TreeDumpStatus
  {
    /**
     * foundCell
     */
    boolean foundCell;

    /**
     * foundClusters
     */
    boolean foundClusters;

    /**
     * foundDomain
     */
    boolean foundDomain;

    /**
     * foundNode
     */
    boolean foundNode;

    /**
     * foundNodes
     */
    boolean foundNodes;

    /**
     * foundPersistent
     */
    boolean foundPersistent;

    /**
     * foundServers
     */
    boolean foundServers;
  }
}
