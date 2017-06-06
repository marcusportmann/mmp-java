-- -------------------------------------------------------------------------------------------------
-- NOTE: When changing this file you may also need to modify the following file:
--       - ApplicationPostgres.sql (mmp-application)
--
--  Execute the following command to start the database server if it is not running:
--
--    OS X: pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/postgres.log start
--    CentOS (as root): service postgresql-9.6 start
--
--  Execute the following command to create the database:
--
--    OS X: createdb  --template=template0 --encoding=UTF8 sample
--    CentOS (as root): sudo su postgres -c 'createdb --template=template1 --encoding=UTF8 sample'
--
--  Execute the following command to initialise the database:
--
--    OS X: psql -d sample -f ApplicationPostgres.sql 
--    CentOS (as root): su postgres -c 'psql -d sample -f ApplicationPostgres.sql'
--
--  Execute the following command to delete the database:
--
--    OS X: dropdb sample
--    CentOS (as root): su postgres -c 'dropdb sample'
--
--  Execute the following command to clean-up unreferenced large objects on the database:
--
--    OS X: vacuumlo sample
--    CentOS (as root): su postgres -c 'vacuumlo sample'
--
-- -------------------------------------------------------------------------------------------------
set client_min_messages='warning';

-- -------------------------------------------------------------------------------------------------
-- DROP TABLES
-- -------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS SAMPLE.DATA CASCADE;
DROP TABLE IF EXISTS SMS.SMS CASCADE;
DROP TABLE IF EXISTS REPORTING.REPORT_DEFINITIONS CASCADE;
DROP TABLE IF EXISTS MESSAGING.ERROR_REPORTS CASCADE;
DROP TABLE IF EXISTS MESSAGING.MESSAGE_STATUSES CASCADE;
DROP TABLE IF EXISTS MESSAGING.MESSAGE_TYPES CASCADE;
DROP TABLE IF EXISTS MESSAGING.ARCHIVED_MESSAGES CASCADE;
DROP TABLE IF EXISTS MESSAGING.MESSAGE_PARTS CASCADE;
DROP TABLE IF EXISTS MESSAGING.MESSAGES CASCADE;
DROP TABLE IF EXISTS CODES.CACHED_CODES CASCADE;
DROP TABLE IF EXISTS CODES.CACHED_CODE_CATEGORIES CASCADE;
DROP TABLE IF EXISTS CODES.CODES CASCADE;
DROP TABLE IF EXISTS CODES.CODE_CATEGORIES CASCADE;
DROP TABLE IF EXISTS SCHEDULER.JOB_PARAMETERS CASCADE;
DROP TABLE IF EXISTS SCHEDULER.JOBS CASCADE;
DROP TABLE IF EXISTS SECURITY.ROLE_TO_GROUP_MAP CASCADE;
DROP TABLE IF EXISTS SECURITY.FUNCTION_TO_ROLE_MAP CASCADE;
DROP TABLE IF EXISTS SECURITY.ROLES CASCADE;
DROP TABLE IF EXISTS SECURITY.FUNCTIONS CASCADE;
DROP TABLE IF EXISTS SECURITY.GROUPS CASCADE;
DROP TABLE IF EXISTS SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP CASCADE;
DROP TABLE IF EXISTS SECURITY.INTERNAL_GROUPS CASCADE;
DROP TABLE IF EXISTS SECURITY.INTERNAL_USERS_PASSWORD_HISTORY CASCADE;
DROP TABLE IF EXISTS SECURITY.INTERNAL_USERS CASCADE;
DROP TABLE IF EXISTS SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP CASCADE;
DROP TABLE IF EXISTS SECURITY.USER_DIRECTORIES CASCADE;
DROP TABLE IF EXISTS SECURITY.USER_DIRECTORY_TYPES CASCADE;
DROP TABLE IF EXISTS SECURITY.ORGANISATIONS CASCADE;
DROP TABLE IF EXISTS SERVICE_REGISTRY.SERVICE_REGISTRY CASCADE;
DROP TABLE IF EXISTS CONFIGURATION.CONFIG CASCADE;
DROP TABLE IF EXISTS IDGENERATOR.IDGENERATOR CASCADE;
DROP TABLE IF EXISTS TEST.TEST_DATA CASCADE;



-- -------------------------------------------------------------------------------------------------
-- DROP SCHEMAS
-- -------------------------------------------------------------------------------------------------
DROP SCHEMA IF EXISTS SAMPLE CASCADE;
DROP SCHEMA IF EXISTS TEST CASCADE;
DROP SCHEMA IF EXISTS SMS CASCADE;
DROP SCHEMA IF EXISTS SERVICE_REGISTRY CASCADE;
DROP SCHEMA IF EXISTS SECURITY CASCADE;
DROP SCHEMA IF EXISTS SCHEDULER CASCADE;
DROP SCHEMA IF EXISTS REPORTING CASCADE;
DROP SCHEMA IF EXISTS MESSAGING CASCADE;
DROP SCHEMA IF EXISTS IDGENERATOR CASCADE;
DROP SCHEMA IF EXISTS CONFIGURATION CASCADE;
DROP SCHEMA IF EXISTS CODES CASCADE;



-- -------------------------------------------------------------------------------------------------
-- DROP ROLES
-- -------------------------------------------------------------------------------------------------
DROP OWNED BY sample CASCADE;
DROP ROLE IF EXISTS sample;



