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
//package guru.mmp.application.web.resources;
//
////~--- non-JDK imports --------------------------------------------------------
//
//import org.apache.wicket.protocol.http.WebApplication;
//
///**
// * The <code>InjectableResourceStream</code> provides a base class for resource streams that wish
// * to use JEE 6 Contexts and Dependency Injection (CDI).
// *
// * @author Marcus Portmann
// */
//@SuppressWarnings("unused")
//public abstract class InjectableResourceStream extends ResourceStream
//{
//  private static final long serialVersionUID = 1000000;
//
//  /**
//   * Constructs a new <code>InjectableResourceStream</code>.
//   */
//  public InjectableResourceStream()
//  {
//    guru.mmp.application.web.WebApplication webApplication = guru.mmp.application.web
//        .WebApplication.class.cast(WebApplication.get());
//
//    webApplication.getWebApplicationInjector().inject(this);
//  }
//}
