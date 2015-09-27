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

package guru.mmp.application.process.bpmn;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.xml.XmlSchemaClasspathInputSource;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * The <code>Parser</code> class provides the capability to parse Business Process Model and
 * Notation (BPMN) format files.
 *
 * @author Marcus Portmann
 */
public class Parser
{
  /**
   * Constructs a new <code>Parser</code>.
   */
  public Parser() {}

  /**
   * Parse the Business Process Model and Notation (BPMN) data for the process definition.
   *
   * @param data the BPMN data for the process definition
   *
   * @return the parsed Business Process Model and Notation (BPMN) process
   */
  public Process parse(byte[] data)
  {
    try
    {
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

      schemaFactory.setResourceResolver(new LSResourceResolver()
      {
        @Override
        public LSInput resolveResource(String type, String namespaceURI, String publicId,
            String systemId, String baseURI)
        {
          switch (systemId)
          {
            case "BPMNDI.xsd":
            {
              return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                  "META-INF/BPMNDI.xsd");
            }

            case "DC.xsd":
            {
              return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                  "META-INF/DC.xsd");
            }

            case "DI.xsd":
            {
              return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                  "META-INF/DI.xsd");
            }

            case "Semantic.xsd":
            {
              return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                  "META-INF/Semantic.xsd");
            }

          }

          return null;
        }
      });

      Schema schema = schemaFactory.newSchema(
          new StreamSource(new ByteArrayInputStream(getClasspathResource("META-INF/BPMN20.xsd"))));

      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

      saxParserFactory.setSchema(schema);

      SAXParser saxParser = saxParserFactory.newSAXParser();

      ParserHandler handler = new ParserHandler();

      saxParser.parse(new ByteArrayInputStream(data), handler);

      return handler.getProcesses();
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the BPMN data for the process definition", e);
    }
  }

  private byte[] getClasspathResource(String path)
  {
    try
    {
      try (InputStream is =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(path))
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int numberOfBytesRead;

        while ((numberOfBytesRead = is.read(buffer)) != -1)
        {
          baos.write(buffer, 0, numberOfBytesRead);
        }

        return baos.toByteArray();
      }
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to read the classpath resource (" + path + ")", e);
    }
  }
}
