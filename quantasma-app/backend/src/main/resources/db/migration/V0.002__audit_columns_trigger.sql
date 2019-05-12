-- trigger updates audit columns
CREATE OR REPLACE FUNCTION audit_columns_trigger()
  RETURNS TRIGGER
AS $$
BEGIN
  IF (TG_OP = 'UPDATE') THEN
    IF (NOT OLD.x_active AND NEW.x_active) THEN
      -- recover
      NEW.x_updated_on = NOW();
      NEW.x_deleted_on = NULL;
    ELSEIF (OLD.x_active AND NOT NEW.x_active) THEN
      -- soft-delete
      NEW.x_updated_on = OLD.x_updated_on;
      NEW.x_deleted_on = NOW();
    ELSE
      -- regular update
      NEW.x_updated_on = NOW();
      NEW.x_deleted_on = OLD.x_deleted_on;
    END IF;

    NEW.x_created_on = OLD.x_created_on;
  ELSIF (TG_OP = 'INSERT') THEN
    -- insert
    NEW.x_created_on = NOW();
    NEW.x_updated_on = NULL;

    IF (NEW.x_active) THEN
      -- new record
      NEW.x_deleted_on = NULL;
    ELSE
      -- soft-deleted from start
      NEW.x_deleted_on = now();
    END IF;
  END IF;

  RETURN NEW;
END;
$$ language plpgsql;


CREATE OR REPLACE FUNCTION create_audit_columns_trigger(
  schema_name TEXT,
  table_name TEXT
)
  RETURNS VOID
AS $$
  DECLARE full_table_name TEXT = COALESCE(NULLIF(schema_name, 'public') || '.', '') || table_name;
BEGIN
  EXECUTE format('DROP TRIGGER IF EXISTS audit_columns_trigger ON %I',
                  full_table_name);

  EXECUTE format('CREATE TRIGGER audit_columns_trigger BEFORE INSERT OR UPDATE ON %I
                  FOR EACH ROW EXECUTE PROCEDURE audit_columns_trigger()',
                  full_table_name);
END; $$
language plpgsql;


CREATE OR REPLACE FUNCTION tables_containing_audit_columns()
  RETURNS TABLE (
    schema_name TEXT,
    table_name TEXT
  )
AS $$
BEGIN
  RETURN QUERY
    SELECT
      quote_ident(ic.table_schema) as schema_name,
      quote_ident(ic.table_name) as table_name
    FROM information_schema.columns ic
    WHERE
      ic.table_schema NOT IN ('pg_catalog', 'information_schema')
      AND ic.table_schema NOT LIKE 'pg_toast%'
      AND ic.column_name in ('x_created_on', 'x_updated_on', 'x_deleted_on', 'x_active')
    GROUP BY ic.table_schema, ic.table_name
    HAVING count(1) = 4;
END;
$$ language plpgsql;


DO $$
DECLARE
  r RECORD;
BEGIN
  FOR r IN
    SELECT * from tables_containing_audit_columns()
  LOOP
    EXECUTE create_audit_columns_trigger(r.schema_name, r.table_name);
  END LOOP;
END;
$$ LANGUAGE plpgsql;
