-- -------------------------------------------------------------------------------------------------
-- NOTE: When changing this file you may also need to modify the following file:
--       - ApplicationPostgres.sql (mmp-application)
--
--  Execute the following command to start the database server if it is not running:
--
--    OS X: sudo su postgres -c '/opt/local/lib/postgresql94/bin/pg_ctl -D /opt/local/var/db/postgresql94/defaultdb -l /opt/local/var/db/postgresql94/postgres.log start'
--    CentOS (as root): service postgresql-9.4 start 
--
--  Execute the following command to create the database:
--
--    OS X: sudo su postgres -c '/opt/local/lib/postgresql94/bin/createdb  --template=template1 --encoding=UTF8 dbname'
--    CentOS (as root): sudo su postgres -c 'createdb --template=template1 --encoding=UTF8 dbname'
--
--  Execute the following command to initialise the database:
--
--    OS X: sudo su postgres -c '/opt/local/lib/postgresql94/bin/psql -d dbname -f ApplicationPostgres.sql'
--    CentOS (as root): su postgres -c 'psql -d dbname -f ApplicationPostgres.sql'
--
--  Execute the following command to delete the database:
--
--    OS X: sudo su postgres -c '/opt/local/lib/postgresql94/bin/dropdb dbname'
--    CentOS (as root): su postgres -c 'dropdb dbname'
--
--  Execute the following command to clean-up unreferenced large objects on the database:
--
--    OS X: sudo su postgres -c '/opt/local/lib/postgresql94/bin/vacuumlo dbname'
--    CentOS (as root): su postgres -c 'vacuumlo dbname'
--
-- -------------------------------------------------------------------------------------------------
set client_min_messages='warning';

-- -------------------------------------------------------------------------------------------------
-- DROP TABLES
-- -------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS MMP.SMS CASCADE;
DROP TABLE IF EXISTS MMP.REPORT_DEFINITIONS CASCADE;
DROP TABLE IF EXISTS MMP.ERROR_REPORTS CASCADE;
DROP TABLE IF EXISTS MMP.PACKAGES CASCADE;
DROP TABLE IF EXISTS MMP.MESSAGE_AUDIT_LOG CASCADE;
DROP TABLE IF EXISTS MMP.MESSAGE_STATUSES CASCADE;
DROP TABLE IF EXISTS MMP.MESSAGE_TYPES CASCADE;
DROP TABLE IF EXISTS MMP.ARCHIVED_MESSAGES CASCADE;
DROP TABLE IF EXISTS MMP.MESSAGE_PARTS CASCADE;
DROP TABLE IF EXISTS MMP.MESSAGES CASCADE;
DROP TABLE IF EXISTS MMP.CACHED_CODES CASCADE;
DROP TABLE IF EXISTS MMP.CACHED_CODE_CATEGORIES CASCADE;
DROP TABLE IF EXISTS MMP.CODES CASCADE;
DROP TABLE IF EXISTS MMP.CODE_CATEGORIES CASCADE;
DROP TABLE IF EXISTS MMP.JOB_PARAMETERS CASCADE;
DROP TABLE IF EXISTS MMP.JOBS CASCADE;
DROP TABLE IF EXISTS MMP.ROLE_TO_GROUP_MAP CASCADE;
DROP TABLE IF EXISTS MMP.FUNCTION_TO_ROLE_MAP CASCADE;
DROP TABLE IF EXISTS MMP.ROLES CASCADE;
DROP TABLE IF EXISTS MMP.FUNCTIONS CASCADE;
DROP TABLE IF EXISTS MMP.GROUPS CASCADE;
DROP TABLE IF EXISTS MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP CASCADE;
DROP TABLE IF EXISTS MMP.INTERNAL_GROUPS CASCADE;
DROP TABLE IF EXISTS MMP.INTERNAL_USERS_PASSWORD_HISTORY CASCADE;
DROP TABLE IF EXISTS MMP.INTERNAL_USERS CASCADE;
DROP TABLE IF EXISTS MMP.USER_DIRECTORY_TO_ORGANISATION_MAP CASCADE;
DROP TABLE IF EXISTS MMP.USER_DIRECTORIES CASCADE;
DROP TABLE IF EXISTS MMP.USER_DIRECTORY_TYPES CASCADE;
DROP TABLE IF EXISTS MMP.ORGANISATIONS CASCADE;
DROP TABLE IF EXISTS MMP.SERVICE_REGISTRY CASCADE;
DROP TABLE IF EXISTS MMP.REGISTRY CASCADE;
DROP TABLE IF EXISTS MMP.IDGENERATOR CASCADE;



-- -------------------------------------------------------------------------------------------------
-- DROP SCHEMAS
-- -------------------------------------------------------------------------------------------------
DROP SCHEMA IF EXISTS MMP CASCADE;



-- -------------------------------------------------------------------------------------------------
-- DROP ROLES
-- -------------------------------------------------------------------------------------------------
DROP OWNED BY dbuser CASCADE;
DROP ROLE IF EXISTS dbuser;



-- -------------------------------------------------------------------------------------------------
-- CREATE ROLES
-- -------------------------------------------------------------------------------------------------
CREATE ROLE dbuser WITH LOGIN PASSWORD 'Password1';
ALTER ROLE dbuser WITH LOGIN;



-- -------------------------------------------------------------------------------------------------
-- CREATE PROCEDURES
-- -------------------------------------------------------------------------------------------------
--
-- CREATE OR REPLACE FUNCTION bytea_import(p_path text, p_result out bytea)
--                    language plpgsql as $$
-- declare
--   l_oid oid;
--   r record;
-- begin
--   p_result := '';
--   select lo_import(p_path) into l_oid;
--   for r in ( select data
--              from pg_largeobject
--              where loid = l_oid
--              order by pageno ) loop
--     p_result = p_result || r.data;
--   end loop;
--   perform lo_unlink(l_oid);
-- end;$$;



-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA MMP;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE MMP.IDGENERATOR (
  NAME     VARCHAR(100) NOT NULL,
  CURRENT  BIGINT DEFAULT 0,

  PRIMARY KEY (NAME)
);

COMMENT ON COLUMN MMP.IDGENERATOR.NAME
  IS 'The name giving the type of entity associated with the generated ID';

COMMENT ON COLUMN MMP.IDGENERATOR.CURRENT
  IS 'The current ID for the type';



CREATE TABLE MMP.REGISTRY (
  ID          UUID NOT NULL,
  PARENT_ID   UUID,
  ENTRY_TYPE  INTEGER NOT NULL,
  NAME        VARCHAR(250) NOT NULL,
  SVALUE      VARCHAR(1024),
  IVALUE      INTEGER,
  DVALUE      DECIMAL(16,12),
  BVALUE      BYTEA,

  PRIMARY KEY (ID)
);

CREATE INDEX MMP_REGISTRY_NAME_IX
  ON MMP.REGISTRY
  (NAME);

CREATE INDEX MMP_REGISTRY_PARENT_ID_IX
  ON MMP.REGISTRY
  (PARENT_ID);

COMMENT ON COLUMN MMP.REGISTRY.ID
  IS 'The unique ID for the registry entry';

COMMENT ON COLUMN MMP.REGISTRY.PARENT_ID
  IS 'The ID of the parent entry for the registry entry';

COMMENT ON COLUMN MMP.REGISTRY.ENTRY_TYPE
  IS 'The type of registry entry';

COMMENT ON COLUMN MMP.REGISTRY.NAME
  IS 'The name of the registry entry';

COMMENT ON COLUMN MMP.REGISTRY.SVALUE
  IS 'The string value for the registry entry';

COMMENT ON COLUMN MMP.REGISTRY.IVALUE
  IS 'The integer value for the registry entry';

COMMENT ON COLUMN MMP.REGISTRY.DVALUE
  IS 'The decimal value for the registry entry';

COMMENT ON COLUMN MMP.REGISTRY.BVALUE
  IS 'The binary value for the registry entry';



CREATE TABLE MMP.SERVICE_REGISTRY (
  NAME                  VARCHAR(255) NOT NULL,
  SECURITY_TYPE         INTEGER NOT NULL,
  REQUIRES_USER_TOKEN   CHAR(1) NOT NULL,
	SUPPORTS_COMPRESSION  CHAR(1) NOT NULL,
	ENDPOINT              VARCHAR(512) NOT NULL,
  SERVICE_CLASS         VARCHAR(512) NOT NULL,
  WSDL_LOCATION         VARCHAR(512) NOT NULL,
  USERNAME              VARCHAR(100),
  PASSWORD              VARCHAR(100),

	PRIMARY KEY (NAME)
);

COMMENT ON COLUMN MMP.SERVICE_REGISTRY.NAME
  IS 'The name used to uniquely identify the web service';

COMMENT ON COLUMN MMP.SERVICE_REGISTRY.SECURITY_TYPE
  IS 'The type of security model implemented by the web service i.e. 0 = None, 1 = WS-Security X509 Certificates, 2 = WS-Security Username Token, 3 = Client SSL, 4 = HTTP Authentication';

