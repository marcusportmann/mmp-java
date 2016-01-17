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

package guru.mmp.application.process.bpmn;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.ResourceUtil;
import guru.mmp.common.xml.XmlSchemaClasspathInputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Parser</code> class provides the capability to parse the XML definition for a Process.
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
   * Parse the XML definition for the Process.
   *
   * @param data the XML definition for the Process
   *
   * @return the parsed Processes
   */
  public List<Process> parse(byte[] data)
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
                  return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId,
                      baseURI, "META-INF/BPMNDI.xsd");
                }

                case "DC.xsd":
                {
                  return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId,
                      baseURI, "META-INF/DC.xsd");
                }

                case "DI.xsd":
                {
                  return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId,
                      baseURI, "META-INF/DI.xsd");
                }

                case "Semantic.xsd":
                {
                  return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId,
                      baseURI, "META-INF/Semantic.xsd");
                }
              }

              throw new RuntimeException("Failed to resolve the resource (" + systemId + ")");
            }
          });

      Schema schema = schemaFactory.newSchema(new StreamSource(new ByteArrayInputStream(
          ResourceUtil.getClasspathResource("META-INF/BPMN20.xsd"))));

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setValidating(false);
      documentBuilderFactory.setNamespaceAware(true);
      documentBuilderFactory.setSchema(schema);

      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

      Document document = documentBuilder.parse(new ByteArrayInputStream(data));

      NodeList elements = document.getDocumentElement().getChildNodes();

      List<Process> processes = new ArrayList<>();

      for (int i = 0; i < elements.getLength(); i++)
      {
        Node node = elements.item(i);

        if (node instanceof Element)
        {
          Element element = (Element) node;

          if (element.getNodeName().equals("process"))
          {
            processes.add(new Process(element));
          }
          else
          {
            throw new ParserException("Failed to parse the unknown node (" + element.getNodeName()
                + ")");
          }
        }
      }

//    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//    saxParserFactory.setValidating(false);
//    saxParserFactory.setNamespaceAware(true);
//    saxParserFactory.setSchema(schema);
//
//    SAXParser saxParser = saxParserFactory.newSAXParser();
//
//    ParserHandler handler = new ParserHandler();
//
//    saxParser.parse(new ByteArrayInputStream(data), handler);
//
//    return handler.getProcesses();

      return processes;
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the XML definition for the Process", e);
    }
  }
}
