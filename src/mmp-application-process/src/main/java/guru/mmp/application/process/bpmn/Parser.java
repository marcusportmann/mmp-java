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

//~--- JDK imports ------------------------------------------------------------

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;

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
      SAXParserFactory parserFactory = SAXParserFactory.newInstance();

      SAXParser parser = parserFactory.newSAXParser();

      Process process = new Process();

      ParserHandler handler = new ParserHandler(process);

      parser.parse(new ByteArrayInputStream(data), handler);

      return process;
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the BPMN data for the process definition", e);
    }
  }
}