-- -------------------------------------------------------------------------------------------------
-- CREATE ROLES
-- -------------------------------------------------------------------------------------------------
CREATE ROLE sample WITH LOGIN PASSWORD 'Password1';
ALTER ROLE sample WITH LOGIN;



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
CREATE SCHEMA CODES;
CREATE SCHEMA CONFIGURATION;
CREATE SCHEMA IDGENERATOR;
CREATE SCHEMA MESSAGING;
CREATE SCHEMA REPORTING;
CREATE SCHEMA SCHEDULER;
CREATE SCHEMA SECURITY;
CREATE SCHEMA SERVICE_REGISTRY;
CREATE SCHEMA SMS;
CREATE SCHEMA TEST;
CREATE SCHEMA SAMPLE;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE CODES.CODE_CATEGORIES (
  ID                  UUID NOT NULL,
  CATEGORY_TYPE       INTEGER NOT NULL,
  NAME                TEXT NOT NULL,
  CODE_DATA           BYTEA,
  ENDPOINT            TEXT,
  IS_ENDPOINT_SECURE  BOOLEAN NOT NULL DEFAULT FALSE,
  IS_CACHEABLE        BOOLEAN,
  CACHE_EXPIRY        INTEGER,
  UPDATED             TIMESTAMP,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN CODES.CODE_CATEGORIES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the code category';

COMMENT ON COLUMN CODES.CODE_CATEGORIES.CATEGORY_TYPE
  IS 'The type of code category e.g. Local, RemoteHTTPService, RemoteWebService, etc';

COMMENT ON COLUMN CODES.CODE_CATEGORIES.NAME
  IS 'The name of the code category';

COMMENT ON COLUMN CODES.CODE_CATEGORIES.CODE_DATA
  IS 'The custom code data for the code category';

COMMENT ON COLUMN CODES.CODE_CATEGORIES.ENDPOINT
  IS 'The endpoint if this is a remote code category';

COMMENT ON COLUMN CODES.CODE_CATEGORIES.IS_ENDPOINT_SECURE
  IS 'Is the endpoint for the remote code category secure';

COMMENT ON COLUMN CODES.CODE_CATEGORIES.IS_CACHEABLE
  IS 'Is the code data retrieved for the remote code category cacheable';

COMMENT ON COLUMN CODES.CODE_CATEGORIES.CACHE_EXPIRY
  IS 'The time in seconds after which the cached code data for the remote code category will expire';

COMMENT ON COLUMN CODES.CODE_CATEGORIES.UPDATED
  IS 'The date and time the code category was updated';



CREATE TABLE CODES.CODES (
  ID           TEXT NOT NULL,
  CATEGORY_ID  UUID NOT NULL,
  NAME         TEXT NOT NULL,
  VALUE        TEXT NOT NULL,

  PRIMARY KEY (ID, CATEGORY_ID),
  CONSTRAINT CODES_CODE_CATEGORY_FK FOREIGN KEY (CATEGORY_ID) REFERENCES CODES.CODE_CATEGORIES(ID) ON DELETE CASCADE
);

CREATE INDEX CODES_CATEGORY_ID_IX
  ON CODES.CODES
  (CATEGORY_ID);

COMMENT ON COLUMN CODES.CODES.ID
  IS 'The ID used to uniquely identify the code';

COMMENT ON COLUMN CODES.CODES.CATEGORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the code category the code is associated with';

COMMENT ON COLUMN CODES.CODES.NAME
  IS 'The name of the code';

COMMENT ON COLUMN CODES.CODES.VALUE
  IS 'The value for the code';



CREATE TABLE CODES.CACHED_CODE_CATEGORIES (
  ID            UUID NOT NULL,
  CODE_DATA     BYTEA,
  LAST_UPDATED  TIMESTAMP NOT NULL,
  CACHED        TIMESTAMP NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT CACHED_CODE_CATEGORIES_CATEGORY_FK FOREIGN KEY (ID) REFERENCES CODES.CODE_CATEGORIES(ID) ON DELETE CASCADE
);

COMMENT ON COLUMN CODES.CACHED_CODE_CATEGORIES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the cached code category';

COMMENT ON COLUMN CODES.CACHED_CODE_CATEGORIES.CODE_DATA
  IS 'The custom code data for the cached code category';

COMMENT ON COLUMN CODES.CACHED_CODE_CATEGORIES.LAST_UPDATED
  IS 'The date and time the cached code category was last updated';

COMMENT ON COLUMN CODES.CACHED_CODE_CATEGORIES.CACHED
  IS 'The date and time the code category was cached';



CREATE TABLE CODES.CACHED_CODES (
  ID           TEXT NOT NULL,
  CATEGORY_ID  UUID NOT NULL,
  NAME         TEXT NOT NULL,
  VALUE        TEXT NOT NULL,

  PRIMARY KEY (ID, CATEGORY_ID),
  CONSTRAINT CACHED_CODES_CACHED_CODE_CATEGORY_FK FOREIGN KEY (CATEGORY_ID) REFERENCES CODES.CACHED_CODE_CATEGORIES(ID) ON DELETE CASCADE
);

CREATE INDEX CACHED_CODES_CATEGORY_ID_IX
  ON CODES.CACHED_CODES
  (CATEGORY_ID);

COMMENT ON COLUMN CODES.CACHED_CODES.ID
  IS 'The ID used to uniquely identify the code';

COMMENT ON COLUMN CODES.CACHED_CODES.CATEGORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the code category the code is associated with';

COMMENT ON COLUMN CODES.CACHED_CODES.NAME
  IS 'The name of the code';

COMMENT ON COLUMN CODES.CACHED_CODES.VALUE
  IS 'The value for the code';  



CREATE TABLE CONFIGURATION.CONFIGURATION (
  KEY          TEXT NOT NULL,
  VALUE        TEXT NOT NULL,
  DESCRIPTION  TEXT NOT NULL,

  PRIMARY KEY (KEY)
);

COMMENT ON COLUMN CONFIGURATION.CONFIGURATION.KEY
  IS 'The key used to uniquely identify the configuration value';

COMMENT ON COLUMN CONFIGURATION.CONFIGURATION.VALUE
  IS 'The value for the configuration value';

COMMENT ON COLUMN CONFIGURATION.CONFIGURATION.DESCRIPTION
  IS 'The description for the configuration value';



CREATE TABLE IDGENERATOR.IDGENERATOR (
  NAME     TEXT NOT NULL,
  CURRENT  BIGINT DEFAULT 0,

  PRIMARY KEY (NAME)
);

COMMENT ON COLUMN IDGENERATOR.IDGENERATOR.NAME
  IS 'The name giving the type of entity associated with the generated ID';

COMMENT ON COLUMN IDGENERATOR.IDGENERATOR.CURRENT
  IS 'The current ID for the type';

  
  
CREATE TABLE MESSAGING.MESSAGE_TYPES (
  ID    UUID NOT NULL,
  NAME  TEXT NOT NULL,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN MESSAGING.MESSAGE_TYPES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message type';

COMMENT ON COLUMN MESSAGING.MESSAGE_TYPES.NAME
  IS 'The name of the message type';



CREATE TABLE MESSAGING.MESSAGE_STATUSES (
  CODE  INTEGER NOT NULL,
  NAME  TEXT NOT NULL,

  PRIMARY KEY (CODE)
);

COMMENT ON COLUMN MESSAGING.MESSAGE_STATUSES.CODE
  IS 'The code identifying the message status';

COMMENT ON COLUMN MESSAGING.MESSAGE_STATUSES.NAME
  IS 'The name of the message status';



CREATE TABLE MESSAGING.MESSAGES (
  ID                 UUID NOT NULL,
  USERNAME           TEXT NOT NULL,
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
  LOCK_NAME          TEXT,
  LAST_PROCESSED     TIMESTAMP,
  DATA               BYTEA,

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

COMMENT ON COLUMN MESSAGING.MESSAGES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message';

COMMENT ON COLUMN MESSAGING.MESSAGES.USERNAME
  IS 'The username identifying the user associated with the message';

COMMENT ON COLUMN MESSAGING.MESSAGES.DEVICE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the message originated from';

COMMENT ON COLUMN MESSAGING.MESSAGES.TYPE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of message';

COMMENT ON COLUMN MESSAGING.MESSAGES.CORRELATION_ID
  IS 'The Universally Unique Identifier (UUID) used to correlate the message';

COMMENT ON COLUMN MESSAGING.MESSAGES.PRIORITY
  IS 'The message priority';

COMMENT ON COLUMN MESSAGING.MESSAGES.STATUS
  IS 'The message status e.g. Initialised, QueuedForSending, etc';

COMMENT ON COLUMN MESSAGING.MESSAGES.CREATED
  IS 'The date and time the message was created';

COMMENT ON COLUMN MESSAGING.MESSAGES.PERSISTED
  IS 'The date and time the message was persisted';

COMMENT ON COLUMN MESSAGING.MESSAGES.UPDATED
  IS 'The date and time the message was last updated';

COMMENT ON COLUMN MESSAGING.MESSAGES.SEND_ATTEMPTS
  IS 'The number of times that the sending of the message was attempted';

COMMENT ON COLUMN MESSAGING.MESSAGES.PROCESS_ATTEMPTS
  IS 'The number of times that the processing of the message was attempted';

COMMENT ON COLUMN MESSAGING.MESSAGES.DOWNLOAD_ATTEMPTS
  IS 'The number of times that an attempt was made to download the message';

COMMENT ON COLUMN MESSAGING.MESSAGES.LOCK_NAME
  IS 'The name of the entity that has locked the message for processing';

COMMENT ON COLUMN MESSAGING.MESSAGES.LAST_PROCESSED
  IS 'The date and time the last attempt was made to process the message';

COMMENT ON COLUMN MESSAGING.MESSAGES.DATA
  IS 'The data for the message';



CREATE TABLE MESSAGING.MESSAGE_PARTS (
  ID                   UUID NOT NULL,
  PART_NO              INTEGER NOT NULL,
  TOTAL_PARTS          INTEGER NOT NULL,
  SEND_ATTEMPTS        INTEGER NOT NULL,
  DOWNLOAD_ATTEMPTS    INTEGER NOT NULL,
  STATUS               INTEGER NOT NULL,
  PERSISTED            TIMESTAMP NOT NULL,
  UPDATED              TIMESTAMP,
  MSG_ID               UUID NOT NULL,
  MSG_USERNAME         TEXT NOT NULL,
  MSG_DEVICE_ID        UUID NOT NULL,
  MSG_TYPE_ID          UUID NOT NULL,
  MSG_CORRELATION_ID   UUID NOT NULL,
  MSG_PRIORITY         INTEGER NOT NULL,
  MSG_CREATED          TIMESTAMP NOT NULL,
  MSG_DATA_HASH        TEXT,
  MSG_ENCRYPTION_IV    TEXT NOT NULL,
  MSG_CHECKSUM         TEXT NOT NULL,
  LOCK_NAME            TEXT,
  DATA                 BYTEA,

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

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message part';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.PART_NO
  IS 'The number of the message part in the set of message parts for the original message';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.TOTAL_PARTS
  IS 'The total number of parts in the set of message parts for the original message';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.SEND_ATTEMPTS
  IS 'The number of times that the sending of the message part was attempted';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.DOWNLOAD_ATTEMPTS
  IS 'The number of times that an attempt was made to download the message part';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.STATUS
  IS 'The message part status e.g. Initialised, QueuedForSending, etc';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.PERSISTED
  IS 'The date and time the message part was persisted';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.UPDATED
  IS 'The date and time the message part was last updated';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.MSG_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the original message';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.MSG_USERNAME
  IS 'The username identifying the user associated with the original message';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.MSG_DEVICE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the original message originated from';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.MSG_TYPE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of the original message';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.MSG_CORRELATION_ID
  IS 'The Universally Unique Identifier (UUID) used to correlate the original message';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.MSG_PRIORITY
  IS 'The priority for the original message';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.MSG_CREATED
  IS 'The date and time the original message was created';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.MSG_DATA_HASH
  IS 'The hash of the unencrypted data for the original message';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.MSG_ENCRYPTION_IV
  IS 'The base-64 encoded initialisation vector for the encryption scheme for the original message';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.MSG_CHECKSUM
  IS 'The checksum for the original message';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.LOCK_NAME
  IS 'The name of the entity that has locked the message part for processing';

COMMENT ON COLUMN MESSAGING.MESSAGE_PARTS.DATA
  IS 'The data for the message part';



CREATE TABLE MESSAGING.ARCHIVED_MESSAGES (
  ID               UUID NOT NULL,
  USERNAME         TEXT NOT NULL,
  DEVICE_ID        UUID NOT NULL,
  TYPE_ID          UUID NOT NULL,
  CORRELATION_ID   UUID NOT NULL,
  CREATED          TIMESTAMP NOT NULL,
  ARCHIVED         TIMESTAMP NOT NULL,
  DATA             BYTEA,

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

COMMENT ON COLUMN MESSAGING.ARCHIVED_MESSAGES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message';

COMMENT ON COLUMN MESSAGING.ARCHIVED_MESSAGES.USERNAME
  IS 'The username identifying the user associated with the message';

COMMENT ON COLUMN MESSAGING.ARCHIVED_MESSAGES.DEVICE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the message originated from';

COMMENT ON COLUMN MESSAGING.ARCHIVED_MESSAGES.TYPE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of message';

COMMENT ON COLUMN MESSAGING.ARCHIVED_MESSAGES.CORRELATION_ID
  IS 'The Universally Unique Identifier (UUID) used to correlate the message';

COMMENT ON COLUMN MESSAGING.ARCHIVED_MESSAGES.CREATED
  IS 'The date and time the message was created';

COMMENT ON COLUMN MESSAGING.ARCHIVED_MESSAGES.ARCHIVED
  IS 'The date and time the message was archived';

COMMENT ON COLUMN MESSAGING.ARCHIVED_MESSAGES.DATA
  IS 'The data for the message';



CREATE TABLE MESSAGING.ERROR_REPORTS (
  ID                   UUID NOT NULL,
  APPLICATION_ID       UUID NOT NULL,
  APPLICATION_VERSION  INTEGER NOT NULL,
  DESCRIPTION          TEXT NOT NULL,
  DETAIL               TEXT NOT NULL,
  FEEDBACK             TEXT NOT NULL,
  CREATED              TIMESTAMP NOT NULL,
  WHO                  TEXT NOT NULL,
  DEVICE_ID            UUID NOT NULL,
  DATA                 BYTEA,

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

COMMENT ON COLUMN MESSAGING.ERROR_REPORTS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the error report';

COMMENT ON COLUMN MESSAGING.ERROR_REPORTS.APPLICATION_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the application that generated the error report';

COMMENT ON COLUMN MESSAGING.ERROR_REPORTS.APPLICATION_VERSION
  IS 'The version of the application that generated the error report';

COMMENT ON COLUMN MESSAGING.ERROR_REPORTS.DESCRIPTION
  IS 'The description of the error';

COMMENT ON COLUMN MESSAGING.ERROR_REPORTS.DETAIL
  IS 'The error detail e.g. a stack trace';

COMMENT ON COLUMN MESSAGING.ERROR_REPORTS.FEEDBACK
  IS 'The feedback provided by the user for the error';

COMMENT ON COLUMN MESSAGING.ERROR_REPORTS.CREATED
  IS 'The date and time the error report was created';

COMMENT ON COLUMN MESSAGING.ERROR_REPORTS.WHO
  IS 'The username identifying the user associated with the error report';

COMMENT ON COLUMN MESSAGING.ERROR_REPORTS.DEVICE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the error report originated from';

COMMENT ON COLUMN MESSAGING.ERROR_REPORTS.DATA
  IS 'The data associated with the error report';



CREATE TABLE REPORTING.REPORT_DEFINITIONS (
  ID        UUID NOT NULL,
  NAME      TEXT NOT NULL,
  TEMPLATE  BYTEA NOT NULL,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN REPORTING.REPORT_DEFINITIONS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the report definition';

COMMENT ON COLUMN REPORTING.REPORT_DEFINITIONS.NAME
  IS 'The name of the report definition';

COMMENT ON COLUMN REPORTING.REPORT_DEFINITIONS.TEMPLATE
  IS 'The JasperReports template for the report definition';



CREATE TABLE SCHEDULER.JOBS (
  ID                  UUID NOT NULL,
  NAME                TEXT NOT NULL,
  SCHEDULING_PATTERN  TEXT NOT NULL,
  JOB_CLASS           TEXT NOT NULL,
  IS_ENABLED          BOOLEAN NOT NULL,
  STATUS              INTEGER NOT NULL DEFAULT 1,
  EXECUTION_ATTEMPTS  INTEGER NOT NULL DEFAULT 0,
  LOCK_NAME           TEXT,
  LAST_EXECUTED       TIMESTAMP,
  NEXT_EXECUTION      TIMESTAMP,
  UPDATED             TIMESTAMP,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN SCHEDULER.JOBS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the job';

COMMENT ON COLUMN SCHEDULER.JOBS.NAME
  IS 'The name of the job';

COMMENT ON COLUMN SCHEDULER.JOBS.SCHEDULING_PATTERN
  IS 'The cron-style scheduling pattern for the job';

COMMENT ON COLUMN SCHEDULER.JOBS.JOB_CLASS
  IS 'The fully qualified name of the Java class that implements the job';

COMMENT ON COLUMN SCHEDULER.JOBS.IS_ENABLED
  IS 'Is the job enabled for execution';

COMMENT ON COLUMN SCHEDULER.JOBS.STATUS
  IS 'The status of the job';

COMMENT ON COLUMN SCHEDULER.JOBS.EXECUTION_ATTEMPTS
  IS 'The number of times the current execution of the job has been attempted';

COMMENT ON COLUMN SCHEDULER.JOBS.LOCK_NAME
  IS 'The name of the entity that has locked the job for execution';

COMMENT ON COLUMN SCHEDULER.JOBS.LAST_EXECUTED
  IS 'The date and time the job was last executed';

COMMENT ON COLUMN SCHEDULER.JOBS.NEXT_EXECUTION
  IS 'The date and time when the job will next be executed';

COMMENT ON COLUMN SCHEDULER.JOBS.UPDATED
  IS 'The date and time the job was updated';



CREATE TABLE SCHEDULER.JOB_PARAMETERS (
  ID      UUID NOT NULL,
  JOB_ID  UUID NOT NULL,
  NAME    TEXT NOT NULL,
  VALUE   TEXT NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT JOB_PARAMETERS_JOB_FK FOREIGN KEY (JOB_ID) REFERENCES SCHEDULER.JOBS(ID) ON DELETE CASCADE
);

CREATE INDEX JOB_PARAMETERS_JOB_ID_IX
  ON SCHEDULER.JOB_PARAMETERS
  (JOB_ID);

CREATE INDEX JOB_PARAMETERS_NAME_IX
  ON SCHEDULER.JOB_PARAMETERS
  (NAME);

COMMENT ON COLUMN SCHEDULER.JOB_PARAMETERS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the job parameter';

COMMENT ON COLUMN SCHEDULER.JOB_PARAMETERS.JOB_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the job';

COMMENT ON COLUMN SCHEDULER.JOB_PARAMETERS.NAME
  IS 'The name of the job parameter';

COMMENT ON COLUMN SCHEDULER.JOB_PARAMETERS.VALUE
  IS 'The value of the job parameter';



CREATE TABLE SECURITY.ORGANISATIONS (
  ID      UUID NOT NULL,
  NAME    TEXT NOT NULL,
  STATUS  INTEGER NOT NULL,

  PRIMARY KEY (ID)
);

CREATE INDEX ORGANISATIONS_NAME_IX
  ON SECURITY.ORGANISATIONS
  (NAME);

COMMENT ON COLUMN SECURITY.ORGANISATIONS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the organisation';

COMMENT ON COLUMN SECURITY.ORGANISATIONS.NAME
  IS 'The name of the organisation';

COMMENT ON COLUMN SECURITY.ORGANISATIONS.STATUS
  IS 'The status for the organisation';



CREATE TABLE SECURITY.USER_DIRECTORY_TYPES (
  ID                    UUID NOT NULL,
  NAME                  TEXT NOT NULL,
  USER_DIRECTORY_CLASS  TEXT NOT NULL,
  ADMINISTRATION_CLASS  TEXT NOT NULL,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN SECURITY.USER_DIRECTORY_TYPES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory type';

COMMENT ON COLUMN SECURITY.USER_DIRECTORY_TYPES.NAME
  IS 'The name of the user directory type';

COMMENT ON COLUMN SECURITY.USER_DIRECTORY_TYPES.USER_DIRECTORY_CLASS
  IS 'The fully qualified name of the Java class that implements the user directory type';

COMMENT ON COLUMN SECURITY.USER_DIRECTORY_TYPES.ADMINISTRATION_CLASS
  IS 'The fully qualified name of the Java class that implements the Wicket component used to administer the configuration for the user directory type';



CREATE TABLE SECURITY.USER_DIRECTORIES (
  ID             UUID NOT NULL,
  TYPE_ID        UUID NOT NULL,
  NAME           TEXT NOT NULL,
  CONFIGURATION  TEXT NOT NULL,

  PRIMARY KEY (ID),
  CONSTRAINT USER_DIRECTORIES_USER_DIRECTORY_TYPE_FK FOREIGN KEY (TYPE_ID) REFERENCES SECURITY.USER_DIRECTORY_TYPES(ID) ON DELETE CASCADE
);

CREATE INDEX USER_DIRECTORIES_NAME_IX
  ON SECURITY.USER_DIRECTORIES
  (NAME);

COMMENT ON COLUMN SECURITY.USER_DIRECTORIES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory';

COMMENT ON COLUMN SECURITY.USER_DIRECTORIES.TYPE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory type';

COMMENT ON COLUMN SECURITY.USER_DIRECTORIES.NAME
  IS 'The name of the user directory';

COMMENT ON COLUMN SECURITY.USER_DIRECTORIES.CONFIGURATION
  IS 'The XML configuration data for the user directory';



CREATE TABLE SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP (
  USER_DIRECTORY_ID  UUID NOT NULL,
  ORGANISATION_ID    UUID NOT NULL,

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

COMMENT ON COLUMN SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP.USER_DIRECTORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory';

COMMENT ON COLUMN SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP.ORGANISATION_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the organisation';



CREATE TABLE SECURITY.INTERNAL_USERS (
  ID                 UUID NOT NULL,
  USER_DIRECTORY_ID  UUID NOT NULL,
  USERNAME           TEXT NOT NULL,
  PASSWORD           TEXT,
  FIRST_NAME         TEXT,
  LAST_NAME          TEXT,
  PHONE              TEXT,
  MOBILE             TEXT,
  EMAIL              TEXT,
  PASSWORD_ATTEMPTS  INTEGER,
  PASSWORD_EXPIRY    TIMESTAMP,

  PRIMARY KEY (ID),
  CONSTRAINT INTERNAL_USERS_USER_DIRECTORY_FK FOREIGN KEY (USER_DIRECTORY_ID) REFERENCES SECURITY.USER_DIRECTORIES(ID) ON DELETE CASCADE
);

CREATE INDEX INTERNAL_USERS_USER_DIRECTORY_ID_IX
  ON SECURITY.INTERNAL_USERS
  (USER_DIRECTORY_ID);

CREATE UNIQUE INDEX INTERNAL_USERS_USERNAME_IX
  ON SECURITY.INTERNAL_USERS
  (USERNAME);

COMMENT ON COLUMN SECURITY.INTERNAL_USERS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS.USER_DIRECTORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the internal user is associated with';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS.USERNAME
  IS 'The username for the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS.PASSWORD
  IS 'The password for the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS.FIRST_NAME
  IS 'The first name for the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS.LAST_NAME
  IS 'The last name for the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS.PHONE
  IS 'The phone number for the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS.MOBILE
  IS 'The mobile number for the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS.EMAIL
  IS 'The e-mail address for the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS.PASSWORD_ATTEMPTS
  IS 'The number of failed attempts to authenticate the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS.PASSWORD_EXPIRY
  IS 'The date and time that the internal user''s password expires';



CREATE TABLE SECURITY.INTERNAL_USERS_PASSWORD_HISTORY (
  ID                UUID NOT NULL,
  INTERNAL_USER_ID  UUID NOT NULL,
  CHANGED           TIMESTAMP NOT NULL,
  PASSWORD          TEXT,

  PRIMARY KEY (ID),
  CONSTRAINT INTERNAL_USERS_PASSWORD_HISTORY_INTERNAL_USER_ID_FK FOREIGN KEY (INTERNAL_USER_ID) REFERENCES SECURITY.INTERNAL_USERS(ID) ON DELETE CASCADE
);

CREATE INDEX INTERNAL_USERS_PASSWORD_HISTORY_INTERNAL_USER_ID_IX
  ON SECURITY.INTERNAL_USERS_PASSWORD_HISTORY
  (INTERNAL_USER_ID);

CREATE INDEX INTERNAL_USERS_PASSWORD_HISTORY_CHANGED_IX
  ON SECURITY.INTERNAL_USERS_PASSWORD_HISTORY
  (CHANGED);

COMMENT ON COLUMN SECURITY.INTERNAL_USERS_PASSWORD_HISTORY.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the password history entry';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS_PASSWORD_HISTORY.INTERNAL_USER_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS_PASSWORD_HISTORY.CHANGED
  IS 'When the password change took place for the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USERS_PASSWORD_HISTORY.PASSWORD
  IS 'The password for the internal user';



CREATE TABLE SECURITY.INTERNAL_GROUPS (
  ID                 UUID NOT NULL,
  USER_DIRECTORY_ID  UUID NOT NULL,
  GROUPNAME          TEXT NOT NULL,
  DESCRIPTION        TEXT,

  PRIMARY KEY (ID),
  CONSTRAINT INTERNAL_GROUPS_USER_DIRECTORY_FK FOREIGN KEY (USER_DIRECTORY_ID) REFERENCES SECURITY.USER_DIRECTORIES(ID) ON DELETE CASCADE
);

CREATE INDEX INTERNAL_GROUPS_USER_DIRECTORY_ID_IX
  ON SECURITY.INTERNAL_GROUPS
  (USER_DIRECTORY_ID);

CREATE INDEX INTERNAL_GROUPS_GROUPNAME_IX
  ON SECURITY.INTERNAL_GROUPS
  (GROUPNAME);

COMMENT ON COLUMN SECURITY.INTERNAL_GROUPS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal group';

COMMENT ON COLUMN SECURITY.INTERNAL_GROUPS.USER_DIRECTORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the internal group is associated with';

COMMENT ON COLUMN SECURITY.INTERNAL_GROUPS.GROUPNAME
  IS 'The group name for the internal group';

COMMENT ON COLUMN SECURITY.INTERNAL_GROUPS.DESCRIPTION
  IS 'A description for the internal group';



CREATE TABLE SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP (
  INTERNAL_USER_ID   UUID NOT NULL,
  INTERNAL_GROUP_ID  UUID NOT NULL,

  PRIMARY KEY (INTERNAL_USER_ID, INTERNAL_GROUP_ID),
  CONSTRAINT INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_USER_FK FOREIGN KEY (INTERNAL_USER_ID) REFERENCES SECURITY.INTERNAL_USERS(ID) ON DELETE CASCADE,
  CONSTRAINT INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_GROUP_FK FOREIGN KEY (INTERNAL_GROUP_ID) REFERENCES SECURITY.INTERNAL_GROUPS(ID) ON DELETE CASCADE
);

CREATE INDEX INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_USER_ID_IX
  ON SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP
  (INTERNAL_USER_ID);

CREATE INDEX INTERNAL_USER_TO_INTERNAL_GROUP_MAP_INTERNAL_GROUP_ID_IX
  ON SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP
  (INTERNAL_GROUP_ID);

COMMENT ON COLUMN SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP.INTERNAL_USER_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal user';

COMMENT ON COLUMN SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP.INTERNAL_GROUP_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the internal group';



CREATE TABLE SECURITY.GROUPS (
  ID                 UUID NOT NULL,
  USER_DIRECTORY_ID  UUID NOT NULL,
  GROUPNAME          TEXT NOT NULL,

  PRIMARY KEY (ID)
);

CREATE INDEX GROUPS_USER_DIRECTORY_ID_IX
  ON SECURITY.GROUPS
  (USER_DIRECTORY_ID);

CREATE INDEX GROUPS_GROUPNAME_IX
  ON SECURITY.GROUPS
  (GROUPNAME);

COMMENT ON COLUMN SECURITY.GROUPS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the group';

COMMENT ON COLUMN SECURITY.GROUPS.USER_DIRECTORY_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the group is associated with';

COMMENT ON COLUMN SECURITY.GROUPS.GROUPNAME
  IS 'The group name for the group';



CREATE TABLE SECURITY.FUNCTIONS (
  ID           UUID NOT NULL,
  CODE         TEXT NOT NULL,
  NAME         TEXT NOT NULL,
  DESCRIPTION  TEXT,

  PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX FUNCTIONS_CODE_IX
  ON SECURITY.FUNCTIONS
  (CODE);

COMMENT ON COLUMN SECURITY.FUNCTIONS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the function';

COMMENT ON COLUMN SECURITY.FUNCTIONS.CODE
  IS 'The unique code used to identify the function';

COMMENT ON COLUMN SECURITY.FUNCTIONS.NAME
  IS 'The name of the function';

COMMENT ON COLUMN SECURITY.FUNCTIONS.DESCRIPTION
  IS 'A description for the function';



CREATE TABLE SECURITY.ROLES (
  ID           UUID NOT NULL,
  NAME         TEXT NOT NULL,
  DESCRIPTION  TEXT,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN SECURITY.ROLES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';

COMMENT ON COLUMN SECURITY.ROLES.NAME
  IS 'The name of the role';

COMMENT ON COLUMN SECURITY.ROLES.DESCRIPTION
  IS 'A description for the role';



CREATE TABLE SECURITY.FUNCTION_TO_ROLE_MAP (
  FUNCTION_ID  UUID NOT NULL,
  ROLE_ID      UUID NOT NULL,

  PRIMARY KEY (FUNCTION_ID, ROLE_ID),
  CONSTRAINT FUNCTION_TO_ROLE_MAP_FUNCTION_FK FOREIGN KEY (FUNCTION_ID) REFERENCES SECURITY.FUNCTIONS(ID) ON DELETE CASCADE,
  CONSTRAINT FUNCTION_TO_ROLE_MAP_ROLE_FK FOREIGN KEY (ROLE_ID) REFERENCES SECURITY.ROLES(ID) ON DELETE CASCADE
);

CREATE INDEX FUNCTION_TO_ROLE_MAP_FUNCTION_ID_IX
  ON SECURITY.FUNCTION_TO_ROLE_MAP
  (FUNCTION_ID);

CREATE INDEX FUNCTION_TO_ROLE_MAP_ROLE_ID_IX
  ON SECURITY.FUNCTION_TO_ROLE_MAP
  (ROLE_ID);

COMMENT ON COLUMN SECURITY.FUNCTION_TO_ROLE_MAP.FUNCTION_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the function';

COMMENT ON COLUMN SECURITY.FUNCTION_TO_ROLE_MAP.ROLE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';



CREATE TABLE SECURITY.ROLE_TO_GROUP_MAP (
  ROLE_ID   UUID NOT NULL,
  GROUP_ID  UUID NOT NULL,

  PRIMARY KEY (ROLE_ID, GROUP_ID),
  CONSTRAINT ROLE_TO_GROUP_MAP_ROLE_FK FOREIGN KEY (ROLE_ID) REFERENCES SECURITY.ROLES(ID) ON DELETE CASCADE,
  CONSTRAINT ROLE_TO_GROUP_MAP_GROUP_FK FOREIGN KEY (GROUP_ID) REFERENCES SECURITY.GROUPS(ID) ON DELETE CASCADE
);

CREATE INDEX ROLE_TO_GROUP_MAP_ROLE_ID_IX
  ON SECURITY.ROLE_TO_GROUP_MAP
  (ROLE_ID);

CREATE INDEX ROLE_TO_GROUP_MAP_GROUP_ID_IX
  ON SECURITY.ROLE_TO_GROUP_MAP
  (GROUP_ID);

COMMENT ON COLUMN SECURITY.ROLE_TO_GROUP_MAP.ROLE_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the role';

COMMENT ON COLUMN SECURITY.ROLE_TO_GROUP_MAP.GROUP_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the group';



CREATE TABLE SERVICE_REGISTRY.SERVICE_REGISTRY (
  NAME                  TEXT NOT NULL,
  SECURITY_TYPE         INTEGER NOT NULL,
	SUPPORTS_COMPRESSION  CHAR(1) NOT NULL,
	ENDPOINT              TEXT NOT NULL,
  SERVICE_CLASS         TEXT NOT NULL,
  WSDL_LOCATION         TEXT NOT NULL,
  USERNAME              TEXT,
  PASSWORD              TEXT,

	PRIMARY KEY (NAME)
);

COMMENT ON COLUMN SERVICE_REGISTRY.SERVICE_REGISTRY.NAME
  IS 'The name used to uniquely identify the web service';

COMMENT ON COLUMN SERVICE_REGISTRY.SERVICE_REGISTRY.SECURITY_TYPE
  IS 'The type of security model implemented by the web service i.e. 0 = None, 1 = Mutual SSL, etc';

COMMENT ON COLUMN SERVICE_REGISTRY.SERVICE_REGISTRY.SUPPORTS_COMPRESSION
  IS 'Does the web service support compression';

COMMENT ON COLUMN SERVICE_REGISTRY.SERVICE_REGISTRY.ENDPOINT
  IS 'The endpoint for the web service';

COMMENT ON COLUMN SERVICE_REGISTRY.SERVICE_REGISTRY.SERVICE_CLASS
  IS 'The fully qualified name of the Java service class';

COMMENT ON COLUMN SERVICE_REGISTRY.SERVICE_REGISTRY.WSDL_LOCATION
  IS 'The location of the WSDL defining the web service on the classpath';

COMMENT ON COLUMN SERVICE_REGISTRY.SERVICE_REGISTRY.USERNAME
  IS 'The username to use when accessing a web service with username-password security enabled';

COMMENT ON COLUMN SERVICE_REGISTRY.SERVICE_REGISTRY.PASSWORD
  IS 'The password to use when accessing a web service with username-password security enabled';



CREATE TABLE SMS.SMS (
  ID              BIGINT NOT NULL,
  MOBILE_NUMBER   TEXT NOT NULL,
  MESSAGE         TEXT NOT NULL,
  STATUS          INTEGER NOT NULL,
  SEND_ATTEMPTS   INTEGER NOT NULL,
  LOCK_NAME       TEXT,
  LAST_PROCESSED  TIMESTAMP,

  PRIMARY KEY (ID)
);

CREATE INDEX SMS_MOBILE_NUMBER_IX
  ON SMS.SMS
  (MOBILE_NUMBER);

COMMENT ON COLUMN SMS.SMS.ID
  IS 'The ID used to uniquely identify the SMS';

COMMENT ON COLUMN SMS.SMS.MOBILE_NUMBER
  IS 'The mobile number to send the SMS to';

COMMENT ON COLUMN SMS.SMS.MESSAGE
  IS 'The message to send';

COMMENT ON COLUMN SMS.SMS.STATUS
  IS 'The status of the SMS';

COMMENT ON COLUMN SMS.SMS.SEND_ATTEMPTS
  IS 'The number of times that the sending of the SMS was attempted';

COMMENT ON COLUMN SMS.SMS.LOCK_NAME
  IS 'The name of the entity that has locked the SMS for sending';

COMMENT ON COLUMN SMS.SMS.LAST_PROCESSED
  IS 'The date and time the last attempt was made to send the SMS';



CREATE TABLE TEST.TEST_DATA (
  ID     TEXT NOT NULL,
  NAME   TEXT NOT NULL,
  VALUE  TEXT NOT NULL,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN TEST.TEST_DATA.ID
  IS 'The ID used to uniquely identify the test data';

COMMENT ON COLUMN TEST.TEST_DATA.NAME
  IS 'The name for the test data';

COMMENT ON COLUMN TEST.TEST_DATA.VALUE
  IS 'The value for the test data';



CREATE TABLE SAMPLE.DATA (
  ID     BIGINT NOT NULL,
  NAME   TEXT NOT NULL,
  VALUE  TEXT NOT NULL,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN SAMPLE.DATA.ID
  IS 'The ID used to uniquely identify the data';

COMMENT ON COLUMN SAMPLE.DATA.NAME
  IS 'The name for the data';

COMMENT ON COLUMN SAMPLE.DATA.VALUE
  IS 'The value for the data';



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
  ('ea26c191-e2ee-4481-aa56-1f03c987d24f', 'Sample Job', '30 * * * *', 'guru.mmp.sample.jobs.SampleJob', TRUE, 0);



-- -------------------------------------------------------------------------------------------------
-- SET PERMISSIONS
-- -------------------------------------------------------------------------------------------------
GRANT ALL ON SCHEMA CODES TO sample;
GRANT ALL ON SCHEMA CONFIGURATION TO sample;
GRANT ALL ON SCHEMA IDGENERATOR TO sample;
GRANT ALL ON SCHEMA MESSAGING TO sample;
GRANT ALL ON SCHEMA REPORTING TO sample;
GRANT ALL ON SCHEMA SCHEDULER TO sample;
GRANT ALL ON SCHEMA SECURITY TO sample;
GRANT ALL ON SCHEMA SERVICE_REGISTRY TO sample;
GRANT ALL ON SCHEMA SMS TO sample;
GRANT ALL ON SCHEMA TEST TO sample;
GRANT ALL ON SCHEMA SAMPLE TO sample;

GRANT ALL ON TABLE CODES.CODE_CATEGORIES TO sample;
GRANT ALL ON TABLE CODES.CODES TO sample;
GRANT ALL ON TABLE CODES.CACHED_CODE_CATEGORIES TO sample;
GRANT ALL ON TABLE CODES.CACHED_CODES TO sample;
GRANT ALL ON TABLE CONFIGURATION.CONFIGURATION TO sample;
GRANT ALL ON TABLE IDGENERATOR.IDGENERATOR TO sample;
GRANT ALL ON TABLE MESSAGING.MESSAGE_TYPES TO sample;
GRANT ALL ON TABLE MESSAGING.MESSAGE_STATUSES TO sample;
GRANT ALL ON TABLE MESSAGING.MESSAGES TO sample;
GRANT ALL ON TABLE MESSAGING.MESSAGE_PARTS TO sample;
GRANT ALL ON TABLE MESSAGING.ARCHIVED_MESSAGES TO sample;
GRANT ALL ON TABLE MESSAGING.ERROR_REPORTS TO sample;
GRANT ALL ON TABLE REPORTING.REPORT_DEFINITIONS TO sample;
GRANT ALL ON TABLE SCHEDULER.JOBS TO sample;
GRANT ALL ON TABLE SCHEDULER.JOB_PARAMETERS TO sample;
GRANT ALL ON TABLE SECURITY.ORGANISATIONS TO sample;
GRANT ALL ON TABLE SECURITY.USER_DIRECTORY_TYPES TO sample;
GRANT ALL ON TABLE SECURITY.USER_DIRECTORIES TO sample;
GRANT ALL ON TABLE SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP TO sample;
GRANT ALL ON TABLE SECURITY.INTERNAL_USERS TO sample;
GRANT ALL ON TABLE SECURITY.INTERNAL_USERS_PASSWORD_HISTORY TO sample;
GRANT ALL ON TABLE SECURITY.INTERNAL_GROUPS TO sample;
GRANT ALL ON TABLE SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP TO sample;
GRANT ALL ON TABLE SECURITY.GROUPS TO sample;
GRANT ALL ON TABLE SECURITY.FUNCTIONS TO sample;
GRANT ALL ON TABLE SECURITY.ROLES TO sample;
GRANT ALL ON TABLE SECURITY.FUNCTION_TO_ROLE_MAP TO sample;
GRANT ALL ON TABLE SECURITY.ROLE_TO_GROUP_MAP TO sample;
GRANT ALL ON TABLE SERVICE_REGISTRY.SERVICE_REGISTRY TO sample;
GRANT ALL ON TABLE SMS.SMS TO sample;
GRANT ALL ON TABLE TEST.TEST_DATA TO sample;
GRANT ALL ON TABLE SAMPLE.DATA TO sample;
