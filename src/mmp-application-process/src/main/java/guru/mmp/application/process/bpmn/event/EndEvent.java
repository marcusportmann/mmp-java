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

package guru.mmp.application.process.bpmn.event;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.ModelExecutionContext;
import guru.mmp.application.process.bpmn.Token;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>EndEvent</code> class represents a Business Process Model and Notation (BPMN)
 * end event that forms part of a BPMN model.
 * <p>
 * End events finish a particular path of the process (or the whole process) and generate a result
 * (a message for example).
 * <p>
 * End events have one or more incoming flows but no outgoing flows.
 *
 * @author Marcus Portmann
 */
public class EndEvent extends ThrowingEvent
{
  /**
   * Constructs a new <code>EndEvent</code>.
   *
   * @param name the name of the end event
   * @param type the type of end event
   */
  public EndEvent(String name, EventType type)
  {
    super(name, type);
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) end event.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) model
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) end event
   */
  @Override
  public List<Token> execute(ModelExecutionContext context)
  {
    return super.execute(context);
  }
}
