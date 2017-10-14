/*
 * Copyright 2017 Marcus Portmann
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

package guru.mmp.application.kafka.serialization;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AvroDeserializer</code> class implements the Apache Avro deserializer for Apache Kafka.
 *
 * @author Marcus Portmann
 */
public class AvroDeserializer<T extends SpecificRecordBase>
  implements Deserializer<T>
{
  protected final Class<T> targetType;

  /**
   * Constructs a new <code>AvroDeserializer</code>.
   *
   * @param targetType the target type
   */
  public AvroDeserializer(Class<T> targetType)
  {
    this.targetType = targetType;
  }

  @Override
  public void close() {}

  @Override
  public void configure(Map<String, ?> map, boolean b) {}

  @Override
  public T deserialize(String topic, byte[] data)
  {
    try
    {
      T result = null;

      if (data != null)
      {
        DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(targetType.newInstance()
            .getSchema());
        Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);

        result = (T) datumReader.read(null, decoder);

      }

      return result;
    }
    catch (Throwable e)
    {
      throw new SerializationException("Failed to deserialize the data for the topic (" + topic
          + ")", e);
    }
  }
}
