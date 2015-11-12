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

import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlUtils;

import org.w3c.dom.Element;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.namespace.QName;

/**
 * The <code>FormalExpression</code> class represents a FormalExpression that forms part of a
 * Process.
 * <p>
 * The FormalExpression class is used to specify an executable Expression using a specified
 * Expression language. A natural-language description of the Expression can also be specified,
 * in addition to the formal specification.
 * <p>
 * The default Expression language for all Expressions is specified in the Definitions element,
 * using the expressionLanguage attribute. It can also be overridden on each individual
 * FormalExpression using the same attribute.
 * <p>
 * <b>FormalExpression</b> XML schema:
 * <pre>
 * &lt;xsd:element name="formalExpression" type="tFormalExpression" substitutionGroup="expression"/&gt;
 * &lt;xsd:complexType name="tFormalExpression"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tExpression"&gt;
 *       &lt;xsd:attribute name="language" type="xsd:anyURI" use="optional"/&gt;
 *       &lt;xsd:attribute name="evaluatesToTypeRef" type="xsd:QName"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class FormalExpression extends Expression
{
  /**
   * The reference to the type of object that this Expression returns when evaluated e.g.
   * conditional Expressions with a reference of xsd:boolean evaluate to a boolean.
   */
  private QName evaluatesToTypeRef;

  /**
   * The URI giving the language for the Expression.
   * <p>
   * This overrides the Expression language specified in the Definitions.
   */
  private String language;

  /**
   * Constructs a new <code>FormalExpression</code>.
   *
   * @param element the XML element containing the FormalExpression information
   */
  public FormalExpression(Element element)
  {
    super(element);

    try
    {
      this.evaluatesToTypeRef = XmlUtils.getQName(element,
          StringUtil.notNull(element.getAttribute("evaluatesToTypeRef")));

      if (!StringUtil.isNullOrEmpty(element.getAttribute("language")))
      {
        this.language = element.getAttribute("language");
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the FormalExpression XML data", e);
    }
  }

  /**
   * Returns the reference to the type of object that this Expression returns when evaluated e.g.
   * conditional Expressions with a reference of xsd:boolean evaluate to a boolean.
   *
   * @return the reference to the type of object that this Expression returns when evaluated e.g.
   *         conditional Expressions with a reference of xsd:boolean evaluate to a boolean.
   */
  public QName getEvaluatesToTypeRef()
  {
    return evaluatesToTypeRef;
  }

  /**
   * Returns the URI giving the language for the Expression.
   * <p>
   * This overrides the Expression language specified in the Definitions.
   *
   * @return the URI giving the language for the Expression
   */
  public String getLanguage()
  {
    return language;
  }
}
