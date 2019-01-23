CREATE USER ksbylending_user PASSWORD 'xxxxxxxx';
CREATE DATABASE ksbylending OWNER ksbylending_user TEMPLATE template0 ENCODING 'UTF8' LC_COLLATE 'C' LC_CTYPE 'C';

-- postgres_exporter user
CREATE USER postgres_exporter PASSWORD 'zzzzzzzz';
ALTER USER postgres_exporter SET SEARCH_PATH TO postgres_exporter,pg_catalog;
-- If deploying as non-superuser (for example in AWS RDS), uncomment the GRANT
-- line below and replace <MASTER_USER> with your root user.
-- GRANT postgres_exporter TO <MASTER_USER>
CREATE SCHEMA postgres_exporter AUTHORIZATION postgres_exporter;
CREATE VIEW postgres_exporter.pg_stat_activity
AS
SELECT * from pg_catalog.pg_stat_activity;
GRANT SELECT ON postgres_exporter.pg_stat_activity TO postgres_exporter;
CREATE VIEW postgres_exporter.pg_stat_replication AS
SELECT * from pg_catalog.pg_stat_replication;
GRANT SELECT ON postgres_exporter.pg_stat_replication TO postgres_exporter;
