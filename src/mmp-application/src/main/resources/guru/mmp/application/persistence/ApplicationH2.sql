-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA MMP;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE MMP.TEST_DATA (
  ID     VARCHAR(40) NOT NULL,
  NAME   VARCHAR(100) NOT NULL,
  VALUE  VARCHAR(1000) NOT NULL,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN MMP.TEST_DATA.ID
  IS 'The ID used to uniquely identify the test data';

COMMENT ON COLUMN MMP.TEST_DATA.NAME
  IS 'The name for the test data';

COMMENT ON COLUMN MMP.TEST_DATA.VALUE
  IS 'The value for the test data';



CREATE TABLE MMP.IDGENERATOR (
  NAME     VARCHAR(100) NOT NULL,
  CURRENT  BIGINT DEFAULT 0,

  PRIMARY KEY (NAME)
);

COMMENT ON COLUMN MMP.IDGENERATOR.NAME
  IS 'The name giving the type of entity associated with the generated ID';

COMMENT ON COLUMN MMP.IDGENERATOR.CURRENT
  IS 'The current ID for the type';



CREATE TABLE MMP.CONFIG (
  KEY    VARCHAR(1000) NOT NULL,
  VALUE  VARCHAR(1000000) NOT NULL,

  PRIMARY KEY (KEY)
);

COMMENT ON COLUMN MMP.CONFIG.KEY
  IS 'The key used to uniquely identify the configuration entry';

COMMENT ON COLUMN MMP.CONFIG.VALUE
  IS 'The value for the configuration entry';



CREATE TABLE MMP.REGISTRY (
  ID          UUID NOT NULL,
  PARENT_ID   UUID,
  ENTRY_TYPE  INTEGER NOT NULL,
  NAME        VARCHAR(100) NOT NULL,
  SVALUE      VARCHAR(1000),
  IVALUE      INTEGER,
  DVALUE      DECIMAL(16,12),
  BVALUE      BLOB,

  PRIMARY KEY (ID)
);

CREATE INDEX MMP_REGISTRY_NAME_IX
  ON MMP.REGISTRY
  (NAME);

CREATE INDEX MMP_REGISTRY_PARENT_ID_IX
  ON MMP.REGISTRY
  (PARENT_ID);

COMMENT ON COLUMN MMP.REGISTRY.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the registry entry';

COMMENT ON COLUMN MMP.REGISTRY.PARENT_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the parent entry for the registry entry';

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
  NAME                  VARCHAR(100) NOT NULL,
  SECURITY_TYPE         INTEGER NOT NULL,
  REQUIRES_USER_TOKEN   CHAR(1) NOT NULL,
	SUPPORTS_COMPRESSION  CHAR(1) NOT NULL,
	ENDPOINT              VARCHAR(1000) NOT NULL,
  SERVICE_CLASS         VARCHAR(200) NOT NULL,
  WSDL_LOCATION         VARCHAR(1000) NOT NULL,
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
  ID           UUID NOT NULL,
  NAME         VARCHAR(100) NOT NULL,

  PRIMARY KEY (ID)
);

CREATE INDEX MMP_ORGANISATIONS_NAME_IX
  ON MMP.ORGANISATIONS
  (NAME);

COMMENT ON COLUMN MMP.ORGANISATIONS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the organisation';

COMMENT ON COLUMN MMP.ORGANISATIONS.NAME
  IS 'The name of the organisation';



CREATE TABLE MMP.USER_DIRECTORY_TYPES (
  ID                    UUID NOT NULL,
  NAME                  VARCHAR(100) NOT NULL,
  USER_DIRECTORY_CLASS  VARCHAR(200) NOT NULL,
  ADMINISTRATION_CLASS  VARCHAR(200) NOT NULL,

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
  ID             UUID NOT NULL,
  TYPE_ID        UUID NOT NULL,
  NAME           VARCHAR(100) NOT NULL,
  CONFIGURATION  VARCHAR(10000) NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_USER_DIRECTORY_USER_DIRECTORY_TYPE_FK FOREIGN KEY (TYPE_ID) REFERENCES MMP.USER_DIRECTORY_TYPES(ID) ON DELETE CASCADE
);

CREATE INDEX MMP_USER_DIRECTORIES_NAME_IX
  ON MMP.USER_DIRECTORIES
  (NAME);

COMMENT ON COLUMN MMP.USER_DIRECTORIES.ID
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory';

COMMENT ON COLUMN MMP.USER_DIRECTORIES.TYPE_ID
IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory type';

COMMENT ON COLUMN MMP.USER_DIRECTORIES.NAME
IS 'The name of the user directory';

COMMENT ON COLUMN MMP.USER_DIRECTORIES.CONFIGURATION
IS 'The XML configuration data for the user directory';



