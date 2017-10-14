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

package guru.mmp.application.kafka.processor;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.kafka.KafkaConfiguration;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Processor</code> class implements the base class for the background threads that
 * provide the capability to retrieve and process records from an Apache Kafka topic.
 *
 * @author Marcus Portmann
 */
public abstract class Processor<K, V extends SpecificRecordBase> extends Thread
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(Processor.class);

  /**
   * The default timeout when polling for records from the Apache Kafka topic.
   */
  public static final long DEFAULT_POLL_TIMEOUT = 30000L;

  /**
   * The default amount of time in milliseconds the processor will pause when the processor or one
   * of its dependencies is temporarily unavailable.
   */
  public static final long DEFAULT_TEMPORARILY_UNAVAILABLE_PAUSE = 5000L;

  /**
   * The default amount of time in milliseconds the processor will pause when a critical error
   * is encountered while processing a record.
   */
  public static final long DEFAULT_CRITICAL_ERROR_PAUSE = 60000L;

  /**
   * The default amount of time in milliseconds the processor will pause after failing to commit a
   * processed record.
   */
  public static final long DEFAULT_COMMIT_FAILURE_PAUSE = 5000L;

  /**
   * The timeout when polling for records from the Apache Kafka topic.
   */
  private long pollTimeout = DEFAULT_POLL_TIMEOUT;

  /**
   * The amount of time in milliseconds the processor will pause when the processor or one of its
   * dependencies is temporarily unavailable.
   */
  private long temporarilyUnavailablePause = DEFAULT_TEMPORARILY_UNAVAILABLE_PAUSE;

  /**
   * The amount of time in milliseconds the processor will pause when a critical error is
   * encountered while processing a record.
   */
  private long criticalErrorPause = DEFAULT_CRITICAL_ERROR_PAUSE;

  /**
   * The amount of time in milliseconds the processor will pause after failing to commit a
   * processed record.
   */
  private long commitFailurePause = DEFAULT_COMMIT_FAILURE_PAUSE;

  /**
   *  Is the processor active?
   */
  private AtomicBoolean isActive = new AtomicBoolean(true);

  /**
   * The Apache Kafka configuration.
   */
  private KafkaConfiguration configuration;

  /**
   * The Apache Kafka key deserializer.
   */
  private Deserializer<K> keyDeserializer;

  /**
   * The Apache Kafka value deserializer.
   */
  private Deserializer<V> valueDeserializer;

  /**
   * The Apache Kafka consumer used to retrieve the messages.
   */
  private Consumer<K, V> consumer;

  /**
   * Constructs a new <code>Processor</code>.
   *
   * @param configuration     the Apache Kafka configuration
   * @param keyDeserializer   the Apache Kafka key deserializer
   * @param valueDeserializer the Apache Kafka value deserializer
   */
  public Processor(KafkaConfiguration configuration, Deserializer<K> keyDeserializer,
      Deserializer<V> valueDeserializer)
  {
    this(configuration, keyDeserializer, valueDeserializer, DEFAULT_POLL_TIMEOUT,
        DEFAULT_TEMPORARILY_UNAVAILABLE_PAUSE, DEFAULT_CRITICAL_ERROR_PAUSE,
        DEFAULT_COMMIT_FAILURE_PAUSE);
  }

  /**
   * Constructs a new <code>Processor</code>.
   *
   * @param configuration               the Apache Kafka configuration
   * @param keyDeserializer             the Apache Kafka key deserializer
   * @param valueDeserializer           the Apache Kafka value deserializer
   * @param pollTimeout                 the timeout when polling for records from the Apache Kafka
   *                                    topic
   * @param temporarilyUnavailablePause the amount of time in milliseconds the processor will pause
   *                                    when the processor or one of its dependencies is
   *                                    temporarily unavailable
   * @param criticalErrorPause          the amount of time in milliseconds the processor will pause
   *                                    when a critical error is encountered while processing a
   *                                    record
   * @param commitFailurePause          the amount of time in milliseconds the processor will
   *                                    pause after failing to commit a processed record
   */
  public Processor(KafkaConfiguration configuration, Deserializer<K> keyDeserializer,
      Deserializer<V> valueDeserializer, long pollTimeout, long temporarilyUnavailablePause,
      long criticalErrorPause, long commitFailurePause)
  {
    this.configuration = configuration;
    this.keyDeserializer = keyDeserializer;
    this.valueDeserializer = valueDeserializer;
    this.pollTimeout = pollTimeout;
    this.temporarilyUnavailablePause = temporarilyUnavailablePause;
    this.criticalErrorPause = criticalErrorPause;
    this.commitFailurePause = commitFailurePause;

    setName("Processor: " + getTopic());
  }

  @Override
  public void run()
  {
    try
    {
      Map<String, Object> properties = new HashMap<>();
      properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServers());
      properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
      properties.put(ConsumerConfig.GROUP_ID_CONFIG, configuration.getClientId());
      properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
      properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");

      /*
       * If maximum interval between poll attempts is not specified then to prevent Kafka from
       * re-balancing the topic while we are paused for a transient error, critical error or commit
       * error we set the max poll interval accordingly.
       */
      if (configuration.getMaxPollInterval() > 0)
      {
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
            configuration.getMaxPollInterval());
      }
      else
      {
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, Long.max(commitFailurePause,
            Long.max(temporarilyUnavailablePause, criticalErrorPause)) + 5000L);
      }

      DefaultKafkaConsumerFactory<K, V> consumerFactory = new DefaultKafkaConsumerFactory<>(
          properties, keyDeserializer, valueDeserializer);

      consumer = consumerFactory.createConsumer();

      consumer.subscribe(Collections.singletonList(getTopic()));

      int commitFailureCount = 0;

      while (isActive.get())
      {
        try
        {
          ConsumerRecords<K, V> records = consumer.poll(pollTimeout);

          for (TopicPartition partition : records.partitions())
          {
            List<ConsumerRecord<K, V>> partitionRecords = records.records(partition);

            for (ConsumerRecord<K, V> record : partitionRecords)
            {
              try
              {
                processRecordValue(record.value());

                if (logger.isDebugEnabled())
                {
                  logger.debug("Committing record with key (" + record.key() + ") and offset ("
                      + record.offset() + ") for the partition (" + partition.partition()
                      + ") and topic " + "(" + partition.topic() + ")");
                }

                consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(
                    record.offset() + 1)));
              }
              catch (InvalidRecordValueException e)
              {
                logger.error("The value for the record with key (" + record.key()
                    + ") and offset (" + record.offset() + ") for the partition ("
                    + partition.partition() + ") and topic " + "(" + partition.topic()
                    + ") is invalid and will be ignored", e);

                consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(
                    record.offset() + 1)));
              }
              catch (TransientErrorException e)
              {
                try
                {
                  sleep(temporarilyUnavailablePause);
                }
                catch (Throwable ignored) {}

              }
              catch (Throwable e)
              {
                logger.error(
                    "A critical error occurred while attempting to process the record with key ("
                    + record.key() + ") and offset (" + record.offset() + ") for the partition ("
                    + partition.partition() + ") and topic " + "(" + partition.topic() + ")", e);

                try
                {
                  Thread.sleep(criticalErrorPause);
                }
                catch (Throwable ignored) {}

                isActive.set(false);

                return;
              }
            }
          }
        }
        catch (CommitFailedException e)
        {
          commitFailureCount++;

          logger.error("Failed to commit the record retrieved from the topic (" + getTopic()
              + "). This error has occurred " + commitFailureCount + " time(s)", e);

          try
          {
            sleep(commitFailurePause);
          }
          catch (Throwable ignored) {}
        }
      }
    }
    catch (WakeupException e)
    {
      // Ignore the exception if closing
      if (isActive.get())
      {
        throw e;
      }
    }
    finally
    {
      try
      {
        consumer.close();
      }
      catch (Throwable e)
      {
        logger.error("Failed to close the consumer", e);
      }
    }
  }

  /**
   * Shutdown the Processor background thread.
   */
  public void shutdown()
  {
    if (isActive.get())
    {
      isActive.set(false);

      consumer.wakeup();
    }

    try
    {
      join();
    }
    catch (Throwable e) {}
  }

  /**
   * Returns the OID identifying the Kafka topic associated with this processor.
   *
   * @return the OID identifying the Kafka topic associated with this processor
   */
  protected abstract String getTopic();

  /**
   * Process the record value.
   *
   * @param value the record value
   */
  protected abstract void processRecordValue(V value)
    throws InvalidRecordValueException, TransientErrorException, ProcessingFailedException;
}
