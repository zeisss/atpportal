ALTER TABLE atp DROP COLUMN path;
ALTER TABLE atp ADD COLUMN atp_name character varying(255);
ALTER TABLE atp ADD COLUMN atp_version character varying(20);

-- Tabelle: atp_option
-- Remove description column, this now comes from the atp implementation itself
ALTER TABLE atp_option DROP COLUMN option_description;
