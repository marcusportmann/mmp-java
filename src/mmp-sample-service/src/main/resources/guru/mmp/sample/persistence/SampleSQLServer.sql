-- -------------------------------------------------------------------------------------------------
-- DROP TABLES
-- -------------------------------------------------------------------------------------------------
IF OBJECT_ID('SAMPLE.DATA', 'U') IS NOT NULL
  DROP TABLE SAMPLE.DATA;
IF OBJECT_ID('SMS.SMS', 'U') IS NOT NULL
  DROP TABLE SMS.SMS;
IF OBJECT_ID('REPORTING.REPORT_DEFINITIONS', 'U') IS NOT NULL
  DROP TABLE REPORTING.REPORT_DEFINITIONS;
IF OBJECT_ID('MESSAGING.ERROR_REPORTS', 'U') IS NOT NULL
  DROP TABLE MESSAGING.ERROR_REPORTS;
IF OBJECT_ID('MESSAGING.MESSAGE_STATUSES', 'U') IS NOT NULL
  DROP TABLE MESSAGING.MESSAGE_STATUSES;
IF OBJECT_ID('MESSAGING.MESSAGE_TYPES', 'U') IS NOT NULL
  DROP TABLE MESSAGING.MESSAGE_TYPES;
IF OBJECT_ID('MESSAGING.ARCHIVED_MESSAGES', 'U') IS NOT NULL
  DROP TABLE MESSAGING.ARCHIVED_MESSAGES;
IF OBJECT_ID('MESSAGING.MESSAGE_PARTS', 'U') IS NOT NULL
  DROP TABLE MESSAGING.MESSAGE_PARTS;
IF OBJECT_ID('MESSAGING.MESSAGES', 'U') IS NOT NULL
  DROP TABLE MESSAGING.MESSAGES;
IF OBJECT_ID('CODES.CACHED_CODES', 'U') IS NOT NULL
  DROP TABLE CODES.CACHED_CODESX;
IF OBJECT_ID('CODES.CACHED_CODE_CATEGORIES', 'U') IS NOT NULL
  DROP TABLE CODES.CACHED_CODE_CATEGORIES;
IF OBJECT_ID('CODES.CODES', 'U') IS NOT NULL
  DROP TABLE CODES.CODES;
IF OBJECT_ID('CODES.CODE_CATEGORIES', 'U') IS NOT NULL
  DROP TABLE CODES.CODE_CATEGORIES;
IF OBJECT_ID('SCHEDULER.JOB_PARAMETERS', 'U') IS NOT NULL
  DROP TABLE SCHEDULER.JOB_PARAMETERS;
IF OBJECT_ID('SCHEDULER.JOBS', 'U') IS NOT NULL
  DROP TABLE SCHEDULER.JOBS;
IF OBJECT_ID('SECURITY.ROLE_TO_GROUP_MAP', 'U') IS NOT NULL
  DROP TABLE SECURITY.ROLE_TO_GROUP_MAP;
IF OBJECT_ID('SECURITY.FUNCTION_TO_ROLE_MAP', 'U') IS NOT NULL
  DROP TABLE SECURITY.FUNCTION_TO_ROLE_MAP;
IF OBJECT_ID('SECURITY.ROLES', 'U') IS NOT NULL
  DROP TABLE SECURITY.ROLES;
IF OBJECT_ID('SECURITY.FUNCTIONS', 'U') IS NOT NULL
  DROP TABLE SECURITY.FUNCTIONS;
IF OBJECT_ID('SECURITY.GROUPS', 'U') IS NOT NULL
  DROP TABLE SECURITY.GROUPS;
IF OBJECT_ID('SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP', 'U') IS NOT NULL
  DROP TABLE SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP;
IF OBJECT_ID('SECURITY.INTERNAL_GROUPS', 'U') IS NOT NULL
  DROP TABLE SECURITY.INTERNAL_GROUPS;
IF OBJECT_ID('SECURITY.INTERNAL_USERS_PASSWORD_HISTORY', 'U') IS NOT NULL
  DROP TABLE SECURITY.INTERNAL_USERS_PASSWORD_HISTORY;
IF OBJECT_ID('SECURITY.INTERNAL_USERS', 'U') IS NOT NULL
  DROP TABLE SECURITY.INTERNAL_USERS;
IF OBJECT_ID('SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP', 'U') IS NOT NULL
  DROP TABLE SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP;
IF OBJECT_ID('SECURITY.USER_DIRECTORIES', 'U') IS NOT NULL
  DROP TABLE SECURITY.USER_DIRECTORIES;
IF OBJECT_ID('SECURITY.USER_DIRECTORY_TYPES', 'U') IS NOT NULL
  DROP TABLE SECURITY.USER_DIRECTORY_TYPES;
IF OBJECT_ID('SECURITY.ORGANISATIONS', 'U') IS NOT NULL
  DROP TABLE SECURITY.ORGANISATIONS;
IF OBJECT_ID('SERVICE_REGISTRY.SERVICE_REGISTRY', 'U') IS NOT NULL
  DROP TABLE SERVICE_REGISTRY.SERVICE_REGISTRY;
IF OBJECT_ID('CONFIGURATION.CONFIG', 'U') IS NOT NULL
  DROP TABLE CONFIGURATION.CONFIG;
IF OBJECT_ID('IDGENERATOR.IDGENERATOR', 'U') IS NOT NULL
  DROP TABLE IDGENERATOR.IDGENERATOR;
IF OBJECT_ID('TEST.TEST_DATA', 'U') IS NOT NULL
  DROP TABLE TEST.TEST_DATA;



-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM SYS.SCHEMAS WHERE name = 'CODES')
BEGIN
    EXEC('CREATE SCHEMA CODES')
END;
IF NOT EXISTS (SELECT 1 FROM SYS.SCHEMAS WHERE name = 'CONFIGURATION')
BEGIN
    EXEC('CREATE SCHEMA CONFIGURATION')
END;
IF NOT EXISTS (SELECT 1 FROM SYS.SCHEMAS WHERE name = 'IDGENERATOR')
BEGIN
    EXEC('CREATE SCHEMA IDGENERATOR')
END;
IF NOT EXISTS (SELECT 1 FROM SYS.SCHEMAS WHERE name = 'MESSAGING')
BEGIN
    EXEC('CREATE SCHEMA MESSAGING')
END;
IF NOT EXISTS (SELECT 1 FROM SYS.SCHEMAS WHERE name = 'REPORTING')
BEGIN
    EXEC('CREATE SCHEMA REPORTING')
END;
IF NOT EXISTS (SELECT 1 FROM SYS.SCHEMAS WHERE name = 'SCHEDULER')
BEGIN
    EXEC('CREATE SCHEMA SCHEDULER')
END;
IF NOT EXISTS (SELECT 1 FROM SYS.SCHEMAS WHERE name = 'SECURITY')
BEGIN
    EXEC('CREATE SCHEMA SECURITY')
END;
IF NOT EXISTS (SELECT 1 FROM SYS.SCHEMAS WHERE name = 'SERVICE_REGISTRY')
BEGIN
    EXEC('CREATE SCHEMA SERVICE_REGISTRY')
END;
IF NOT EXISTS (SELECT 1 FROM SYS.SCHEMAS WHERE name = 'SMS')
BEGIN
    EXEC('CREATE SCHEMA SMS')
END;
IF NOT EXISTS (SELECT 1 FROM SYS.SCHEMAS WHERE name = 'TEST')
BEGIN
    EXEC('CREATE SCHEMA TEST')
END;
IF NOT EXISTS (SELECT 1 FROM SYS.SCHEMAS WHERE name = 'SAMPLE')
BEGIN
    EXEC('CREATE SCHEMA SAMPLE')
