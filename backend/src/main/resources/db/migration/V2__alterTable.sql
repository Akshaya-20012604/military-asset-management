ALTER TABLE audit_logs
ALTER COLUMN performed_by TYPE BIGINT USING performed_by::bigint;
