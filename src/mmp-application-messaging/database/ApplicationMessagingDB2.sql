-- ----------------------------------------------------------------------------------------------------------------------------------------
-- 
--      _                _ _           _   _             __  __                           _             ____  ____ ____              _ 
--     / \   _ __  _ __ | (_) ___ __ _| |_(_) ___  _ __ |  \/  | ___  ___ ___  __ _  __ _(_)_ __   __ _|  _ \| __ )___ \   ___  __ _| |
--    / _ \ | '_ \| '_ \| | |/ __/ _` | __| |/ _ \| '_ \| |\/| |/ _ \/ __/ __|/ _` |/ _` | | '_ \ / _` | | | |  _ \ __) | / __|/ _` | |
--   / ___ \| |_) | |_) | | | (_| (_| | |_| | (_) | | | | |  | |  __/\__ \__ \ (_| | (_| | | | | | (_| | |_| | |_) / __/ _\__ \ (_| | |
--  /_/   \_\ .__/| .__/|_|_|\___\__,_|\__|_|\___/|_| |_|_|  |_|\___||___/___/\__,_|\__, |_|_| |_|\__, |____/|____/_____(_)___/\__, |_|
--          |_|   |_|                                                               |___/         |___/                           |_|  
-- 
-- ----------------------------------------------------------------------------------------------------------------------------------------

-- -------------------------------------------------------------------------------------------------
-- DROP EXISTING TABLES
-- -------------------------------------------------------------------------------------------------
DROP TABLE ERROR_REPORTS;

DROP TABLE PACKAGES;

DROP TABLE MESSAGES;
DROP TABLE MESSAGE_AUDIT_LOG;
DROP TABLE MESSAGE_PARTS;
DROP TABLE ARCHIVED_MESSAGES;
DROP TABLE MESSAGE_TYPES;
DROP TABLE MESSAGE_STATUSES;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE MESSAGES (
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
  DATA               BLOB(4096k),
  
  PRIMARY KEY (ID)
);

CREATE INDEX MESSAGES_USERNAME_IX
  ON MESSAGES
  (USERNAME);

CREATE INDEX MESSAGES_ORGANISATION_IX
  ON MESSAGES
  (ORGANISATION);
  
CREATE INDEX MESSAGES_DEVICE_IX
  ON MESSAGES
  (DEVICE);

CREATE INDEX MESSAGES_MSG_TYPE_IX
  ON MESSAGES
  (MSG_TYPE);

CREATE INDEX MESSAGES_PRIORITY_IX
  ON MESSAGES
  (PRIORITY);

CREATE INDEX MESSAGES_STATUS_IX
  ON MESSAGES
  (STATUS);
  
CREATE INDEX MESSAGES_LOCK_NAME_IX
  ON MESSAGES
  (LOCK_NAME);
  
COMMENT ON COLUMN MESSAGES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message';

COMMENT ON COLUMN MESSAGES.USERNAME
  IS 'The username identifying the user associated with the message';

COMMENT ON COLUMN MESSAGES.ORGANISATION
  IS 'The organisation code identifying the organisation associated with the message';
  
COMMENT ON COLUMN MESSAGES.DEVICE
  IS 'The device ID identifying the device the message originated from';

COMMENT ON COLUMN MESSAGES.MSG_TYPE
  IS 'The UUID identifying the type of message';

COMMENT ON COLUMN MESSAGES.MSG_TYPE_VER
  IS 'The version of the message type';

COMMENT ON COLUMN MESSAGES.CORRELATION_ID
  IS 'The UUID used to correlate the message';
  
COMMENT ON COLUMN MESSAGES.PRIORITY
  IS 'The message priority';

COMMENT ON COLUMN MESSAGES.STATUS
  IS 'The message status e.g. Initialised, QueuedForSending, etc';

COMMENT ON COLUMN MESSAGES.CREATED
  IS 'The date and time the message was created';

COMMENT ON COLUMN MESSAGES.PERSISTED
  IS 'The date and time the message was persisted';
  
COMMENT ON COLUMN MESSAGES.UPDATED
  IS 'The date and time the message was last updated';

COMMENT ON COLUMN MESSAGES.SEND_ATTEMPTS
  IS 'The number of times that the sending of the message was attempted';
  
COMMENT ON COLUMN MESSAGES.PROCESS_ATTEMPTS
  IS 'The number of times that the processing of the message was attempted';
  
COMMENT ON COLUMN MESSAGES.DOWNLOAD_ATTEMPTS
  IS 'The number of times that an attempt was made to download the message';

COMMENT ON COLUMN MESSAGES.LOCK_NAME
  IS 'The name of the entity that has locked the message for processing';

COMMENT ON COLUMN MESSAGES.LAST_PROCESSED
  IS 'The date and time the last attempt was made to process the message';
  
COMMENT ON COLUMN MESSAGES.DATA
  IS 'The data for the message';
  
  
  
CREATE TABLE MESSAGE_PARTS (
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
  DATA                BLOB(64k),
  
  PRIMARY KEY (ID)
);

CREATE INDEX MESSAGE_PARTS_STATUS_IX
  ON MESSAGE_PARTS
  (STATUS);

CREATE INDEX MESSAGE_PARTS_MSG_ID_IX
  ON MESSAGE_PARTS
  (MSG_ID);

CREATE INDEX MESSAGE_PARTS_MSG_DEVICE_IX
  ON MESSAGE_PARTS
  (MSG_DEVICE);

CREATE INDEX MESSAGE_PARTS_MSG_LOCK_NAME_IX
  ON MESSAGE_PARTS
  (LOCK_NAME);

COMMENT ON COLUMN MESSAGE_PARTS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message part';

COMMENT ON COLUMN MESSAGE_PARTS.PART_NO
  IS 'The number of the message part in the set of message parts for the original message';  

COMMENT ON COLUMN MESSAGE_PARTS.TOTAL_PARTS
  IS 'The total number of parts in the set of message parts for the original message';  

COMMENT ON COLUMN MESSAGE_PARTS.SEND_ATTEMPTS
  IS 'The number of times that the sending of the message part was attempted';  

COMMENT ON COLUMN MESSAGE_PARTS.DOWNLOAD_ATTEMPTS
  IS 'The number of times that an attempt was made to download the message part';
  
COMMENT ON COLUMN MESSAGE_PARTS.STATUS
  IS 'The message part status e.g. Initialised, QueuedForSending, etc';
  
COMMENT ON COLUMN MESSAGE_PARTS.PERSISTED
  IS 'The date and time the message part was persisted';
  
COMMENT ON COLUMN MESSAGE_PARTS.UPDATED
  IS 'The date and time the message part was last updated';
  
COMMENT ON COLUMN MESSAGE_PARTS.MSG_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the original message';

COMMENT ON COLUMN MESSAGE_PARTS.MSG_USERNAME
  IS 'The username identifying the user associated with the original message';

COMMENT ON COLUMN MESSAGE_PARTS.MSG_ORGANISATION
  IS 'The organisation code identifying the organisation associated with the original message';
  
COMMENT ON COLUMN MESSAGE_PARTS.MSG_DEVICE
  IS 'The device ID identifying the device the original message originated from';

COMMENT ON COLUMN MESSAGE_PARTS.MSG_TYPE
  IS 'The UUID identifying the type of the original message';

COMMENT ON COLUMN MESSAGE_PARTS.MSG_TYPE_VER
  IS 'The version of the original message type';

COMMENT ON COLUMN MESSAGE_PARTS.MSG_CORRELATION_ID
  IS 'The UUID used to correlate the original message';
  
COMMENT ON COLUMN MESSAGE_PARTS.MSG_PRIORITY
  IS 'The priority for the original message';

COMMENT ON COLUMN MESSAGE_PARTS.MSG_CREATED
  IS 'The date and time the original message was created';

COMMENT ON COLUMN MESSAGE_PARTS.MSG_DATA_HASH
  IS 'The hash of the unencrypted data for the original message';
  
COMMENT ON COLUMN MESSAGE_PARTS.MSG_ENC_SCHEME
  IS 'The encryption scheme used to secure the original message';
  
COMMENT ON COLUMN MESSAGE_PARTS.MSG_ENC_IV
  IS 'The base-64 encoded initialisation vector for the encryption scheme for the original message';
  
COMMENT ON COLUMN MESSAGE_PARTS.MSG_CHECKSUM
  IS 'The checksum for the original message';

COMMENT ON COLUMN MESSAGE_PARTS.LOCK_NAME
  IS 'The name of the entity that has locked the message part for processing';

COMMENT ON COLUMN MESSAGE_PARTS.DATA
  IS 'The data for the message part';  

  

CREATE TABLE ARCHIVED_MESSAGES (
  ID              VARCHAR(40) NOT NULL,
  USERNAME        VARCHAR(100) NOT NULL,
  ORGANISATION    VARCHAR(40) NOT NULL,
  DEVICE          VARCHAR(20) NOT NULL,
  MSG_TYPE        VARCHAR(40) NOT NULL,
  MSG_TYPE_VER    INTEGER NOT NULL,
  CORRELATION_ID  VARCHAR(40) NOT NULL,
  CREATED         TIMESTAMP NOT NULL,
  ARCHIVED        TIMESTAMP NOT NULL,
  DATA            BLOB(4096k),
  
  PRIMARY KEY (ID)
);

CREATE INDEX ARCHIVED_MESSAGES_USERNAME_IX
  ON ARCHIVED_MESSAGES
  (USERNAME);

CREATE INDEX ARCHIVED_MESSAGES_ORGANISATION_IX
  ON ARCHIVED_MESSAGES
  (ORGANISATION);
  
CREATE INDEX ARCHIVED_MESSAGES_DEVICE_IX
  ON ARCHIVED_MESSAGES
  (DEVICE);

CREATE INDEX ARCHIVED_MESSAGES_MSG_TYPE_IX
  ON ARCHIVED_MESSAGES
  (MSG_TYPE);

COMMENT ON COLUMN ARCHIVED_MESSAGES.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message';

COMMENT ON COLUMN ARCHIVED_MESSAGES.USERNAME
  IS 'The username identifying the user associated with the message';

COMMENT ON COLUMN ARCHIVED_MESSAGES.ORGANISATION
  IS 'The organisation code identifying the organisation associated with the message';
  
COMMENT ON COLUMN ARCHIVED_MESSAGES.DEVICE
  IS 'The device ID identifying the device the message originated from';

COMMENT ON COLUMN ARCHIVED_MESSAGES.MSG_TYPE
  IS 'The UUID identifying the type of message';

COMMENT ON COLUMN ARCHIVED_MESSAGES.MSG_TYPE_VER
  IS 'The version of the message type';

COMMENT ON COLUMN ARCHIVED_MESSAGES.CORRELATION_ID
  IS 'The UUID used to correlate the message';
  
COMMENT ON COLUMN ARCHIVED_MESSAGES.CREATED
  IS 'The date and time the message was created';

COMMENT ON COLUMN ARCHIVED_MESSAGES.ARCHIVED
  IS 'The date and time the message was archived';

COMMENT ON COLUMN ARCHIVED_MESSAGES.DATA
  IS 'The data for the message';

  
  
CREATE TABLE MESSAGE_TYPES (
  ID           VARCHAR(40) NOT NULL,
  NAME         VARCHAR(100) NOT NULL,
  DESCRIPTION  VARCHAR(512),
  
  PRIMARY KEY (ID)
);
  
COMMENT ON COLUMN MESSAGE_TYPES.ID
  IS 'The UUID identifying the message type';

COMMENT ON COLUMN MESSAGE_TYPES.NAME
  IS 'The name of the message type';

COMMENT ON COLUMN MESSAGE_TYPES.DESCRIPTION
  IS 'A description of the message type';
  
  

CREATE TABLE MESSAGE_STATUSES (
  CODE  INTEGER NOT NULL,
  NAME  VARCHAR(100) NOT NULL,
  
  PRIMARY KEY (CODE)
);
  
COMMENT ON COLUMN MESSAGE_STATUSES.CODE
  IS 'The code identifying the message status';

COMMENT ON COLUMN MESSAGE_STATUSES.NAME
  IS 'The name of the message status';


  
CREATE TABLE MESSAGE_AUDIT_LOG (
  ID            BIGINT NOT NULL,
  MSG_TYPE      VARCHAR(40) NOT NULL,
  USERNAME      VARCHAR(100) NOT NULL,
  ORGANISATION  VARCHAR(40) NOT NULL,
  DEVICE        VARCHAR(20) NOT NULL,
  IP            VARCHAR(20) NOT NULL,
  LOGGED        TIMESTAMP NOT NULL,
  SUCCESSFUL    CHAR(1) NOT NULL,
  
  PRIMARY KEY (ID)
);
  
COMMENT ON COLUMN MESSAGE_AUDIT_LOG.ID
  IS 'The ID used to uniquely identify the message audit entry';

COMMENT ON COLUMN MESSAGE_AUDIT_LOG.MSG_TYPE
  IS 'The type of message associated with the message audit entry';

COMMENT ON COLUMN MESSAGE_AUDIT_LOG.USERNAME
  IS 'The user responsible for the message audit entry';
  
COMMENT ON COLUMN MESSAGE_AUDIT_LOG.ORGANISATION
  IS 'The organisation code identifying the organisation associated with the message audit entry';
  
COMMENT ON COLUMN MESSAGE_AUDIT_LOG.DEVICE
  IS 'The ID for the device associated with the message audit entry';

COMMENT ON COLUMN MESSAGE_AUDIT_LOG.IP
  IS 'The IP address of the remote device associated with the message audit entry';

COMMENT ON COLUMN MESSAGE_AUDIT_LOG.LOGGED
  IS 'The date and time the message audit entry was logged';

COMMENT ON COLUMN MESSAGE_AUDIT_LOG.SUCCESSFUL
  IS 'Was the message associated with the message audit entry successfully processed';

  
  
CREATE TABLE PACKAGES (
  ID            VARCHAR(40) NOT NULL,
  VERSION       INTEGER NOT NULL,
  ORGANISATION  VARCHAR(40) NOT NULL,
  NAME          VARCHAR(255) NOT NULL,
  IS_CURRENT    NUMERIC(1,0) NOT NULL DEFAULT 0,
  HASH          VARCHAR(40) NOT NULL,
  SIZE          INTEGER NOT NULL,
  CREATED       TIMESTAMP NOT NULL,
  CREATED_BY    VARCHAR(100) NOT NULL,
  DATA          BLOB(8192k) NOT NULL,
  
  PRIMARY KEY (ID, VERSION)
);

CREATE INDEX PACKAGES_ORGANISATION_IX
  ON PACKAGES
  (ORGANISATION);

COMMENT ON COLUMN PACKAGES.ID
  IS 'The Universally Unique Identifier (UUID) used to identify the package';

COMMENT ON COLUMN PACKAGES.VERSION
  IS 'The version of the package';

COMMENT ON COLUMN PACKAGES.ORGANISATION
  IS 'The organisation code identifying the organisation the package is associated with';

COMMENT ON COLUMN PACKAGES.NAME
  IS 'The name of the package';

COMMENT ON COLUMN PACKAGES.IS_CURRENT
  IS 'Is the package version current i.e. is this the version of the package that should be installed on a device';

COMMENT ON COLUMN PACKAGES.HASH
  IS 'The SHA-1 hash used to confirm the authenticity of the package version';

COMMENT ON COLUMN PACKAGES.SIZE
  IS 'The size of the package version in bytes';
  
COMMENT ON COLUMN PACKAGES.CREATED
  IS 'The date and time the package version was created';

COMMENT ON COLUMN PACKAGES.CREATED_BY
  IS 'The username identifying the user that created the package version';

COMMENT ON COLUMN PACKAGES.DATA
  IS 'The package version data';   
  
  
  
CREATE TABLE ERROR_REPORTS (
  ID                   VARCHAR(40) NOT NULL,
  APPLICATION_ID       VARCHAR(40) NOT NULL,
  APPLICATION_VERSION  INTEGER NOT NULL,
  DESCRIPTION          VARCHAR(2048) NOT NULL,
  DETAIL               VARCHAR(16384) NOT NULL,
  FEEDBACK             VARCHAR(4000) NOT NULL,
  CREATED              TIMESTAMP NOT NULL,
  WHO                  VARCHAR(100) NOT NULL,
  DEVICE               VARCHAR(40) NOT NULL,
  DATA                 BLOB(4096k),
  
  PRIMARY KEY (ID)
);  

CREATE INDEX ERROR_REPORTS_APPLICATION_ID_IX
  ON ERROR_REPORTS
  (APPLICATION_ID);

CREATE INDEX ERROR_REPORTS_CREATED_IX
  ON ERROR_REPORTS
  (CREATED);

CREATE INDEX ERROR_REPORTS_WHO_IX
  ON ERROR_REPORTS
  (WHO);

COMMENT ON COLUMN ERROR_REPORTS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the error report';

COMMENT ON COLUMN ERROR_REPORTS.APPLICATION_ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the application that generated the error report';

COMMENT ON COLUMN ERROR_REPORTS.APPLICATION_VERSION
  IS 'The version of the application that generated the error report';
  
COMMENT ON COLUMN ERROR_REPORTS.DESCRIPTION
  IS 'The description of the error';
  
COMMENT ON COLUMN ERROR_REPORTS.DETAIL
  IS 'The error detail e.g. a stack trace';
  
COMMENT ON COLUMN ERROR_REPORTS.FEEDBACK
  IS 'The feedback provided by the user for the error';
  
COMMENT ON COLUMN ERROR_REPORTS.CREATED
  IS 'The date and time the error report was created';
  
COMMENT ON COLUMN ERROR_REPORTS.WHO
  IS 'The username identifying the user associated with the error report';
  
COMMENT ON COLUMN ERROR_REPORTS.DEVICE
  IS 'The device ID identifying the device the error report originated from';
  
COMMENT ON COLUMN ERROR_REPORTS.DATA
  IS 'The data associated with the error report'; 
  
  
  
-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (1000, 'ApplicationMessaging.ErrorReports', 'Error Reports', 'Error Reports');

INSERT INTO FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 1000);

--INSERT INTO FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (2, 1000);

INSERT INTO EXTERNAL_FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 1000);

--INSERT INTO EXTERNAL_FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (2, 1000);

INSERT INTO MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('3dbf238d-b56f-468a-8850-4ddf9f15c329', 'RegisterRequest', '');
INSERT INTO MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('aa08aac9-4d15-452f-b3f9-756641b71735', 'RegisterResponse', '');
INSERT INTO MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('a589dc87-2328-4a9b-bdb6-970e55ca2323', 'TestRequest', '');
INSERT INTO MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('a3bad7ba-f9d4-4403-b54a-cb1f335ebbad', 'TestResponse', '');
INSERT INTO MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('e9918051-8ebc-48f1-bad7-13c59b550e1a', 'AnotherTestRequest', '');
INSERT INTO MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('a714a9c6-2914-4498-ab59-64be9991bf37', 'AnotherTestResponse', '');
INSERT INTO MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('ff638c33-b4f1-4e79-804c-9560da2543d6', 'SubmitErrorReportRequest', '');
INSERT INTO MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('8be50cfa-2fb1-4634-9bfa-d01e77eaf766', 'SubmitErrorReportResponse', '');
INSERT INTO MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('d21fb54e-5c5b-49e8-881f-ce00c6ced1a3', 'AuthenticateRequest', '');
INSERT INTO MESSAGE_TYPES (ID, NAME, DESCRIPTION) VALUES ('82223035-1726-407f-8703-3977708e792c', 'AuthenticateResponse', '');

INSERT INTO MESSAGE_STATUSES (CODE, NAME) VALUES (0, 'Initialised');
INSERT INTO MESSAGE_STATUSES (CODE, NAME) VALUES (1, 'QueuedForSending');
INSERT INTO MESSAGE_STATUSES (CODE, NAME) VALUES (2, 'QueuedForProcessing');
INSERT INTO MESSAGE_STATUSES (CODE, NAME) VALUES (3, 'Aborted');
INSERT INTO MESSAGE_STATUSES (CODE, NAME) VALUES (4, 'Failed');
INSERT INTO MESSAGE_STATUSES (CODE, NAME) VALUES (5, 'Processing');
INSERT INTO MESSAGE_STATUSES (CODE, NAME) VALUES (6, 'Sending');
INSERT INTO MESSAGE_STATUSES (CODE, NAME) VALUES (7, 'QueuedForDownload');
INSERT INTO MESSAGE_STATUSES (CODE, NAME) VALUES (8, 'Downloading');
INSERT INTO MESSAGE_STATUSES (CODE, NAME) VALUES (10, 'Processed');



-- -------------------------------------------------------------------------------------------------
-- SET PERMISSIONS
-- -------------------------------------------------------------------------------------------------



