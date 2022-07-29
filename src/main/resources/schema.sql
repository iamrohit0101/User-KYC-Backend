
drop table if exists country;
CREATE TABLE country (
                         id   INTEGER      NOT NULL AUTO_INCREMENT,
                         name VARCHAR(128) NOT NULL,
                         PRIMARY KEY (id)
);

--     IF NOT EXISTS
-- BEGIN
--     -- The schema must be run in its own batch!




-- IF NOT EXISTS (SELECT TOP (1) 1 FROM [sys].[schemas] WHERE [name] = 'country')
-- BEGIN
-- EXEC ('CREATE SCHEMA [country]')
-- END