COMMENT ON COLUMN MMP.SERVICE_REGISTRY.REQUIRES_USER_TOKEN
  IS 'Does the web service require a user security token';

COMMENT ON COLUMN MMP.SERVICE_REGISTRY.SUPPORTS_COMPRESSION
  IS 'Does the web service support compression';

COMMENT ON COLUMN MMP.SERVICE_REGISTRY.ENDPOINT
  IS 'The endpoint for the web service';

COMMENT ON COLUMN MMP.SERVICE_REGISTRY.SERVICE_CLASS
  IS 'The fully qualified name of the Java service class';

COMMENT ON COLUMN MMP.SERVICE_REGISTRY.WSDL_LOCATION
  IS 'The location of the WSDL defining the web service on the classpath';



CREATE TABLE MMP.ORGANISATIONS (
  ID           BIGINT NOT NULL,
  CODE         VARCHAR(40) NOT NULL,
  NAME         VARCHAR(256) NOT NULL,
  DESCRIPTION  VARCHAR(512),

  PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX MMP_ORGANISATIONS_CODE_IX
  ON MMP.ORGANISATIONS
  (CODE);

COMMENT ON COLUMN MMP.ORGANISATIONS.ID
  IS 'The unique ID for the organisation used to associate the organisation with other database entities';

COMMENT ON COLUMN MMP.ORGANISATIONS.CODE
  IS 'The code uniquely identifying the organisation';

COMMENT ON COLUMN MMP.ORGANISATIONS.NAME
  IS 'The name of the organisation';

COMMENT ON COLUMN MMP.ORGANISATIONS.DESCRIPTION
  IS 'A description for the organisation';



CREATE TABLE MMP.USER_DIRECTORY_TYPES (
  ID                    VARCHAR(40) NOT NULL,
  NAME                  VARCHAR(256) NOT NULL,
  USER_DIRECTORY_CLASS  VARCHAR(256) NOT NULL,
  ADMINISTRATION_CLASS  VARCHAR(256) NOT NULL,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN MMP.USER_DIRECTORY_TYPES.ID
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory type';

COMMENT ON COLUMN MMP.USER_DIRECTORY_TYPES.NAME
IS 'The name of the user directory type';

COMMENT ON COLUMN MMP.USER_DIRECTORY_TYPES.USER_DIRECTORY_CLASS
IS 'The fully qualified name of the Java class that implements the user directory type';

COMMENT ON COLUMN MMP.USER_DIRECTORY_TYPES.ADMINISTRATION_CLASS
IS 'The fully qualified name of the Java class that implements the Wicket component used to administer the configuration for the user directory type';



CREATE TABLE MMP.USER_DIRECTORIES (
  ID             BIGINT NOT NULL,
  TYPE_ID        VARCHAR(40) NOT NULL,
  NAME           VARCHAR(256) NOT NULL,
  DESCRIPTION    VARCHAR(512),
  CONFIGURATION  TEXT NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_USER_DIRECTORY_USER_DIRECTORY_TYPE_FK FOREIGN KEY (TYPE_ID) REFERENCES MMP.USER_DIRECTORY_TYPES(ID) ON DELETE CASCADE
);

COMMENT ON COLUMN MMP.USER_DIRECTORIES.ID
IS 'The unique ID for the user directory used to associate the user directory with other database entities';

COMMENT ON COLUMN MMP.USER_DIRECTORIES.TYPE_ID
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory type';

COMMENT ON COLUMN MMP.USER_DIRECTORIES.NAME
IS 'The name of the user directory';

COMMENT ON COLUMN MMP.USER_DIRECTORIES.DESCRIPTION
IS 'A description for the user directory';

COMMENT ON COLUMN MMP.USER_DIRECTORIES.CONFIGURATION
IS 'The XML configuration data for the user directory';



CREATE TABLE MMP.USER_DIRECTORY_TO_ORGANISATION_MAP (
  USER_DIRECTORY_ID  BIGINT NOT NULL,
  ORGANISATION_ID    BIGINT NOT NULL,

  PRIMARY KEY (USER_DIRECTORY_ID, ORGANISATION_ID),
  CONSTRAINT MMP_USER_DIRECTORY_TO_ORGANISATION_MAP_USER_DIRECTORY_FK FOREIGN KEY (USER_DIRECTORY_ID) REFERENCES MMP.USER_DIRECTORIES(ID) ON DELETE CASCADE,
  CONSTRAINT MMP_USER_DIRECTORY_TO_ORGANISATION_MAP_ORGANISATION_FK FOREIGN KEY (ORGANISATION_ID) REFERENCES MMP.ORGANISATIONS(ID) ON DELETE CASCADE
);

CREATE INDEX MMP_USER_DIRECTORY_TO_ORGANISATION_MAP_USER_DIRECTORY_ID_IX
  ON MMP.USER_DIRECTORY_TO_ORGANISATION_MAP
  (USER_DIRECTORY_ID);

CREATE INDEX MMP_USER_DIRECTORY_TO_ORGANISATION_MAP_ORGANISATION_ID_IX
  ON MMP.USER_DIRECTORY_TO_ORGANISATION_MAP
  (ORGANISATION_ID);

COMMENT ON COLUMN MMP.USER_DIRECTORY_TO_ORGANISATION_MAP.USER_DIRECTORY_ID
  IS 'The unique ID for the user directory';

COMMENT ON COLUMN MMP.USER_DIRECTORY_TO_ORGANISATION_MAP.ORGANISATION_ID
  IS 'The unique ID for the organisation';



CREATE TABLE MMP.INTERNAL_USERS (
  ID                 BIGINT NOT NULL,
  USER_DIRECTORY_ID  BIGINT NOT NULL,
  USERNAME           VARCHAR(100) NOT NULL,
  PASSWORD           VARCHAR(100),
  TITLE              VARCHAR(100),
  FIRST_NAMES        VARCHAR(100),
  LAST_NAME          VARCHAR(100),
  PHONE              VARCHAR(30),
  FAX                VARCHAR(30),
  MOBILE             VARCHAR(30),
  EMAIL              VARCHAR(200),
  PASSWORD_ATTEMPTS  INTEGER,
  PASSWORD_EXPIRY    TIMESTAMP,
  DESCRIPTION        VARCHAR(250),

  PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX MMP_INTERNAL_USERS_USERNAME_IX
  ON MMP.INTERNAL_USERS
  (USERNAME);

COMMENT ON COLUMN MMP.INTERNAL_USERS.ID
  IS 'The unique ID for the internal user used to associate the internal user with other database entities';

COMMENT ON COLUMN MMP.INTERNAL_USERS.USER_DIRECTORY_ID
  IS 'The unique ID for the internal user directory the internal user is associated with';

COMMENT ON COLUMN MMP.INTERNAL_USERS.USERNAME
  IS 'The username for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.PASSWORD
  IS 'The password for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.TITLE
  IS 'The title for the internal user e.g. Mr, Mrs, etc';

COMMENT ON COLUMN MMP.INTERNAL_USERS.FIRST_NAMES
  IS 'The first name(s) / forname(s) for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.LAST_NAME
  IS 'The last name / surname for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.PHONE
  IS 'The telephone number for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.FAX
  IS 'The fax number for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.MOBILE
  IS 'The mobile number for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.EMAIL
  IS 'The e-mail address for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.PASSWORD_ATTEMPTS
  IS 'The number of failed attempts to authenticate the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.PASSWORD_EXPIRY
  IS 'The date and time that the internal user''s password expires';

COMMENT ON COLUMN MMP.INTERNAL_USERS.DESCRIPTION
  IS 'A description for the internal user';



CREATE TABLE MMP.INTERNAL_USERS_PASSWORD_HISTORY (
  ID                BIGINT NOT NULL,
  INTERNAL_USER_ID  BIGINT NOT NULL,
  CHANGED           TIMESTAMP NOT NULL,
  PASSWORD          VARCHAR(100),

  PRIMARY KEY (ID),
  CONSTRAINT MMP_INTERNAL_USERS_PASSWORD_HISTORY_INTERNAL_USER_ID_FK FOREIGN KEY (INTERNAL_USER_ID) REFERENCES MMP.INTERNAL_USERS(ID) ON DELETE CASCADE
);

CREATE INDEX MMP_INTERNAL_USERS_PASSWORD_HISTORY_INTERNAL_USER_ID_IX
  ON MMP.INTERNAL_USERS_PASSWORD_HISTORY
  (INTERNAL_USER_ID);

CREATE INDEX MMP_INTERNAL_USERS_PASSWORD_HISTORY_CHANGED_IX
  ON MMP.INTERNAL_USERS_PASSWORD_HISTORY
  (CHANGED);

COMMENT ON COLUMN MMP.INTERNAL_USERS_PASSWORD_HISTORY.ID
  IS 'The unique ID for the internal user password history entry';

COMMENT ON COLUMN MMP.INTERNAL_USERS_PASSWORD_HISTORY.INTERNAL_USER_ID
  IS 'The unique ID for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS_PASSWORD_HISTORY.CHANGED
  IS 'When the password change took place for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS_PASSWORD_HISTORY.PASSWORD
  IS 'The password for the internal user';



CREATE TABLE MMP.INTERNAL_GROUPS (
  ID                 BIGINT NOT NULL,
  USER_DIRECTORY_ID  BIGINT NOT NULL,
  GROUPNAME          VARCHAR(100) NOT NULL,
  DESCRIPTION        VARCHAR(250),

  PRIMARY KEY (ID)
);

CREATE INDEX MMP_INTERNAL_GROUPS_GROUPNAME_IX
  ON MMP.INTERNAL_GROUPS
  (GROUPNAME);

COMMENT ON COLUMN MMP.INTERNAL_GROUPS.ID
  IS 'The unique ID for the internal group used to associate the internal group with other database entities';

COMMENT ON COLUMN MMP.INTERNAL_GROUPS.USER_DIRECTORY_ID
  IS 'The unique ID for the internal user directory the internal group is associated with';

COMMENT ON COLUMN MMP.INTERNAL_GROUPS.GROUPNAME
  IS 'The group name for the internal group';

COMMENT ON COLUMN MMP.INTERNAL_GROUPS.DESCRIPTION
  IS 'A description for the internal group';



CREATE TABLE MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP (
  USER_DIRECTORY_ID  BIGINT NOT NULL,
  INTERNAL_USER_ID   BIGINT NOT NULL,
  INTERNAL_GROUP_ID  BIGINT NOT NULL,

  PRIMARY KEY (USER_DIRECTORY_ID, INTERNAL_USER_ID, INTERNAL_GROUP_ID),
  CONSTRAINT MMP_INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_USER_DIRECTORY_FK FOREIGN KEY (USER_DIRECTORY_ID) REFERENCES MMP.USER_DIRECTORIES(ID) ON DELETE CASCADE,
  CONSTRAINT MMP_INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_USER_FK FOREIGN KEY (INTERNAL_USER_ID) REFERENCES MMP.INTERNAL_USERS(ID) ON DELETE CASCADE,
  CONSTRAINT MMP_INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_GROUP_FK FOREIGN KEY (INTERNAL_GROUP_ID) REFERENCES MMP.INTERNAL_GROUPS(ID) ON DELETE CASCADE
);

CREATE INDEX MMP_INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_USER_DIRECTORY_ID_IX
  ON MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP
  (USER_DIRECTORY_ID);

CREATE INDEX MMP_INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_USER_ID_IX
  ON MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP
  (INTERNAL_USER_ID);

CREATE INDEX MMP_INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_GROUP_ID_IX
  ON MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP
  (INTERNAL_GROUP_ID);

COMMENT ON COLUMN MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP.USER_DIRECTORY_ID
  IS 'The unique ID for the internal user directory';

COMMENT ON COLUMN MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP.INTERNAL_USER_ID
  IS 'The unique ID for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP.INTERNAL_GROUP_ID
  IS 'The unique ID for the internal group';



CREATE TABLE MMP.GROUPS (
  ID                 BIGINT NOT NULL,
  USER_DIRECTORY_ID  BIGINT NOT NULL,
  GROUPNAME          VARCHAR(100) NOT NULL,

  PRIMARY KEY (ID)
);

CREATE INDEX MMP_GROUPS_USER_DIRECTORY_ID_IX
  ON MMP.GROUPS
  (USER_DIRECTORY_ID);

CREATE INDEX MMP_GROUPS_GROUPNAME_IX
  ON MMP.GROUPS
  (GROUPNAME);

COMMENT ON COLUMN MMP.GROUPS.ID
  IS 'The unique ID for the group used to associate the external group with other database entities';

COMMENT ON COLUMN MMP.GROUPS.USER_DIRECTORY_ID
  IS 'The unique ID for the user directory the external group is associated with';

COMMENT ON COLUMN MMP.GROUPS.GROUPNAME
  IS 'The group name for the external group';



CREATE TABLE MMP.FUNCTIONS (
  ID           BIGINT NOT NULL,
  CODE         VARCHAR(100) NOT NULL,
  NAME         VARCHAR(100) NOT NULL,
  DESCRIPTION  VARCHAR(250),

  PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX MMP_FUNCTIONS_CODE_IX
  ON MMP.FUNCTIONS
  (CODE);

COMMENT ON COLUMN MMP.FUNCTIONS.ID
  IS 'The unique ID for the function used to associate the function with other database entities';

COMMENT ON COLUMN MMP.FUNCTIONS.CODE
  IS 'The unique code used to identify the function';

COMMENT ON COLUMN MMP.FUNCTIONS.NAME
  IS 'The name of the function';

COMMENT ON COLUMN MMP.FUNCTIONS.DESCRIPTION
  IS 'A description for the function';



CREATE TABLE MMP.ROLES (
  ID           BIGINT NOT NULL,
  NAME         VARCHAR(100) NOT NULL,
  DESCRIPTION  VARCHAR(250),

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN MMP.ROLES.ID
  IS 'The unique ID for the role used to associate the role with other database entities';

COMMENT ON COLUMN MMP.ROLES.NAME
  IS 'The name of the role';

COMMENT ON COLUMN MMP.ROLES.DESCRIPTION
  IS 'A description for the role';



CREATE TABLE MMP.FUNCTION_TO_ROLE_MAP (
  FUNCTION_ID  BIGINT NOT NULL,
  ROLE_ID      BIGINT NOT NULL,

  PRIMARY KEY (FUNCTION_ID, ROLE_ID),
  CONSTRAINT MMP_FUNCTION_TO_ROLE_MAP_FUNCTION_FK FOREIGN KEY (FUNCTION_ID) REFERENCES MMP.FUNCTIONS(ID) ON DELETE CASCADE,
  CONSTRAINT MMP_FUNCTION_TO_ROLE_MAP_ROLE_FK FOREIGN KEY (ROLE_ID) REFERENCES MMP.ROLES(ID) ON DELETE CASCADE
);

CREATE INDEX MMP_FUNCTION_TO_ROLE_MAP_FUNCTION_ID_IX
  ON MMP.FUNCTION_TO_ROLE_MAP
  (FUNCTION_ID);

CREATE INDEX MMP_FUNCTION_TO_ROLE_MAP_ROLE_ID_IX
  ON MMP.FUNCTION_TO_ROLE_MAP
  (ROLE_ID);

COMMENT ON COLUMN MMP.FUNCTION_TO_ROLE_MAP.FUNCTION_ID
  IS 'The unique ID for the function';

COMMENT ON COLUMN MMP.FUNCTION_TO_ROLE_MAP.ROLE_ID
  IS 'The unique ID for the role';



CREATE TABLE MMP.ROLE_TO_GROUP_MAP (
  ROLE_ID   BIGINT NOT NULL,
  GROUP_ID  BIGINT NOT NULL,

  PRIMARY KEY (ROLE_ID, GROUP_ID),
  CONSTRAINT MMP_ROLE_TO_GROUP_MAP_ROLE_FK FOREIGN KEY (ROLE_ID) REFERENCES MMP.ROLES(ID) ON DELETE CASCADE,
  CONSTRAINT MMP_ROLE_TO_GROUP_MAP_GROUP_FK FOREIGN KEY (GROUP_ID) REFERENCES MMP.GROUPS(ID) ON DELETE CASCADE
);

CREATE INDEX MMP_ROLE_TO_GROUP_MAP_ROLE_ID_IX
  ON MMP.ROLE_TO_GROUP_MAP
  (ROLE_ID);

CREATE INDEX MMP_ROLE_TO_GROUP_MAP_GROUP_ID_IX
  ON MMP.ROLE_TO_GROUP_MAP
  (GROUP_ID);

COMMENT ON COLUMN MMP.ROLE_TO_GROUP_MAP.ROLE_ID
  IS 'The unique ID for the role';

COMMENT ON COLUMN MMP.ROLE_TO_GROUP_MAP.GROUP_ID
  IS 'The unique ID for the external group';


CREATE TABLE MMP.JOBS (
  ID                  VARCHAR(40) NOT NULL,
  NAME                VARCHAR(512) NOT NULL,
  SCHEDULING_PATTERN  VARCHAR(200) NOT NULL,
  JOB_CLASS           VARCHAR(512) NOT NULL,
  STATUS              INTEGER NOT NULL DEFAULT 1,
  EXECUTION_ATTEMPTS  INTEGER NOT NULL DEFAULT 0,
  LOCK_NAME           VARCHAR(100),
  LAST_EXECUTED       TIMESTAMP,
  NEXT_EXECUTION      TIMESTAMP,
  UPDATED             TIMESTAMP,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN MMP.JOBS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the job';

COMMENT ON COLUMN MMP.JOBS.NAME
  IS 'The name of the job';

COMMENT ON COLUMN MMP.JOBS.SCHEDULING_PATTERN
  IS 'The cron-style scheduling pattern for the job';

COMMENT ON COLUMN MMP.JOBS.JOB_CLASS
  IS 'The fully qualified name of the Java class that implements the job';

COMMENT ON COLUMN MMP.JOBS.STATUS
  IS 'The status of the job';

COMMENT ON COLUMN MMP.JOBS.EXECUTION_ATTEMPTS
  IS 'The number of times the current execution of the job has been attempted';

COMMENT ON COLUMN MMP.JOBS.LOCK_NAME
  IS 'The name of the entity that has locked the job for execution';

COMMENT ON COLUMN MMP.JOBS.LAST_EXECUTED
  IS 'The date and time the job was last executed';

COMMENT ON COLUMN MMP.JOBS.NEXT_EXECUTION
  IS 'The date and time when the job will next be executed';

COMMENT ON COLUMN MMP.JOBS.UPDATED
  IS 'The date and time the job was updated';



CREATE TABLE MMP.JOB_PARAMETERS (
  ID      BIGINT NOT NULL,
  JOB_ID  VARCHAR(40) NOT NULL,
  NAME    VARCHAR(250) NOT NULL,
  VALUE   VARCHAR(1024) NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_JOB_PARAMETERS_JOB_FK FOREIGN KEY (JOB_ID) REFERENCES MMP.JOBS(ID) ON DELETE CASCADE
);

CREATE INDEX MMP_JOB_PARAMETERS_JOB_ID_IX
  ON MMP.JOB_PARAMETERS
  (JOB_ID);

CREATE INDEX MMP_JOB_PARAMETERS_NAME_IX
  ON MMP.JOB_PARAMETERS
  (NAME);

COMMENT ON COLUMN MMP.JOB_PARAMETERS.ID
  IS 'The ID uniquely identifying the job parameter';

COMMENT ON COLUMN MMP.JOB_PARAMETERS.JOB_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the job the job parameter is associated with';

COMMENT ON COLUMN MMP.JOB_PARAMETERS.NAME
  IS 'The name of the job parameter';

COMMENT ON COLUMN MMP.JOB_PARAMETERS.VALUE
  IS 'The value of the job parameter';



CREATE TABLE MMP.CODE_CATEGORIES (
  ID                  VARCHAR(40) NOT NULL,
  ORGANISATION        VARCHAR(40) NOT NULL,
  CATEGORY_TYPE       INTEGER NOT NULL,
  NAME                VARCHAR(256) NOT NULL,
  DESCRIPTION         VARCHAR(512) NOT NULL,
  CODE_DATA           BYTEA,
  ENDPOINT            VARCHAR(512),
  IS_ENDPOINT_SECURE  BOOLEAN NOT NULL DEFAULT FALSE,
  IS_CACHEABLE        BOOLEAN,
  CACHE_EXPIRY        INTEGER,
  UPDATED             TIMESTAMP,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_CODE_CATEGORIES_ORGANISATION_FK FOREIGN KEY (ORGANISATION) REFERENCES MMP.ORGANISATIONS(CODE) ON DELETE CASCADE
);

CREATE INDEX MMP_CODE_CATEGORIES_ORGANISATION_IX
  ON MMP.CODE_CATEGORIES
  (ORGANISATION);

COMMENT ON COLUMN MMP.CODE_CATEGORIES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the code category';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.ORGANISATION
  IS 'The organisation code identifying the organisation the code category is associated with';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.CATEGORY_TYPE
  IS 'The type of code category e.g. Local, RemoteHTTPService, RemoteWebService, etc';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.NAME
  IS 'The name of the code category';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.DESCRIPTION
  IS 'The description for the code category';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.CODE_DATA
  IS 'The custom code data for the code category';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.ENDPOINT
  IS 'The endpoint if this is a remote code category';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.IS_ENDPOINT_SECURE
  IS 'Is the endpoint for the remote code category secure';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.IS_CACHEABLE
  IS 'Is the code data retrieved for the remote code category cacheable';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.CACHE_EXPIRY
  IS 'The time in seconds after which the cached code data for the remote code category will expire';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.UPDATED
  IS 'The date and time the code category was updated';



CREATE TABLE MMP.CODES (
  ID           VARCHAR(80) NOT NULL,
  CATEGORY_ID  VARCHAR(40) NOT NULL,
  NAME         VARCHAR(256) NOT NULL,
  DESCRIPTION  VARCHAR(512),
  VALUE        VARCHAR(512) NOT NULL,

  PRIMARY KEY (CATEGORY_ID, ID),
  CONSTRAINT MMP_CODES_CODE_CATEGORY_FK FOREIGN KEY (CATEGORY_ID) REFERENCES MMP.CODE_CATEGORIES(ID) ON DELETE CASCADE
);

CREATE INDEX MMP_CODES_CATEGORY_ID_IX
  ON MMP.CODES
  (CATEGORY_ID);

COMMENT ON COLUMN MMP.CODES.ID
  IS 'The ID used to uniquely identify the code';

COMMENT ON COLUMN MMP.CODES.CATEGORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the category the code is associated with';

COMMENT ON COLUMN MMP.CODES.NAME
  IS 'The name of the code';

COMMENT ON COLUMN MMP.CODES.DESCRIPTION
  IS 'The description for the code';

COMMENT ON COLUMN MMP.CODES.VALUE
  IS 'The value for the code';



CREATE TABLE MMP.CACHED_CODE_CATEGORIES (
  ID            VARCHAR(40) NOT NULL,
  CODE_DATA     BYTEA,
  LAST_UPDATED  TIMESTAMP NOT NULL,
  CACHED        TIMESTAMP NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_CACHED_CODE_CATEGORIES_CATEGORY_FK FOREIGN KEY (ID) REFERENCES MMP.CODE_CATEGORIES(ID) ON DELETE CASCADE
);

COMMENT ON COLUMN MMP.CACHED_CODE_CATEGORIES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the cached code category';

COMMENT ON COLUMN MMP.CACHED_CODE_CATEGORIES.CODE_DATA
  IS 'The custom code data for the cached code category';

COMMENT ON COLUMN MMP.CACHED_CODE_CATEGORIES.LAST_UPDATED
  IS 'The date and time the cached code category was last updated';

COMMENT ON COLUMN MMP.CACHED_CODE_CATEGORIES.CACHED
  IS 'The date and time the code category was cached';

ALTER TABLE MMP.CACHED_CODE_CATEGORIES ADD CONSTRAINT MMP_FK_CACHED_CODE_CATEGORIES_CODE_CATEGORY FOREIGN KEY (ID) REFERENCES MMP.CODE_CATEGORIES(ID) ON DELETE CASCADE;



CREATE TABLE MMP.CACHED_CODES (
  ID           VARCHAR(80) NOT NULL,
  CATEGORY_ID  VARCHAR(40) NOT NULL,
  NAME         VARCHAR(256) NOT NULL,
  DESCRIPTION  VARCHAR(512),
  VALUE        VARCHAR(512) NOT NULL,

  PRIMARY KEY (CATEGORY_ID, ID),
  CONSTRAINT MMP_CACHED_CODES_CACHED_CODE_CATEGORY_FK FOREIGN KEY (CATEGORY_ID) REFERENCES MMP.CACHED_CODE_CATEGORIES(ID) ON DELETE CASCADE
);

CREATE INDEX MMP_CACHED_CODES_CATEGORY_ID_IX
  ON MMP.CACHED_CODES
  (CATEGORY_ID);

COMMENT ON COLUMN MMP.CACHED_CODES.ID
  IS 'The ID used to uniquely identify the code';

COMMENT ON COLUMN MMP.CACHED_CODES.CATEGORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the category the code is associated with';

COMMENT ON COLUMN MMP.CACHED_CODES.NAME
  IS 'The name of the code';

COMMENT ON COLUMN MMP.CACHED_CODES.DESCRIPTION
  IS 'The description for the code';

COMMENT ON COLUMN MMP.CACHED_CODES.VALUE
  IS 'The value for the code';



CREATE TABLE MMP.MESSAGE_TYPES (
  ID           VARCHAR(40) NOT NULL,
  NAME         VARCHAR(100) NOT NULL,
  DESCRIPTION  VARCHAR(512),

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN MMP.MESSAGE_TYPES.ID
  IS 'The UUID identifying the message type';

COMMENT ON COLUMN MMP.MESSAGE_TYPES.NAME
  IS 'The name of the message type';

COMMENT ON COLUMN MMP.MESSAGE_TYPES.DESCRIPTION
  IS 'A description of the message type';



CREATE TABLE MMP.MESSAGE_STATUSES (
  CODE  INTEGER NOT NULL,
  NAME  VARCHAR(100) NOT NULL,

  PRIMARY KEY (CODE)
);

COMMENT ON COLUMN MMP.MESSAGE_STATUSES.CODE
  IS 'The code identifying the message status';

COMMENT ON COLUMN MMP.MESSAGE_STATUSES.NAME
  IS 'The name of the message status';



CREATE TABLE MMP.MESSAGES (
  ID                 VARCHAR(40) NOT NULL,
  USERNAME           VARCHAR(100) NOT NULL,
  ORGANISATION       VARCHAR(40) NOT NULL,
  DEVICE             VARCHAR(20) NOT NULL,
  MSG_TYPE           VARCHAR(40) NOT NULL,
  MSG_TYPE_VER       INTEGER NOT NULL,
  CORRELATION_ID     VARCHAR(40) NOT NULL,
  PRIORITY           INTEGER NOT NULL,
  STATUS             INTEGER NOT NULL,
  CREATED            TIMESTAMP NOT NULL,
  PERSISTED          TIMESTAMP NOT NULL,
  UPDATED            TIMESTAMP,
  SEND_ATTEMPTS      INTEGER NOT NULL,
  PROCESS_ATTEMPTS   INTEGER NOT NULL,
  DOWNLOAD_ATTEMPTS  INTEGER NOT NULL,
  LOCK_NAME          VARCHAR(100),
  LAST_PROCESSED     TIMESTAMP,
  DATA               BYTEA,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_MESSAGES_ORGANISATION_FK FOREIGN KEY (ORGANISATION) REFERENCES MMP.ORGANISATIONS(CODE),
  CONSTRAINT MMP_MESSAGES_MESSAGE_TYPE_FK FOREIGN KEY (MSG_TYPE) REFERENCES MMP.MESSAGE_TYPES(ID),
  CONSTRAINT MMP_MESSAGES_MESSAGE_STATUS_FK FOREIGN KEY (STATUS) REFERENCES MMP.MESSAGE_STATUSES(CODE)
);

CREATE INDEX MMP_MESSAGES_USERNAME_IX
  ON MMP.MESSAGES
  (USERNAME);

CREATE INDEX MMP_MESSAGES_ORGANISATION_IX
  ON MMP.MESSAGES
  (ORGANISATION);

CREATE INDEX MMP_MESSAGES_DEVICE_IX
  ON MMP.MESSAGES
  (DEVICE);

CREATE INDEX MMP_MESSAGES_MSG_TYPE_IX
  ON MMP.MESSAGES
  (MSG_TYPE);

CREATE INDEX MMP_MESSAGES_PRIORITY_IX
  ON MMP.MESSAGES
  (PRIORITY);

CREATE INDEX MMP_MESSAGES_STATUS_IX
  ON MMP.MESSAGES
  (STATUS);

CREATE INDEX MMP_MESSAGES_LOCK_NAME_IX
  ON MMP.MESSAGES
  (LOCK_NAME);

COMMENT ON COLUMN MMP.MESSAGES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message';

COMMENT ON COLUMN MMP.MESSAGES.USERNAME
  IS 'The username identifying the user associated with the message';

COMMENT ON COLUMN MMP.MESSAGES.ORGANISATION
  IS 'The organisation code identifying the organisation associated with the message';

COMMENT ON COLUMN MMP.MESSAGES.DEVICE
  IS 'The device ID identifying the device the message originated from';

COMMENT ON COLUMN MMP.MESSAGES.MSG_TYPE
  IS 'The UUID identifying the type of message';

COMMENT ON COLUMN MMP.MESSAGES.MSG_TYPE_VER
  IS 'The version of the message type';

COMMENT ON COLUMN MMP.MESSAGES.CORRELATION_ID
  IS 'The UUID used to correlate the message';

COMMENT ON COLUMN MMP.MESSAGES.PRIORITY
  IS 'The message priority';

COMMENT ON COLUMN MMP.MESSAGES.STATUS
  IS 'The message status e.g. Initialised, QueuedForSending, etc';

COMMENT ON COLUMN MMP.MESSAGES.CREATED
  IS 'The date and time the message was created';

COMMENT ON COLUMN MMP.MESSAGES.PERSISTED
  IS 'The date and time the message was persisted';

COMMENT ON COLUMN MMP.MESSAGES.UPDATED
  IS 'The date and time the message was last updated';

COMMENT ON COLUMN MMP.MESSAGES.SEND_ATTEMPTS
  IS 'The number of times that the sending of the message was attempted';

COMMENT ON COLUMN MMP.MESSAGES.PROCESS_ATTEMPTS
  IS 'The number of times that the processing of the message was attempted';

COMMENT ON COLUMN MMP.MESSAGES.DOWNLOAD_ATTEMPTS
  IS 'The number of times that an attempt was made to download the message';

COMMENT ON COLUMN MMP.MESSAGES.LOCK_NAME
  IS 'The name of the entity that has locked the message for processing';

COMMENT ON COLUMN MMP.MESSAGES.LAST_PROCESSED
  IS 'The date and time the last attempt was made to process the message';

COMMENT ON COLUMN MMP.MESSAGES.DATA
  IS 'The data for the message';



CREATE TABLE MMP.MESSAGE_PARTS (
  ID                  VARCHAR(40) NOT NULL,
  PART_NO             INTEGER NOT NULL,
  TOTAL_PARTS         INTEGER NOT NULL,
  SEND_ATTEMPTS       INTEGER NOT NULL,
  DOWNLOAD_ATTEMPTS   INTEGER NOT NULL,
  STATUS              INTEGER NOT NULL,
  PERSISTED           TIMESTAMP NOT NULL,
  UPDATED             TIMESTAMP,
  MSG_ID              VARCHAR(40) NOT NULL,
  MSG_USERNAME        VARCHAR(100) NOT NULL,
  MSG_ORGANISATION    VARCHAR(40) NOT NULL,
  MSG_DEVICE          VARCHAR(20) NOT NULL,
  MSG_TYPE            VARCHAR(40) NOT NULL,
  MSG_TYPE_VER        INTEGER NOT NULL,
  MSG_CORRELATION_ID  VARCHAR(40) NOT NULL,
  MSG_PRIORITY        INTEGER NOT NULL,
  MSG_CREATED         TIMESTAMP NOT NULL,
  MSG_DATA_HASH       VARCHAR(40),
  MSG_ENC_SCHEME      INTEGER NOT NULL,
  MSG_ENC_IV          VARCHAR(512) NOT NULL,
  MSG_CHECKSUM        VARCHAR(40) NOT NULL,
  LOCK_NAME           VARCHAR(100),
  DATA                BYTEA,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_MESSAGE_PARTS_ORGANISATION_FK FOREIGN KEY (MSG_ORGANISATION) REFERENCES MMP.ORGANISATIONS(CODE),
  CONSTRAINT MMP_MESSAGE_PARTS_MESSAGE_TYPE_FK FOREIGN KEY (MSG_TYPE) REFERENCES MMP.MESSAGE_TYPES(ID)
);

CREATE INDEX MMP_MESSAGE_PARTS_STATUS_IX
  ON MMP.MESSAGE_PARTS
  (STATUS);

CREATE INDEX MMP_MESSAGE_PARTS_MSG_ID_IX
  ON MMP.MESSAGE_PARTS
  (MSG_ID);
  
CREATE INDEX MMP_MESSAGE_PARTS_MSG_ORGANISATION_IX
  ON MMP.MESSAGE_PARTS
  (MSG_ORGANISATION);  
  
CREATE INDEX MMP_MESSAGE_PARTS_MSG_TYPE_IX
  ON MMP.MESSAGE_PARTS
  (MSG_TYPE);    

CREATE INDEX MMP_MESSAGE_PARTS_MSG_DEVICE_IX
  ON MMP.MESSAGE_PARTS
  (MSG_DEVICE);

CREATE INDEX MMP_MESSAGE_PARTS_LOCK_NAME_IX
  ON MMP.MESSAGE_PARTS
  (LOCK_NAME);

COMMENT ON COLUMN MMP.MESSAGE_PARTS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message part';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.PART_NO
  IS 'The number of the message part in the set of message parts for the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.TOTAL_PARTS
  IS 'The total number of parts in the set of message parts for the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.SEND_ATTEMPTS
  IS 'The number of times that the sending of the message part was attempted';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.DOWNLOAD_ATTEMPTS
  IS 'The number of times that an attempt was made to download the message part';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.STATUS
  IS 'The message part status e.g. Initialised, QueuedForSending, etc';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.PERSISTED
  IS 'The date and time the message part was persisted';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.UPDATED
  IS 'The date and time the message part was last updated';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_USERNAME
  IS 'The username identifying the user associated with the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_ORGANISATION
  IS 'The organisation code identifying the organisation associated with the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_DEVICE
  IS 'The device ID identifying the device the original message originated from';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_TYPE
  IS 'The UUID identifying the type of the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_TYPE_VER
  IS 'The version of the original message type';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_CORRELATION_ID
  IS 'The UUID used to correlate the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_PRIORITY
  IS 'The priority for the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_CREATED
  IS 'The date and time the original message was created';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_DATA_HASH
  IS 'The hash of the unencrypted data for the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_ENC_SCHEME
  IS 'The encryption scheme used to secure the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_ENC_IV
  IS 'The base-64 encoded initialisation vector for the encryption scheme for the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_CHECKSUM
  IS 'The checksum for the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.LOCK_NAME
  IS 'The name of the entity that has locked the message part for processing';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.DATA
  IS 'The data for the message part';



CREATE TABLE MMP.ARCHIVED_MESSAGES (
  ID              VARCHAR(40) NOT NULL,
  USERNAME        VARCHAR(100) NOT NULL,
  ORGANISATION    VARCHAR(40) NOT NULL,
  DEVICE          VARCHAR(20) NOT NULL,
  MSG_TYPE        VARCHAR(40) NOT NULL,
  MSG_TYPE_VER    INTEGER NOT NULL,
  CORRELATION_ID  VARCHAR(40) NOT NULL,
  CREATED         TIMESTAMP NOT NULL,
  ARCHIVED        TIMESTAMP NOT NULL,
  DATA            BYTEA,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_ARCHIVED_MESSAGES_ORGANISATION_FK FOREIGN KEY (ORGANISATION) REFERENCES MMP.ORGANISATIONS(CODE),
  CONSTRAINT MMP_ARCHIVED_MESSAGES_MESSAGE_TYPE_FK FOREIGN KEY (MSG_TYPE) REFERENCES MMP.MESSAGE_TYPES(ID)
);

CREATE INDEX MMP_ARCHIVED_MESSAGES_USERNAME_IX
  ON MMP.ARCHIVED_MESSAGES
  (USERNAME);

CREATE INDEX MMP_ARCHIVED_MESSAGES_ORGANISATION_IX
  ON MMP.ARCHIVED_MESSAGES
  (ORGANISATION);

CREATE INDEX MMP_ARCHIVED_MESSAGES_DEVICE_IX
  ON MMP.ARCHIVED_MESSAGES
  (DEVICE);

CREATE INDEX MMP_ARCHIVED_MESSAGES_MSG_TYPE_IX
  ON MMP.ARCHIVED_MESSAGES
  (MSG_TYPE);

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.USERNAME
  IS 'The username identifying the user associated with the message';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.ORGANISATION
  IS 'The organisation code identifying the organisation associated with the message';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.DEVICE
  IS 'The device ID identifying the device the message originated from';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.MSG_TYPE
  IS 'The UUID identifying the type of message';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.MSG_TYPE_VER
  IS 'The version of the message type';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.CORRELATION_ID
  IS 'The UUID used to correlate the message';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.CREATED
  IS 'The date and time the message was created';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.ARCHIVED
  IS 'The date and time the message was archived';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.DATA
  IS 'The data for the message';



CREATE TABLE MMP.MESSAGE_AUDIT_LOG (
  ID            BIGINT NOT NULL,
  MSG_TYPE      VARCHAR(40) NOT NULL,
  USERNAME      VARCHAR(100) NOT NULL,
  ORGANISATION  VARCHAR(40) NOT NULL,
  DEVICE        VARCHAR(20) NOT NULL,
  IP            VARCHAR(20) NOT NULL,
  LOGGED        TIMESTAMP NOT NULL,
  SUCCESSFUL    CHAR(1) NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_MESSAGE_AUDIT_LOG_MESSAGE_TYPE_FK FOREIGN KEY (MSG_TYPE) REFERENCES MMP.MESSAGE_TYPES(ID),    
  CONSTRAINT MMP_MESSAGE_AUDIT_LOG_ORGANISATION_FK FOREIGN KEY (ORGANISATION) REFERENCES MMP.ORGANISATIONS(CODE)  
);

CREATE INDEX MMP_MESSAGE_AUDIT_LOG_MSG_TYPE_IX
  ON MMP.MESSAGE_AUDIT_LOG
  (MSG_TYPE);
  
CREATE INDEX MMP_MESSAGE_AUDIT_LOG_ORGANISATION_IX
  ON MMP.MESSAGE_AUDIT_LOG
  (ORGANISATION); 

COMMENT ON COLUMN MMP.MESSAGE_AUDIT_LOG.ID
  IS 'The ID used to uniquely identify the message audit entry';

COMMENT ON COLUMN MMP.MESSAGE_AUDIT_LOG.MSG_TYPE
  IS 'The type of message associated with the message audit entry';

COMMENT ON COLUMN MMP.MESSAGE_AUDIT_LOG.USERNAME
  IS 'The user responsible for the message audit entry';

COMMENT ON COLUMN MMP.MESSAGE_AUDIT_LOG.ORGANISATION
  IS 'The organisation code identifying the organisation associated with the message audit entry';

COMMENT ON COLUMN MMP.MESSAGE_AUDIT_LOG.DEVICE
  IS 'The ID for the device associated with the message audit entry';

COMMENT ON COLUMN MMP.MESSAGE_AUDIT_LOG.IP
  IS 'The IP address of the remote device associated with the message audit entry';

COMMENT ON COLUMN MMP.MESSAGE_AUDIT_LOG.LOGGED
  IS 'The date and time the message audit entry was logged';

COMMENT ON COLUMN MMP.MESSAGE_AUDIT_LOG.SUCCESSFUL
  IS 'Was the message associated with the message audit entry successfully processed';



CREATE TABLE MMP.PACKAGES (
  ID            VARCHAR(40) NOT NULL,
  VERSION       INTEGER NOT NULL,
  ORGANISATION  VARCHAR(40) NOT NULL,
  NAME          VARCHAR(255) NOT NULL,
  IS_CURRENT    BOOLEAN NOT NULL,
  HASH          VARCHAR(40) NOT NULL,
  SIZE          INTEGER NOT NULL,
  CREATED       TIMESTAMP NOT NULL,
  CREATED_BY    VARCHAR(100) NOT NULL,
  DATA          BYTEA NOT NULL,

  PRIMARY KEY (ID, VERSION),
  CONSTRAINT MMP_PACKAGES_ORGANISATION_FK FOREIGN KEY (ORGANISATION) REFERENCES MMP.ORGANISATIONS(CODE)
);

CREATE INDEX MMP_PACKAGES_ORGANISATION_IX
  ON MMP.PACKAGES
  (ORGANISATION);

COMMENT ON COLUMN MMP.PACKAGES.ID
  IS 'The Universally Unique Identifier (UUID) used to identify the package';

COMMENT ON COLUMN MMP.PACKAGES.VERSION
  IS 'The version of the package';

COMMENT ON COLUMN MMP.PACKAGES.ORGANISATION
  IS 'The organisation code identifying the organisation the package is associated with';

COMMENT ON COLUMN MMP.PACKAGES.NAME
  IS 'The name of the package';

COMMENT ON COLUMN MMP.PACKAGES.IS_CURRENT
  IS 'Is the package version current i.e. is this the version of the package that should be installed on a device';

COMMENT ON COLUMN MMP.PACKAGES.HASH
  IS 'The SHA-1 hash used to confirm the authenticity of the package version';

COMMENT ON COLUMN MMP.PACKAGES.SIZE
  IS 'The size of the package version in bytes';

COMMENT ON COLUMN MMP.PACKAGES.CREATED
  IS 'The date and time the package version was created';

COMMENT ON COLUMN MMP.PACKAGES.CREATED_BY
  IS 'The username identifying the user that created the package version';

COMMENT ON COLUMN MMP.PACKAGES.DATA
  IS 'The package version data';



CREATE TABLE MMP.ERROR_REPORTS (
  ID                   VARCHAR(40) NOT NULL,
  APPLICATION_ID       VARCHAR(40) NOT NULL,
  APPLICATION_VERSION  INTEGER NOT NULL,
  DESCRIPTION          VARCHAR(2048) NOT NULL,
  DETAIL               VARCHAR(16384) NOT NULL,
  FEEDBACK             VARCHAR(4000) NOT NULL,
  CREATED              TIMESTAMP NOT NULL,
  WHO                  VARCHAR(100) NOT NULL,
  DEVICE               VARCHAR(40) NOT NULL,
  DATA                 BYTEA,

  PRIMARY KEY (ID)
);

CREATE INDEX MMP_ERROR_REPORTS_APPLICATION_ID_IX
  ON MMP.ERROR_REPORTS
  (APPLICATION_ID);

CREATE INDEX MMP_ERROR_REPORTS_CREATED_IX
  ON MMP.ERROR_REPORTS
  (CREATED);

CREATE INDEX MMP_ERROR_REPORTS_WHO_IX
  ON MMP.ERROR_REPORTS
  (WHO);

COMMENT ON COLUMN MMP.ERROR_REPORTS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the error report';

COMMENT ON COLUMN MMP.ERROR_REPORTS.APPLICATION_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the application that generated the error report';

COMMENT ON COLUMN MMP.ERROR_REPORTS.APPLICATION_VERSION
  IS 'The version of the application that generated the error report';

COMMENT ON COLUMN MMP.ERROR_REPORTS.DESCRIPTION
  IS 'The description of the error';

COMMENT ON COLUMN MMP.ERROR_REPORTS.DETAIL
  IS 'The error detail e.g. a stack trace';

COMMENT ON COLUMN MMP.ERROR_REPORTS.FEEDBACK
  IS 'The feedback provided by the user for the error';

COMMENT ON COLUMN MMP.ERROR_REPORTS.CREATED
  IS 'The date and time the error report was created';

COMMENT ON COLUMN MMP.ERROR_REPORTS.WHO
  IS 'The username identifying the user associated with the error report';

COMMENT ON COLUMN MMP.ERROR_REPORTS.DEVICE
  IS 'The device ID identifying the device the error report originated from';

COMMENT ON COLUMN MMP.ERROR_REPORTS.DATA
  IS 'The data associated with the error report';



CREATE TABLE MMP.REPORT_DEFINITIONS (
  ID            VARCHAR(40) NOT NULL,
  ORGANISATION  VARCHAR(40) NOT NULL,
  NAME          VARCHAR(255) NOT NULL,
  TEMPLATE      BYTEA NULL,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_REPORT_DEFINITIONS_ORGANISATION_FK FOREIGN KEY (ORGANISATION) REFERENCES MMP.ORGANISATIONS(CODE)
);

CREATE INDEX MMP_REPORT_DEFINITIONS_ORGANISATION_IX
  ON MMP.REPORT_DEFINITIONS
  (ORGANISATION);

COMMENT ON COLUMN MMP.REPORT_DEFINITIONS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the report definition';

COMMENT ON COLUMN MMP.REPORT_DEFINITIONS.ORGANISATION
  IS 'The organisation code identifying the organisation the report definition is associated with';

COMMENT ON COLUMN MMP.REPORT_DEFINITIONS.NAME
  IS 'The name of the report definition';

COMMENT ON COLUMN MMP.REPORT_DEFINITIONS.TEMPLATE
  IS 'The JasperReports template for the report definition';




CREATE TABLE MMP.SMS (
  ID              BIGINT NOT NULL,
  MOBILE_NUMBER   VARCHAR(40) NOT NULL,
  MESSAGE         VARCHAR(1024) NOT NULL,
  STATUS          INTEGER NOT NULL,
  SEND_ATTEMPTS   INTEGER NOT NULL,
  LOCK_NAME       VARCHAR(100),
  LAST_PROCESSED  TIMESTAMP,

  PRIMARY KEY (ID)
);

CREATE INDEX MMP_SMS_MOBILE_NUMBER_IX
  ON MMP.SMS
  (MOBILE_NUMBER);

COMMENT ON COLUMN MMP.SMS.ID
  IS 'The ID used to uniquely identify the SMS';

COMMENT ON COLUMN MMP.SMS.MOBILE_NUMBER
  IS 'The mobile number to send the SMS to';

COMMENT ON COLUMN MMP.SMS.MESSAGE
  IS 'The message to send';

COMMENT ON COLUMN MMP.SMS.STATUS
  IS 'The status of the SMS';

COMMENT ON COLUMN MMP.SMS.SEND_ATTEMPTS
  IS 'The number of times that the sending of the SMS was attempted';

COMMENT ON COLUMN MMP.SMS.LOCK_NAME
  IS 'The name of the entity that has locked the SMS for sending';

COMMENT ON COLUMN MMP.SMS.LAST_PROCESSED
  IS 'The date and time the last attempt was made to send the SMS';



CREATE TABLE MMP.PROCESS_DEFINITIONS (
  ID            VARCHAR(40) NOT NULL,
  VERSION       INTEGER NOT NULL,
  ORGANISATION  VARCHAR(40) NOT NULL,
  NAME          VARCHAR(255) NOT NULL,
  DATA          BYTEA NULL,

  PRIMARY KEY (ID, VERSION),
  CONSTRAINT MMP_PROCESS_DEFINITIONS_ORGANISATION_FK FOREIGN KEY (ORGANISATION) REFERENCES MMP.ORGANISATIONS(CODE)
);

CREATE INDEX MMP_PROCESS_DEFINITIONS_ORGANISATION_IX
  ON MMP.PROCESS_DEFINITIONS
  (ORGANISATION);

COMMENT ON COLUMN MMP.PROCESS_DEFINITIONS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the process definition';

COMMENT ON COLUMN MMP.PROCESS_DEFINITIONS.VERSION
  IS 'The version of the process definition';

COMMENT ON COLUMN MMP.PROCESS_DEFINITIONS.ORGANISATION
  IS 'The organisation code identifying the organisation the process definition is associated with';

COMMENT ON COLUMN MMP.PROCESS_DEFINITIONS.NAME
  IS 'The name of the process definition';

COMMENT ON COLUMN MMP.PROCESS_DEFINITIONS.DATA
  IS 'The data for the process definition';



-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO MMP.IDGENERATOR (NAME, CURRENT) VALUES
  ('Application.OrganisationId', 100000);
INSERT INTO MMP.IDGENERATOR (NAME, CURRENT) VALUES
  ('Application.UserDirectoryId', 100000);
INSERT INTO MMP.IDGENERATOR (NAME, CURRENT) VALUES
  ('Application.InternalUserId', 100000);
INSERT INTO MMP.IDGENERATOR (NAME, CURRENT) VALUES
  ('Application.InternalUserPasswordHistoryId', 100000);
INSERT INTO MMP.IDGENERATOR (NAME, CURRENT) VALUES
  ('Application.GroupId', 100000);
INSERT INTO MMP.IDGENERATOR (NAME, CURRENT) VALUES
  ('Application.ExternalGroupId', 100000);
INSERT INTO MMP.IDGENERATOR (NAME, CURRENT) VALUES
  ('Application.FunctionId', 100000);
INSERT INTO MMP.IDGENERATOR (NAME, CURRENT) VALUES
  ('Application.RoleId', 100000);
INSERT INTO MMP.IDGENERATOR (NAME, CURRENT) VALUES
  ('Application.ScheduledTaskParameterId', 100000);
INSERT INTO MMP.IDGENERATOR (NAME, CURRENT) VALUES
  ('Application.CodeId', 10000000);

INSERT INTO MMP.ORGANISATIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (1, 'MMP', 'MMP', 'MMP');

INSERT INTO MMP.USER_DIRECTORY_TYPES (ID, NAME, USER_DIRECTORY_CLASS, ADMINISTRATION_CLASS) VALUES
  ('b43fda33-d3b0-4f80-a39a-110b8e530f4f', 'Internal User Directory', 'guru.mmp.application.security.InternalUserDirectory', 'guru.mmp.application.web.template.component.InternalUserDirectoryAdministrationPanel');

INSERT INTO MMP.USER_DIRECTORIES (ID, TYPE_ID, NAME, DESCRIPTION, CONFIGURATION) VALUES
  (1, 'b43fda33-d3b0-4f80-a39a-110b8e530f4f', 'Internal User Directory', 'Internal User Directory', '<?xml version="1.0" encoding="UTF-8"?><user-directory><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter></user-directory>');

INSERT INTO MMP.USER_DIRECTORY_TO_ORGANISATION_MAP (USER_DIRECTORY_ID, ORGANISATION_ID) VALUES
  (1, 1);

INSERT INTO MMP.INTERNAL_USERS (ID, USER_DIRECTORY_ID, USERNAME, PASSWORD, TITLE, FIRST_NAMES, LAST_NAME, PHONE, FAX,
  MOBILE, EMAIL, PASSWORD_ATTEMPTS, PASSWORD_EXPIRY, DESCRIPTION) VALUES
  (1, 1, 'Administrator', 'GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=', '', '', '', '', '', '', '', null, null, 'Administrator');

INSERT INTO MMP.INTERNAL_GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME, DESCRIPTION) VALUES
  (1, 1, 'Administrators', 'Administrators');
INSERT INTO MMP.INTERNAL_GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME, DESCRIPTION) VALUES
  (2, 1, 'Organisation Administrators', 'Organisation Administrators');

INSERT INTO MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP (USER_DIRECTORY_ID, INTERNAL_USER_ID, INTERNAL_GROUP_ID) VALUES
  (1, 1, 1);

INSERT INTO MMP.GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME) VALUES (1, 1, 'Administrators');
INSERT INTO MMP.GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME) VALUES (2, 1, 'Organisation Administrators');

INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (1, 'Application.SecureHome', 'Secure Home', 'Secure Home');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (2, 'Application.Dashboard', 'Dashboard', 'Dashboard');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (3, 'Application.OrganisationAdministration', 'Organisation Administration', 'Organisation Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (4, 'Application.AddOrganisation', 'Add Organisation', 'Add Organisation');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (5, 'Application.UpdateOrganisation', 'Update Organisation', 'Update Organisation');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (6, 'Application.RemoveOrganisation', 'Remove Organisation', 'Remove Organisation');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (7, 'Application.UserAdministration', 'User Administration', 'User Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (8, 'Application.AddUser', 'Add User', 'Add User');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (9, 'Application.UpdateUser', 'Update User', 'Update User');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (10, 'Application.RemoveUser', 'Remove User', 'Remove User');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (11, 'Application.GroupAdministration', 'Group Administration', 'Group Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (12, 'Application.AddGroup', 'Add Group', 'Add Group');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (13, 'Application.UpdateGroup', 'Update Group', 'Update Group');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (14, 'Application.RemoveGroup', 'Remove Group', 'Remove Group');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (15, 'Application.UserGroups', 'User Groups', 'User Groups');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (16, 'Application.CodeCategoryAdministration', 'Code Category Administration', 'Code Category Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (17, 'Application.AddCodeCategory', 'Add Code Category', 'Add Code Category');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (18, 'Application.RemoveCodeCategory', 'Remove Code Category', 'Remove Code Category');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (19, 'Application.UpdateCodeCategory', 'Update Code Category', 'Update Code Category');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (20, 'Application.CodeAdministration', 'Code Administration', 'Code Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (21, 'Application.AddCode', 'Add Code', 'Add Code');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (22, 'Application.RemoveCode', 'Remove Code', 'Remove Code');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (23, 'Application.UpdateCode', 'Update Code', 'Update Code');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (24, 'Application.ResetUserPassword', 'Reset User Password', 'Reset User Password');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (25, 'Application.SecurityAdministration', 'Security Administration', 'Security Administration');

INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (1000, 'ApplicationMessaging.ErrorReports', 'Error Reports', 'Error Reports');

INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (3000, 'ApplicationReporting.ReportDefinitionAdministration', 'Report Definition Administration', 'Report Definition Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (3001, 'ApplicationReporting.AddReportDefinition', 'Add Report Definition', 'Add Report Definition');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (3002, 'ApplicationReporting.RemoveReportDefinition', 'Remove Report Definition', 'Remove Report Definition');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (3003, 'ApplicationReporting.UpdateReportDefinition', 'Update Report Definition', 'Update Report Definition');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (3004, 'ApplicationReporting.ViewReport', 'View Report', 'View Report');

INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (4000, 'ApplicationProcess.ProcessDefinitionAdministration', 'Process Definition Administration', 'Process Definition Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (4001, 'ApplicationProcess.AddProcessDefinition', 'Add Process Definition', 'Add Process Definition');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (4002, 'ApplicationProcess.RemoveProcessDefinition', 'Remove Process Definition', 'Remove Process Definition');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (4003, 'ApplicationProcess.UpdateProcessDefinition', 'Update Process Definition', 'Update Process Definition');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (4004, 'ApplicationProcess.ViewProcess', 'View Process', 'View Process');

-- INSERT INTO MMP.FUNCTION_TEMPLATES (ID, CODE, NAME, DESCRIPTION) VALUES
--   (1, 'Application.Administration', 'Administration', 'Administration');

-- INSERT INTO MMP.FUNCTION_TEMPLATE_MAP (FUNCTION_ID, TEMPLATE_ID) VALUES (1, 1);
-- INSERT INTO MMP.FUNCTION_TEMPLATE_MAP (FUNCTION_ID, TEMPLATE_ID) VALUES (2, 1);

INSERT INTO MMP.ROLES (ID, NAME, DESCRIPTION) VALUES
  (1, 'Administrator', 'Administrator');
INSERT INTO MMP.ROLES (ID, NAME, DESCRIPTION) VALUES
  (2, 'Organisation Administrator', 'Organisation Administrator');

INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (1, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (2, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (3, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (4, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (5, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (6, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (7, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (8, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (9, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (10, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (11, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (12, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (13, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (14, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (15, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (16, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (17, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (18, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (19, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (20, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (21, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (22, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (23, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (24, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (25, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (1000, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (3000, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (3001, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (3002, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (3003, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (3004, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (4000, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (4001, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (4002, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (4003, 1);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (4004, 1);

INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (1, 2);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (2, 2);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (7, 2);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (8, 2);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (9, 2);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (10, 2);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (15, 2);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (23, 2);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (24, 2);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (3004, 2);
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES (4004, 2);

INSERT INTO MMP.ROLE_TO_GROUP_MAP (ROLE_ID, GROUP_ID) VALUES (1, 1);
INSERT INTO MMP.ROLE_TO_GROUP_MAP (ROLE_ID, GROUP_ID) VALUES (2, 2);

INSERT INTO MMP.MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('3dbf238d-b56f-468a-8850-4ddf9f15c329', 'RegisterRequest', '');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('aa08aac9-4d15-452f-b3f9-756641b71735', 'RegisterResponse', '');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('a589dc87-2328-4a9b-bdb6-970e55ca2323', 'TestRequest', '');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('a3bad7ba-f9d4-4403-b54a-cb1f335ebbad', 'TestResponse', '');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('e9918051-8ebc-48f1-bad7-13c59b550e1a', 'AnotherTestRequest', '');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('a714a9c6-2914-4498-ab59-64be9991bf37', 'AnotherTestResponse', '');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('ff638c33-b4f1-4e79-804c-9560da2543d6', 'SubmitErrorReportRequest', '');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('8be50cfa-2fb1-4634-9bfa-d01e77eaf766', 'SubmitErrorReportResponse', '');

INSERT INTO MMP.MESSAGE_STATUSES (CODE, NAME) VALUES (0, 'Initialised');
INSERT INTO MMP.MESSAGE_STATUSES (CODE, NAME) VALUES (1, 'QueuedForSending');
INSERT INTO MMP.MESSAGE_STATUSES (CODE, NAME) VALUES (2, 'QueuedForProcessing');
INSERT INTO MMP.MESSAGE_STATUSES (CODE, NAME) VALUES (3, 'Aborted');
INSERT INTO MMP.MESSAGE_STATUSES (CODE, NAME) VALUES (4, 'Failed');
INSERT INTO MMP.MESSAGE_STATUSES (CODE, NAME) VALUES (5, 'Processing');
INSERT INTO MMP.MESSAGE_STATUSES (CODE, NAME) VALUES (6, 'Sending');
INSERT INTO MMP.MESSAGE_STATUSES (CODE, NAME) VALUES (7, 'QueuedForDownload');
INSERT INTO MMP.MESSAGE_STATUSES (CODE, NAME) VALUES (8, 'Downloading');
INSERT INTO MMP.MESSAGE_STATUSES (CODE, NAME) VALUES (10, 'Processed');



-- -------------------------------------------------------------------------------------------------
-- SET PERMISSIONS
-- -------------------------------------------------------------------------------------------------
GRANT ALL ON SCHEMA MMP TO dbuser;

GRANT ALL ON TABLE MMP.IDGENERATOR TO dbuser;
GRANT ALL ON TABLE MMP.REGISTRY TO dbuser;
GRANT ALL ON TABLE MMP.SERVICE_REGISTRY TO dbuser;
GRANT ALL ON TABLE MMP.ORGANISATIONS TO dbuser;
GRANT ALL ON TABLE MMP.USER_DIRECTORY_TYPES TO dbuser;
GRANT ALL ON TABLE MMP.USER_DIRECTORIES TO dbuser;
GRANT ALL ON TABLE MMP.USER_DIRECTORY_TO_ORGANISATION_MAP TO dbuser;
GRANT ALL ON TABLE MMP.INTERNAL_USERS TO dbuser;
GRANT ALL ON TABLE MMP.INTERNAL_USERS_PASSWORD_HISTORY TO dbuser;
GRANT ALL ON TABLE MMP.INTERNAL_GROUPS TO dbuser;
GRANT ALL ON TABLE MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP TO dbuser;
GRANT ALL ON TABLE MMP.GROUPS TO dbuser;
GRANT ALL ON TABLE MMP.FUNCTIONS TO dbuser;
GRANT ALL ON TABLE MMP.ROLES TO dbuser;
GRANT ALL ON TABLE MMP.FUNCTION_TO_ROLE_MAP TO dbuser;
GRANT ALL ON TABLE MMP.ROLE_TO_GROUP_MAP TO dbuser;
GRANT ALL ON TABLE MMP.JOBS TO dbuser;
GRANT ALL ON TABLE MMP.JOB_PARAMETERS TO dbuser;
GRANT ALL ON TABLE MMP.CODE_CATEGORIES TO dbuser;
GRANT ALL ON TABLE MMP.CODES TO dbuser;
GRANT ALL ON TABLE MMP.CACHED_CODE_CATEGORIES TO dbuser;
GRANT ALL ON TABLE MMP.CACHED_CODES TO dbuser;
GRANT ALL ON TABLE MMP.MESSAGES TO dbuser;
GRANT ALL ON TABLE MMP.MESSAGE_PARTS TO dbuser;
GRANT ALL ON TABLE MMP.ARCHIVED_MESSAGES TO dbuser;
GRANT ALL ON TABLE MMP.MESSAGE_TYPES TO dbuser;
GRANT ALL ON TABLE MMP.MESSAGE_STATUSES TO dbuser;
GRANT ALL ON TABLE MMP.MESSAGE_AUDIT_LOG TO dbuser;
GRANT ALL ON TABLE MMP.PACKAGES TO dbuser;
GRANT ALL ON TABLE MMP.ERROR_REPORTS TO dbuser;
GRANT ALL ON TABLE MMP.REPORT_DEFINITIONS TO dbuser;
GRANT ALL ON TABLE MMP.SMS TO dbuser;


