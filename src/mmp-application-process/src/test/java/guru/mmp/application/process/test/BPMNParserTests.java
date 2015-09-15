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

package guru.mmp.application.process.test;

import guru.mmp.common.xml.DtdJarResolver;
import guru.mmp.common.xml.XmlParserErrorHandler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * The <code>BPMNParserTests</code> implements the unit tests for the BPMN parsing capability.
 */
public class BPMNParserTests
{
  protected byte[] getClasspathResource(String path)
  {
    try
    {
      try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path))
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

  @Test
  public void bpmnParserTest()
  {
    try
    {
      // Load the XML Business Process Model and Notation (BPMN) data from the classpath
      byte[] processDefinitionData = getClasspathResource(
        "guru/mmp/application/process/test/Test.bpmn");

      SAXParserFactory parserFactory = SAXParserFactory.newInstance();

      SAXParser parser = parserFactory.newSAXParser();

      //SAXHandler handler = new SAXHandler();

      //parser.parse(new ByteArrayInputStream(processDefinitionData), handler);



    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to parse the BPMN file", e);
    }
  }
}
