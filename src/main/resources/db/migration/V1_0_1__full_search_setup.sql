CREATE INDEX IF NOT EXISTS users_search_field_idx  ON bp.users USING GIN(search_field);

 CREATE OR REPLACE FUNCTION bp.users_tsvector_trigger() RETURNS trigger AS $$
            begin
            new.search_field := setweight(to_tsvector('english', coalesce(new.last_name, '')), 'A')
            || setweight(to_tsvector('english', coalesce(new.first_name, '')), 'B')
            || setweight(to_tsvector('english', coalesce(new.middle_name, '')), 'C');
            return new;
            end
            $$ LANGUAGE plpgsql;

CREATE TRIGGER tsvectorUsersUpdateSearchField BEFORE INSERT OR UPDATE
                ON bp.users FOR EACH ROW EXECUTE PROCEDURE bp.users_tsvector_trigger();


