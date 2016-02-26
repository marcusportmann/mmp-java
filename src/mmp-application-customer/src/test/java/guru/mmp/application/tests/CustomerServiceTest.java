package guru.mmp.application.tests;

import guru.mmp.application.customer.Customer;
import guru.mmp.application.customer.ICustomerService;
import guru.mmp.application.security.Organisation;
import guru.mmp.application.test.ApplicationClassRunner;
import guru.mmp.application.test.ApplicationDataSourceResourceReference;
import guru.mmp.application.test.ApplicationDataSourceSQLResource;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

import javax.inject.Inject;

/**
 * The <code>CustomerServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>CustomerService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationClassRunner.class)
public class CustomerServiceTest
{
  private static int organisationCount;
  private static int customerCount;
  @Inject
  private ICustomerService customerService;

  /**
   * customerTest
   *
   * @throws Exception
   */
  @Test
  public void customerTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    customerService.createOrganisation(organisation);

    Organisation retrievedOrganisation = customerService.getOrganisation(organisation.getId());

    assertNotNull(retrievedOrganisation);

    Customer customer = new Customer();
    customer.setId(organisation.getId());
    customer.setOrganisation(organisation);
    customer.setName("Test Customer");

    customerService.createCustomer(customer);

    Customer retrievedCustomer = customerService.getCustomer(customer.getId());

    assertNotNull(retrievedCustomer);

    compareCustomers(customer, retrievedCustomer);


  }

  /**
   * organisationTest
   *
   * @throws Exception
   */
  @Test
  public void organisationTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    customerService.createOrganisation(organisation);

    Organisation retrievedOrganisation = customerService.getOrganisation(organisation.getId());

    assertNotNull(retrievedOrganisation);

    compareOrganisations(organisation, retrievedOrganisation);

    organisation.setName(organisation.getName() + " Updated");

    customerService.updateOrganisation(organisation);

    retrievedOrganisation = customerService.getOrganisation(organisation.getId());

    assertNotNull(retrievedOrganisation);
  }

  private static synchronized Customer getTestCustomerDetails(Organisation organisation)
  {
    customerCount++;

    Customer customer = new Customer();
    customer.setId(organisation.getId());
    customer.setOrganisation(organisation);
    customer.setName("Test Customer Name " + customerCount);

    return customer;
  }

  private static synchronized Organisation getTestOrganisationDetails()
  {
    organisationCount++;

    Organisation organisation = new Organisation();
    organisation.setId(UUID.randomUUID());
    organisation.setName("Test Organisation Name " + organisationCount);

    return organisation;
  }

  private void compareCustomers(Customer customer1, Customer customer2)
  {
    assertEquals("The ID values for the two customers do not match", customer1.getId(),
      customer2.getId());
    assertEquals("The organisation ID values for the two customers do not match",
      customer1.getOrganisation().getId(), customer2.getOrganisation().getId());
    assertEquals("The name values for the two customers do not match", customer1.getName(),
      customer2.getName());
  }

  private void compareOrganisations(Organisation organisation1, Organisation organisation2)
  {
    assertEquals("The ID values for the two organisations do not match", organisation1.getId(),
      organisation2.getId());
    assertEquals("The name values for the two organisations do not match", organisation1.getName(),
      organisation2.getName());
  }
}

