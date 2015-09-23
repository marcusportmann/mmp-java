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

package guru.mmp.application.process.bpmn.activity;

import guru.mmp.application.process.bpmn.ModelExecutionContext;
import guru.mmp.application.process.bpmn.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>ScriptTask</code> class represents a Business Process Model and Notation (BPMN)
 * script task that forms part of a BPMN model.
 * <p>
 * This task represents work that is performed by the BPM engine as an automated function written
 * in a script language like JavaScript.
 *
 * @author Marcus Portmann
 */
public class ScriptTask extends Task
{
  /**
   * Execute the Business Process Model and Notation (BPMN) element.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) model
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) element
   */
  @Override
  public List<Token> execute(ModelExecutionContext context)
  {
    return new ArrayList<>();
  }
}
