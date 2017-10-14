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

import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AvroSerializer</code> class implements the Apache Avro serializer for Apache Kafka.
 *
 * @param <T>
 *
 * @author Marcus Portmann
 */
public class AvroSerializer<T extends SpecificRecordBase>
  implements Serializer<T>
{
  @Override
  public void close()
  {
    // No-op
  }

  @Override
  public void configure(Map<String, ?> arg0, boolean arg1)
  {
    // No-op
  }

  @Override
  public byte[] serialize(String topic, T data)
  {
    try
    {
      byte[] result = null;

      if (data != null)
      {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream,
            null);

        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(data.getSchema());
        datumWriter.write(data, binaryEncoder);

        binaryEncoder.flush();
        byteArrayOutputStream.close();

        result = byteArrayOutputStream.toByteArray();
      }

      return result;
    }
    catch (IOException e)
    {
      throw new SerializationException("Failed to serialize the data for the topic (" + topic
          + ")", e);
    }
  }
}
