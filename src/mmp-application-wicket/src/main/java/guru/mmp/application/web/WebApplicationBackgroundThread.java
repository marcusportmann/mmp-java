///*
// * Copyright 2017 Marcus Portmann
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
//package guru.mmp.application.web;
//
///**
// * The <code>WebApplicationBackgroundThread</code> class implements the abstract base class for all
// * background threads started by the web application.
// *
// * @author Marcus Portmann
// */
//@SuppressWarnings("unused")
//public abstract class WebApplicationBackgroundThread extends Thread
//{
//  /**
//   * The default sleep interval.
//   */
//  private static final long DEFAULT_SLEEP_INTERVAL = 30000L;
//  private boolean isRunning;
//  private long sleepInterval;
//
//  /**
//   * Constructs a new <code>WebApplicationBackgroundThread</code>.
//   */
//  public WebApplicationBackgroundThread()
//  {
//    isRunning = true;
//
//    sleepInterval = DEFAULT_SLEEP_INTERVAL;
//  }
//
//  /**
//   * Constructs a new <code>WebApplicationBackgroundThread</code>.
//   *
//   * @param isDaemon <code>true</code> if the thread should be started as a daemon thread or
//   *                 <code>false</code> otherwise
//   */
//  public WebApplicationBackgroundThread(boolean isDaemon)
//  {
//    setDaemon(isDaemon);
//
//    isRunning = true;
//
//    sleepInterval = DEFAULT_SLEEP_INTERVAL;
//  }
//
//  /**
//   * Constructs a new <code>WebApplicationBackgroundThread</code>.
//   *
//   * @param sleepInterval the sleep interval
//   */
//  public WebApplicationBackgroundThread(long sleepInterval)
//  {
//    isRunning = true;
//
//    this.sleepInterval = sleepInterval;
//  }
//
//  /**
//   * Constructs a new <code>WebApplicationBackgroundThread</code>.
//   *
//   * @param isDaemon      <code>true</code> if the thread should be started as a daemon thread or
//   *                      <code>false</code> otherwise
//   * @param sleepInterval the sleep interval
//   */
//  public WebApplicationBackgroundThread(boolean isDaemon, long sleepInterval)
//  {
//    setDaemon(isDaemon);
//
//    isRunning = true;
//
//    this.sleepInterval = sleepInterval;
//  }
//
//  /**
//   * Initialise the background thread.
//   */
//  public abstract void init();
//
//  /**
//   * Perform the processing for the background thread.
//   */
//  public abstract void process();
//
//  /**
//   * Run.
//   */
//  @Override
//  public void run()
//  {
//    while (isRunning)
//    {
//      process();
//
//      try
//      {
//        if (isRunning)
//        {
//          synchronized (this)
//          {
//            wait(sleepInterval);
//          }
//        }
//      }
//      catch (Throwable ignored) {}
//    }
//  }
//
//  /**
//   * Shutdown the background thread.
//   */
//  public void shutdown()
//  {
//    if (isRunning)
//    {
//      isRunning = false;
//
//      synchronized (this)
//      {
//        notify();
//
//        try
//        {
//          join();
//        }
//        catch (Throwable ignored) {}
//      }
//    }
//  }
//}
