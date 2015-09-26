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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The <code>ParserHandler</code> class implements the SAX handler that provides the capability to
 * parse Business Process Model and Notation (BPMN) format files.
 *
 * @author Marcus Portmann
 */
public class ParserHandler extends DefaultHandler
{
  /**
   * The Business Process Model and Notation (BPMN) process to populate.
   */
  private Process process;

  /**
   * The Business Process Model and Notation (BPMN) process to populate.
   *
   * @param process the Business Process Model and Notation (BPMN) process to populate
   */
  public ParserHandler(Process process)
  {
    this.process = process;
  }

  /**
   * Receive notification of character data inside an element.
   *
   * @param ch     the characters
   * @param start  the start position in the character array
   * @param length the number of characters to use from the character array
   *
   * @throws SAXException
   */
  @Override
  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    System.out.println("[DEBUG][characters] Read: " + String.copyValueOf(ch, start, length).trim());
  }

  /**
   * Receive notification of the end of an element.
   *
   * @param uri       the Namespace URI, or the empty string if the element has no Namespace URI or
   *                  if Namespace processing is not being performed.
   * @param localName the local name (without prefix), or the empty string if Namespace processing
   *                  is not being performed
   * @param qName     the qualified name (with prefix), or the empty string if qualified names are
   *                  not available
   *
   * @throws SAXException
   */
  @Override
  public void endElement(String uri, String localName, String qName)
    throws SAXException
  {
    System.out.println("[DEBUG][endElement] {uri=\"" + uri + "\", localName=\"" + localName
        + "\", qName=\"" + qName + "\"}");

//  switch(qName){
//    //Add the employee to list once end tag is found
//    case "employee":
//      empList.add(emp);
//      break;
//    //For all other end tags the employee has to be updated.
//    case "firstName":
//      emp.firstName = content;
//      break;
//    case "lastName":
//      emp.lastName = content;
//      break;
//    case "location":
//      emp.location = content;
//      break;
//  }
  }

  /**
   * Method description
   *
   * @param uri
   * @param localName
   * @param qName
   * @param attributes
   *
   * @throws SAXException
   */
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
    throws SAXException
  {
    // super.startElement(uri, localName, qName, attributes);

    System.out.println("[DEBUG][startElement] {uri=\"" + uri + "\", localName=\"" + localName
        + "\", qName=\"" + qName + "\"}");

//  switch(qName){
//    //Create a new Employee object when the start tag is found
//    case "employee":
//      emp = new Employee();
//      emp.id = attributes.getValue("id");
//      break;

  }

  /**
   * Method description
   *
   * @param prefix
   * @param uri
   *
   * @throws SAXException
   */
  @Override
  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {
    // super.startPrefixMapping(prefix, uri);

    System.out.println("[DEBUG][startPrefixMapping] startPrefixMapping {prefixe=\"" + prefix
        + "\", uri = \"" + uri + "\"");

  }
}
