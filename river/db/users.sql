-- River Database User Instantiation
-- Mark Xia (mxia@lolRiver.com)

-- This should be run after git clone in order to get Flyway working
-- mysql -uroot < users.sql

-- The 'river' user is the default production user for the River databases.
CREATE USER 'river'@'%'
  IDENTIFIED BY 'river';
CREATE USER 'river'@'localhost'
  IDENTIFIED BY 'river';

-- The 'river_test' user only works with the elophant database.
CREATE USER 'river_test'@'%'
  IDENTIFIED BY 'river_test';
CREATE USER 'river_test'@'localhost'
  IDENTIFIED BY 'river_test';

-- The 'river' has basic permissions: no schema modification
GRANT
ALTER,
CREATE,
CREATE TEMPORARY TABLES,
DELETE,
DROP,
INDEX,
INSERT,
LOCK TABLES,
EXECUTE,
SELECT,
UPDATE
ON river.*
TO 'river'@'%', 'river'@'localhost'
IDENTIFIED BY 'river';

-- The 'river_test' user can do what ever it wants with the elophant database.
GRANT
ALL
PRIVILEGES
ON river_test.*
TO 'river_test'@'%', 'river_test'@'localhost'
IDENTIFIED BY 'river_test';