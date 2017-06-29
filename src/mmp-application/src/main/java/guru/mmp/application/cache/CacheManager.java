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

package guru.mmp.application.cache;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CacheManager</code> class implements the distributed in-memory cache manager.
 *
 * @author Marcus Portmann
 */
public class CacheManager
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);

  /**
   * The distributed in-memory caches we have initialized.
   */
  Map<String, Map> caches = new ConcurrentHashMap<>();

  /**
   * Constructs a new <code>CacheManager</code>.
   *
   * @param configuration the distributed in-memory cache manager configuration
   */
  public CacheManager(CacheManagerConfiguration configuration)
    throws CacheManagerException
  {
    try
    {
      Thread.currentThread().getContextClassLoader().loadClass("com.hazelcast.config.Config");
    }
    catch (ClassNotFoundException ignored)
    {
      throw new CacheManagerException(
          "Failed to initialize the distributed in-memory cache cluster: The Hazelcast library could not be found");
    }

    try
    {
      logger.info("Initialising the distributed in-memory cache cluster ("
          + configuration.getCluster().getName() + ")");

      // Configure and create the Hazelcast instance
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      Class<?> configClass = classLoader.loadClass("com.hazelcast.config.Config");
      Class<?> networkConfigClass = classLoader.loadClass("com.hazelcast.config.NetworkConfig");
      Class<?> joinConfigClass = classLoader.loadClass("com.hazelcast.config.JoinConfig");
      Class<?> multicastConfigClass = classLoader.loadClass("com.hazelcast.config.MulticastConfig");
      Class<?> awsConfigClass = classLoader.loadClass("com.hazelcast.config.AwsConfig");
      Class<?> tcpIpConfigClass = classLoader.loadClass("com.hazelcast.config.TcpIpConfig");
      Class<?> groupConfigClass = classLoader.loadClass("com.hazelcast.config.GroupConfig");
      Class<?> maxSizeConfigClass = classLoader.loadClass("com.hazelcast.config.MaxSizeConfig");
      Class<?> maxSizePolicyClass = classLoader.loadClass(
          "com.hazelcast.config.MaxSizeConfig$MaxSizePolicy");
      Class<?> mapConfigClass = classLoader.loadClass("com.hazelcast.config.MapConfig");
      Class<?> inMemoryFormatClass = classLoader.loadClass("com.hazelcast.config.InMemoryFormat");
      Class<?> evictionPolicyClass = classLoader.loadClass("com.hazelcast.config.EvictionPolicy");

      Class<?> hazelcastClass = classLoader.loadClass("com.hazelcast.core.Hazelcast");
      Class<?> hazelcastInstanceClass = classLoader.loadClass(
          "com.hazelcast.core.HazelcastInstance");
      Class<?> clusterClass = classLoader.loadClass("com.hazelcast.core.Cluster");

      // Config config = new Config();
      Object configObject = configClass.newInstance();

      // config.setInstanceName(configObject,cacheConfiguration.getName());
      configClass.getMethod("setInstanceName", String.class).invoke(configObject,
          configuration.getCluster().getName());

      // config.setProperty("hazelcast.logging.type", "slf4j");
      configClass.getMethod("setProperty", String.class, String.class).invoke(configObject,
          "hazelcast.logging.type", "slf4j");

      // config.setProperty("hazelcast.rest.enabled", "false");
      configClass.getMethod("setProperty", String.class, String.class).invoke(configObject,
          "hazelcast.rest.enabled", "false");

      // NetworkConfig networkConfig = config.getNetworkConfig();
      Object networkConfigObject = configClass.getMethod("getNetworkConfig").invoke(configObject);

      // networkConfig.setPort(cacheConfiguration.getPort());
      networkConfigClass.getMethod("setPort", Integer.TYPE).invoke(networkConfigObject,
          configuration.getCluster().getPort());

      // networkConfig.setPortAutoIncrement(false);
      networkConfigClass.getMethod("setPortAutoIncrement", Boolean.TYPE).invoke(
          networkConfigObject, true);

      // networkConfig.setReuseAddress(true);
      networkConfigClass.getMethod("setReuseAddress", Boolean.TYPE).invoke(networkConfigObject,
          true);

      // JoinConfig joinConfig = networkConfig.getJoin();
      Object joinConfigObject = networkConfigClass.getMethod("getJoin").invoke(networkConfigObject);

      // MulticastConfig multicastConfig = joinConfig.getMulticastConfig();
      Object multicastConfigObject = joinConfigClass.getMethod("getMulticastConfig").invoke(
          joinConfigObject);

      // multicastConfig.setEnabled(false);
      multicastConfigClass.getMethod("setEnabled", Boolean.TYPE).invoke(multicastConfigObject,
          false);

      // AwsConfig awsConfig = joinConfig.getAwsConfig();
      Object awsConfigObject = joinConfigClass.getMethod("getAwsConfig").invoke(joinConfigObject);

      // awsConfig.setEnabled(false);
      awsConfigClass.getMethod("setEnabled", Boolean.TYPE).invoke(awsConfigObject, false);

      // TcpIpConfig tcpIpConfig = joinConfig.getTcpIpConfig();
      Object tcpIpConfigObject = joinConfigClass.getMethod("getTcpIpConfig").invoke(
          joinConfigObject);

      // tcpIpConfig.setEnabled(true);
      tcpIpConfigClass.getMethod("setEnabled", Boolean.TYPE).invoke(tcpIpConfigObject, true);

      // Add the cache members
      String[] members = { "127.0.0.1" };

      if ((configuration.getCluster().getMembers() != null)
          && (configuration.getCluster().getMembers().trim().length() > 0))
      {
        members = configuration.getCluster().getMembers().trim().split(",");
      }

      if (members.length > 0)
      {
        for (String member : members)
        {
          // tcpIpConfig.addMember(member);
          tcpIpConfigClass.getMethod("addMember", String.class).invoke(tcpIpConfigObject, member);
        }
      }

      // GroupConfig groupConfig = config.getGroupConfig();
      Object groupConfigObject = configClass.getMethod("getGroupConfig").invoke(configObject);

      // groupConfig.setName(cacheClusterConfiguration.getName());
      groupConfigClass.getMethod("setName", String.class).invoke(groupConfigObject,
          configuration.getCluster().getName());

      // groupConfig.setPassword(cacheClusterConfiguration.getPassword());
      groupConfigClass.getMethod("setPassword", String.class).invoke(groupConfigObject,
          configuration.getCluster().getPassword());

      // Initialise the caches
      for (CacheManagerConfiguration.CacheConfiguration cacheConfiguration :
          configuration.getCaches())
      {
        logger.info("Initialising the distributed in-memory cache (" + cacheConfiguration.getName()
            + ")");

        // MapConfig mapConfig = config.getMapConfig("cache-name");
        Object mapConfigObject = configClass.getMethod("getMapConfig", String.class).invoke(
            configObject, cacheConfiguration.getName());

        // mapConfig.setInMemoryFormat(InMemoryFormat.OBJECT);
        mapConfigClass.getMethod("setInMemoryFormat", inMemoryFormatClass).invoke(mapConfigObject,
            Enum.valueOf((Class<Enum>) inMemoryFormatClass,
            cacheConfiguration.getInMemoryFormat()));

        // mapConfig.setEvictionPolicy(EvictionPolicy.LRU);
        mapConfigClass.getMethod("setEvictionPolicy", evictionPolicyClass).invoke(mapConfigObject,
            Enum.valueOf((Class<Enum>) evictionPolicyClass,
            cacheConfiguration.getEvictionPolicy()));

        // mapConfig.setStatisticsEnabled(configuration.getClusterStatisticsEnabled());
        mapConfigClass.getMethod("setStatisticsEnabled", Boolean.TYPE).invoke(mapConfigObject,
            cacheConfiguration.getStatisticsEnabled());

        // mapConfig.setMaxIdleSeconds(300);
        mapConfigClass.getMethod("setMaxIdleSeconds", Integer.TYPE).invoke(mapConfigObject,
            cacheConfiguration.getMaxIdleSeconds());

        // MaxSizeConfig maxSizeConfig = new MaxSizeConfig();
        Object maxSizeConfigObject = maxSizeConfigClass.newInstance();

        // maxSizeConfig.setMaxSizePolicy(com.hazelcast.config.MaxSizeConfig.MaxSizePolicy.PER_NODE);
        maxSizeConfigClass.getMethod("setMaxSizePolicy", maxSizePolicyClass).invoke(
            maxSizeConfigObject, Enum.valueOf((Class<Enum>) maxSizePolicyClass,
            cacheConfiguration.getMaxSizePolicy()));

        // maxSizeConfig.setSize(10000);
        maxSizeConfigClass.getMethod("setSize", Integer.TYPE).invoke(maxSizeConfigObject,
            cacheConfiguration.getMaxSize());

        // mapConfig.setMaxSizeConfig(maxSizeConfig);
        mapConfigClass.getMethod("setMaxSizeConfig", maxSizeConfigClass).invoke(mapConfigObject,
            maxSizeConfigObject);

        // mapConfig.setBackupCount(0);
        mapConfigClass.getMethod("setBackupCount", Integer.TYPE).invoke(mapConfigObject,
            cacheConfiguration.getBackupCount());

        // mapConfig.setAsyncBackupCount(1);
        mapConfigClass.getMethod("setAsyncBackupCount", Integer.TYPE).invoke(mapConfigObject,
            cacheConfiguration.getAsyncBackupCount());

        // mapConfig.setReadBackupData(true);
        mapConfigClass.getMethod("setReadBackupData", Boolean.TYPE).invoke(mapConfigObject,
            cacheConfiguration.getReadBackupData());
      }

      // HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
      Object hazelcastInstanceObject = hazelcastClass.getMethod("newHazelcastInstance", configClass)
          .invoke(null, configObject);

      // Retrieve the cluster members
      Object clusterObject = hazelcastInstanceClass.getMethod("getCluster").invoke(
          hazelcastInstanceObject);

      Set<?> clusterMembers = (Set<?>) clusterClass.getMethod("getMembers").invoke(clusterObject);

      for (CacheManagerConfiguration.CacheConfiguration cacheConfiguration :
          configuration.getCaches())
      {
        Map<?, ?> cache = (Map<?, ?>) hazelcastInstanceClass.getMethod("getMap", String.class)
            .invoke(hazelcastInstanceObject, cacheConfiguration.getName());

        caches.put(cacheConfiguration.getName(), cache);

      }

      logger.info("Successfully connected to the distributed in-memory cache cluster ("
          + configuration.getCluster().getName() + ") with " + clusterMembers.size()
          + " cluster member(s)");
    }
    catch (Throwable e)
    {
      throw new CacheManagerException(
          "Failed to initialise the distributed in-memory cache cluster ("
          + configuration.getCluster().getName() + ")", e);
    }
  }

  /**
   * Returns the distributed in-memory cache with the specified name.
   *
   * @param name the name of the distributed in-memory cache
   *
   * @return the distributed in-memory cache with the specified name
   */
  public Map getCache(String name)
    throws CacheManagerException
  {
    if (caches.containsKey(name))
    {
      return caches.get(name);
    }
    else
    {
      throw new CacheManagerException("The distributed in-memory cache (" + name
          + ") does not exist");
    }
  }
}
