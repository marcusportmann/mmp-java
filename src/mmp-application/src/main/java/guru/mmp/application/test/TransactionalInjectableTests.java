///*
// * Copyright 2015 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package guru.mmp.application.test;
//
////~--- non-JDK imports --------------------------------------------------------
//
//import org.junit.After;
//import org.junit.Before;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
////~--- JDK imports ------------------------------------------------------------
//
//import javax.transaction.Status;
//import javax.transaction.UserTransaction;
//
///**
// * The <code>InjectableTests</code> class provides a base class for JUnit tests that wish to use
// * JEE 6 Contexts and Dependency Injection (CDI) and JTA transaction management.
// *
// * @author Marcus Portmann
// */
//@SuppressWarnings("unused")
//public abstract class TransactionalInjectableTests extends InjectableTests
//{
//  /* Logger */
//  private static final Logger logger = LoggerFactory.getLogger(TransactionalInjectableTests.class);
//
//  /**
//   * Constructs a new <code>TransactionalInjectableTests</code>.
//   */
//  public TransactionalInjectableTests() {}
//
//  /**
//   * This method is executed by the JUnit test infrastructure before a JUnit test is executed.
//   * It is responsible for initialising the JTA transaction.
//   */
//  @Before
//  public void setup()
//  {
//    // Start the JTA transction
//    try
//    {
//      if (getUserTransaction().getStatus() != Status.STATUS_NO_TRANSACTION)
//      {
//        throw new RuntimeException("A JTA transaction already exists");
//      }
//
//      getUserTransaction().begin();
//    }
//    catch (Throwable e)
//    {
//      throw new RuntimeException(
//          "Failed to initialise the JTA transaction before executing the test", e);
//    }
//  }
//
//  /**
//   * This method is executed by the JUnit test infrastructure after each JUnit test has been
//   * executed. It is responsible for finalising the JTA transaction.
//   */
//  @After
//  public void tearDown()
//  {
//    try
//    {
//      if (getUserTransaction().getStatus() == Status.STATUS_ACTIVE)
//      {
//        getUserTransaction().commit();
//      }
//      else if (getUserTransaction().getStatus() == Status.STATUS_MARKED_ROLLBACK)
//      {
//        getUserTransaction().rollback();
//
//        logger.warn("Rolled-back the JTA transaction associated with the test");
//      }
//    }
//    catch (Throwable e)
//    {
//      logger.error("Failed to finalise the JTA transaction after executing the test", e);
//    }
//  }
//
//  /**
//   * Returns the JTA transaction.
//   *
//   * @return the JTA transaction
//   */
//  protected abstract UserTransaction getUserTransaction();
//}
