-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA SAMPLE;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE SAMPLE.DATA (
  ID     BIGINT NOT NULL,
  NAME   VARCHAR(100) NOT NULL,
  VALUE  VARCHAR(100) NOT NULL,

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
INSERT INTO MMP.ORGANISATIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (2, 'TEST', 'Test', 'Test');

INSERT INTO MMP.USER_DIRECTORIES (ID, NAME, DESCRIPTION, USER_DIRECTORY_CLASS) VALUES
  (2, 'Test Internal User Directory', 'Test Internal User Directory', 'guru.mmp.application.security.InternalUserDirectory');

INSERT INTO MMP.USER_DIRECTORY_PARAMETERS (ID, USER_DIRECTORY_ID, NAME, VALUE) VALUES
  (5, 2, 'MaxPasswordAttempts', '5');
INSERT INTO MMP.USER_DIRECTORY_PARAMETERS (ID, USER_DIRECTORY_ID, NAME, VALUE) VALUES
  (6, 2, 'PasswordExpiryMonths', '3');
INSERT INTO MMP.USER_DIRECTORY_PARAMETERS (ID, USER_DIRECTORY_ID, NAME, VALUE) VALUES
  (7, 2, 'PasswordHistoryMonths', '12');
INSERT INTO MMP.USER_DIRECTORY_PARAMETERS (ID, USER_DIRECTORY_ID, NAME, VALUE) VALUES
  (8, 2, 'MaxFilteredUsers', '100');

INSERT INTO MMP.USER_DIRECTORY_TO_ORGANISATION_MAP (USER_DIRECTORY_ID, ORGANISATION_ID) VALUES
  (1, 2);
INSERT INTO MMP.USER_DIRECTORY_TO_ORGANISATION_MAP (USER_DIRECTORY_ID, ORGANISATION_ID) VALUES
  (2, 2);

INSERT INTO MMP.INTERNAL_USERS (ID, USER_DIRECTORY_ID, USERNAME, PASSWORD, TITLE, FIRST_NAMES, LAST_NAME, PHONE, FAX,
  MOBILE, EMAIL, PASSWORD_ATTEMPTS, PASSWORD_EXPIRY, DESCRIPTION) VALUES
  (3, 2, 'sample', 'GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=', '', '', '', '', '', '', '', null, null,
  'Sample User');

INSERT INTO MMP.INTERNAL_GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME, DESCRIPTION) VALUES
  (3, 2, 'Organisation Administrators', 'Organisation Administrators');

INSERT INTO MMP.INTERNAL_USER_TO_INTERNAL_GROUP_MAP (USER_DIRECTORY_ID, INTERNAL_USER_ID, INTERNAL_GROUP_ID) VALUES
  (2, 3, 3);

INSERT INTO MMP.GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME) VALUES (3, 2, 'Organisation Administrators');

INSERT INTO MMP.ROLE_TO_GROUP_MAP (ROLE_ID, GROUP_ID) VALUES (2, 3);

INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (1, 'Sample Name 1', 'Sample Value 1');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (2, 'Sample Name 2', 'Sample Value 2');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (3, 'Sample Name 3', 'Sample Value 3');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (4, 'Sample Name 4', 'Sample Value 4');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (5, 'Sample Name 5', 'Sample Value 5');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (6, 'Sample Name 6', 'Sample Value 6');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (7, 'Sample Name 7', 'Sample Value 7');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (8, 'Sample Name 8', 'Sample Value 8');
INSERT INTO SAMPLE.DATA (ID, NAME, VALUE) VALUES (9, 'Sample Name 9', 'Sample Value 9');

INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('d7d32bbd-ef44-4e5e-8c8f-8108ea2ab53a', 'MMP', 0, 'Test Code Category 1', 'Test Code Category Description 1');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('414586a2-836a-42b3-ad09-be23448c6cdb', 'MMP', 0, 'Test Code Category 2', 'Test Code Category Description 2');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('7b48fc44-03d3-467b-bde3-44f99a0b5824', 'MMP', 0, 'Test Code Category 3', 'Test Code Category Description 3');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('ebe02fae-0305-4965-9727-08bce55762de', 'MMP', 0, 'Test Code Category 4', 'Test Code Category Description 4');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('afc0fc9b-357b-4da9-83ad-3f36eaaf7aa9', 'MMP', 0, 'Test Code Category 5', 'Test Code Category Description 5');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('d94a549d-311e-4078-9689-392c9e4091ab', 'MMP', 0, 'Test Code Category 6', 'Test Code Category Description 6');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('5c1cf103-3e51-4942-b2b7-934f4ef1a712', 'MMP', 0, 'Test Code Category 7', 'Test Code Category Description 7');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('3c9de85c-27e8-463c-8d7a-571310de9429', 'MMP', 0, 'Test Code Category 8', 'Test Code Category Description 8');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('df82e5aa-b3ff-4bcf-8c14-57db2ee8576a', 'MMP', 0, 'Test Code Category 9', 'Test Code Category Description 9');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('28ee0d70-a173-43dd-9f4f-2bec53981ba6', 'MMP', 0, 'Test Code Category 10', 'Test Code Category Description 10');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('bdea98c2-456e-4a61-9371-88b6e09715c5', 'MMP', 0, 'Test Code Category 11', 'Test Code Category Description 11');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('54895a59-cc05-4549-a250-06bf615aff75', 'MMP', 0, 'Test Code Category 12', 'Test Code Category Description 12');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('7253a209-92cf-4ff3-b64b-bcb3dfebcffc', 'MMP', 0, 'Test Code Category 13', 'Test Code Category Description 13');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('8a901a99-bd8e-454e-ab30-332f701bd358', 'MMP', 0, 'Test Code Category 14', 'Test Code Category Description 14');
INSERT INTO MMP.CODE_CATEGORIES (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION) VALUES
  ('18ad1667-6a43-4118-a31f-cfc0b472d456', 'MMP', 0, 'Test Code Category 15', 'Test Code Category Description 15');




