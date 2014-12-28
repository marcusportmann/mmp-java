-- -------------------------------------------------------------------------------------------------------------------------------------------------------------
--      _                _ _           _   _             ____                       _   _             ____           _                                  _ 
--     / \   _ __  _ __ | (_) ___ __ _| |_(_) ___  _ __ |  _ \ ___ _ __   ___  _ __| |_(_)_ __   __ _|  _ \ ___  ___| |_ __ _ _ __ ___  ___   ___  __ _| |
--    / _ \ | '_ \| '_ \| | |/ __/ _` | __| |/ _ \| '_ \| |_) / _ \ '_ \ / _ \| '__| __| | '_ \ / _` | |_) / _ \/ __| __/ _` | '__/ _ \/ __| / __|/ _` | |
--   / ___ \| |_) | |_) | | | (_| (_| | |_| | (_) | | | |  _ <  __/ |_) | (_) | |  | |_| | | | | (_| |  __/ (_) \__ \ || (_| | | |  __/\__ \_\__ \ (_| | |
--  /_/   \_\ .__/| .__/|_|_|\___\__,_|\__|_|\___/|_| |_|_| \_\___| .__/ \___/|_|   \__|_|_| |_|\__, |_|   \___/|___/\__\__, |_|  \___||___(_)___/\__, |_|
--          |_|   |_|                                             |_|                           |___/                   |___/                        |_|  
--
-- -----------------------------------------------------------------------------------------------------------------------------------------------------------

-- -------------------------------------------------------------------------------------------------
-- DROP EXISTING TABLES
-- -------------------------------------------------------------------------------------------------
DROP TABLE REPORT_DEFINITIONS;



-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE REPORT_DEFINITIONS (
  ID            VARCHAR(40) NOT NULL,
  ORGANISATION  VARCHAR(40) NOT NULL,
  NAME          VARCHAR(255) NOT NULL,
  TEMPLATE      BYTEA NULL,
  CREATED       TIMESTAMP NOT NULL,
  CREATED_BY    VARCHAR(100) NOT NULL,
  UPDATED       TIMESTAMP,
  UPDATED_BY    VARCHAR(100),
  
  PRIMARY KEY (ID)
);
  
CREATE INDEX REPORT_DEFINITIONS_ORGANISATION_IX
  ON REPORT_DEFINITIONS
  (ORGANISATION);

COMMENT ON COLUMN REPORT_DEFINITIONS.ID
  IS 'The Universally Unique Identifier (UUID) used to uniquely identify the report definition';

COMMENT ON COLUMN REPORT_DEFINITIONS.ORGANISATION
  IS 'The organisation code identifying the organisation the report definition is associated with';
  
COMMENT ON COLUMN REPORT_DEFINITIONS.NAME
  IS 'The name of the report definition';

COMMENT ON COLUMN REPORT_DEFINITIONS.TEMPLATE
  IS 'The JasperReports template for the report definition';
  
COMMENT ON COLUMN REPORT_DEFINITIONS.CREATED
  IS 'The date and time the report definition was created';

COMMENT ON COLUMN REPORT_DEFINITIONS.CREATED_BY
  IS 'The username identifying the user that created the report definition';

COMMENT ON COLUMN REPORT_DEFINITIONS.UPDATED
  IS 'The date and time the report definition was updated';

COMMENT ON COLUMN REPORT_DEFINITIONS.UPDATED_BY
  IS 'The username identifying the user that updated the report definition';  
  
  
  
-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (3000, 'ApplicationReporting.ReportDefinitionAdministration', 'Report Definition Administration', 'Report Definition Administration');
INSERT INTO FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (3001, 'ApplicationReporting.AddReportDefinition', 'Add Report Definition', 'Add Report Definition');
INSERT INTO FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (3002, 'ApplicationReporting.RemoveReportDefinition', 'Remove Report Definition', 'Remove Report Definition');
INSERT INTO FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (3003, 'ApplicationReporting.UpdateReportDefinition', 'Update Report Definition', 'Update Report Definition');
INSERT INTO FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
  (3004, 'ApplicationReporting.ViewReport', 'View Report', 'View Report');

INSERT INTO FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 3000);
INSERT INTO FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 3001);
INSERT INTO FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 3002);
INSERT INTO FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 3003);
INSERT INTO FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 3004);

INSERT INTO EXTERNAL_FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 3000);
INSERT INTO EXTERNAL_FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 3001);
INSERT INTO EXTERNAL_FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 3002);
INSERT INTO EXTERNAL_FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 3003);
INSERT INTO EXTERNAL_FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 3004);

INSERT INTO FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (2, 3004);

INSERT INTO EXTERNAL_FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (2, 3004);



-- -------------------------------------------------------------------------------------------------
-- SET PERMISSIONS
-- -------------------------------------------------------------------------------------------------
GRANT ALL ON TABLE REPORT_DEFINITIONS TO dbuser; 
