/*
 * Copyright 2015 Marcus Portmann
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

package guru.mmp.application.web.servlet;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.test.Tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The <code>UnitTestServlet</code> class provides a simple servlet that can be used to execute
 * JUnit tests.
 *
 * @author Marcus Portmann
 */
public class UnitTestServlet extends HttpServlet
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UnitTestServlet.class);

  /**
   * @see HttpServlet#HttpServlet()
   */
  public UnitTestServlet()
  {
    super();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    try
    {
      String className = request.getParameter("class");

      if (className != null)
      {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);

        Result result = JUnitCore.runClasses(clazz);

        PrintWriter pw = response.getWriter();

        printHeader(pw);

        if (result.getFailureCount() == 1)
        {
          pw.println("<h1><thirdparty color=\"red\">Unit Test Failed</thirdparty></h1>");
        }
        else if (result.getFailureCount() > 1)
        {
          pw.println("<h1><thirdparty color=\"red\">Unit Tests Failed</thirdparty></h1>");
        }
        else if (result.getRunCount() > 1)
        {
          pw.println("<h1><thirdparty color=\"green\">Unit Tests Succeeded</thirdparty></h1>");
        }
        else
        {
          pw.println("<h1><thirdparty color=\"green\">Unit Test Succeeded</thirdparty></h1>");
        }

        pw.println("<table cellpadding=\"2\" cellspacing=\"2\">");
        pw.println("<tr><td><b>Elapsed Time</b></td><td>" + result.getRunTime() + " ms</td></tr>");
        pw.println("<tr><td><b>Total Tests</b></td><td>" + result.getRunCount() + "</td></tr>");
        pw.println("<tr><td><b>Failed Tests</b></td><td>" + result.getFailureCount()
            + "</td></tr>");
        pw.println("<tr><td><b>Ignored Tests</b></td><td>" + result.getIgnoreCount()
            + "</td></tr>");
        pw.println("</table><br/>");

        for (Failure failure : result.getFailures())
        {
          pw.println("<blockquote>");
          pw.println("<h2>" + failure.getTestHeader() + "</h2>");
          pw.println(failure.getMessage());
          pw.println("<pre>" + failure.getTrace() + "</pre>");
          pw.println("</blockquote>");
        }

        printFooter(pw);
      }
      else
      {
        PrintWriter pw = response.getWriter();

        printHeader(pw);

        pw.println("    <h1>Unit Tests</h1>");
        pw.println("    <ul>");

        String unitTestClassesParam = getServletConfig().getInitParameter("UnitTestClasses");

        if (unitTestClassesParam != null)
        {
          String[] unitTestClassNames = unitTestClassesParam.split(",");

          for (String unitTestClassName : unitTestClassNames)
          {
            unitTestClassName = unitTestClassName.trim();

            try
            {
              Class<?> clazz =
                Thread.currentThread().getContextClassLoader().loadClass(unitTestClassName);

              Object object = clazz.newInstance();

              if (object instanceof Tests)
              {
                pw.println("      <li><a href=\"" + request.getContextPath()
                    + "/servlet/UnitTestServlet?class=" + unitTestClassName + "\">"
                    + unitTestClassName + "</a></li>");

              }
            }
            catch (Throwable e)
            {
              logger.error("Failed to process the JUnit unit tests (" + unitTestClassName + ")", e);
            }
          }
        }

        pw.println("    </ul>");

        printFooter(pw);
      }
    }
    catch (Throwable e)
    {
      writeErrorResponse(e.getMessage(), e, response);
    }
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
  }

  private void printFooter(PrintWriter pw)
  {
    pw.println("  </body>");
    pw.println("</html>");
  }

  private void printHeader(PrintWriter pw)
  {
    pw.println("<html>");
    pw.println("  <head>");
    pw.println("    <style>");
    pw.println(
        "      body {thirdparty-family: Tahoma, Verdana, Arial, Helvetica; thirdparty-size: 9pt;}");
    pw.println(
        "      td {thirdparty-family: Tahoma, Verdana, Arial, Helvetica; thirdparty-size: 9pt;}");
    pw.println(
        "      h1 {thirdparty-family: Tahoma, Verdana, Arial, Helvetica; thirdparty-size: 12pt;}");
    pw.println(
        "      h2 {thirdparty-family: Tahoma, Verdana, Arial, Helvetica; thirdparty-size: 10pt;"
        + " color: red;}");
    pw.println("      pre {thirdparty-size: 8pt;}");
    pw.println("    </style>");
    pw.println("  </head>");
    pw.println("  <body>");
  }

  /**
   * Write the specified error information to the HTTP response.
   *
   * @param message   the error message
   * @param exception the exception containing the error information which may be <code>null</code>
   * @param response  the HTTP servlet response to write the error information to
   */
  private void writeErrorResponse(String message, Throwable exception, HttpServletResponse response)
  {
    try
    {
      response.sendError(500, message);

      PrintWriter pw = response.getWriter();

      printHeader(pw);

      pw.println("    <h1><thirdparty color=\"red\">ERROR</thirdparty></h1>");
      pw.println("    " + exception.getMessage());

      printFooter(pw);
    }
    catch (IOException ignored) {}
  }
}
