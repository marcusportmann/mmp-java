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

package guru.mmp.application.process.test;

import guru.mmp.application.process.bpmn.Parser;
import guru.mmp.common.util.ResourceUtil;
import org.junit.Test;

/**
 * The <code>BPMNParserTests</code> implements the unit tests for the BPMN parsing capability.
 */
public class BPMNParserTests
{
  @Test
  public void bpmnParserTest()
  {
    try
    {
      // Load the XML BPMN data from the classpath
      byte[] processDefinitionData = ResourceUtil.getClasspathResource(
        "guru/mmp/application/process/test/Test.bpmn");

      Parser parser = new Parser();

      parser.parse(processDefinitionData);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to parse the BPMN file", e);
    }
  }
}