CREATE TABLE MMP.USER_DIRECTORY_TO_ORGANISATION_MAP (
  USER_DIRECTORY_ID  UUID NOT NULL,
  ORGANISATION_ID    UUID NOT NULL,

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
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory';

COMMENT ON COLUMN MMP.USER_DIRECTORY_TO_ORGANISATION_MAP.ORGANISATION_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the organisation';



CREATE TABLE MMP.INTERNAL_USERS (
  ID                 UUID NOT NULL,
  USER_DIRECTORY_ID  UUID NOT NULL,
  USERNAME           VARCHAR(100) NOT NULL,
  PASSWORD           VARCHAR(50),
  FIRST_NAMES        VARCHAR(100),
  LAST_NAME          VARCHAR(100),
  MOBILE             VARCHAR(30),
  EMAIL              VARCHAR(200),
  PASSWORD_ATTEMPTS  INTEGER,
  PASSWORD_EXPIRY    TIMESTAMP,

  PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX MMP_INTERNAL_USERS_USERNAME_IX
  ON MMP.INTERNAL_USERS
  (USERNAME);

COMMENT ON COLUMN MMP.INTERNAL_USERS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.USER_DIRECTORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the internal user is associated with';

COMMENT ON COLUMN MMP.INTERNAL_USERS.USERNAME
  IS 'The username for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.PASSWORD
  IS 'The password for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.FIRST_NAMES
  IS 'The first name(s) / forname(s) for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.LAST_NAME
  IS 'The last name / surname for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.MOBILE
  IS 'The mobile number for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.EMAIL
  IS 'The e-mail address for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.PASSWORD_ATTEMPTS
  IS 'The number of failed attempts to authenticate the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS.PASSWORD_EXPIRY
  IS 'The date and time that the internal user''s password expires';



CREATE TABLE MMP.INTERNAL_USERS_PASSWORD_HISTORY (
  ID                UUID NOT NULL,
  INTERNAL_USER_ID  UUID NOT NULL,
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
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the password history entry';

COMMENT ON COLUMN MMP.INTERNAL_USERS_PASSWORD_HISTORY.INTERNAL_USER_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS_PASSWORD_HISTORY.CHANGED
  IS 'When the password change took place for the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USERS_PASSWORD_HISTORY.PASSWORD
  IS 'The password for the internal user';



CREATE TABLE MMP.INTERNAL_GROUPS (
  ID                 UUID NOT NULL,
  USER_DIRECTORY_ID  UUID NOT NULL,
  GROUPNAME          VARCHAR(100) NOT NULL,
  DESCRIPTION        VARCHAR(250),

  PRIMARY KEY (ID)
);

CREATE INDEX MMP_INTERNAL_GROUPS_GROUPNAME_IX
  ON MMP.INTERNAL_GROUPS
  (GROUPNAME);

COMMENT ON COLUMN MMP.INTERNAL_GROUPS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal group';

COMMENT ON COLUMN MMP.INTERNAL_GROUPS.USER_DIRECTORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the internal group is associated with';

COMMENT ON COLUMN MMP.INTERNAL_GROUPS.GROUPNAME
  IS 'The group name for the internal group';

COMMENT ON COLUMN MMP.INTERNAL_GROUPS.DESCRIPTION
  IS 'A description for the internal group';



CREATE TABLE MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP (
  USER_DIRECTORY_ID  UUID NOT NULL,
  INTERNAL_USER_ID   UUID NOT NULL,
  INTERNAL_GROUP_ID  UUID NOT NULL,

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
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory';

COMMENT ON COLUMN MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP.INTERNAL_USER_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP.INTERNAL_GROUP_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal group';



CREATE TABLE MMP.GROUPS (
  ID                 UUID NOT NULL,
  USER_DIRECTORY_ID  UUID NOT NULL,
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
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the external group';

COMMENT ON COLUMN MMP.GROUPS.USER_DIRECTORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the internal group is associated with';

COMMENT ON COLUMN MMP.GROUPS.GROUPNAME
  IS 'The group name for the external group';



CREATE TABLE MMP.FUNCTIONS (
  ID           UUID NOT NULL,
  CODE         VARCHAR(100) NOT NULL,
  NAME         VARCHAR(100) NOT NULL,
  DESCRIPTION  VARCHAR(250),

  PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX MMP_FUNCTIONS_CODE_IX
  ON MMP.FUNCTIONS
  (CODE);

COMMENT ON COLUMN MMP.FUNCTIONS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the function';

COMMENT ON COLUMN MMP.FUNCTIONS.CODE
  IS 'The unique code used to identify the function';

COMMENT ON COLUMN MMP.FUNCTIONS.NAME
  IS 'The name of the function';

COMMENT ON COLUMN MMP.FUNCTIONS.DESCRIPTION
  IS 'A description for the function';



CREATE TABLE MMP.ROLES (
  ID           UUID NOT NULL,
  NAME         VARCHAR(100) NOT NULL,
  DESCRIPTION  VARCHAR(250),

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN MMP.ROLES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';

COMMENT ON COLUMN MMP.ROLES.NAME
  IS 'The name of the role';

COMMENT ON COLUMN MMP.ROLES.DESCRIPTION
  IS 'A description for the role';



CREATE TABLE MMP.FUNCTION_TO_ROLE_MAP (
  FUNCTION_ID  UUID NOT NULL,
  ROLE_ID      UUID NOT NULL,

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
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the function';

COMMENT ON COLUMN MMP.FUNCTION_TO_ROLE_MAP.ROLE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';



CREATE TABLE MMP.ROLE_TO_GROUP_MAP (
  ROLE_ID   UUID NOT NULL,
  GROUP_ID  UUID NOT NULL,

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
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';

COMMENT ON COLUMN MMP.ROLE_TO_GROUP_MAP.GROUP_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the external group';



CREATE TABLE MMP.JOBS (
  ID                  UUID NOT NULL,
  NAME                VARCHAR(100) NOT NULL,
  SCHEDULING_PATTERN  VARCHAR(200) NOT NULL,
  JOB_CLASS           VARCHAR(200) NOT NULL,
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
  ID      UUID NOT NULL,
  JOB_ID  UUID NOT NULL,
  NAME    VARCHAR(100) NOT NULL,
  VALUE   VARCHAR(10000) NOT NULL,

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
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the job parameter';

COMMENT ON COLUMN MMP.JOB_PARAMETERS.JOB_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the job';

COMMENT ON COLUMN MMP.JOB_PARAMETERS.NAME
  IS 'The name of the job parameter';

COMMENT ON COLUMN MMP.JOB_PARAMETERS.VALUE
  IS 'The value of the job parameter';



CREATE TABLE MMP.CODE_CATEGORIES (
  ID                  UUID NOT NULL,
  CATEGORY_TYPE       INTEGER NOT NULL,
  NAME                VARCHAR(100) NOT NULL,
  CODE_DATA           BLOB,
  ENDPOINT            VARCHAR(200),
  IS_ENDPOINT_SECURE  BOOLEAN NOT NULL DEFAULT FALSE,
  IS_CACHEABLE        BOOLEAN,
  CACHE_EXPIRY        INTEGER,
  UPDATED             TIMESTAMP,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN MMP.CODE_CATEGORIES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the code category';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.CATEGORY_TYPE
  IS 'The type of code category e.g. Local, RemoteHTTPService, RemoteWebService, etc';

COMMENT ON COLUMN MMP.CODE_CATEGORIES.NAME
  IS 'The name of the code category';

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
  ID           VARCHAR(100) NOT NULL,
  CATEGORY_ID  UUID NOT NULL,
  NAME         VARCHAR(100) NOT NULL,
  VALUE        VARCHAR(100) NOT NULL,

  PRIMARY KEY (ID, CATEGORY_ID),
  CONSTRAINT MMP_CODES_CODE_CATEGORY_FK FOREIGN KEY (CATEGORY_ID) REFERENCES MMP.CODE_CATEGORIES(ID) ON DELETE CASCADE
);

CREATE INDEX MMP_CODES_CATEGORY_ID_IX
  ON MMP.CODES
  (CATEGORY_ID);

COMMENT ON COLUMN MMP.CODES.ID
  IS 'The ID used to uniquely identify the code';

COMMENT ON COLUMN MMP.CODES.CATEGORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the code category the code is associated with';

COMMENT ON COLUMN MMP.CODES.NAME
  IS 'The name of the code';

COMMENT ON COLUMN MMP.CODES.VALUE
  IS 'The value for the code';



CREATE TABLE MMP.CACHED_CODE_CATEGORIES (
  ID            UUID NOT NULL,
  CODE_DATA     BLOB,
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



CREATE TABLE MMP.CACHED_CODES (
  ID           VARCHAR(100) NOT NULL,
  CATEGORY_ID  UUID NOT NULL,
  NAME         VARCHAR(100) NOT NULL,
  VALUE        VARCHAR(100) NOT NULL,

  PRIMARY KEY (ID, CATEGORY_ID),
  CONSTRAINT MMP_CACHED_CODES_CACHED_CODE_CATEGORY_FK FOREIGN KEY (CATEGORY_ID) REFERENCES MMP.CACHED_CODE_CATEGORIES(ID) ON DELETE CASCADE
);

CREATE INDEX MMP_CACHED_CODES_CATEGORY_ID_IX
  ON MMP.CACHED_CODES
  (CATEGORY_ID);

COMMENT ON COLUMN MMP.CACHED_CODES.ID
  IS 'The ID used to uniquely identify the code';

COMMENT ON COLUMN MMP.CACHED_CODES.CATEGORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the code category the code is associated with';

COMMENT ON COLUMN MMP.CACHED_CODES.NAME
  IS 'The name of the code';

COMMENT ON COLUMN MMP.CACHED_CODES.VALUE
  IS 'The value for the code';



CREATE TABLE MMP.MESSAGE_TYPES (
  ID    UUID NOT NULL,
  NAME  VARCHAR(100) NOT NULL,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN MMP.MESSAGE_TYPES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message type';

COMMENT ON COLUMN MMP.MESSAGE_TYPES.NAME
  IS 'The name of the message type';



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
  ID                 UUID NOT NULL,
  USERNAME           VARCHAR(100) NOT NULL,
  DEVICE_ID          UUID NOT NULL,
  TYPE_ID            UUID NOT NULL,
  CORRELATION_ID     UUID NOT NULL,
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
  DATA               BLOB,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_MESSAGES_MESSAGE_TYPE_FK FOREIGN KEY (TYPE_ID) REFERENCES MMP.MESSAGE_TYPES(ID),
  CONSTRAINT MMP_MESSAGES_MESSAGE_STATUS_FK FOREIGN KEY (STATUS) REFERENCES MMP.MESSAGE_STATUSES(CODE)
);

CREATE INDEX MMP_MESSAGES_USERNAME_IX
  ON MMP.MESSAGES
  (USERNAME);

CREATE INDEX MMP_MESSAGES_DEVICE_ID_IX
  ON MMP.MESSAGES
  (DEVICE_ID);

CREATE INDEX MMP_MESSAGES_TYPE_ID_IX
  ON MMP.MESSAGES
  (TYPE_ID);

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

COMMENT ON COLUMN MMP.MESSAGES.DEVICE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the message originated from';

COMMENT ON COLUMN MMP.MESSAGES.TYPE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of message';

COMMENT ON COLUMN MMP.MESSAGES.CORRELATION_ID
  IS 'The Universally Unique Identifier (UUID) used to correlate the message';

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
  ID                   UUID NOT NULL,
  PART_NO              INTEGER NOT NULL,
  TOTAL_PARTS          INTEGER NOT NULL,
  SEND_ATTEMPTS        INTEGER NOT NULL,
  DOWNLOAD_ATTEMPTS    INTEGER NOT NULL,
  STATUS               INTEGER NOT NULL,
  PERSISTED            TIMESTAMP NOT NULL,
  UPDATED              TIMESTAMP,
  MSG_ID               UUID NOT NULL,
  MSG_USERNAME         VARCHAR(100) NOT NULL,
  MSG_DEVICE_ID        UUID NOT NULL,
  MSG_TYPE_ID          UUID NOT NULL,
  MSG_CORRELATION_ID   UUID NOT NULL,
  MSG_PRIORITY         INTEGER NOT NULL,
  MSG_CREATED          TIMESTAMP NOT NULL,
  MSG_DATA_HASH        VARCHAR(100),
  MSG_ENCRYPTION_IV    VARCHAR(100) NOT NULL,
  MSG_CHECKSUM         VARCHAR(100) NOT NULL,
  LOCK_NAME            VARCHAR(100),
  DATA                 BLOB,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_MESSAGE_PARTS_MESSAGE_TYPE_FK FOREIGN KEY (MSG_TYPE_ID) REFERENCES MMP.MESSAGE_TYPES(ID)
);

CREATE INDEX MMP_MESSAGE_PARTS_STATUS_IX
  ON MMP.MESSAGE_PARTS
  (STATUS);

CREATE INDEX MMP_MESSAGE_PARTS_MSG_ID_IX
  ON MMP.MESSAGE_PARTS
  (MSG_ID);
  
CREATE INDEX MMP_MESSAGE_PARTS_MSG_DEVICE_ID_IX
  ON MMP.MESSAGE_PARTS
  (MSG_DEVICE_ID);

CREATE INDEX MMP_MESSAGE_PARTS_MSG_TYPE_ID_IX
  ON MMP.MESSAGE_PARTS
  (MSG_TYPE_ID);

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

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_DEVICE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the original message originated from';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_TYPE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_CORRELATION_ID
  IS 'The Universally Unique Identifier (UUID) used to correlate the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_PRIORITY
  IS 'The priority for the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_CREATED
  IS 'The date and time the original message was created';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_DATA_HASH
  IS 'The hash of the unencrypted data for the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_ENCRYPTION_IV
  IS 'The base-64 encoded initialisation vector for the encryption scheme for the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.MSG_CHECKSUM
  IS 'The checksum for the original message';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.LOCK_NAME
  IS 'The name of the entity that has locked the message part for processing';

COMMENT ON COLUMN MMP.MESSAGE_PARTS.DATA
  IS 'The data for the message part';



CREATE TABLE MMP.ARCHIVED_MESSAGES (
  ID               UUID NOT NULL,
  USERNAME         VARCHAR(100) NOT NULL,
  DEVICE_ID        UUID NOT NULL,
  TYPE_ID          UUID NOT NULL,
  CORRELATION_ID   UUID NOT NULL,
  CREATED          TIMESTAMP NOT NULL,
  ARCHIVED         TIMESTAMP NOT NULL,
  DATA             BLOB,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_ARCHIVED_MESSAGES_MESSAGE_TYPE_FK FOREIGN KEY (TYPE_ID) REFERENCES MMP.MESSAGE_TYPES(ID)
);

CREATE INDEX MMP_ARCHIVED_MESSAGES_USERNAME_IX
  ON MMP.ARCHIVED_MESSAGES
  (USERNAME);

CREATE INDEX MMP_ARCHIVED_MESSAGES_DEVICE_ID_IX
  ON MMP.ARCHIVED_MESSAGES
  (DEVICE_ID);

CREATE INDEX MMP_ARCHIVED_MESSAGES_TYPE_ID_IX
  ON MMP.ARCHIVED_MESSAGES
  (TYPE_ID);

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.USERNAME
  IS 'The username identifying the user associated with the message';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.DEVICE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the message originated from';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.TYPE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of message';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.CORRELATION_ID
  IS 'The Universally Unique Identifier (UUID) used to correlate the message';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.CREATED
  IS 'The date and time the message was created';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.ARCHIVED
  IS 'The date and time the message was archived';

COMMENT ON COLUMN MMP.ARCHIVED_MESSAGES.DATA
  IS 'The data for the message';



CREATE TABLE MMP.PACKAGES (
  ID          UUID NOT NULL,
  VERSION     INTEGER NOT NULL,
  NAME        VARCHAR(100) NOT NULL,
  IS_CURRENT  BOOLEAN NOT NULL,
  HASH        VARCHAR(100) NOT NULL,
  SIZE        INTEGER NOT NULL,
  DATA        BLOB NOT NULL,

  PRIMARY KEY (ID, VERSION)
);

COMMENT ON COLUMN MMP.PACKAGES.ID
  IS 'The Universally Unique Identifier (UUID) used to identify the package';

COMMENT ON COLUMN MMP.PACKAGES.VERSION
  IS 'The version of the package';

COMMENT ON COLUMN MMP.PACKAGES.NAME
  IS 'The name of the package';

COMMENT ON COLUMN MMP.PACKAGES.IS_CURRENT
  IS 'Is the package version current i.e. is this the version of the package that should be installed on a device';

COMMENT ON COLUMN MMP.PACKAGES.HASH
  IS 'The SHA-256 hash used to confirm the authenticity of the package version';

COMMENT ON COLUMN MMP.PACKAGES.SIZE
  IS 'The size of the package version in bytes';

COMMENT ON COLUMN MMP.PACKAGES.DATA
  IS 'The package version data';



CREATE TABLE MMP.ERROR_REPORTS (
  ID                   UUID NOT NULL,
  APPLICATION_ID       UUID NOT NULL,
  APPLICATION_VERSION  INTEGER NOT NULL,
  DESCRIPTION          VARCHAR(1000) NOT NULL,
  DETAIL               VARCHAR(10000) NOT NULL,
  FEEDBACK             VARCHAR(2000) NOT NULL,
  CREATED              TIMESTAMP NOT NULL,
  WHO                  VARCHAR(100) NOT NULL,
  DEVICE_ID            UUID NOT NULL,
  DATA                 BLOB,

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

COMMENT ON COLUMN MMP.ERROR_REPORTS.DEVICE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the error report originated from';

COMMENT ON COLUMN MMP.ERROR_REPORTS.DATA
  IS 'The data associated with the error report';



CREATE TABLE MMP.REPORT_DEFINITIONS (
  ID        UUID NOT NULL,
  NAME      VARCHAR(100) NOT NULL,
  TEMPLATE  BLOB NULL,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN MMP.REPORT_DEFINITIONS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the report definition';

COMMENT ON COLUMN MMP.REPORT_DEFINITIONS.NAME
  IS 'The name of the report definition';

COMMENT ON COLUMN MMP.REPORT_DEFINITIONS.TEMPLATE
  IS 'The JasperReports template for the report definition';



CREATE TABLE MMP.SMS (
  ID              BIGINT NOT NULL,
  MOBILE_NUMBER   VARCHAR(40) NOT NULL,
  MESSAGE         VARCHAR(500) NOT NULL,
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
  ID       UUID NOT NULL,
  VERSION  INTEGER NOT NULL,
  NAME     VARCHAR(100) NOT NULL,
  DATA     BLOB NULL,

  PRIMARY KEY (ID, VERSION)
);

COMMENT ON COLUMN MMP.PROCESS_DEFINITIONS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the process definition';

COMMENT ON COLUMN MMP.PROCESS_DEFINITIONS.VERSION
  IS 'The version of the process definition';

COMMENT ON COLUMN MMP.PROCESS_DEFINITIONS.NAME
  IS 'The name of the process definition';

COMMENT ON COLUMN MMP.PROCESS_DEFINITIONS.DATA
  IS 'The data for the process definition';



CREATE TABLE MMP.PROCESS_INSTANCES (
  ID                  UUID NOT NULL,
  DEFINITION_ID       UUID NOT NULL,
  DEFINITION_VERSION  INTEGER NOT NULL,
  DATA                BLOB NOT NULL,
  STATUS              INTEGER NOT NULL DEFAULT 1,
  NEXT_EXECUTION      TIMESTAMP,
  LOCK_NAME           VARCHAR(100),

  PRIMARY KEY (ID),
  CONSTRAINT MMP_PROCESS_INSTANCES_PROCESS_DEFINITION_FK FOREIGN KEY (DEFINITION_ID, DEFINITION_VERSION) REFERENCES MMP.PROCESS_DEFINITIONS(ID, VERSION)
);

CREATE INDEX MMP_PROCESSES_PROCESS_DEFINITION_IX
  ON MMP.PROCESS_INSTANCES
  (DEFINITION_ID, DEFINITION_VERSION);

COMMENT ON COLUMN MMP.PROCESS_INSTANCES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the process instance';

COMMENT ON COLUMN MMP.PROCESS_INSTANCES.DEFINITION_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the process definition for the process instance';

COMMENT ON COLUMN MMP.PROCESS_INSTANCES.DEFINITION_VERSION
  IS 'The version of the process definition for the process instance';

COMMENT ON COLUMN MMP.PROCESS_INSTANCES.DATA
  IS 'The data giving the current execution state for the process instance';

COMMENT ON COLUMN MMP.PROCESS_INSTANCES.STATUS
  IS 'The status of the process instance';

COMMENT ON COLUMN MMP.PROCESS_INSTANCES.NEXT_EXECUTION
  IS 'The date and time when the process instance will next be executed';

COMMENT ON COLUMN MMP.PROCESS_INSTANCES.LOCK_NAME
  IS 'The name of the entity that has locked the process instance for execution';



CREATE TABLE MMP.PROCESS_INSTANCE_EVENTS (
  ID                   UUID NOT NULL,
  PROCESS_INSTANCE_ID  UUID NOT NULL,
  DATA                 BLOB NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT MMP_PROCESS_INSTANCE_EVENTS_PROCESS_INSTANCE_FK FOREIGN KEY (PROCESS_INSTANCE_ID) REFERENCES MMP.PROCESS_INSTANCES(ID)
);

CREATE INDEX MMP_PROCESS_INSTANCE_EVENTS_PROCESS_INSTANCE_IX
  ON MMP.PROCESS_INSTANCE_EVENTS
  (PROCESS_INSTANCE_ID);

COMMENT ON COLUMN MMP.PROCESS_INSTANCE_EVENTS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the process instance event';

COMMENT ON COLUMN MMP.PROCESS_INSTANCE_EVENTS.PROCESS_INSTANCE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the process instance the process instance event is associated with';

COMMENT ON COLUMN MMP.PROCESS_INSTANCE_EVENTS.DATA
  IS 'The data for the process instance event';



-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO MMP.ORGANISATIONS (ID, NAME) VALUES
  ('c1685b92-9fe5-453a-995b-89d8c0f29cb5', 'MMP');

INSERT INTO MMP.USER_DIRECTORY_TYPES (ID, NAME, USER_DIRECTORY_CLASS, ADMINISTRATION_CLASS) VALUES
  ('b43fda33-d3b0-4f80-a39a-110b8e530f4f', 'Internal User Directory', 'guru.mmp.application.security.InternalUserDirectory', 'guru.mmp.application.web.template.components.InternalUserDirectoryAdministrationPanel');
INSERT INTO MMP.USER_DIRECTORY_TYPES (ID, NAME, USER_DIRECTORY_CLASS, ADMINISTRATION_CLASS) VALUES
  ('e5741a89-c87b-4406-8a60-2cc0b0a5fa3e', 'LDAP User Directory', 'guru.mmp.application.security.LDAPUserDirectory', 'guru.mmp.application.web.template.components.LDAPUserDirectoryAdministrationPanel');

INSERT INTO MMP.USER_DIRECTORIES (ID, TYPE_ID, NAME, CONFIGURATION) VALUES
  ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'b43fda33-d3b0-4f80-a39a-110b8e530f4f', 'Internal User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter></userDirectory>');

INSERT INTO MMP.USER_DIRECTORY_TO_ORGANISATION_MAP (USER_DIRECTORY_ID, ORGANISATION_ID) VALUES
  ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'c1685b92-9fe5-453a-995b-89d8c0f29cb5');

INSERT INTO MMP.INTERNAL_USERS (ID, USER_DIRECTORY_ID, USERNAME, PASSWORD, FIRST_NAMES, LAST_NAME, MOBILE, EMAIL, PASSWORD_ATTEMPTS, PASSWORD_EXPIRY) VALUES
  ('b2bbf431-4af8-4104-b96c-d33b5f66d1e4', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrator', 'GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=', '', '', '', '', null, null);

INSERT INTO MMP.INTERNAL_GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME, DESCRIPTION) VALUES
  ('a9e01fa2-f017-46e2-8187-424bf50a4f33', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrators', 'Administrators');
INSERT INTO MMP.INTERNAL_GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME, DESCRIPTION) VALUES
  ('758c0a2a-f3a3-4561-bebc-90569291976e', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Organisation Administrators', 'Organisation Administrators');

INSERT INTO MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP (USER_DIRECTORY_ID, INTERNAL_USER_ID, INTERNAL_GROUP_ID) VALUES
  ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'b2bbf431-4af8-4104-b96c-d33b5f66d1e4', 'a9e01fa2-f017-46e2-8187-424bf50a4f33');

INSERT INTO MMP.GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME) VALUES ('a9e01fa2-f017-46e2-8187-424bf50a4f33', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrators');
INSERT INTO MMP.GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME) VALUES ('758c0a2a-f3a3-4561-bebc-90569291976e', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Organisation Administrators');

INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5', 'Application.SecureHome', 'Secure Home', 'Secure Home');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9', 'Application.Dashboard', 'Dashboard', 'Dashboard');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('2d52b029-920f-4b15-b646-5b9955c188e3', 'Application.OrganisationAdministration', 'Organisation Administration', 'Organisation Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('567d7e55-f3d0-4191-bc4c-12d357900fa3', 'Application.UserAdministration', 'User Administration', 'User Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('ef03f384-24f7-43eb-a29c-f5c5b838698d', 'Application.GroupAdministration', 'Group Administration', 'Group Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('7a54a71e-3680-4d49-b87d-29604a247413', 'Application.UserGroups', 'User Groups', 'User Groups');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('0623bc3f-9a1b-4f19-8438-236660d789c5', 'Application.CodeCategoryAdministration', 'Code Category Administration', 'Code Category Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('4e6bc7c4-ee29-4cd7-b4d7-3be42db73dd6', 'Application.CodeAdministration', 'Code Administration', 'Code Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('029b9a06-0241-4a44-a234-5c489f2017ba', 'Application.ResetUserPassword', 'Reset User Password', 'Reset User Password');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('9105fb6d-1629-4014-bf4c-1990a92db276', 'Application.SecurityAdministration', 'Security Administration', 'Security Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('b233ed4a-b30f-4356-a5d3-1c660aa69f00', 'Application.ConfigurationAdministration', 'Configuration Administration', 'Configuration Administration');

INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('97f0f870-a871-48de-a3e0-a32a95770f12', 'Application.ErrorReports', 'Error Reports', 'Error Reports');

INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('3a17959c-5dfc-43a2-9587-48a1eb95a22a', 'Application.ReportDefinitionAdministration', 'Report Definition Administration', 'Report Definition Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('539fceb8-da82-4170-ab1a-ae6b04001c03', 'Application.ViewReport', 'View Report', 'View Report');
  
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('180c84f9-9816-48d0-9762-dc753b2228b1', 'Application.ProcessDefinitionAdministration', 'Process Definition Administration', 'Process Definition Administration');
INSERT INTO MMP.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('d2854c65-9a59-40b8-9dc7-a882c64b2610', 'Application.ViewProcess', 'View Process', 'View Process');

INSERT INTO MMP.ROLES (ID, NAME, DESCRIPTION) VALUES
  ('100fafb4-783a-4204-a22d-9e27335dc2ea', 'Administrator', 'Administrator');
INSERT INTO MMP.ROLES (ID, NAME, DESCRIPTION) VALUES
  ('44ff0ad2-fbe1-489f-86c9-cef7f82acf35', 'Organisation Administrator', 'Organisation Administrator');

INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.SecureHome
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.Dashboard
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('2d52b029-920f-4b15-b646-5b9955c188e3', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.OrganisationAdministration
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('567d7e55-f3d0-4191-bc4c-12d357900fa3', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.UserAdministration
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('ef03f384-24f7-43eb-a29c-f5c5b838698d', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.GroupAdministration
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('7a54a71e-3680-4d49-b87d-29604a247413', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.UserGroups
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('0623bc3f-9a1b-4f19-8438-236660d789c5', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.CodeCategoryAdministration
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('4e6bc7c4-ee29-4cd7-b4d7-3be42db73dd6', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.CodeAdministration
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('029b9a06-0241-4a44-a234-5c489f2017ba', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ResetUserPassword
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('9105fb6d-1629-4014-bf4c-1990a92db276', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.SecurityAdministration
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('b233ed4a-b30f-4356-a5d3-1c660aa69f00', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ConfigurationAdministration
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('97f0f870-a871-48de-a3e0-a32a95770f12', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ErrorReports
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('3a17959c-5dfc-43a2-9587-48a1eb95a22a', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ReportDefinitionAdministration
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('539fceb8-da82-4170-ab1a-ae6b04001c03', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ViewReport
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('180c84f9-9816-48d0-9762-dc753b2228b1', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ProcessDefinitionAdministration
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('d2854c65-9a59-40b8-9dc7-a882c64b2610', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ViewProcess

INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.SecureHome
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.Dashboard
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('567d7e55-f3d0-4191-bc4c-12d357900fa3', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.UserAdministration
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('7a54a71e-3680-4d49-b87d-29604a247413', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.UserGroups
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('029b9a06-0241-4a44-a234-5c489f2017ba', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.ResetUserPassword
INSERT INTO MMP.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('539fceb8-da82-4170-ab1a-ae6b04001c03', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.ViewReport

INSERT INTO MMP.ROLE_TO_GROUP_MAP (ROLE_ID, GROUP_ID) VALUES ('100fafb4-783a-4204-a22d-9e27335dc2ea', 'a9e01fa2-f017-46e2-8187-424bf50a4f33');
INSERT INTO MMP.ROLE_TO_GROUP_MAP (ROLE_ID, GROUP_ID) VALUES ('44ff0ad2-fbe1-489f-86c9-cef7f82acf35', '758c0a2a-f3a3-4561-bebc-90569291976e');

INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('d21fb54e-5c5b-49e8-881f-ce00c6ced1a3', 'AuthenticateRequest');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('82223035-1726-407f-8703-3977708e792c', 'AuthenticateResponse');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('cc005e6a-b01b-48eb-98a0-026297be69f3', 'CheckUserExistsRequest');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a38bd55e-3470-46f1-a96a-a6b08a9adc63', 'CheckUserExistsResponse');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('94d60eb6-a062-492d-b5e7-9fb1f05cf088', 'GetCodeCategoryRequest');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('0336b544-91e5-4eb9-81db-3dd94e116c92', 'GetCodeCategoryResponse');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('3500a28a-6a2c-482b-b81f-a849c9c3ef79', 'GetCodeCategoryWithParametersRequest');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('12757310-9eee-4a3a-970c-9b4ee0e1108e', 'GetCodeCategoryWithParametersResponse');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a589dc87-2328-4a9b-bdb6-970e55ca2323', 'TestRequest');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a3bad7ba-f9d4-4403-b54a-cb1f335ebbad', 'TestResponse');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('e9918051-8ebc-48f1-bad7-13c59b550e1a', 'AnotherTestRequest');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a714a9c6-2914-4498-ab59-64be9991bf37', 'AnotherTestResponse');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('ff638c33-b4f1-4e79-804c-9560da2543d6', 'SubmitErrorReportRequest');
INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('8be50cfa-2fb1-4634-9bfa-d01e77eaf766', 'SubmitErrorReportResponse');

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

INSERT INTO MMP.TEST_DATA (ID, NAME, VALUE) VALUES (1, 'Sample Name 1', 'Sample Value 1');
INSERT INTO MMP.TEST_DATA (ID, NAME, VALUE) VALUES (2, 'Sample Name 2', 'Sample Value 2');
INSERT INTO MMP.TEST_DATA (ID, NAME, VALUE) VALUES (3, 'Sample Name 3', 'Sample Value 3');
INSERT INTO MMP.TEST_DATA (ID, NAME, VALUE) VALUES (4, 'Sample Name 4', 'Sample Value 4');
INSERT INTO MMP.TEST_DATA (ID, NAME, VALUE) VALUES (5, 'Sample Name 5', 'Sample Value 5');
INSERT INTO MMP.TEST_DATA (ID, NAME, VALUE) VALUES (6, 'Sample Name 6', 'Sample Value 6');
INSERT INTO MMP.TEST_DATA (ID, NAME, VALUE) VALUES (7, 'Sample Name 7', 'Sample Value 7');
INSERT INTO MMP.TEST_DATA (ID, NAME, VALUE) VALUES (8, 'Sample Name 8', 'Sample Value 8');
INSERT INTO MMP.TEST_DATA (ID, NAME, VALUE) VALUES (9, 'Sample Name 9', 'Sample Value 9');




