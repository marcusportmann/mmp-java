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

package guru.mmp.application.test;

/**
 * The <code>ITestTransactionalService</code> interface defines the functionality provided by the
 * Test Transactional Service.
 *
 * @author Marcus Portmann
 */
public interface ITestTransactionalService
{
  /**
   * Create the test data.
   *
   * @param testData the test data
   *
   * @throws TestTransactionalServiceException
   */
  void createTestData(TestData testData)
    throws TestTransactionalServiceException;

  /**
   * Create the test data in a new transaction.
   *
   * @param testData the test data
   *
   * @throws TestTransactionalServiceException
   */
  void createTestDataInNewTransaction(TestData testData)
    throws TestTransactionalServiceException;

  /**
   * Retrieve the test data with the specified ID.
   *
   * @param id the ID
   *
   * @return the test data or <code>null</code> if the test data cannot be found
   *
   * @throws TestTransactionalServiceException
   */
  TestData getTestData(String id)
    throws TestTransactionalServiceException;
}
