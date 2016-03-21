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

package guru.mmp.common.model;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.BinaryBuffer;

//~--- JDK imports ------------------------------------------------------------

import java.math.BigDecimal;

import java.util.List;

/**
 * The <code>NameValue</code> class stores a name-value pair where the value is one of a number of
 * possible types e.g. String, double, long, BigDecimal, binary data, etc.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class NameValue
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private String name;
  private Object value;

  /**
   * the enum defining the types of values that a <code>NameValue</code> instance can store
   */
  public enum ValueType
  {
    STRING_VALUE, LONG_VALUE, DOUBLE_VALUE, DECIMAL_VALUE, BINARY_VALUE, UNKNOWN_VALUE
  }

  /**
   * Constructs a new <code>NameValue</code>.
   *
   * @param name  the name for the name-value pair
   * @param value the <code>BigDecimal</code> value for the name-value pair
   */
  public NameValue(String name, BigDecimal value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Constructs a new <code>NameValue</code>.
   *
   * @param name  the name for the name-value pair
   * @param value the binary value for the name-value pair
   */
  public NameValue(String name, BinaryBuffer value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Constructs a new <code>NameValue</code>.
   *
   * @param name  the name for the name-value pair
   * @param value the binary value for the name-value pair
   */
  public NameValue(String name, byte[] value)
  {
    this.name = name;
    this.value = new BinaryBuffer(value);
  }

  /**
   * Constructs a new <code>NameValue</code>.
   *
   * @param name  the name for the name-value pair
   * @param value the <code>double</code> value for the name-value pair
   */
  public NameValue(String name, double value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Constructs a new <code>NameValue</code>.
   *
   * @param name  the name for the name-value pair
   * @param value the <code>long</code> value for the name-value pair
   */
  public NameValue(String name, long value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Constructs a new <code>NameValue</code>.
   *
   * @param name  the name for the name-value pair
   * @param value the <code>String</code> value for the name-value pair
   */
  public NameValue(String name, String value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the binary value for the <code>NameValue</code> instance with the specified name in
   * the specified list.
   *
   * @param list the list of <code>NameValue</code> instances to search through
   * @param name the name for the name-value pair
   *
   * @return the binary value for the <code>NameValue</code> instance with the specified name
   */
  public static byte[] getBinaryValue(List<NameValue> list, String name)
  {
    for (NameValue nameValue : list)
    {
      if (nameValue.name.equalsIgnoreCase(name))
      {
        if (nameValue.value instanceof BinaryBuffer)
        {
          return BinaryBuffer.class.cast(nameValue.value).getData();
        }

        throw new NameValueException("Failed to retrieve the binary value for the NameValue"
            + " object (" + nameValue.name + ") which contains a value of type "
            + nameValue.getTypeName());
      }
    }

    throw new NameValueException("Failed to retrieve the binary value for the NameValue"
        + " object (" + name + ") since "
        + "no matching NameValue entry could be found in the list");
  }

  /**
   * Returns the <code>BigDecimal</code> value for the <code>NameValue</code> instance with the
   * specified name in the specified list.
   *
   * @param list the list of <code>NameValue</code> instances to search through
   * @param name the name for the name-value pair
   *
   * @return the <code>BigDecimal</code> value for the <code>NameValue</code> instance with the
   * specified name
   */
  public static BigDecimal getDecimalValue(List<NameValue> list, String name)
  {
    for (NameValue nameValue : list)
    {
      if (nameValue.name.equalsIgnoreCase(name))
      {
        if (nameValue.value instanceof BigDecimal)
        {
          return BigDecimal.class.cast(nameValue.value);
        }
        else if (nameValue.value instanceof String)
        {
          try
          {
            return new BigDecimal(String.class.cast(nameValue.value));
          }
          catch (Throwable ignored) {}
        }
        else if (nameValue.value instanceof Double)
        {
          return BigDecimal.valueOf(Double.class.cast(nameValue.value));
        }
        else if (nameValue.value instanceof Long)
        {
          return BigDecimal.valueOf(Long.class.cast(nameValue.value));
        }

        throw new NameValueException("Failed to retrieve the decimal value for the NameValue"
            + " object (" + nameValue.name + ") which contains a value of type "
            + nameValue.getTypeName());
      }
    }

    throw new NameValueException("Failed to retrieve the decimal value for the NameValue"
        + " object (" + name + ") since "
        + "no matching NameValue entry could be found in the list");
  }

  /**
   * Returns the <code>double</code> value for the <code>NameValue</code> instance with the
   * specified name in the specified list.
   *
   * @param list the list of <code>NameValue</code> instances to search through
   * @param name the name for the name-value pair
   *
   * @return the <code>double</code> value for the <code>NameValue</code> instance with the
   * specified name
   */
  public static double getDoubleValue(List<NameValue> list, String name)
  {
    for (NameValue nameValue : list)
    {
      if (nameValue.name.equalsIgnoreCase(name))
      {
        if (nameValue.value instanceof Double)
        {
          return Double.class.cast(nameValue.value);
        }
        else if (nameValue.value instanceof String)
        {
          try
          {
            return Double.valueOf(String.class.cast(nameValue.value));
          }
          catch (Throwable ignored) {}
        }
        else if (nameValue.value instanceof BigDecimal)
        {
          return BigDecimal.class.cast(nameValue.value).doubleValue();
        }
        else if (nameValue.value instanceof Long)
        {
          return Long.class.cast(nameValue.value);
        }

        throw new NameValueException("Failed to retrieve the double value for the NameValue"
            + " object (" + nameValue.name + ") which contains a value of type "
            + nameValue.getTypeName());
      }
    }

    throw new NameValueException("Failed to retrieve the double value for the NameValue"
        + " object (" + name + ") since "
        + "no matching NameValue entry could be found in the list");
  }

  /**
   * Returns the <code>long</code> value for the <code>NameValue</code> instance with the specified
   * name in the specified list.
   *
   * @param list the list of <code>NameValue</code> instances to search through
   * @param name the name for the name-value pair
   *
   * @return the <code>long</code> value for the <code>NameValue</code> instance with the
   * specified name
   */
  public static long getLongValue(List<NameValue> list, String name)
  {
    for (NameValue nameValue : list)
    {
      if (nameValue.name.equalsIgnoreCase(name))
      {
        if (nameValue.value instanceof Long)
        {
          return Long.class.cast(nameValue.value);
        }
        else if (nameValue.value instanceof String)
        {
          try
          {
            return Long.valueOf(String.class.cast(nameValue.value));
          }
          catch (Throwable ignored) {}
        }
        else if (nameValue.value instanceof BigDecimal)
        {
          return BigDecimal.class.cast(nameValue.value).longValue();
        }
        else if (nameValue.value instanceof Double)
        {
          return Double.class.cast(nameValue.value).longValue();
        }

        throw new NameValueException("Failed to retrieve the long value for the NameValue"
            + " object (" + nameValue.name + ") which contains a value of type "
            + nameValue.getTypeName());
      }
    }

    throw new NameValueException("Failed to retrieve the long value for the NameValue"
        + " object (" + name + ") since no "
        + "matching NameValue entry could be found in the list");
  }

  /**
   * Returns the <code>String</code> value for the <code>NameValue</code> instance with the
   * specified name in the specified list.
   *
   * @param list the list of <code>NameValue</code> instances to search through
   * @param name the name for the name-value pair
   *
   * @return the <code>String</code> value for the <code>NameValue</code> instance with the
   * specified name
   */
  public static String getStringValue(List<NameValue> list, String name)
  {
    for (NameValue nameValue : list)
    {
      if (nameValue.name.equalsIgnoreCase(name))
      {
        if (nameValue.value instanceof String)
        {
          return String.class.cast(nameValue.value);
        }
        else if (nameValue.value instanceof BigDecimal)
        {
          return nameValue.value.toString();
        }
        else if (nameValue.value instanceof Double)
        {
          return nameValue.value.toString();
        }
        else if (nameValue.value instanceof Long)
        {
          return nameValue.value.toString();
        }

        throw new NameValueException("Failed to retrieve the string value for the NameValue"
            + " object (" + nameValue.name + ") which contains a value of type "
            + nameValue.getTypeName());
      }
    }

    throw new NameValueException("Failed to retrieve the string value for the NameValue"
        + " object (" + name + ") since "
        + "no matching NameValue entry could be found in the list");
  }

  /**
   * Set the binary value for the <code>NameValue</code> instance with the specified name in the
   * specified list.
   *
   * @param list  the list of <code>NameValue</code> instances to search through
   * @param name  the name for the name-value pair
   * @param value the binary value for the name-value pair
   */
  public static void setBinaryValue(List<NameValue> list, String name, BinaryBuffer value)
  {
    setBinaryValue(list, name, value.getData());
  }

  /**
   * Set the binary value for the <code>NameValue</code> instance with the specified name in the
   * specified list.
   *
   * @param list  the list of <code>NameValue</code> instances to search through
   * @param name  the name for the name-value pair
   * @param value the binary value for the name-value pair
   */
  public static void setBinaryValue(List<NameValue> list, String name, byte[] value)
  {
    for (NameValue nameValue : list)
    {
      if (nameValue.name.equalsIgnoreCase(name))
      {
        nameValue.setBinaryValue(value);

        return;
      }
    }

    throw new NameValueException("Failed to set the binary value for the NameValue object (" + name
        + ") since no matching" + " NameValue entry could be found in the list");
  }

  /**
   * Set the <code>BigDecimal</code> value for the <code>NameValue</code> instance with the
   * specified name in the specified list.
   *
   * @param list  the list of <code>NameValue</code> instances to search through
   * @param name  the name for the name-value pair
   * @param value the <code>BigDecimal</code> value for the name-value pair
   */
  public static void setDecimalValue(List<NameValue> list, String name, BigDecimal value)
  {
    for (NameValue nameValue : list)
    {
      if (nameValue.name.equalsIgnoreCase(name))
      {
        nameValue.setDecimalValue(value);

        return;
      }
    }

    throw new NameValueException("Failed to set the decimal value for the NameValue object ("
        + name + ") to the value (" + value + ") since no matching NameValue entry"
        + " could be found in the list");
  }

  /**
   * Set the <code>double</code> value for the <code>NameValue</code> instance with the specified
   * name in the specified list.
   *
   * @param list  the list of <code>NameValue</code> instances to search through
   * @param name  the name for the name-value pair
   * @param value the <code>double</code> value for the name-value pair
   */
  public static void setDoubleValue(List<NameValue> list, String name, double value)
  {
    for (NameValue nameValue : list)
    {
      if (nameValue.name.equalsIgnoreCase(name))
      {
        nameValue.setDoubleValue(value);

        return;
      }
    }

    throw new NameValueException("Failed to set the double value for the NameValue object (" + name
        + ") to the value (" + value + ") since no matching NameValue entry"
        + " could be found in the list");
  }

  /**
   * Set the <code>long</code> value for the <code>NameValue</code> instance with the specified
   * name in the specified list.
   *
   * @param list  the list of <code>NameValue</code> instances to search through
   * @param name  the name for the name-value pair
   * @param value the <code>long</code> value for the name-value pair
   */
  public static void setLongValue(List<NameValue> list, String name, long value)
  {
    for (NameValue nameValue : list)
    {
      if (nameValue.name.equalsIgnoreCase(name))
      {
        nameValue.setLongValue(value);

        return;
      }
    }

    throw new NameValueException("Failed to set the long value for the NameValue object (" + name
        + ") to the value (" + value + ") since no matching NameValue entry"
        + " could be found in the list");
  }

  /**
   * Set the <code>String</code> value for the <code>NameValue</code> instance with the specified
   * name in the specified list.
   *
   * @param list  the list of <code>NameValue</code> instances to search through
   * @param name  the name for the name-value pair
   * @param value the <code>String</code> value for the name-value pair
   */
  public static void setStringValue(List<NameValue> list, String name, String value)
  {
    for (NameValue nameValue : list)
    {
      if (nameValue.name.equalsIgnoreCase(name))
      {
        nameValue.setStringValue(value);

        return;
      }
    }

    throw new NameValueException("Failed to set the string value for the NameValue object (" + name
        + ") to the value (" + value + ") since no matching NameValue entry"
        + " could be found in the list");
  }

  /**
   * Returns the binary value for the <code>NameValue</code> instance.
   *
   * @return the binary value for the <code>NameValue</code> instance
   */
  public byte[] getBinaryValue()
  {
    if (value instanceof BinaryBuffer)
    {
      return BinaryBuffer.class.cast(value).getData();
    }

    throw new NameValueException("Failed to retrieve the binary value for the NameValue"
        + " object (" + name + ") which " + "contains a value of type \"" + getTypeName() + "\"");
  }

  /**
   * Returns the <code>BigDecimal</code> value for the <code>NameValue</code> instance.
   *
   * @return the <code>BigDecimal</code> value for the <code>NameValue</code> instance
   */
  public BigDecimal getDecimalValue()
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

    throw new NameValueException("Failed to retrieve the decimal value for the NameValue"
        + " object (" + name + ") which " + "contains a value of type " + getTypeName());
  }

  /**
   * Returns the <code>double</code> value for the <code>NameValue</code> instance.
   *
   * @return the <code>double</code> value for the <code>NameValue</code> instance
   */
  public double getDoubleValue()
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

    throw new NameValueException("Failed to retrieve the double value for the NameValue"
        + " object (" + name + ") which " + "contains a value of type " + getTypeName());
  }

  /**
   * Returns the <code>long</code> value for the <code>NameValue</code> instance.
   *
   * @return the <code>long</code> value for the <code>NameValue</code> instance
   */
  public long getLongValue()
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

    throw new NameValueException("Failed to retrieve the long value for the NameValue"
        + " object (" + name + ") which " + "contains a value of type " + getTypeName());
  }

  /**
   * Returns the name for the <code>NameValue</code> instance.
   *
   * @return the name for the <code>NameValue</code> instance
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the <code>String</code> value for the <code>NameValue</code> instance.
   *
   * @return the <code>String</code> value for the <code>NameValue</code> instance
   */
  public String getStringValue()
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

    throw new NameValueException("Failed to retrieve the string value for the NameValue"
        + " object (" + name + ") which " + "contains a value of type " + getTypeName());
  }

  /**
   * Returns the <code>ValueType</code> for the <code>NameValue</code> instance.
   *
   * @return the <code>ValueType</code> for the <code>NameValue</code> instance
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
   * Returns the name of the <code>ValueType</code> for the <code>NameValue</code> instance.
   *
   * @return the name of the <code>ValueType</code> for the <code>NameValue</code> instance
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
   * Set the binary value for the name-value pair.
   *
   * @param value the binary value for the name-value pair
   */
  public void setBinaryValue(BinaryBuffer value)
  {
    this.value = new BinaryBuffer(value);
  }

  /**
   * Set the binary value for the name-value pair.
   *
   * @param value the binary value for the name-value pair
   */
  public void setBinaryValue(byte[] value)
  {
    this.value = new BinaryBuffer(value);
  }

  /**
   * Set the <code>BigDecimal</code> value for the name-value pair.
   *
   * @param value the <code>BigDecimal</code> value for the name-value pair
   */
  public void setDecimalValue(BigDecimal value)
  {
    this.value = value;
  }

  /**
   * Set the <code>double</code> value for the name-value pair.
   *
   * @param value the <code>double</code> value for the name-value pair
   */
  public void setDoubleValue(double value)
  {
    this.value = value;
  }

  /**
   * Set the <code>Long</code> value for the name-value pair.
   *
   * @param value the <code>long</code> value for the name-value pair
   */
  public void setLongValue(long value)
  {
    this.value = value;
  }

  /**
   * Set the name for the name-value pair.
   *
   * @param name the name for the name-value pair
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the <code>String</code> value for the name-value pair.
   *
   * @param value the <code>String</code> value for the name-value pair
   */
  public void setStringValue(String value)
  {
    this.value = value;
  }
}
