-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA SAMPLE;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE SAMPLE.DATA (
  ID               BIGINT NOT NULL,
  NAME             VARCHAR(4000) NOT NULL,
  INTEGER_VALUE    INTEGER,
  STRING_VALUE     VARCHAR(4000),
  DATE_VALUE       DATE,
  TIMESTAMP_VALUE  DATETIME,

  PRIMARY KEY (ID)
);

COMMENT ON COLUMN SAMPLE.DATA.ID
  IS 'The ID used to uniquely identify the data';

COMMENT ON COLUMN SAMPLE.DATA.NAME
  IS 'The name for the data';

COMMENT ON COLUMN SAMPLE.DATA.INTEGER_VALUE
IS 'The integer value for the data';

COMMENT ON COLUMN SAMPLE.DATA.STRING_VALUE
  IS 'The string value for the data';

COMMENT ON COLUMN SAMPLE.DATA.DATE_VALUE
IS 'The date value for the data';

COMMENT ON COLUMN SAMPLE.DATA.TIMESTAMP_VALUE
IS 'The timestamp value for the data';



-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
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

INSERT INTO SAMPLE.DATA (ID, NAME, STRING_VALUE) VALUES (1, 'Sample Name 1', 'Sample Value 1');
INSERT INTO SAMPLE.DATA (ID, NAME, STRING_VALUE) VALUES (2, 'Sample Name 2', 'Sample Value 2');
INSERT INTO SAMPLE.DATA (ID, NAME, STRING_VALUE) VALUES (3, 'Sample Name 3', 'Sample Value 3');
INSERT INTO SAMPLE.DATA (ID, NAME, STRING_VALUE) VALUES (4, 'Sample Name 4', 'Sample Value 4');
INSERT INTO SAMPLE.DATA (ID, NAME, STRING_VALUE) VALUES (5, 'Sample Name 5', 'Sample Value 5');
INSERT INTO SAMPLE.DATA (ID, NAME, STRING_VALUE) VALUES (6, 'Sample Name 6', 'Sample Value 6');
INSERT INTO SAMPLE.DATA (ID, NAME, STRING_VALUE) VALUES (7, 'Sample Name 7', 'Sample Value 7');
INSERT INTO SAMPLE.DATA (ID, NAME, STRING_VALUE) VALUES (8, 'Sample Name 8', 'Sample Value 8');
INSERT INTO SAMPLE.DATA (ID, NAME, STRING_VALUE) VALUES (9, 'Sample Name 9', 'Sample Value 9');

INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
  ('d7d32bbd-ef44-4e5e-8c8f-8108ea2ab53a', 0, 'Sample Code Category 1');
INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME) VALUES
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




