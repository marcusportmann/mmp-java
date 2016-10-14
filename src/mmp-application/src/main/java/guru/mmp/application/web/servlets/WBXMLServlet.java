/*
 * Copyright 2016 Marcus Portmann
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

import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Encoder;
import guru.mmp.common.wbxml.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>WBXMLServlet</code> servlet acts as an endpoint that remote clients can use to send
 * and receive WBXML messages.
 *
 * @author Marcus Portmann
 */
public class WBXMLServlet extends HttpServlet
{
  /**
   * The HTTP content-type used when receiving and sending WBXML.
   */
  private static final String WBXML_CONTENT_TYPE = "application/wbxml";

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(WBXMLServlet.class);
  private static final long serialVersionUID = 1000000;

  /**
   * Initialise the servlet.
   *
   * @param config the servlet configuration
   */
  @Override
  public void init(ServletConfig config)
    throws ServletException
  {
    super.init(config);
  }

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
    // Check the format of the request data
    if ((request.getContentType() == null)
        || (!request.getContentType().equals(WBXML_CONTENT_TYPE)))
    {
      response.sendError(500, "Invalid content type (" + request.getContentType() + ") expecting ("
          + WBXML_CONTENT_TYPE + ")");

      return;
    }

    try
    {
      // Retrieve the WBXML document from the HTTP servlet request
      Document document = readRequestDocument(request);

      if (document != null)
      {
        document.print(System.out);

        // Re-encode the document
        Encoder encoder = new Encoder(document);

        // Echo the re-encoded document as the response
        writeResponseDocument(encoder.getData(), response);
      }
    }
    catch (Throwable e)
    {
      logger.error("Failed to process the HTTP request", e);

      writeErrorResponse(e.getMessage(), e, response);
    }
  }

  /**
   * Read the WBXML request document from the HTTP servlet request.
   *
   * @param request the HTTP servlet request to read the WBXML request document from
   *
   * @return the WBXML request document
   */
  private Document readRequestDocument(HttpServletRequest request)
    throws ServletException
  {
    // Read the request data
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ServletInputStream in = null;

    try
    {
      in = request.getInputStream();

      byte[] readBuffer = new byte[2048];

      int numberOfBytesRead;

      while ((numberOfBytesRead = in.read(readBuffer)) != -1)
      {
        baos.write(readBuffer, 0, numberOfBytesRead);
      }
    }
    catch (Throwable e)
    {
      // A network error means that the document could not be read so stop here
      return null;
    }
    finally
    {
      try
      {
        if (in != null)
        {
          in.close();
        }
      }
      catch (Throwable ignored) {}
    }

    try
    {
      Parser parser = new Parser();

      return parser.parse(baos.toByteArray());
    }
    catch (Throwable e)
    {
      throw new ServletException("Failed to parse the WBXML request document from the HTTP"
          + " servlet request", e);
    }
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

      pw.println("<html>");
      pw.println("<head>");
      pw.println("  <style>");
      pw.println(
          "    body {thirdparty-family: Tahoma, Verdana, Arial, Helvetica; thirdparty-size: 8pt;}");
      pw.println(
          "    h1 {thirdparty-family: Tahoma, Verdana, Arial, Helvetica; thirdparty-size: 12pt;}");
      pw.println("    .section {padding-top: 10px; padding-bottom: 2px; color: green; "
          + "thirdparty-weight: bold; thirdparty-size: 9pt;}");
      pw.println("    .className {color: 808080;}");
      pw.println("  </style>");
      pw.println("</head>");
      pw.println("<body>");

      pw.println("  <h1><thirdparty color=\"red\">ERROR</thirdparty></h1>");
      pw.println("  " + exception.getMessage());

      pw.println("</body>");
      pw.println("</html>");
    }
    catch (IOException ignored) {}
  }

  /**
   * Write the binary data for the WBXML response document to the HTTP servlet response.
   *
   * @param data     the binary data for the WBXML response document
   * @param response the HTTP servlet response to write the binary data for the WBXML response
   *                 document to
   */
  private void writeResponseDocument(byte[] data, HttpServletResponse response)
  {
    try
    {
      ServletOutputStream out = response.getOutputStream();

      response.setContentType(WBXML_CONTENT_TYPE);

      out.write(data);

      out.flush();
    }
    catch (Throwable e)
    {
      logger.error(
          "Failed to write the binary data for the WBXML response document to the HTTP servlet "
          + "response", e);
    }
  }
}
