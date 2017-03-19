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
//package guru.mmp.common.persistence;
//
////~--- non-JDK imports --------------------------------------------------------
//
//import guru.mmp.common.util.StringUtil;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
////~--- JDK imports ------------------------------------------------------------
//
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.SQLException;
//
//import javax.naming.InitialContext;
//
//import javax.sql.DataSource;
//
///**
// * The <code>DataAccessObject</code> class is the common base class which all data access objects
// * must extend.
// *
// * @author Marcus Portmann
// */
//public abstract class DataAccessObject
//  implements IDataAccessObject
//{
//  /**
//   * The mmp-java Java and JEE development framework database schema.
//   */
//  public static final String MMP_DATABASE_SCHEMA = "MMP";
//
//  /* Logger */
//  private static final Logger logger = LoggerFactory.getLogger(DataAccessObject.class);
//
//  /**
//   * The data source used to provide connections to the database.
//   */
//  private DataSource dataSource;
//
//  /**
//   * The ID generator used to generate unique numeric IDs for the DAO.
//   */
//  private IDGenerator idGenerator;
//
//  /**
//   * The name of the database schema that contains the database objects associated with the DAO.
//   * Also called the <b>database schema</b>.
//   */
//  private String schema;
//
//  /**
//   * Constructs a new <code>DataAccessObject</code>.
//   * <p/>
//   * The data source will be retrieved using the JNDI name java:app/jdbc/ApplicationDataSource
//   * and the database schema will be retrieved using the JNDI name
//   * java:app/jdbc/ApplicationDatabaseSchema.
//   */
//  public DataAccessObject()
//  {
//    try
//    {
//      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
//    }
//    catch (Throwable ignored) {}
//
//    if (dataSource == null)
//    {
//      try
//      {
//        dataSource = InitialContext.doLookup("java:comp/env/jdbc/ApplicationDataSource");
//      }
//      catch (Throwable ignored) {}
//    }
//
//    if (dataSource == null)
//    {
//      throw new DAOException("Failed to retrieve the application data source using the JNDI names "
//          + "(java:app/jdbc/ApplicationDataSource) and (java:comp/env/jdbc/ApplicationDataSource)");
//    }
//
//    try
//    {
//      schema = InitialContext.doLookup("java:app/env/ApplicationDatabaseSchema");
//    }
//    catch (Throwable ignored) {}
//
//    if (schema == null)
//    {
//      try
//      {
//        schema = InitialContext.doLookup("java:comp/env/ApplicationDatabaseSchema");
//      }
//      catch (Throwable ignored) {}
//    }
//
//    if (StringUtil.isNullOrEmpty(schema))
//    {
//      logger.info("The application database schema was not set using the JNDI environment entry "
//          + "(java:app/env/ApplicationDatabaseSchema) or "
//          + "(java:comp/env/ApplicationDatabaseSchema) using the default schema ("
//          + MMP_DATABASE_SCHEMA + ")");
//
//      schema = MMP_DATABASE_SCHEMA;
//    }
//
//    init();
//  }
//
//  /**
//   * Constructs a new <code>DataAccessObject</code>.
//   *
//   * @param dataSource the data source to use
//   */
//  protected DataAccessObject(DataSource dataSource)
//  {
//    if (dataSource == null)
//    {
//      throw new DAOException("Unable to initialise the " + getClass().getName()
//          + " data access object using a " + "<code>null</code> data source");
//    }
//
//    this.dataSource = dataSource;
//    this.schema = MMP_DATABASE_SCHEMA;
//    init();
//  }
//
//  /**
//   * Constructs a new <code>DataAccessObject</code>.
//   *
//   * @param dataSource the data source to use
//   * @param schema     the name of the database schema that contains the database objects
//   *                   referenced by the DAO
//   */
//  protected DataAccessObject(DataSource dataSource, String schema)
//  {
//    if (dataSource == null)
//    {
//      throw new DAOException("Unable to initialise the " + getClass().getName()
//          + " data access object using a " + "<code>null</code> data source");
//    }
//
//    if (StringUtil.isNullOrEmpty(schema))
//    {
//      throw new DAOException("Unable to initialise the " + getClass().getName()
//          + " data access object using a " + "<code>null</code> or empty schema name");
//    }
//
//    this.dataSource = dataSource;
//    init();
//  }
//
//  /**
//   * Constructs a new <code>DataAccessObject</code>.
//   *
//   * @param dataSourceJndiName the JNDI name of the data source used to access the database
//   * @param schema             the name of the database schema that contains the database objects
//   *                           referenced by the DAO
//   */
//  protected DataAccessObject(String dataSourceJndiName, String schema)
//  {
//    if (StringUtil.isNullOrEmpty(schema))
//    {
//      throw new DAOException("Unable to initialise the " + getClass().getName()
//          + " data access object using a " + "<code>null</code> or empty schema name");
//    }
//
//    try
//    {
//      InitialContext ic = new InitialContext();
//
//      try
//      {
//        this.dataSource = (DataSource) ic.lookup(dataSourceJndiName);
//      }
//      finally
//      {
//        try
//        {
//          ic.close();
//        }
//        catch (Throwable e)
//        {
//          // Do nothing
//        }
//      }
//    }
//    catch (Throwable e)
//    {
//      throw new DAOException("Failed to initialise the " + getClass().getName()
//          + " data access object: Failed to " + "lookup the data source (" + dataSourceJndiName
//          + ") using JNDI: " + e.getMessage(), e);
//    }
//
//    init();
//  }
//
//  /**
//   * This method must be implemented by all classes derived from <code>DataAccessObject</code> and
//   * should contain the code to generate the SQL statements for the DAO.
//   *
//   * @param schema          the name of the database schema that contains the database objects
//   *                        referenced by the DAO
//   * @param schemaSeparator the separator that the database uses to separate the name of a
//   *                        database schema and a database object located in the schema
//   * @param schemaPrefix    the schema prefix to prepend to database objects referenced by the DAO
//   * @param idQuote         the string used quote SQL identifiers
//   *
//   * @throws SQLException
//   */
//  protected abstract void buildStatements(String schema, String schemaSeparator,
//      String schemaPrefix, String idQuote)
//    throws SQLException;
//
//  /**
//   * Returns the data source associated with the DAO.
//   *
//   * @return the data source associated with the DAO
//   */
//  protected DataSource getDataSource()
//  {
//    return dataSource;
//  }
//
//  /**
//   * Returns the ID generator used to generate unique numeric IDs for the DAO.
//   *
//   * @return the ID generator used to generate unique numeric IDs for the DAO
//   */
//  @SuppressWarnings("unused")
//  protected IDGenerator getIDGenerator()
//  {
//    return idGenerator;
//  }
//
//  /**
//   * Return the name of the database schema that contains the database objects referenced by
//   * the DAO.
//   *
//   * @return the name of the database schema that contains the database objects referenced by
//   * the DAO
//   */
//  protected String getSchema()
//  {
//    return schema;
//  }
//
//  /**
//   * Initialise the <code>DataAccessObject</code> instance.
//   */
//  private void init()
//  {
//    try
//    {
//      // Retrieve the database meta data
//      String schemaSeparator;
//      String idQuote;
//
//      try (Connection connection = dataSource.getConnection())
//      {
//        DatabaseMetaData metaData = connection.getMetaData();
//
//        // Retrieve the schema separator for the database
//        schemaSeparator = metaData.getCatalogSeparator();
//
//        if ((schemaSeparator == null) || (schemaSeparator.length() == 0))
//        {
//          schemaSeparator = ".";
//        }
//
//        // Retrieve the identifier enquoting string for the database
//        idQuote = metaData.getIdentifierQuoteString();
//
//        if ((idQuote == null) || (idQuote.length() == 0))
//        {
//          idQuote = "\"";
//        }
//      }
//
//      // Determine the schema prefix
//      String schemaPrefix = idQuote + schema + idQuote + schemaSeparator;
//
//      // Build the SQL statements for the DAO
//      buildStatements(schema, schemaSeparator, schemaPrefix, idQuote);
//    }
//    catch (Throwable e)
//    {
//      throw new DAOException("Failed to initialise the " + getClass().getName()
//          + " data access object: " + e.getMessage(), e);
//    }
//
//    idGenerator = new IDGenerator(dataSource, schema);
//  }
//}
