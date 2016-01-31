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

package guru.mmp.sample.test;

import guru.mmp.common.test.ApplicationDataSourceResourceReference;
import guru.mmp.common.test.ApplicationJUnit4ClassRunner;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The <code>SampleServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>SampleService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationJUnit4ClassRunner.class)
//@ApplicationDataSourceResourceReference(name="java:jboss/datasources/SampleDS")
public class SampleServiceTest
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleServiceTest.class);

  @Test
  public void jpaTest()
  {
    try
    {
      //HibernatePersistenceProvider persistenceProvider = new HibernatePersistenceProvider();

//      Class<?> clazz = Class.forName("org.hibernate.jpa.HibernatePersistenceProvider");


      Map<String, String> properties = new HashMap<>();

//      properties.put("javax.persistence.provider", "org.hibernate.jpa.HibernatePersistenceProvider");
      properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");


      EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Sample", properties);

      EntityManager entityManager = entityManagerFactory.createEntityManager();

      int xxx = 0;
      xxx++;
    }
    catch (Throwable e)
    {
      logger.error("Failed to execute the JPA test", e);

      System.out.println("[ERROR] " + e.getMessage());

    }



  }


}

