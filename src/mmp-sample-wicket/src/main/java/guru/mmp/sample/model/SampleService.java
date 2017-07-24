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

package guru.mmp.sample.model;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleService</code> class provides the Sample Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class SampleService
  implements ISampleService
{
  private static final String VERSION = "1.0.0";

  /* Entity Manager */
  @PersistenceContext(unitName = "applicationPersistenceUnit")
  private EntityManager entityManager;

  /**
   * The JSR 303 Bean Validation validator.
   */
  @Inject
  private Validator validator;

  /**
   * Add the data.
   *
   * @throws SampleServiceException
   */
  @Transactional
  public void addData()
    throws SampleServiceException
  {
    try
    {
      Data newData = new Data();
      newData.setId(666);
      newData.setName("New Name");
      newData.setStringValue("New String Value");
      newData.setDateValue(LocalDate.now());
      newData.setTimestampValue(LocalDateTime.now());

      entityManager.persist(newData);
    }
    catch (Throwable e)
    {
      throw new SampleServiceException("Failed to add the data", e);
    }
  }

  /**
   * Add the data.
   *
   * @param data the data
   *
   * @throws SampleServiceException
   */
  @Transactional
  public void addData(Data data)
    throws SampleServiceException
  {
    try
    {
      entityManager.persist(data);
    }
    catch (Throwable e)
    {
      throw new SampleServiceException("Failed to add the data", e);
    }
  }

  /**
   * Returns the data.
   *
   * @return the data
   *
   * @throws SampleServiceException
   */
  @Transactional
  public List<Data> getData()
    throws SampleServiceException
  {
    try
    {
      TypedQuery<Data> query = entityManager.createQuery("SELECT d FROM Data d", Data.class);

      return query.getResultList();
    }
    catch (Throwable e)
    {
      throw new SampleServiceException("Failed to retrieve the data", e);
    }
  }

  /**
   * Returns the data.
   *
   * @param id the ID used to uniquely identify the data
   *
   * @return the data
   *
   * @throws SampleServiceException
   */
  @Transactional
  public Data getData(long id)
    throws SampleServiceException
  {
    try
    {
      TypedQuery<Data> query = entityManager.createQuery("SELECT d FROM Data d WHERE ID=:id",
          Data.class);

      query.setParameter("id", id);

      List<Data> list = query.getResultList();

      if (list.size() > 0)
      {
        return list.get(0);
      }
      else
      {
        return null;
      }
    }
    catch (Throwable e)
    {
      throw new SampleServiceException("Failed to retrieve the data (" + id + ")", e);
    }
  }

  /**
   * Returns the version of the service.
   *
   * @return the version of the service
   */
  public String getVersion()
  {
    return VERSION;
  }

  /**
   * The test method.
   */
  public void testMethod()
  {
    System.out.println("[DEBUG] Hello world from the test method!!!");
  }
}
