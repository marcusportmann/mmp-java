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

package guru.mmp.vmware.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.vmware.service.ws.VMwareWebServiceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vim25.*;

import vim25service.VimPortType;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The <code>VMwareTest</code> class.
 *
 * @author Marcus Portmann
 */
public class VMwareTest
{
  /**
   * The VIM service endpoint.
   */
  public static final String VIM_SERVICE_ENDPOINT = "https://22.150.50.16/sdk/vimService.wsdl";

  /**
   * The STS service endpoint.
   */
  public static final String STS_SERVICE_ENDPOINT = "https://22.150.50.16:7444/ims/STSService";

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(VMwareTest.class);

  /**
   * Main.
   *
   * @param args the command-line arguments
   */
  public static void main(String args[])
  {
    VimPortType vimPort = null;
    ServiceContent serviceContent = null;

    try
    {
      vimPort = VMwareWebServiceUtil.getVimPort(STS_SERVICE_ENDPOINT,
          "Administrator@vsphere.local", "Madness5!", VIM_SERVICE_ENDPOINT);

      ManagedObjectReference serviceInstanceReference = new ManagedObjectReference();
      serviceInstanceReference.setType("ServiceInstance");
      serviceInstanceReference.setValue("ServiceInstance");

      serviceContent = vimPort.retrieveServiceContent(serviceInstanceReference);

      ManagedObjectReference viewManagerRef = serviceContent.getViewManager();
      ManagedObjectReference propertyCollector = serviceContent.getPropertyCollector();

      ManagedObjectReference containerViewRef = vimPort.createContainerView(viewManagerRef,
          serviceContent.getRootFolder(), Arrays.asList(new String[] { "Folder" }), true);

      ObjectSpec objectSpec = new ObjectSpec();
      objectSpec.setObj(containerViewRef);
      objectSpec.setSkip(true);

      TraversalSpec traversalSpec = new TraversalSpec();
      traversalSpec.setName("traverseEntities");
      traversalSpec.setPath("view");
      traversalSpec.setSkip(false);
      traversalSpec.setType("ContainerView");

      objectSpec.getSelectSet().add(traversalSpec);

      PropertySpec propertySpec = new PropertySpec();
      propertySpec.setType("Folder");
      propertySpec.getPathSet().add("name");

      PropertyFilterSpec propertyFilterSpec = new PropertyFilterSpec();
      propertyFilterSpec.getObjectSet().add(objectSpec);
      propertyFilterSpec.getPropSet().add(propertySpec);

      List<PropertyFilterSpec> propertyFilterSpecs = new ArrayList<>();
      propertyFilterSpecs.add(propertyFilterSpec);

      RetrieveOptions retrieveOptions = new RetrieveOptions();

      RetrieveResult properties = vimPort.retrievePropertiesEx(propertyCollector,
          propertyFilterSpecs, retrieveOptions);

      if (properties != null)
      {
        for (ObjectContent oc : properties.getObjects())
        {
          String vmName = null;
          String path = null;
          List<DynamicProperty> dps = oc.getPropSet();
          if (dps != null)
          {
            for (DynamicProperty dp : dps)
            {
              vmName = (String) dp.getVal();
              path = dp.getName();
              System.out.println(path + " = " + vmName);
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      logger.error("Failed to execute the VMWare test", e);
    }
    finally
    {
      if ((vimPort != null) && (serviceContent != null))
      {
        try
        {
          vimPort.logout(serviceContent.getSessionManager());
        }
        catch (Throwable e)
        {
          logger.error("Failed to logout of the VMware VIM Service", e);
        }
      }
    }
  }
}
