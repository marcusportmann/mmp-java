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

package guru.mmp.application.kafka;

/**
 * The <code>KafkaConfiguration</code> class provides access to the Kafka configuration.
 */
public class KafkaConfiguration
{
  /**
   * The bootstap servers.
   */
  private String bootstrapServers;

  /**
   * The maximum time to block in milliseconds.
   */
  private int maxBlock;

  /**
   * The request timeout in milliseconds.
   */
  private int requestTimeout;

  /**
   * The transaction timeout in milliseconds.
   */
  private int transactionTimeout;

  /**
   * The maximum poll interval in milliseconds.
   */
  private int maxPollInterval;

  /**
   * The client ID.
   */
  private String clientId;

  /**
   * Returns the bootstap servers.
   *
   * @return the bootstap servers
   */
  public String getBootstrapServers()
  {
    return bootstrapServers;
  }

  /**
   * Returns the client ID.
   *
   * @return the client ID
   */
  public String getClientId()
  {
    return clientId;
  }

  /**
   * Returns the maximum time to block in milliseconds.
   *
   * @return the maximum time to block in milliseconds
   */
  public int getMaxBlock()
  {
    return maxBlock;
  }

  /**
   * Returns the maximum poll interval in milliseconds.
   *
   * @return the maximum poll interval in milliseconds
   */
  public int getMaxPollInterval()
  {
    return maxPollInterval;
  }

  /**
   * Returns the request timeout in milliseconds.
   *
   * @return the request timeout in milliseconds
   */
  public int getRequestTimeout()
  {
    return requestTimeout;
  }

  /**
   * Returns the transaction timeout in milliseconds.
   *
   * @return the transaction timeout in milliseconds
   */
  public int getTransactionTimeout()
  {
    return transactionTimeout;
  }

  /**
   * Set the bootstap servers.
   *
   * @param bootstrapServers the bootstap servers
   */
  public void setBootstrapServers(String bootstrapServers)
  {
    this.bootstrapServers = bootstrapServers;
  }

  /**
   * Set the client ID.
   *
   * @param clientId the client ID
   */
  public void setClientId(String clientId)
  {
    this.clientId = clientId;
  }

  /**
   * Set the maximum time to block in milliseconds.
   *
   * @param maxBlock the maximum time to block in milliseconds
   */
  public void setMaxBlock(int maxBlock)
  {
    this.maxBlock = maxBlock;
  }

  /**
   * Set the maximum poll interval in milliseconds.
   *
   * @param maxPollInterval the maximum poll interval in milliseconds
   */
  public void setMaxPollInterval(int maxPollInterval)
  {
    this.maxPollInterval = maxPollInterval;
  }

  /**
   * Set the request timeout in milliseconds.
   *
   * @param requestTimeout the request timeout in milliseconds
   */
  public void setRequestTimeout(int requestTimeout)
  {
    this.requestTimeout = requestTimeout;
  }

  /**
   * Set the transaction timeout in milliseconds.
   *
   * @param transactionTimeout the transaction timeout in milliseconds
   */
  public void setTransactionTimeout(int transactionTimeout)
  {
    this.transactionTimeout = transactionTimeout;
  }
}
