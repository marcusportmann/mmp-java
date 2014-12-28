/*
 * Copyright 2014 Marcus Portmann
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

package guru.mmp.application.security;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.BinaryBuffer;

import java.math.BigDecimal;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Attribute</code> class stores an attribute for a security entity as name-value pair
 * where the value is one of a number of possible types e.g. String, double, long, BigDecimal,
 * binary data, etc.
 *
 * @author Marcus Portmann
 */
public class Attribute
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private String name;
  private Object value;

  /**
   * The enumeration defining the types of values that an <code>Attribute</code> instance can
   * store.
   */
  public enum ValueType
  {
    STRING_VALUE, LONG_VALUE, DOUBLE_VALUE, DECIMAL_VALUE, BINARY_VALUE, UNKNOWN_VALUE
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the <code>BigDecimal</code> value for the attribute
   */
  public Attribute(String name, BigDecimal value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the binary value for the attribute
   */
  public Attribute(String name, BinaryBuffer value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the binary value for the attribute
   */
  public Attribute(String name, byte[] value)
  {
    this.name = name;
    this.value = new BinaryBuffer(value);
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the <code>double</code> value for the attribute
   */
  public Attribute(String name, double value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the <code>long</code> value for the attribute
   */
  public Attribute(String name, long value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the <code>String</code> value for the attribute
   */
  public Attribute(String name, String value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the binary value for the <code>Attribute</code> instance with the specified name in
   * the specified list.
   *
   * @param list the list of <code>Attribute</code> instances to search for the
   *             <code>Attribute</code> with the specified name
   * @param name the name for the attribute
   * @return the binary value for the <code>Attribute</code> instance with the specified name in
   * the specified list
   * @throws AttributeException
   */
  public static byte[] getBinaryValue(List<Attribute> list, String name)
    throws AttributeException
  {
    for (Attribute attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        if (attribute.value instanceof BinaryBuffer)
        {
          return BinaryBuffer.class.cast(attribute.value).getData();
        }

        throw new AttributeException("Failed to retrieve the \"binary\" value for the Attribute ("
            + attribute.name + ") which contains a value of type \"" + attribute.getTypeName()
            + "\"");
      }
    }

    throw new AttributeException("Failed to retrieve the \"binary\" value for the Attribute ("
        + name + ") since no matching Attribute entry could be found in the specified list");
  }

  /**
   * Returns the <code>BigDecimal</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list.
   *
   * @param list the list of <code>Attribute</code> instances to search for the
   *             <code>Attribute</code> with the specified name
   * @param name the name for the attribute
   * @return the <code>BigDecimal</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list
   * @throws AttributeException
   */
  public static BigDecimal getDecimalValue(List<Attribute> list, String name)
    throws AttributeException
  {
    for (Attribute attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        if (attribute.value instanceof BigDecimal)
        {
          return BigDecimal.class.cast(attribute.value);
        }
        else if (attribute.value instanceof String)
        {
          try
          {
            return new BigDecimal(String.class.cast(attribute.value));
          }
          catch (Throwable ignored) {}
        }
        else if (attribute.value instanceof Double)
        {
          return BigDecimal.valueOf(Double.class.cast(attribute.value));
        }
        else if (attribute.value instanceof Long)
        {
          return BigDecimal.valueOf(Long.class.cast(attribute.value));
        }

        throw new AttributeException("Failed to retrieve the \"decimal\" value for the Attribute ("
            + attribute.name + ") which contains a value of type \"" + attribute.getTypeName()
            + "\"");
      }
    }

    throw new AttributeException("Failed to retrieve the \"decimal\" value for the Attribute ("
        + name + ") since no matching Attribute entry could be found in the specified list");
  }

  /**
   * Returns the <code>double</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list.
   *
   * @param list the list of <code>Attribute</code> instances to search for the
   *             <code>Attribute</code> with the specified name
   * @param name the name for the attribute
   * @return the <code>double</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list
   * @throws AttributeException
   */
  public static double getDoubleValue(List<Attribute> list, String name)
    throws AttributeException
  {
    for (Attribute attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        if (attribute.value instanceof Double)
        {
          return Double.class.cast(attribute.value);
        }
        else if (attribute.value instanceof String)
        {
          try
          {
            return Double.valueOf(String.class.cast(attribute.value));
          }
          catch (Throwable ignored) {}
        }
        else if (attribute.value instanceof BigDecimal)
        {
          return BigDecimal.class.cast(attribute.value).doubleValue();
        }
        else if (attribute.value instanceof Long)
        {
          return Long.class.cast(attribute.value);
        }

        throw new AttributeException("Failed to retrieve the \"double\" value for the Attribute ("
            + attribute.name + ") which contains a value of type \"" + attribute.getTypeName()
            + "\"");
      }
    }

    throw new AttributeException("Failed to retrieve the \"double\" value for the Attribute ("
        + name + ") since no matching Attribute entry could be found in the specified list");
  }

  /**
   * Returns the <code>long</code> value for the <code>Attribute</code> instance with the specified
   * name in the specified list.
   *
   * @param list the list of <code>Attribute</code> instances to search for the
   *             <code>Attribute</code> with the specified name
   * @param name the name for the attribute
   * @return the <code>long</code> value for the <code>Attribute</code> instance with the specified
   * name in the specified list
   * @throws AttributeException
   */
  public static long getLongValue(List<Attribute> list, String name)
    throws AttributeException
  {
    for (Attribute attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        if (attribute.value instanceof Long)
        {
          return Long.class.cast(attribute.value);
        }
        else if (attribute.value instanceof String)
        {
          try
          {
            return Long.valueOf(String.class.cast(attribute.value));
          }
          catch (Throwable ignored) {}
        }
        else if (attribute.value instanceof BigDecimal)
        {
          return BigDecimal.class.cast(attribute.value).longValue();
        }
        else if (attribute.value instanceof Double)
        {
          return Double.class.cast(attribute.value).longValue();
        }

        throw new AttributeException("Failed to retrieve the \"long\" value for the Attribute ("
            + attribute.name + ") which contains a value of type \"" + attribute.getTypeName()
            + "\"");
      }
    }

    throw new AttributeException("Failed to retrieve the \"long\" value for the Attribute (" + name
        + ") since no matching Attribute entry could be found in the specified list");
  }

  /**
   * Returns the <code>String</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list.
   *
   * @param list the list of <code>Attribute</code> instances to search for the
   *             <code>Attribute</code> with the specified name
   * @param name the name for the attribute
   * @return the <code>String</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list
   * @throws AttributeException
   */
  public static String getStringValue(List<Attribute> list, String name)
    throws AttributeException
  {
    for (Attribute attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        if (attribute.value instanceof String)
        {
          return String.class.cast(attribute.value);
        }
        else if (attribute.value instanceof BigDecimal)
        {
          return attribute.value.toString();
        }
        else if (attribute.value instanceof Double)
        {
          return attribute.value.toString();
        }
        else if (attribute.value instanceof Long)
        {
          return attribute.value.toString();
        }

        throw new AttributeException("Failed to retrieve the \"long\" value for the Attribute ("
            + attribute.name + ") which contains a value of type \"" + attribute.getTypeName()
            + "\"");
      }
    }

    throw new AttributeException("Failed to retrieve the \"string\" value for the Attribute ("
        + name + ") since no matching Attribute entry could be found in the specified list");
  }

  /**
   * Set the binary value for the <code>Attribute</code> instance with the specified name in the
   * specified list.
   *
   * @param list  the list of <code>Attribute</code> instances to search for the
   *              <code>Attribute</code> with the specified name
   * @param name  the name for the attribute
   * @param value the binary value for the attribute
   * @throws AttributeException
   */
  public static void setBinaryValue(List<Attribute> list, String name, BinaryBuffer value)
    throws AttributeException
  {
    setBinaryValue(list, name, value.getData());
  }

  /**
   * Set the binary value for the <code>Attribute</code> instance with the specified name in the
   * specified list.
   *
   * @param list  the list of <code>Attribute</code> instances to search for the
   *              <code>Attribute</code> with the specified name
   * @param name  the name for the attribute
   * @param value the binary value for the attribute
   * @throws AttributeException
   */
  public static void setBinaryValue(List<Attribute> list, String name, byte[] value)
    throws AttributeException
  {
    for (Attribute attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        attribute.setBinaryValue(value);

        return;
      }
    }

    throw new AttributeException("Failed to set the \"binary\" value for the Attribute (" + name
        + ") since no matching Attribute entry could be found in the specified list");
  }

  /**
   * Set the <code>BigDecimal</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list.
   *
   * @param list  the list of <code>Attribute</code> instances to search for the
   *              <code>Attribute</code> with the specified name
   * @param name  the name for the attribute
   * @param value the <code>BigDecimal</code> value for the attribute
   * @throws AttributeException
   */
  public static void setDecimalValue(List<Attribute> list, String name, BigDecimal value)
    throws AttributeException
  {
    for (Attribute attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        attribute.setDecimalValue(value);

        return;
      }
    }

    throw new AttributeException("Failed to set the \"decimal\" value for the Attribute (" + name
        + ") to the value (" + value
        + ") since no matching Attribute entry could be found in the specified list");
  }

  /**
   * Set the <code>double</code> value for the <code>Attribute</code> instance with the specified
   * name in the specified list.
   *
   * @param list  the list of <code>Attribute</code> instances to search for the
   *              <code>Attribute</code> with the specified name
   * @param name  the name for the attribute
   * @param value the <code>double</code> value for the attribute
   * @throws AttributeException
   */
  public static void setDoubleValue(List<Attribute> list, String name, double value)
    throws AttributeException
  {
    for (Attribute attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        attribute.setDoubleValue(value);

        return;
      }
    }

    throw new AttributeException("Failed to set the \"double\" value for the Attribute (" + name
        + ") to the value (" + value
        + ") since no matching Attribute entry could be found in the specified list");
  }

  /**
   * Set the <code>long</code> value for the <code>Attribute</code> instance with the specified
   * name in the specified list.
   *
   * @param list  the list of <code>Attribute</code> instances to search for the
   *              <code>Attribute</code> with the specified name
   * @param name  the name for the attribute
   * @param value the <code>long</code> value for the attribute
   * @throws AttributeException
   */
  public static void setLongValue(List<Attribute> list, String name, long value)
    throws AttributeException
  {
    for (Attribute attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        attribute.setLongValue(value);

        return;
      }
    }

    throw new AttributeException("Failed to set the \"long\" value for the Attribute (" + name
        + ") to the value (" + value
        + ") since no matching Attribute entry could be found in the specified list");
  }

  /**
   * Set the <code>String</code> value for the <code>Attribute</code> instance with the specified
   * name in the specified list.
   *
   * @param list  the list of <code>Attribute</code> instances to search for the
   *              <code>Attribute</code> with the specified name
   * @param name  the name for the attribute
   * @param value the <code>String</code> value for the attribute
   * @throws AttributeException
   */
  public static void setStringValue(List<Attribute> list, String name, String value)
    throws AttributeException
  {
    for (Attribute attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        attribute.setStringValue(value);

        return;
      }
    }

    throw new AttributeException("Failed to set the \"string\" value for the Attribute (" + name
        + ") to the value (" + value
        + ") since no matching Attribute entry could be found in the specified list");
  }

  /**
   * Returns the binary value for the <code>Attribute</code> instance.
   *
   * @return the binary value for the <code>Attribute</code> instance
   * @throws AttributeException
   */
  public byte[] getBinaryValue()
    throws AttributeException
  {
    if (value instanceof BinaryBuffer)
    {
      return BinaryBuffer.class.cast(value).getData();
    }

    throw new AttributeException("Failed to retrieve the \"binary\" value for the Attribute ("
        + name + ") which contains a value of type \"" + getTypeName() + "\"");
  }

  /**
   * Returns the <code>BigDecimal</code> value for the <code>Attribute</code> instance.
   *
   * @return the <code>BigDecimal</code> value for the <code>Attribute</code> instance
   * @throws AttributeException
   */
  public BigDecimal getDecimalValue()
    throws AttributeException
  {
    if (value instanceof BigDecimal)
    {
      return BigDecimal.class.cast(value);
    }
    else if (value instanceof String)
    {
      try
      {
        return new BigDecimal(String.class.cast(value));
      }
      catch (Throwable ignored) {}
    }
    else if (value instanceof Double)
    {
      return BigDecimal.valueOf(Double.class.cast(value));
    }
    else if (value instanceof Long)
    {
      return BigDecimal.valueOf(Long.class.cast(value));
    }

    throw new AttributeException("Failed to retrieve the \"decimal\" value for the Attribute ("
        + name + ") which contains a value of type \"" + getTypeName() + "\"");
  }

  /**
   * Returns the <code>double</code> value for the <code>Attribute</code> instance.
   *
   * @return the <code>double</code> value for the <code>Attribute</code> instance
   * @throws AttributeException
   */
  public double getDoubleValue()
    throws AttributeException
  {
    if (value instanceof Double)
    {
      return Double.class.cast(value);
    }
    else if (value instanceof String)
    {
      try
      {
        return Double.valueOf(String.class.cast(value));
      }
      catch (Throwable ignored) {}
    }
    else if (value instanceof BigDecimal)
    {
      return BigDecimal.class.cast(value).doubleValue();
    }
    else if (value instanceof Long)
    {
      return Long.class.cast(value);
    }

    throw new AttributeException("Failed to retrieve the \"double\" value for the Attribute ("
        + name + ") which contains a value of type \"" + getTypeName() + "\"");
  }

  /**
   * Returns the <code>long</code> value for the <code>Attribute</code> instance.
   *
   * @return the <code>long</code> value for the <code>Attribute</code> instance
   * @throws AttributeException
   */
  public long getLongValue()
    throws AttributeException
  {
    if (value instanceof Long)
    {
      return Long.class.cast(value);
    }
    else if (value instanceof String)
    {
      try
      {
        return Long.valueOf(String.class.cast(value));
      }
      catch (Throwable ignored) {}
    }
    else if (value instanceof BigDecimal)
    {
      return BigDecimal.class.cast(value).longValue();
    }
    else if (value instanceof Double)
    {
      return Double.class.cast(value).longValue();
    }

    throw new AttributeException("Failed to retrieve the \"long\" value for the Attribute (" + name
        + ") which contains a value of type \"" + getTypeName() + "\"");
  }

  /**
   * Returns the name for the <code>Attribute</code> instance.
   *
   * @return the name for the <code>Attribute</code> instance
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the <code>String</code> value for the <code>Attribute</code> instance.
   *
   * @return the <code>String</code> value for the <code>Attribute</code> instance
   * @throws AttributeException
   */
  public String getStringValue()
    throws AttributeException
  {
    if (value instanceof String)
    {
      return String.class.cast(value);
    }
    else if (value instanceof Long)
    {
      return value.toString();
    }
    else if (value instanceof BigDecimal)
    {
      return value.toString();
    }
    else if (value instanceof Double)
    {
      return value.toString();
    }

    throw new AttributeException("Failed to retrieve the \"long\" value for the Attribute (" + name
        + ") which contains a value of type \"" + getTypeName() + "\"");
  }

  /**
   * Returns the <code>ValueType</code> for the <code>Attribute</code> instance.
   *
   * @return the <code>ValueType</code> for the <code>Attribute</code> instance
   */
  public ValueType getType()
  {
    if (value instanceof String)
    {
      return ValueType.STRING_VALUE;
    }
    else if (value instanceof Long)
    {
      return ValueType.LONG_VALUE;
    }
    else if (value instanceof BigDecimal)
    {
      return ValueType.DECIMAL_VALUE;
    }
    else if (value instanceof Double)
    {
      return ValueType.DOUBLE_VALUE;
    }
    else if (value instanceof BinaryBuffer)
    {
      return ValueType.BINARY_VALUE;
    }
    else
    {
      return ValueType.UNKNOWN_VALUE;
    }
  }

  /**
   * Returns the name of the <code>ValueType</code> for the <code>Attribute</code> instance.
   *
   * @return the name of the <code>ValueType</code> for the <code>Attribute</code> instance
   */
  public String getTypeName()
  {
    if (value instanceof String)
    {
      return "string";
    }
    else if (value instanceof Long)
    {
      return "long";
    }
    else if (value instanceof Double)
    {
      return "double";
    }
    else if (value instanceof BigDecimal)
    {
      return "decimal";
    }
    else if (value instanceof BinaryBuffer)
    {
      return "binary";
    }
    else
    {
      return "unknown";
    }
  }

  /**
   * Set the binary value for the attribute.
   *
   * @param value the binary value for the attribute
   */
  public void setBinaryValue(BinaryBuffer value)
  {
    this.value = new BinaryBuffer(value);
  }

  /**
   * Set the binary value for the attribute.
   *
   * @param value the binary value for the attribute
   */
  public void setBinaryValue(byte[] value)
  {
    this.value = new BinaryBuffer(value);
  }

  /**
   * Set the <code>BigDecimal</code> value for the attribute.
   *
   * @param value the <code>BigDecimal</code> value for the attribute
   */
  public void setDecimalValue(BigDecimal value)
  {
    this.value = value;
  }

  /**
   * Set the <code>double</code> value for the attribute.
   *
   * @param value the <code>double</code> value for the attribute
   */
  public void setDoubleValue(double value)
  {
    this.value = value;
  }

  /**
   * Set the <code>Long</code> value for the attribute.
   *
   * @param value the <code>long</code> value for the attribute
   */
  public void setLongValue(long value)
  {
    this.value = value;
  }

  /**
   * Set the name for the attribute.
   *
   * @param name the name for the attribute
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the <code>String</code> value for the attribute.
   *
   * @param value the <code>String</code> value for the attribute
   */
  public void setStringValue(String value)
  {
    this.value = value;
  }
}
