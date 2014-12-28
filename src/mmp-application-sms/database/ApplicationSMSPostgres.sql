-- ---------------------------------------------------------------------------------------------------------------------------------
-- 
--      _                _ _           _   _             ____  __  __ ____  ____           _                                  _ 
--     / \   _ __  _ __ | (_) ___ __ _| |_(_) ___  _ __ / ___||  \/  / ___||  _ \ ___  ___| |_ __ _ _ __ ___  ___   ___  __ _| |
--    / _ \ | '_ \| '_ \| | |/ __/ _` | __| |/ _ \| '_ \\___ \| |\/| \___ \| |_) / _ \/ __| __/ _` | '__/ _ \/ __| / __|/ _` | |
--   / ___ \| |_) | |_) | | | (_| (_| | |_| | (_) | | | |___) | |  | |___) |  __/ (_) \__ \ || (_| | | |  __/\__ \_\__ \ (_| | |
--  /_/   \_\ .__/| .__/|_|_|\___\__,_|\__|_|\___/|_| |_|____/|_|  |_|____/|_|   \___/|___/\__\__, |_|  \___||___(_)___/\__, |_|
--          |_|   |_|                                                                         |___/                        |_|  
-- 
-- ---------------------------------------------------------------------------------------------------------------------------------

-- -------------------------------------------------------------------------------------------------
-- DROP EXISTING TABLES
-- -------------------------------------------------------------------------------------------------
DROP TABLE SMS;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE SMS (
  ID              BIGINT NOT NULL,
  MOBILE_NUMBER   VARCHAR(40) NOT NULL,
  MESSAGE         VARCHAR(1024) NOT NULL,
  STATUS          INTEGER NOT NULL,
  SEND_ATTEMPTS   INTEGER NOT NULL,
  LOCK_NAME       VARCHAR(100),
  LAST_PROCESSED  TIMESTAMP,
  
  PRIMARY KEY (ID)
);
  
COMMENT ON COLUMN SMS.ID
  IS 'The ID used to uniquely identify the SMS';

COMMENT ON COLUMN SMS.MOBILE_NUMBER
  IS 'The mobile number to send the SMS to';

COMMENT ON COLUMN SMS.MESSAGE
  IS 'The message to send';
  
COMMENT ON COLUMN SMS.STATUS
  IS 'The status of the SMS';
  
COMMENT ON COLUMN SMS.SEND_ATTEMPTS
  IS 'The number of times that the sending of the SMS was attempted';

COMMENT ON COLUMN SMS.LOCK_NAME
  IS 'The name of the entity that has locked the SMS for sending';

COMMENT ON COLUMN SMS.LAST_PROCESSED
  IS 'The date and time the last attempt was made to send the SMS';
  
  
  
-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
--INSERT INTO FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES
--  (4000, 'Application.NameOfSMSFunction', 'Name of SMS Function', 'Name of SMS Function');

--INSERT INTO FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 4000);

--INSERT INTO FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (2, 4000);

--INSERT INTO EXTERNAL_FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (1, 4000);

--INSERT INTO EXTERNAL_FUNCTION_GROUP_MAP (GROUP_ID, FUNCTION_ID) VALUES (2, 4000);  
  

  
-- -------------------------------------------------------------------------------------------------
-- SET PERMISSIONS
-- -------------------------------------------------------------------------------------------------
GRANT ALL ON TABLE SMS TO dbuser; 