END;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE CODES.CODE_CATEGORIES (
  ID                  UNIQUEIDENTIFIER NOT NULL,
  CATEGORY_TYPE       INTEGER NOT NULL,
  NAME                NVARCHAR(256) NOT NULL,
  CODE_DATA           VARBINARY(MAX),
  ENDPOINT            NVARCHAR(2048),
  IS_ENDPOINT_SECURE  BIT NOT NULL DEFAULT 0,
  IS_CACHEABLE        BIT,
  CACHE_EXPIRY        INTEGER,
  UPDATED             DATETIME,

  PRIMARY KEY (ID)
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the code category' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The type of code category e.g. Local, RemoteHTTPService, RemoteWebService, etc' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'CATEGORY_TYPE';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the code category' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The custom code data for the code category' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'CODE_DATA';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The endpoint if this is a remote code category' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'ENDPOINT';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'Is the endpoint for the remote code category secure' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'IS_ENDPOINT_SECURE';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'Is the code data retrieved for the remote code category cacheable' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'IS_CACHEABLE';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The time in seconds after which the cached code data for the remote code category will expire' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'CACHE_EXPIRY';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the code category was updated' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'UPDATED';



CREATE TABLE CODES.CODES (
  ID           NVARCHAR(256) NOT NULL,
  CATEGORY_ID  UNIQUEIDENTIFIER NOT NULL,
  NAME         NVARCHAR(1024) NOT NULL,
  VALUE        NVARCHAR(2048) NOT NULL,

  PRIMARY KEY (ID, CATEGORY_ID),
  CONSTRAINT CODES_CODE_CATEGORY_FK FOREIGN KEY (CATEGORY_ID) REFERENCES CODES.CODE_CATEGORIES(ID) ON DELETE CASCADE
);

CREATE INDEX CODES_CATEGORY_ID_IX
  ON CODES.CODES
  (CATEGORY_ID);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The ID used to uniquely identify the code' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODES', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the code category the code is associated with' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODES', @level2type=N'COLUMN', @level2name=N'CATEGORY_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the code' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODES', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The value for the code' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CODES', @level2type=N'COLUMN', @level2name=N'VALUE';



CREATE TABLE CODES.CACHED_CODE_CATEGORIES (
  ID            UNIQUEIDENTIFIER NOT NULL,
  CODE_DATA     VARBINARY(MAX),
  LAST_UPDATED  DATETIME NOT NULL,
  CACHED        DATETIME NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT CACHED_CODE_CATEGORIES_CATEGORY_FK FOREIGN KEY (ID) REFERENCES CODES.CODE_CATEGORIES(ID) ON DELETE CASCADE
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the cached code category' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CACHED_CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The custom code data for the cached code category' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CACHED_CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'CODE_DATA';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the cached code category was last updated' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CACHED_CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'LAST_UPDATED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the code category was cached' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CACHED_CODE_CATEGORIES', @level2type=N'COLUMN', @level2name=N'CACHED';



CREATE TABLE CODES.CACHED_CODES (
  ID           NVARCHAR(256) NOT NULL,
  CATEGORY_ID  UNIQUEIDENTIFIER NOT NULL,
  NAME         NVARCHAR(1024) NOT NULL,
  VALUE        NVARCHAR(4000) NOT NULL,

  PRIMARY KEY (ID, CATEGORY_ID),
  CONSTRAINT CACHED_CODES_CACHED_CODE_CATEGORY_FK FOREIGN KEY (CATEGORY_ID) REFERENCES CODES.CACHED_CODE_CATEGORIES(ID) ON DELETE CASCADE
);

CREATE INDEX CACHED_CODES_CATEGORY_ID_IX
  ON CODES.CACHED_CODES
  (CATEGORY_ID);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The ID used to uniquely identify the code' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CACHED_CODES', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the code category the code is associated with' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CACHED_CODES', @level2type=N'COLUMN', @level2name=N'CATEGORY_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the code' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CACHED_CODES', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The value for the code' ,
@level0type=N'SCHEMA', @level0name=N'CODES', @level1type=N'TABLE', @level1name=N'CACHED_CODES', @level2type=N'COLUMN', @level2name=N'VALUE';



CREATE TABLE CONFIGURATION.CONFIGURATION (
  [KEY]        NVARCHAR(256) NOT NULL,
  [VALUE]      NVARCHAR(MAX) NOT NULL,
  DESCRIPTION  NVARCHAR(MAX) NOT NULL,

  PRIMARY KEY ([KEY])
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The key used to uniquely identify the configuration value' ,
@level0type=N'SCHEMA', @level0name=N'CONFIGURATION', @level1type=N'TABLE', @level1name=N'CONFIGURATION', @level2type=N'COLUMN', @level2name=N'KEY';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The value for the configuration value' ,
@level0type=N'SCHEMA', @level0name=N'CONFIGURATION', @level1type=N'TABLE', @level1name=N'CONFIGURATION', @level2type=N'COLUMN', @level2name=N'VALUE';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The description for the configuration value' ,
@level0type=N'SCHEMA', @level0name=N'CONFIGURATION', @level1type=N'TABLE', @level1name=N'CONFIGURATION', @level2type=N'COLUMN', @level2name=N'DESCRIPTION';



CREATE TABLE IDGENERATOR.IDGENERATOR (
  NAME       NVARCHAR(256) NOT NULL,
  [CURRENT]  BIGINT DEFAULT 0,

  PRIMARY KEY (NAME)
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name giving the type of entity associated with the generated ID' ,
@level0type=N'SCHEMA', @level0name=N'IDGENERATOR', @level1type=N'TABLE', @level1name=N'IDGENERATOR', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The current ID for the type' ,
@level0type=N'SCHEMA', @level0name=N'IDGENERATOR', @level1type=N'TABLE', @level1name=N'IDGENERATOR', @level2type=N'COLUMN', @level2name=N'CURRENT';



CREATE TABLE MESSAGING.MESSAGE_TYPES (
  ID    UNIQUEIDENTIFIER NOT NULL,
  NAME  NVARCHAR(256) NOT NULL,

  PRIMARY KEY (ID)
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the message type' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_TYPES', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the message type' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_TYPES', @level2type=N'COLUMN', @level2name=N'NAME';



CREATE TABLE MESSAGING.MESSAGE_STATUSES (
  CODE  INTEGER NOT NULL,
  NAME  NVARCHAR(256) NOT NULL,

  PRIMARY KEY (CODE)
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The code identifying the message status' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_STATUSES', @level2type=N'COLUMN', @level2name=N'CODE';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the message status' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_STATUSES', @level2type=N'COLUMN', @level2name=N'NAME';



CREATE TABLE MESSAGING.MESSAGES (
  ID                 UNIQUEIDENTIFIER NOT NULL,
  USERNAME           NVARCHAR(256) NOT NULL,
  DEVICE_ID          UNIQUEIDENTIFIER NOT NULL,
  TYPE_ID            UNIQUEIDENTIFIER NOT NULL,
  CORRELATION_ID     UNIQUEIDENTIFIER NOT NULL,
  PRIORITY           INTEGER NOT NULL,
  STATUS             INTEGER NOT NULL,
  CREATED            DATETIME NOT NULL,
  PERSISTED          DATETIME NOT NULL,
  UPDATED            DATETIME,
  SEND_ATTEMPTS      INTEGER NOT NULL,
  PROCESS_ATTEMPTS   INTEGER NOT NULL,
  DOWNLOAD_ATTEMPTS  INTEGER NOT NULL,
  LOCK_NAME          NVARCHAR(256),
  LAST_PROCESSED     DATETIME,
  DATA               VARBINARY(MAX),

  PRIMARY KEY (ID),
  CONSTRAINT MESSAGES_MESSAGE_TYPE_FK FOREIGN KEY (TYPE_ID) REFERENCES MESSAGING.MESSAGE_TYPES(ID),
  CONSTRAINT MESSAGES_MESSAGE_STATUS_FK FOREIGN KEY (STATUS) REFERENCES MESSAGING.MESSAGE_STATUSES(CODE)
);

CREATE INDEX MESSAGES_USERNAME_IX
  ON MESSAGING.MESSAGES
  (USERNAME);

CREATE INDEX MESSAGES_DEVICE_ID_IX
  ON MESSAGING.MESSAGES
  (DEVICE_ID);

CREATE INDEX MESSAGES_TYPE_ID_IX
  ON MESSAGING.MESSAGES
  (TYPE_ID);

CREATE INDEX MESSAGES_PRIORITY_IX
  ON MESSAGING.MESSAGES
  (PRIORITY);

CREATE INDEX MESSAGES_STATUS_IX
  ON MESSAGING.MESSAGES
  (STATUS);

CREATE INDEX MESSAGES_LOCK_NAME_IX
  ON MESSAGING.MESSAGES
  (LOCK_NAME);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The username identifying the user associated with the message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'USERNAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the device the message originated from' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'DEVICE_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the type of message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'TYPE_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to correlate the message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'CORRELATION_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The message priority' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'PRIORITY';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The message status e.g. Initialised, QueuedForSending, etc' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'STATUS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the message was created' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'CREATED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the message was persisted' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'PERSISTED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the message was last updated' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'UPDATED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The number of times that the sending of the message was attempted' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'SEND_ATTEMPTS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The number of times that the processing of the message was attempted' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'PROCESS_ATTEMPTS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The number of times that an attempt was made to download the message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'DOWNLOAD_ATTEMPTS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the entity that has locked the message for processing' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'LOCK_NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the last attempt was made to process the message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'LAST_PROCESSED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The data for the message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGES', @level2type=N'COLUMN', @level2name=N'DATA';



CREATE TABLE MESSAGING.MESSAGE_PARTS (
  ID                   UNIQUEIDENTIFIER NOT NULL,
  PART_NO              INTEGER NOT NULL,
  TOTAL_PARTS          INTEGER NOT NULL,
  SEND_ATTEMPTS        INTEGER NOT NULL,
  DOWNLOAD_ATTEMPTS    INTEGER NOT NULL,
  STATUS               INTEGER NOT NULL,
  PERSISTED            DATETIME NOT NULL,
  UPDATED              DATETIME,
  MSG_ID               UNIQUEIDENTIFIER NOT NULL,
  MSG_USERNAME         NVARCHAR(256) NOT NULL,
  MSG_DEVICE_ID        UNIQUEIDENTIFIER NOT NULL,
  MSG_TYPE_ID          UNIQUEIDENTIFIER NOT NULL,
  MSG_CORRELATION_ID   UNIQUEIDENTIFIER NOT NULL,
  MSG_PRIORITY         INTEGER NOT NULL,
  MSG_CREATED          DATETIME NOT NULL,
  MSG_DATA_HASH        NVARCHAR(512),
  MSG_ENCRYPTION_IV    NVARCHAR(512) NOT NULL,
  MSG_CHECKSUM         NVARCHAR(512) NOT NULL,
  LOCK_NAME            NVARCHAR(256),
  DATA                 VARBINARY(MAX),

  PRIMARY KEY (ID),
  CONSTRAINT MESSAGE_PARTS_MESSAGE_TYPE_FK FOREIGN KEY (MSG_TYPE_ID) REFERENCES MESSAGING.MESSAGE_TYPES(ID)
);

CREATE INDEX MESSAGE_PARTS_STATUS_IX
  ON MESSAGING.MESSAGE_PARTS
  (STATUS);

CREATE INDEX MESSAGE_PARTS_MSG_ID_IX
  ON MESSAGING.MESSAGE_PARTS
  (MSG_ID);

CREATE INDEX MESSAGE_PARTS_MSG_DEVICE_ID_IX
  ON MESSAGING.MESSAGE_PARTS
  (MSG_DEVICE_ID);

CREATE INDEX MESSAGE_PARTS_MSG_TYPE_ID_IX
  ON MESSAGING.MESSAGE_PARTS
  (MSG_TYPE_ID);

CREATE INDEX MESSAGE_PARTS_LOCK_NAME_IX
  ON MESSAGING.MESSAGE_PARTS
  (LOCK_NAME);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the message part' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The number of the message part in the set of message parts for the original message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'PART_NO';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The total number of parts in the set of message parts for the original message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'TOTAL_PARTS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The number of times that the sending of the message part was attempted' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'SEND_ATTEMPTS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The number of times that an attempt was made to download the message part' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'DOWNLOAD_ATTEMPTS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The message part status e.g. Initialised, QueuedForSending, etc' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'STATUS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the message part was persisted' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'PERSISTED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the message part was last updated' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'UPDATED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the original message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'MSG_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The username identifying the user associated with the original message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'MSG_USERNAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the device the original message originated from' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'MSG_DEVICE_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the type of the original message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'MSG_TYPE_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to correlate the original message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'MSG_CORRELATION_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The priority for the original message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'MSG_PRIORITY';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the original message was created' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'MSG_CREATED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The hash of the unencrypted data for the original message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'MSG_DATA_HASH';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The base-64 encoded initialisation vector for the encryption scheme for the original message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'MSG_ENCRYPTION_IV';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The checksum for the original message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'MSG_CHECKSUM';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the entity that has locked the message part for processing' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'LOCK_NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The data for the message part' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'MESSAGE_PARTS', @level2type=N'COLUMN', @level2name=N'DATA';



CREATE TABLE MESSAGING.ARCHIVED_MESSAGES (
  ID               UNIQUEIDENTIFIER NOT NULL,
  USERNAME         NVARCHAR(256) NOT NULL,
  DEVICE_ID        UNIQUEIDENTIFIER NOT NULL,
  TYPE_ID          UNIQUEIDENTIFIER NOT NULL,
  CORRELATION_ID   UNIQUEIDENTIFIER NOT NULL,
  CREATED          DATETIME NOT NULL,
  ARCHIVED         DATETIME NOT NULL,
  DATA             VARBINARY(MAX),

  PRIMARY KEY (ID),
  CONSTRAINT ARCHIVED_MESSAGES_MESSAGE_TYPE_FK FOREIGN KEY (TYPE_ID) REFERENCES MESSAGING.MESSAGE_TYPES(ID)
);

CREATE INDEX ARCHIVED_MESSAGES_USERNAME_IX
  ON MESSAGING.ARCHIVED_MESSAGES
  (USERNAME);

CREATE INDEX ARCHIVED_MESSAGES_DEVICE_ID_IX
  ON MESSAGING.ARCHIVED_MESSAGES
  (DEVICE_ID);

CREATE INDEX ARCHIVED_MESSAGES_TYPE_ID_IX
  ON MESSAGING.ARCHIVED_MESSAGES
  (TYPE_ID);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ARCHIVED_MESSAGES', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The username identifying the user associated with the message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ARCHIVED_MESSAGES', @level2type=N'COLUMN', @level2name=N'USERNAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the device the message originated from' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ARCHIVED_MESSAGES', @level2type=N'COLUMN', @level2name=N'DEVICE_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the type of message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ARCHIVED_MESSAGES', @level2type=N'COLUMN', @level2name=N'TYPE_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to correlate the message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ARCHIVED_MESSAGES', @level2type=N'COLUMN', @level2name=N'CORRELATION_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the message was created' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ARCHIVED_MESSAGES', @level2type=N'COLUMN', @level2name=N'CREATED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the message was archived' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ARCHIVED_MESSAGES', @level2type=N'COLUMN', @level2name=N'ARCHIVED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The data for the message' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ARCHIVED_MESSAGES', @level2type=N'COLUMN', @level2name=N'DATA';



CREATE TABLE MESSAGING.ERROR_REPORTS (
  ID                   UNIQUEIDENTIFIER NOT NULL,
  APPLICATION_ID       UNIQUEIDENTIFIER NOT NULL,
  APPLICATION_VERSION  INTEGER NOT NULL,
  DESCRIPTION          NVARCHAR(MAX) NOT NULL,
  DETAIL               NVARCHAR(MAX) NOT NULL,
  FEEDBACK             NVARCHAR(MAX) NOT NULL,
  CREATED              DATETIME NOT NULL,
  WHO                  NVARCHAR(256) NOT NULL,
  DEVICE_ID            UNIQUEIDENTIFIER NOT NULL,
  DATA                 VARBINARY(MAX),

  PRIMARY KEY (ID)
);

CREATE INDEX ERROR_REPORTS_APPLICATION_ID_IX
  ON MESSAGING.ERROR_REPORTS
  (APPLICATION_ID);

CREATE INDEX ERROR_REPORTS_CREATED_IX
  ON MESSAGING.ERROR_REPORTS
  (CREATED);

CREATE INDEX ERROR_REPORTS_WHO_IX
  ON MESSAGING.ERROR_REPORTS
  (WHO);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the error report' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ERROR_REPORTS', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the application that generated the error report' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ERROR_REPORTS', @level2type=N'COLUMN', @level2name=N'APPLICATION_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The version of the application that generated the error report' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ERROR_REPORTS', @level2type=N'COLUMN', @level2name=N'APPLICATION_VERSION';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The description of the error' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ERROR_REPORTS', @level2type=N'COLUMN', @level2name=N'DESCRIPTION';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The error detail e.g. a stack trace' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ERROR_REPORTS', @level2type=N'COLUMN', @level2name=N'DETAIL';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The feedback provided by the user for the error' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ERROR_REPORTS', @level2type=N'COLUMN', @level2name=N'FEEDBACK';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the error report was created' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ERROR_REPORTS', @level2type=N'COLUMN', @level2name=N'CREATED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The username identifying the user associated with the error report' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ERROR_REPORTS', @level2type=N'COLUMN', @level2name=N'WHO';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the device the error report originated from' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ERROR_REPORTS', @level2type=N'COLUMN', @level2name=N'DEVICE_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The data associated with the error report' ,
@level0type=N'SCHEMA', @level0name=N'MESSAGING', @level1type=N'TABLE', @level1name=N'ERROR_REPORTS', @level2type=N'COLUMN', @level2name=N'DATA';



CREATE TABLE REPORTING.REPORT_DEFINITIONS (
  ID        UNIQUEIDENTIFIER NOT NULL,
  NAME      NVARCHAR(256) NOT NULL,
  TEMPLATE  VARBINARY(MAX) NOT NULL,

  PRIMARY KEY (ID)
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the report definition' ,
@level0type=N'SCHEMA', @level0name=N'REPORTING', @level1type=N'TABLE', @level1name=N'REPORT_DEFINITIONS', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the report definition' ,
@level0type=N'SCHEMA', @level0name=N'REPORTING', @level1type=N'TABLE', @level1name=N'REPORT_DEFINITIONS', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The JasperReports template for the report definition' ,
@level0type=N'SCHEMA', @level0name=N'REPORTING', @level1type=N'TABLE', @level1name=N'REPORT_DEFINITIONS', @level2type=N'COLUMN', @level2name=N'TEMPLATE';



CREATE TABLE SCHEDULER.JOBS (
  ID                  UNIQUEIDENTIFIER NOT NULL,
  NAME                NVARCHAR(256) NOT NULL,
  SCHEDULING_PATTERN  NVARCHAR(1024) NOT NULL,
  JOB_CLASS           NVARCHAR(1024) NOT NULL,
  IS_ENABLED          BIT NOT NULL,
  STATUS              INTEGER NOT NULL DEFAULT 1,
  EXECUTION_ATTEMPTS  INTEGER NOT NULL DEFAULT 0,
  LOCK_NAME           NVARCHAR(256),
  LAST_EXECUTED       DATETIME,
  NEXT_EXECUTION      DATETIME,
  UPDATED             DATETIME,

  PRIMARY KEY (ID)
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the job' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOBS', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the job' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOBS', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The cron-style scheduling pattern for the job' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOBS', @level2type=N'COLUMN', @level2name=N'SCHEDULING_PATTERN';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The fully qualified name of the Java class that implements the job' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOBS', @level2type=N'COLUMN', @level2name=N'JOB_CLASS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'Is the job enabled for execution' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOBS', @level2type=N'COLUMN', @level2name=N'IS_ENABLED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The status of the job' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOBS', @level2type=N'COLUMN', @level2name=N'STATUS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The number of times the current execution of the job has been attempted' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOBS', @level2type=N'COLUMN', @level2name=N'EXECUTION_ATTEMPTS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the entity that has locked the job for execution' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOBS', @level2type=N'COLUMN', @level2name=N'LOCK_NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the job was last executed' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOBS', @level2type=N'COLUMN', @level2name=N'LAST_EXECUTED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time when the job will next be executed' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOBS', @level2type=N'COLUMN', @level2name=N'NEXT_EXECUTION';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the job was updated' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOBS', @level2type=N'COLUMN', @level2name=N'UPDATED';



CREATE TABLE SCHEDULER.JOB_PARAMETERS (
  ID      UNIQUEIDENTIFIER NOT NULL,
  JOB_ID  UNIQUEIDENTIFIER NOT NULL,
  NAME    NVARCHAR(256) NOT NULL,
  VALUE   NVARCHAR(MAX) NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT JOB_PARAMETERS_JOB_FK FOREIGN KEY (JOB_ID) REFERENCES SCHEDULER.JOBS(ID) ON DELETE CASCADE
);

CREATE INDEX JOB_PARAMETERS_JOB_ID_IX
  ON SCHEDULER.JOB_PARAMETERS
  (JOB_ID);

CREATE INDEX JOB_PARAMETERS_NAME_IX
  ON SCHEDULER.JOB_PARAMETERS
  (NAME);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the job parameter' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOB_PARAMETERS', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the job' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOB_PARAMETERS', @level2type=N'COLUMN', @level2name=N'JOB_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the job parameter' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOB_PARAMETERS', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The value of the job parameter' ,
@level0type=N'SCHEMA', @level0name=N'SCHEDULER', @level1type=N'TABLE', @level1name=N'JOB_PARAMETERS', @level2type=N'COLUMN', @level2name=N'VALUE';



CREATE TABLE SECURITY.ORGANISATIONS (
  ID      UNIQUEIDENTIFIER NOT NULL,
  NAME    NVARCHAR(256) NOT NULL,
  STATUS  INTEGER NOT NULL,

  PRIMARY KEY (ID)
);

CREATE INDEX ORGANISATIONS_NAME_IX
  ON SECURITY.ORGANISATIONS
  (NAME);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the organisation' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'ORGANISATIONS', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the organisation' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'ORGANISATIONS', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The status for the organisation' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'ORGANISATIONS', @level2type=N'COLUMN', @level2name=N'STATUS';



CREATE TABLE SECURITY.USER_DIRECTORY_TYPES (
  ID                    UNIQUEIDENTIFIER NOT NULL,
  NAME                  NVARCHAR(256) NOT NULL,
  USER_DIRECTORY_CLASS  NVARCHAR(1024) NOT NULL,
  ADMINISTRATION_CLASS  NVARCHAR(1024) NOT NULL,

  PRIMARY KEY (ID)
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the user directory type' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'USER_DIRECTORY_TYPES', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the user directory type' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'USER_DIRECTORY_TYPES', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The fully qualified name of the Java class that implements the user directory type' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'USER_DIRECTORY_TYPES', @level2type=N'COLUMN', @level2name=N'USER_DIRECTORY_CLASS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The fully qualified name of the Java class that implements the Wicket component used to administer the configuration for the user directory type' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'USER_DIRECTORY_TYPES', @level2type=N'COLUMN', @level2name=N'ADMINISTRATION_CLASS';



CREATE TABLE SECURITY.USER_DIRECTORIES (
  ID             UNIQUEIDENTIFIER NOT NULL,
  TYPE_ID        UNIQUEIDENTIFIER NOT NULL,
  NAME           NVARCHAR(256) NOT NULL,
  CONFIGURATION  NVARCHAR(MAX) NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT USER_DIRECTORIES_USER_DIRECTORY_TYPE_FK FOREIGN KEY (TYPE_ID) REFERENCES SECURITY.USER_DIRECTORY_TYPES(ID) ON DELETE CASCADE
);

CREATE INDEX USER_DIRECTORIES_NAME_IX
  ON SECURITY.USER_DIRECTORIES
  (NAME);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the user directory' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'USER_DIRECTORIES', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the user directory type' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'USER_DIRECTORIES', @level2type=N'COLUMN', @level2name=N'TYPE_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the user directory' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'USER_DIRECTORIES', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The XML configuration data for the user directory' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'USER_DIRECTORIES', @level2type=N'COLUMN', @level2name=N'CONFIGURATION';



CREATE TABLE SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP (
  USER_DIRECTORY_ID  UNIQUEIDENTIFIER NOT NULL,
  ORGANISATION_ID    UNIQUEIDENTIFIER NOT NULL,

  PRIMARY KEY (USER_DIRECTORY_ID, ORGANISATION_ID),
  CONSTRAINT USER_DIRECTORY_TO_ORGANISATION_MAP_USER_DIRECTORY_FK FOREIGN KEY (USER_DIRECTORY_ID) REFERENCES SECURITY.USER_DIRECTORIES(ID) ON DELETE CASCADE,
  CONSTRAINT USER_DIRECTORY_TO_ORGANISATION_MAP_ORGANISATION_FK FOREIGN KEY (ORGANISATION_ID) REFERENCES SECURITY.ORGANISATIONS(ID) ON DELETE CASCADE
);

CREATE INDEX USER_DIRECTORY_TO_ORGANISATION_MAP_USER_DIRECTORY_ID_IX
  ON SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP
  (USER_DIRECTORY_ID);

CREATE INDEX USER_DIRECTORY_TO_ORGANISATION_MAP_ORGANISATION_ID_IX
  ON SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP
  (ORGANISATION_ID);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the user directory' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'USER_DIRECTORY_TO_ORGANISATION_MAP', @level2type=N'COLUMN', @level2name=N'USER_DIRECTORY_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the organisation' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'USER_DIRECTORY_TO_ORGANISATION_MAP', @level2type=N'COLUMN', @level2name=N'ORGANISATION_ID';



CREATE TABLE SECURITY.INTERNAL_USERS (
  ID                 UNIQUEIDENTIFIER NOT NULL,
  USER_DIRECTORY_ID  UNIQUEIDENTIFIER NOT NULL,
  USERNAME           NVARCHAR(256) NOT NULL,
  PASSWORD           NVARCHAR(512),
  FIRST_NAME         NVARCHAR(256),
  LAST_NAME          NVARCHAR(256),
  PHONE              NVARCHAR(256),
  MOBILE             NVARCHAR(256),
  EMAIL              NVARCHAR(256),
  PASSWORD_ATTEMPTS  INTEGER,
  PASSWORD_EXPIRY    DATETIME,

  PRIMARY KEY (ID),
  CONSTRAINT INTERNAL_USERS_USER_DIRECTORY_FK FOREIGN KEY (USER_DIRECTORY_ID) REFERENCES SECURITY.USER_DIRECTORIES(ID) ON DELETE CASCADE
);

CREATE INDEX INTERNAL_USERS_USER_DIRECTORY_ID_IX
  ON SECURITY.INTERNAL_USERS
  (USER_DIRECTORY_ID);

CREATE UNIQUE INDEX INTERNAL_USERS_USERNAME_IX
  ON SECURITY.INTERNAL_USERS
  (USERNAME);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the internal user is associated with' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS', @level2type=N'COLUMN', @level2name=N'USER_DIRECTORY_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The username for the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS', @level2type=N'COLUMN', @level2name=N'USERNAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The password for the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS', @level2type=N'COLUMN', @level2name=N'PASSWORD';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The first name for the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS', @level2type=N'COLUMN', @level2name=N'FIRST_NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The last name for the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS', @level2type=N'COLUMN', @level2name=N'LAST_NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The phone number for the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS', @level2type=N'COLUMN', @level2name=N'PHONE';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The mobile number for the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS', @level2type=N'COLUMN', @level2name=N'MOBILE';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The e-mail address for the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS', @level2type=N'COLUMN', @level2name=N'EMAIL';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The number of failed attempts to authenticate the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS', @level2type=N'COLUMN', @level2name=N'PASSWORD_ATTEMPTS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time that the internal user''s password expires' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS', @level2type=N'COLUMN', @level2name=N'PASSWORD_EXPIRY';



CREATE TABLE SECURITY.INTERNAL_USERS_PASSWORD_HISTORY (
  ID                UNIQUEIDENTIFIER NOT NULL,
  INTERNAL_USER_ID  UNIQUEIDENTIFIER NOT NULL,
  CHANGED           DATETIME NOT NULL,
  PASSWORD          NVARCHAR(512),

  PRIMARY KEY (ID),
  CONSTRAINT INTERNAL_USERS_PASSWORD_HISTORY_INTERNAL_USER_ID_FK FOREIGN KEY (INTERNAL_USER_ID) REFERENCES SECURITY.INTERNAL_USERS(ID) ON DELETE CASCADE
);

CREATE INDEX INTERNAL_USERS_PASSWORD_HISTORY_INTERNAL_USER_ID_IX
  ON SECURITY.INTERNAL_USERS_PASSWORD_HISTORY
  (INTERNAL_USER_ID);

CREATE INDEX INTERNAL_USERS_PASSWORD_HISTORY_CHANGED_IX
  ON SECURITY.INTERNAL_USERS_PASSWORD_HISTORY
  (CHANGED);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the password history entry' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS_PASSWORD_HISTORY', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS_PASSWORD_HISTORY', @level2type=N'COLUMN', @level2name=N'INTERNAL_USER_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'When the password change took place for the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS_PASSWORD_HISTORY', @level2type=N'COLUMN', @level2name=N'CHANGED';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The password for the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USERS_PASSWORD_HISTORY', @level2type=N'COLUMN', @level2name=N'PASSWORD';



CREATE TABLE SECURITY.INTERNAL_GROUPS (
  ID                 UNIQUEIDENTIFIER NOT NULL,
  USER_DIRECTORY_ID  UNIQUEIDENTIFIER NOT NULL,
  GROUPNAME          NVARCHAR(256) NOT NULL,
  DESCRIPTION        NVARCHAR(512),

  PRIMARY KEY (ID),
  CONSTRAINT INTERNAL_GROUPS_USER_DIRECTORY_FK FOREIGN KEY (USER_DIRECTORY_ID) REFERENCES SECURITY.USER_DIRECTORIES(ID) ON DELETE CASCADE
);

CREATE INDEX INTERNAL_GROUPS_USER_DIRECTORY_ID_IX
  ON SECURITY.INTERNAL_GROUPS
  (USER_DIRECTORY_ID);

CREATE INDEX INTERNAL_GROUPS_GROUPNAME_IX
  ON SECURITY.INTERNAL_GROUPS
  (GROUPNAME);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the internal group' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_GROUPS', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the internal group is associated with' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_GROUPS', @level2type=N'COLUMN', @level2name=N'USER_DIRECTORY_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The group name for the internal group' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_GROUPS', @level2type=N'COLUMN', @level2name=N'GROUPNAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'A description for the internal group' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_GROUPS', @level2type=N'COLUMN', @level2name=N'DESCRIPTION';



CREATE TABLE SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP (
  INTERNAL_USER_ID   UNIQUEIDENTIFIER NOT NULL,
  INTERNAL_GROUP_ID  UNIQUEIDENTIFIER NOT NULL,

  PRIMARY KEY (INTERNAL_USER_ID, INTERNAL_GROUP_ID)
);

CREATE INDEX INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_USER_ID_IX
  ON SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP
  (INTERNAL_USER_ID);

CREATE INDEX INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_GROUP_ID_IX
  ON SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP
  (INTERNAL_GROUP_ID);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the internal user' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USER_TO_INTERNAL_GROUP_MAP', @level2type=N'COLUMN', @level2name=N'INTERNAL_USER_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the internal group' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'INTERNAL_USER_TO_INTERNAL_GROUP_MAP', @level2type=N'COLUMN', @level2name=N'INTERNAL_GROUP_ID';

CREATE TRIGGER INTERNAL_GROUPS_ON_DELETE_INTERNAL_USER_TO_INTERNAL_GROUP_MAP_TRIGGER
    ON SECURITY.INTERNAL_GROUPS
    FOR DELETE
AS
    DELETE FROM SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP
    WHERE INTERNAL_GROUP_ID IN(SELECT DELETED.ID FROM DELETED)
GO

CREATE TRIGGER INTERNAL_USERS_ON_DELETE_INTERNAL_USER_TO_INTERNAL_GROUP_MAP_TRIGGER
    ON SECURITY.INTERNAL_USERS
    FOR DELETE
AS
    DELETE FROM SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP
    WHERE INTERNAL_USER_ID IN(SELECT DELETED.ID FROM DELETED)
GO



CREATE TABLE SECURITY.GROUPS (
  ID                 UNIQUEIDENTIFIER NOT NULL,
  USER_DIRECTORY_ID  UNIQUEIDENTIFIER NOT NULL,
  GROUPNAME          NVARCHAR(256) NOT NULL,

  PRIMARY KEY (ID)
);

CREATE INDEX GROUPS_USER_DIRECTORY_ID_IX
  ON SECURITY.GROUPS
  (USER_DIRECTORY_ID);

CREATE INDEX GROUPS_GROUPNAME_IX
  ON SECURITY.GROUPS
  (GROUPNAME);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the group' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'GROUPS', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the group is associated with' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'GROUPS', @level2type=N'COLUMN', @level2name=N'USER_DIRECTORY_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The group name for the group' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'GROUPS', @level2type=N'COLUMN', @level2name=N'GROUPNAME';



CREATE TABLE SECURITY.FUNCTIONS (
  ID           UNIQUEIDENTIFIER NOT NULL,
  CODE         NVARCHAR(256) NOT NULL,
  NAME         NVARCHAR(256) NOT NULL,
  DESCRIPTION  NVARCHAR(512),

  PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX FUNCTIONS_CODE_IX
  ON SECURITY.FUNCTIONS
  (CODE);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the function' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'FUNCTIONS', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The unique code used to identify the function' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'FUNCTIONS', @level2type=N'COLUMN', @level2name=N'CODE';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the function' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'FUNCTIONS', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'A description for the function' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'FUNCTIONS', @level2type=N'COLUMN', @level2name=N'DESCRIPTION';



CREATE TABLE SECURITY.ROLES (
  ID           UNIQUEIDENTIFIER NOT NULL,
  NAME         NVARCHAR(256) NOT NULL,
  DESCRIPTION  NVARCHAR(512),

  PRIMARY KEY (ID)
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the role' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'ROLES', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the role' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'ROLES', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'A description for the role' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'ROLES', @level2type=N'COLUMN', @level2name=N'DESCRIPTION';



CREATE TABLE SECURITY.FUNCTION_TO_ROLE_MAP (
  FUNCTION_ID  UNIQUEIDENTIFIER NOT NULL,
  ROLE_ID      UNIQUEIDENTIFIER NOT NULL,

  PRIMARY KEY (FUNCTION_ID, ROLE_ID)
);

CREATE INDEX FUNCTION_TO_ROLE_MAP_FUNCTION_ID_IX
  ON SECURITY.FUNCTION_TO_ROLE_MAP
  (FUNCTION_ID);

CREATE INDEX FUNCTION_TO_ROLE_MAP_ROLE_ID_IX
  ON SECURITY.FUNCTION_TO_ROLE_MAP
  (ROLE_ID);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the function' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'FUNCTION_TO_ROLE_MAP', @level2type=N'COLUMN', @level2name=N'FUNCTION_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the role' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'FUNCTION_TO_ROLE_MAP', @level2type=N'COLUMN', @level2name=N'ROLE_ID';

CREATE TRIGGER FUNCTIONS_ON_DELETE_FUNCTION_TO_ROLE_MAP_TRIGGER
    ON SECURITY.FUNCTIONS
    FOR DELETE
AS
    DELETE FROM SECURITY.FUNCTION_TO_ROLE_MAP
    WHERE FUNCTION_ID IN(SELECT DELETED.ID FROM DELETED)
GO

CREATE TRIGGER ROLES_ON_DELETE_FUNCTION_TO_ROLE_MAP_TRIGGER
    ON SECURITY.ROLES
    FOR DELETE
AS
    DELETE FROM SECURITY.FUNCTION_TO_ROLE_MAP
    WHERE ROLE_ID IN(SELECT DELETED.ID FROM DELETED)
GO



CREATE TABLE SECURITY.ROLE_TO_GROUP_MAP (
  ROLE_ID   UNIQUEIDENTIFIER NOT NULL,
  GROUP_ID  UNIQUEIDENTIFIER NOT NULL,

  PRIMARY KEY (ROLE_ID, GROUP_ID),
);

CREATE INDEX ROLE_TO_GROUP_MAP_ROLE_ID_IX
  ON SECURITY.ROLE_TO_GROUP_MAP
  (ROLE_ID);

CREATE INDEX ROLE_TO_GROUP_MAP_GROUP_ID_IX
  ON SECURITY.ROLE_TO_GROUP_MAP
  (GROUP_ID);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the role' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'ROLE_TO_GROUP_MAP', @level2type=N'COLUMN', @level2name=N'ROLE_ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The Universally Unique Identifier (UUID) used to uniquely identify the group' ,
@level0type=N'SCHEMA', @level0name=N'SECURITY', @level1type=N'TABLE', @level1name=N'ROLE_TO_GROUP_MAP', @level2type=N'COLUMN', @level2name=N'GROUP_ID';

CREATE TRIGGER ROLES_ON_DELETE_ROLE_TO_GROUP_MAP_TRIGGER
    ON SECURITY.ROLES
    FOR DELETE
AS
    DELETE FROM SECURITY.ROLE_TO_GROUP_MAP
    WHERE ROLE_ID IN(SELECT DELETED.ID FROM DELETED)
GO

CREATE TRIGGER GROUPS_ON_DELETE_ROLE_TO_GROUP_MAP_TRIGGER
    ON SECURITY.GROUPS
    FOR DELETE
AS
    DELETE FROM SECURITY.ROLE_TO_GROUP_MAP
    WHERE GROUP_ID IN(SELECT DELETED.ID FROM DELETED)
GO



CREATE TABLE SERVICE_REGISTRY.SERVICE_REGISTRY (
  NAME                  NVARCHAR(256) NOT NULL,
  SECURITY_TYPE         INTEGER NOT NULL,
  SUPPORTS_COMPRESSION  CHAR(1) NOT NULL,
  ENDPOINT              NVARCHAR(1024) NOT NULL,
  SERVICE_CLASS         NVARCHAR(1024) NOT NULL,
  WSDL_LOCATION         NVARCHAR(1024) NOT NULL,
  USERNAME              NVARCHAR(256),
  PASSWORD              NVARCHAR(256),

  PRIMARY KEY (NAME)
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name used to uniquely identify the web service' ,
@level0type=N'SCHEMA', @level0name=N'SERVICE_REGISTRY', @level1type=N'TABLE', @level1name=N'SERVICE_REGISTRY', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The type of security model implemented by the web service i.e. 0 = None, 1 = Mutual SSL, etc' ,
@level0type=N'SCHEMA', @level0name=N'SERVICE_REGISTRY', @level1type=N'TABLE', @level1name=N'SERVICE_REGISTRY', @level2type=N'COLUMN', @level2name=N'SECURITY_TYPE';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'Does the web service support compression' ,
@level0type=N'SCHEMA', @level0name=N'SERVICE_REGISTRY', @level1type=N'TABLE', @level1name=N'SERVICE_REGISTRY', @level2type=N'COLUMN', @level2name=N'SUPPORTS_COMPRESSION';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The endpoint for the web service' ,
@level0type=N'SCHEMA', @level0name=N'SERVICE_REGISTRY', @level1type=N'TABLE', @level1name=N'SERVICE_REGISTRY', @level2type=N'COLUMN', @level2name=N'ENDPOINT';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The fully qualified name of the Java service class' ,
@level0type=N'SCHEMA', @level0name=N'SERVICE_REGISTRY', @level1type=N'TABLE', @level1name=N'SERVICE_REGISTRY', @level2type=N'COLUMN', @level2name=N'SERVICE_CLASS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The location of the WSDL defining the web service on the classpath' ,
@level0type=N'SCHEMA', @level0name=N'SERVICE_REGISTRY', @level1type=N'TABLE', @level1name=N'SERVICE_REGISTRY', @level2type=N'COLUMN', @level2name=N'WSDL_LOCATION';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The username to use when accessing a web service with username-password security enabled' ,
@level0type=N'SCHEMA', @level0name=N'SERVICE_REGISTRY', @level1type=N'TABLE', @level1name=N'SERVICE_REGISTRY', @level2type=N'COLUMN', @level2name=N'USERNAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The password to use when accessing a web service with username-password security enabled' ,
@level0type=N'SCHEMA', @level0name=N'SERVICE_REGISTRY', @level1type=N'TABLE', @level1name=N'SERVICE_REGISTRY', @level2type=N'COLUMN', @level2name=N'PASSWORD';



CREATE TABLE SMS.SMS (
  ID              BIGINT NOT NULL,
  MOBILE_NUMBER   NVARCHAR(100) NOT NULL,
  MESSAGE         NVARCHAR(1024) NOT NULL,
  STATUS          INTEGER NOT NULL,
  SEND_ATTEMPTS   INTEGER NOT NULL,
  LOCK_NAME       NVARCHAR(256),
  LAST_PROCESSED  DATETIME,

  PRIMARY KEY (ID)
);

CREATE INDEX SMS_MOBILE_NUMBER_IX
  ON SMS.SMS
  (MOBILE_NUMBER);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The ID used to uniquely identify the SMS' ,
@level0type=N'SCHEMA', @level0name=N'SMS', @level1type=N'TABLE', @level1name=N'SMS', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The mobile number to send the SMS to' ,
@level0type=N'SCHEMA', @level0name=N'SMS', @level1type=N'TABLE', @level1name=N'SMS', @level2type=N'COLUMN', @level2name=N'MOBILE_NUMBER';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The message to send' ,
@level0type=N'SCHEMA', @level0name=N'SMS', @level1type=N'TABLE', @level1name=N'SMS', @level2type=N'COLUMN', @level2name=N'MESSAGE';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The status of the SMS' ,
@level0type=N'SCHEMA', @level0name=N'SMS', @level1type=N'TABLE', @level1name=N'SMS', @level2type=N'COLUMN', @level2name=N'STATUS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The number of times that the sending of the SMS was attempted' ,
@level0type=N'SCHEMA', @level0name=N'SMS', @level1type=N'TABLE', @level1name=N'SMS', @level2type=N'COLUMN', @level2name=N'SEND_ATTEMPTS';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name of the entity that has locked the SMS for sending' ,
@level0type=N'SCHEMA', @level0name=N'SMS', @level1type=N'TABLE', @level1name=N'SMS', @level2type=N'COLUMN', @level2name=N'LOCK_NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The date and time the last attempt was made to send the SMS' ,
@level0type=N'SCHEMA', @level0name=N'SMS', @level1type=N'TABLE', @level1name=N'SMS', @level2type=N'COLUMN', @level2name=N'LAST_PROCESSED';



CREATE TABLE TEST.TEST_DATA (
  ID     INTEGER NOT NULL,
  NAME   NVARCHAR(4000) NOT NULL,
  VALUE  NVARCHAR(4000) NOT NULL,

  PRIMARY KEY (ID)
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The ID used to uniquely identify the test data' ,
@level0type=N'SCHEMA', @level0name=N'TEST', @level1type=N'TABLE', @level1name=N'TEST_DATA', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name for the test data' ,
@level0type=N'SCHEMA', @level0name=N'TEST', @level1type=N'TABLE', @level1name=N'TEST_DATA', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The value for the test data' ,
@level0type=N'SCHEMA', @level0name=N'TEST', @level1type=N'TABLE', @level1name=N'TEST_DATA', @level2type=N'COLUMN', @level2name=N'VALUE';


CREATE TABLE SAMPLE.DATA (
  ID     BIGINT NOT NULL,
  NAME   NVARCHAR(256) NOT NULL,
  VALUE  NVARCHAR(MAX) NOT NULL,

  PRIMARY KEY (ID)
);

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The ID used to uniquely identify the data' ,
@level0type=N'SCHEMA', @level0name=N'SAMPLE', @level1type=N'TABLE', @level1name=N'DATA', @level2type=N'COLUMN', @level2name=N'ID';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The name for the data' ,
@level0type=N'SCHEMA', @level0name=N'SAMPLE', @level1type=N'TABLE', @level1name=N'DATA', @level2type=N'COLUMN', @level2name=N'NAME';

EXEC sys.sp_addextendedproperty
@name=N'MS_Description', @value=N'The value for the data' ,
@level0type=N'SCHEMA', @level0name=N'SAMPLE', @level1type=N'TABLE', @level1name=N'DATA', @level2type=N'COLUMN', @level2name=N'VALUE';



-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO SECURITY.ORGANISATIONS (ID, NAME, STATUS) VALUES
  ('c1685b92-9fe5-453a-995b-89d8c0f29cb5', 'MMP', 1);

INSERT INTO SECURITY.USER_DIRECTORY_TYPES (ID, NAME, USER_DIRECTORY_CLASS, ADMINISTRATION_CLASS) VALUES
  ('b43fda33-d3b0-4f80-a39a-110b8e530f4f', 'Internal User Directory', 'guru.mmp.application.security.InternalUserDirectory', 'guru.mmp.application.web.template.components.InternalUserDirectoryAdministrationPanel');
INSERT INTO SECURITY.USER_DIRECTORY_TYPES (ID, NAME, USER_DIRECTORY_CLASS, ADMINISTRATION_CLASS) VALUES
  ('e5741a89-c87b-4406-8a60-2cc0b0a5fa3e', 'LDAP User Directory', 'guru.mmp.application.security.LDAPUserDirectory', 'guru.mmp.application.web.template.components.LDAPUserDirectoryAdministrationPanel');

INSERT INTO SECURITY.USER_DIRECTORIES (ID, TYPE_ID, NAME, CONFIGURATION) VALUES
  ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'b43fda33-d3b0-4f80-a39a-110b8e530f4f', 'Internal User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter></userDirectory>');

INSERT INTO SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP (USER_DIRECTORY_ID, ORGANISATION_ID) VALUES
  ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'c1685b92-9fe5-453a-995b-89d8c0f29cb5');

INSERT INTO SECURITY.INTERNAL_USERS (ID, USER_DIRECTORY_ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, PHONE, MOBILE, EMAIL, PASSWORD_ATTEMPTS, PASSWORD_EXPIRY) VALUES
  ('b2bbf431-4af8-4104-b96c-d33b5f66d1e4', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrator', 'GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=', '', '', '', '', '', null, null);

INSERT INTO SECURITY.INTERNAL_GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME, DESCRIPTION) VALUES
  ('a9e01fa2-f017-46e2-8187-424bf50a4f33', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrators', 'Administrators');
INSERT INTO SECURITY.INTERNAL_GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME, DESCRIPTION) VALUES
  ('758c0a2a-f3a3-4561-bebc-90569291976e', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Organisation Administrators', 'Organisation Administrators');

INSERT INTO SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP (INTERNAL_USER_ID, INTERNAL_GROUP_ID) VALUES
  ('b2bbf431-4af8-4104-b96c-d33b5f66d1e4', 'a9e01fa2-f017-46e2-8187-424bf50a4f33');

INSERT INTO SECURITY.GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME) VALUES ('a9e01fa2-f017-46e2-8187-424bf50a4f33', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrators');
INSERT INTO SECURITY.GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME) VALUES ('758c0a2a-f3a3-4561-bebc-90569291976e', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Organisation Administrators');

INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5', 'Application.SecureHome', 'Secure Home', 'Secure Home');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9', 'Application.Dashboard', 'Dashboard', 'Dashboard');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('2d52b029-920f-4b15-b646-5b9955c188e3', 'Application.OrganisationAdministration', 'Organisation Administration', 'Organisation Administration');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('567d7e55-f3d0-4191-bc4c-12d357900fa3', 'Application.UserAdministration', 'User Administration', 'User Administration');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('ef03f384-24f7-43eb-a29c-f5c5b838698d', 'Application.GroupAdministration', 'Group Administration', 'Group Administration');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('7a54a71e-3680-4d49-b87d-29604a247413', 'Application.UserGroups', 'User Groups', 'User Groups');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('0623bc3f-9a1b-4f19-8438-236660d789c5', 'Application.CodeCategoryAdministration', 'Code Category Administration', 'Code Category Administration');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('4e6bc7c4-ee29-4cd7-b4d7-3be42db73dd6', 'Application.CodeAdministration', 'Code Administration', 'Code Administration');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('029b9a06-0241-4a44-a234-5c489f2017ba', 'Application.ResetUserPassword', 'Reset User Password', 'Reset User Password');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('9105fb6d-1629-4014-bf4c-1990a92db276', 'Application.SecurityAdministration', 'Security Administration', 'Security Administration');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('b233ed4a-b30f-4356-a5d3-1c660aa69f00', 'Application.ConfigurationAdministration', 'Configuration Administration', 'Configuration Administration');

INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('97f0f870-a871-48de-a3e0-a32a95770f12', 'Application.ErrorReports', 'Error Reports', 'Error Reports');

INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('3a17959c-5dfc-43a2-9587-48a1eb95a22a', 'Application.ReportDefinitionAdministration', 'Report Definition Administration', 'Report Definition Administration');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('539fceb8-da82-4170-ab1a-ae6b04001c03', 'Application.ViewReport', 'View Report', 'View Report');

INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('180c84f9-9816-48d0-9762-dc753b2228b1', 'Application.ProcessDefinitionAdministration', 'Process Definition Administration', 'Process Definition Administration');
INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('d2854c65-9a59-40b8-9dc7-a882c64b2610', 'Application.ViewProcess', 'View Process', 'View Process');

INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  ('4d60aed6-2d4b-4a91-a178-ac06d4b1769a', 'Application.SchedulerAdministration', 'Scheduler Administration', 'Scheduler Administration');

INSERT INTO SECURITY.ROLES (ID, NAME, DESCRIPTION) VALUES
  ('100fafb4-783a-4204-a22d-9e27335dc2ea', 'Administrator', 'Administrator');
INSERT INTO SECURITY.ROLES (ID, NAME, DESCRIPTION) VALUES
  ('44ff0ad2-fbe1-489f-86c9-cef7f82acf35', 'Organisation Administrator', 'Organisation Administrator');

INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.SecureHome
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.Dashboard
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('2d52b029-920f-4b15-b646-5b9955c188e3', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.OrganisationAdministration
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('567d7e55-f3d0-4191-bc4c-12d357900fa3', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.UserAdministration
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('ef03f384-24f7-43eb-a29c-f5c5b838698d', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.GroupAdministration
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('7a54a71e-3680-4d49-b87d-29604a247413', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.UserGroups
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('0623bc3f-9a1b-4f19-8438-236660d789c5', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.CodeCategoryAdministration
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('4e6bc7c4-ee29-4cd7-b4d7-3be42db73dd6', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.CodeAdministration
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('029b9a06-0241-4a44-a234-5c489f2017ba', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ResetUserPassword
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('9105fb6d-1629-4014-bf4c-1990a92db276', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.SecurityAdministration
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('b233ed4a-b30f-4356-a5d3-1c660aa69f00', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ConfigurationAdministration
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('97f0f870-a871-48de-a3e0-a32a95770f12', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ErrorReports
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('3a17959c-5dfc-43a2-9587-48a1eb95a22a', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ReportDefinitionAdministration
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('539fceb8-da82-4170-ab1a-ae6b04001c03', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ViewReport
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('180c84f9-9816-48d0-9762-dc753b2228b1', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ProcessDefinitionAdministration
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('d2854c65-9a59-40b8-9dc7-a882c64b2610', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.ViewProcess
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('4d60aed6-2d4b-4a91-a178-ac06d4b1769a', '100fafb4-783a-4204-a22d-9e27335dc2ea'); -- Application.SchedulerAdministration

INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('2a43152c-d8ae-4b08-8ad9-2448ec5debd5', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.SecureHome
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('f4e3b387-8cd1-4c56-a2da-fe39a78a56d9', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.Dashboard
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('567d7e55-f3d0-4191-bc4c-12d357900fa3', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.UserAdministration
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('7a54a71e-3680-4d49-b87d-29604a247413', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.UserGroups
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('029b9a06-0241-4a44-a234-5c489f2017ba', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.ResetUserPassword
INSERT INTO SECURITY.FUNCTION_TO_ROLE_MAP (FUNCTION_ID, ROLE_ID) VALUES ('539fceb8-da82-4170-ab1a-ae6b04001c03', '44ff0ad2-fbe1-489f-86c9-cef7f82acf35'); -- Application.ViewReport

INSERT INTO SECURITY.ROLE_TO_GROUP_MAP (ROLE_ID, GROUP_ID) VALUES ('100fafb4-783a-4204-a22d-9e27335dc2ea', 'a9e01fa2-f017-46e2-8187-424bf50a4f33');
INSERT INTO SECURITY.ROLE_TO_GROUP_MAP (ROLE_ID, GROUP_ID) VALUES ('44ff0ad2-fbe1-489f-86c9-cef7f82acf35', '758c0a2a-f3a3-4561-bebc-90569291976e');

INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('d21fb54e-5c5b-49e8-881f-ce00c6ced1a3', 'AuthenticateRequest');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('82223035-1726-407f-8703-3977708e792c', 'AuthenticateResponse');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('cc005e6a-b01b-48eb-98a0-026297be69f3', 'CheckUserExistsRequest');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('a38bd55e-3470-46f1-a96a-a6b08a9adc63', 'CheckUserExistsResponse');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('94d60eb6-a062-492d-b5e7-9fb1f05cf088', 'GetCodeCategoryRequest');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('0336b544-91e5-4eb9-81db-3dd94e116c92', 'GetCodeCategoryResponse');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('3500a28a-6a2c-482b-b81f-a849c9c3ef79', 'GetCodeCategoryWithParametersRequest');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('12757310-9eee-4a3a-970c-9b4ee0e1108e', 'GetCodeCategoryWithParametersResponse');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('a589dc87-2328-4a9b-bdb6-970e55ca2323', 'TestRequest');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('a3bad7ba-f9d4-4403-b54a-cb1f335ebbad', 'TestResponse');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('e9918051-8ebc-48f1-bad7-13c59b550e1a', 'AnotherTestRequest');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('a714a9c6-2914-4498-ab59-64be9991bf37', 'AnotherTestResponse');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('ff638c33-b4f1-4e79-804c-9560da2543d6', 'SubmitErrorReportRequest');
INSERT INTO MESSAGING.MESSAGE_TYPES (ID, NAME) VALUES ('8be50cfa-2fb1-4634-9bfa-d01e77eaf766', 'SubmitErrorReportResponse');

INSERT INTO MESSAGING.MESSAGE_STATUSES (CODE, NAME) VALUES (0, 'Initialised');
INSERT INTO MESSAGING.MESSAGE_STATUSES (CODE, NAME) VALUES (1, 'QueuedForSending');
INSERT INTO MESSAGING.MESSAGE_STATUSES (CODE, NAME) VALUES (2, 'QueuedForProcessing');
INSERT INTO MESSAGING.MESSAGE_STATUSES (CODE, NAME) VALUES (3, 'Aborted');
INSERT INTO MESSAGING.MESSAGE_STATUSES (CODE, NAME) VALUES (4, 'Failed');
INSERT INTO MESSAGING.MESSAGE_STATUSES (CODE, NAME) VALUES (5, 'Processing');
INSERT INTO MESSAGING.MESSAGE_STATUSES (CODE, NAME) VALUES (6, 'Sending');
INSERT INTO MESSAGING.MESSAGE_STATUSES (CODE, NAME) VALUES (7, 'QueuedForDownload');
INSERT INTO MESSAGING.MESSAGE_STATUSES (CODE, NAME) VALUES (8, 'Downloading');
INSERT INTO MESSAGING.MESSAGE_STATUSES (CODE, NAME) VALUES (10, 'Processed');

INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (1, 'Sample Name 1', 'Sample Value 1');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (2, 'Sample Name 2', 'Sample Value 2');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (3, 'Sample Name 3', 'Sample Value 3');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (4, 'Sample Name 4', 'Sample Value 4');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (5, 'Sample Name 5', 'Sample Value 5');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (6, 'Sample Name 6', 'Sample Value 6');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (7, 'Sample Name 7', 'Sample Value 7');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (8, 'Sample Name 8', 'Sample Value 8');
INSERT INTO TEST.TEST_DATA (ID, NAME, VALUE) VALUES (9, 'Sample Name 9', 'Sample Value 9');

INSERT INTO SECURITY.ORGANISATIONS (ID, NAME, STATUS) VALUES
  ('204e5b8f-48e7-4354-bd15-753e6543b64d', 'Sample', 1);

INSERT INTO SECURITY.USER_DIRECTORIES (ID, TYPE_ID, NAME, CONFIGURATION) VALUES
  ('34ccdbc9-4a01-46f5-a284-ba13e095675c', 'b43fda33-d3b0-4f80-a39a-110b8e530f4f', 'Sample Internal User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter></userDirectory>');

--INSERT INTO MMP.USER_DIRECTORIES (ID, TYPE_ID, NAME, CONFIGURATION) VALUES
--  ('595d13ac-22d6-4ce2-b898-3add4658a748', 'e5741a89-c87b-4406-8a60-2cc0b0a5fa3e', 'Sample LDAP User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>Host</name><value>sds.mmp.guru</value></parameter><parameter><name>Port</name><value>389</value></parameter><parameter><name>UseSSL</name><value>false</value></parameter><parameter><name>BindDN</name><value>uid=system,ou=users,ou=test,ou=applications,o=MMP</value></parameter><parameter><name>BindPassword</name><value>Password1</value></parameter><parameter><name>BaseDN</name><value>ou=test,ou=applications,o=MMP</value></parameter><parameter><name>UserBaseDN</name><value>ou=users,ou=test,ou=applications,o=MMP</value></parameter><parameter><name>GroupBaseDN</name><value>ou=groups,ou=test,ou=applications,o=MMP</value></parameter><parameter><name>SharedBaseDN</name><value>ou=staff,o=MMP</value></parameter><parameter><name>UserObjectClass</name><value>inetOrgPerson</value></parameter><parameter><name>UserUsernameAttribute</name><value>uid</value></parameter><parameter><name>UserPasswordExpiryAttribute</name><value>passwordexpiry</value></parameter><parameter><name>UserPasswordAttemptsAttribute</name><value>passwordattempts</value></parameter><parameter><name>UserPasswordHistoryAttribute</name><value>passwordhistory</value></parameter><parameter><name>UserFirstNameAttribute</name><value>givenName</value></parameter><parameter><name>UserLastNameAttribute</name><value>sn</value></parameter><parameter><name>UserPhoneNumberAttribute</name><value>telephoneNumber</value></parameter><parameter><name>UserFaxNumberAttribute</name><value>facsimileTelephoneNumber</value></parameter><parameter><name>UserMobileNumberAttribute</name><value>mobile</value></parameter><parameter><name>UserEmailAttribute</name><value>mail</value></parameter><parameter><name>UserDescriptionAttribute</name><value>cn</value></parameter><parameter><name>GroupObjectClass</name><value>groupOfNames</value></parameter><parameter><name>GroupNameAttribute</name><value>cn</value></parameter><parameter><name>GroupMemberAttribute</name><value>member</value></parameter><parameter><name>GroupDescriptionAttribute</name><value>description</value></parameter><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>SupportPasswordHistory</name><value>true</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>PasswordHistoryMaxLength</name><value>128</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter><parameter><name>MaxFilteredGroups</name><value>100</value></parameter></userDirectory>');

INSERT INTO SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP (USER_DIRECTORY_ID, ORGANISATION_ID) VALUES
  ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', '204e5b8f-48e7-4354-bd15-753e6543b64d');
INSERT INTO SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP (USER_DIRECTORY_ID, ORGANISATION_ID) VALUES
  ('34ccdbc9-4a01-46f5-a284-ba13e095675c', '204e5b8f-48e7-4354-bd15-753e6543b64d');
--INSERT INTO SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP (USER_DIRECTORY_ID, ORGANISATION_ID) VALUES
--  ('595d13ac-22d6-4ce2-b898-3add4658a748', '204e5b8f-48e7-4354-bd15-753e6543b64d');

INSERT INTO SECURITY.INTERNAL_USERS (ID, USER_DIRECTORY_ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, PHONE, MOBILE, EMAIL, PASSWORD_ATTEMPTS, PASSWORD_EXPIRY) VALUES
  ('54166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'sample', 'GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=', '', '', '', '', '', null, null);

INSERT INTO SECURITY.INTERNAL_GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME, DESCRIPTION) VALUES
  ('956c5550-cd3d-42de-8660-7749e1b4df52', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'Organisation Administrators', 'Organisation Administrators');

INSERT INTO SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP (INTERNAL_USER_ID, INTERNAL_GROUP_ID) VALUES
  ('54166574-6564-468a-b845-8a5c127a4345', '956c5550-cd3d-42de-8660-7749e1b4df52');

INSERT INTO SECURITY.GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME) VALUES ('956c5550-cd3d-42de-8660-7749e1b4df52', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'Organisation Administrators');

INSERT INTO SECURITY.ROLE_TO_GROUP_MAP (ROLE_ID, GROUP_ID) VALUES ('44ff0ad2-fbe1-489f-86c9-cef7f82acf35', '956c5550-cd3d-42de-8660-7749e1b4df52');

INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (1, 'Sample Name 1', 'Sample Value 1');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (2, 'Sample Name 2', 'Sample Value 2');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (3, 'Sample Name 3', 'Sample Value 3');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (4, 'Sample Name 4', 'Sample Value 4');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (5, 'Sample Name 5', 'Sample Value 5');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (6, 'Sample Name 6', 'Sample Value 6');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (7, 'Sample Name 7', 'Sample Value 7');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (8, 'Sample Name 8', 'Sample Value 8');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (9, 'Sample Name 9', 'Sample Value 9');

INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME,) VALUES
  ('d7d32bbd-ef44-4e5e-8c8f-8108ea2ab53a', 0, 'Sample Code Category 1');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME,) VALUES
  ('414586a2-836a-42b3-ad09-be23448c6cdb', 0, 'Sample Code Category 2');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('7b48fc44-03d3-467b-bde3-44f99a0b5824', 0, 'Sample Code Category 3');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('ebe02fae-0305-4965-9727-08bce55762de', 0, 'Sample Code Category 4');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('afc0fc9b-357b-4da9-83ad-3f36eaaf7aa9', 0, 'Sample Code Category 5');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('d94a549d-311e-4078-9689-392c9e4091ab', 0, 'Sample Code Category 6');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('5c1cf103-3e51-4942-b2b7-934f4ef1a712', 0, 'Sample Code Category 7');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('3c9de85c-27e8-463c-8d7a-571310de9429', 0, 'Sample Code Category 8');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('df82e5aa-b3ff-4bcf-8c14-57db2ee8576a', 0, 'Sample Code Category 9');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('28ee0d70-a173-43dd-9f4f-2bec53981ba6', 0, 'Sample Code Category 10');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('bdea98c2-456e-4a61-9371-88b6e09715c5', 0, 'Sample Code Category 11');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('54895a59-cc05-4549-a250-06bf615aff75', 0, 'Sample Code Category 12');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('7253a209-92cf-4ff3-b64b-bcb3dfebcffc', 0, 'Sample Code Category 13');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('8a901a99-bd8e-454e-ab30-332f701bd358', 0, 'Sample Code Category 14');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('18ad1667-6a43-4118-a31f-cfc0b472d456', 0, 'Sample Code Category 15');

INSERT INTO SCHEDULER.JOBS (ID, NAME, SCHEDULING_PATTERN, JOB_CLASS, IS_ENABLED, STATUS) VALUES
  ('ea26c191-e2ee-4481-aa56-1f03c987d24f', 'Sample Job', '30 * * * *', 'guru.mmp.sample.jobs.SampleJob', 1, 0);


