
DROP FUNCTION IF EXISTS bp.FindAllUser;
CREATE FUNCTION bp.FindAllUser(PageNumber INT, PageSize INT)
RETURNS TABLE (
			   id BIGINT,
			   username VARCHAR(50),
			   email VARCHAR(75),
			   first_name VARCHAR(50),
			   middle_name VARCHAR(50),
    		   last_name VARCHAR(50),
			   mobile_no VARCHAR(20),
			   picture_url VARCHAR(500),
			   confirmed BOOLEAN,
			   locked BOOLEAN,
    		   lock_expired_at TIMESTAMP WITHOUT TIME ZONE,
    		   enabled BOOLEAN,
    		   created_at TIMESTAMP WITHOUT TIME ZONE,
    		   last_updated_at  TIMESTAMP WITHOUT TIME ZONE,
    		   created_by VARCHAR(50),
    		   updated_by VARCHAR(50),
    		   version BIGINT
			  )
LANGUAGE SQL
AS
$$
	  SELECT
	    id,
        username,
        email,
        first_name,
        middle_name,
        last_name,
        mobile_no,
        picture_url,
        confirmed,
        locked,
        lock_expired_at,
        enabled,
        created_at,
        last_updated_at,
        created_by,
        updated_by,
        version
    FROM bp.users ORDER BY id
	LIMIT (PageSize + 1) OFFSET PageNumber;
$$;


DROP FUNCTION IF EXISTS bp.FindUser;
CREATE FUNCTION bp.FindUser(user_id BIGINT)
RETURNS TABLE (
			   id BIGINT,
			   username VARCHAR(50),
			   email VARCHAR(75),
			   first_name VARCHAR(50),
			   middle_name VARCHAR(50),
    		   last_name VARCHAR(50),
			   mobile_no VARCHAR(20),
			   picture_url VARCHAR(500),
			   password TEXT,
			   confirmed BOOLEAN,
			   locked BOOLEAN,
    		   lock_expired_at TIMESTAMP WITHOUT TIME ZONE,
    		   enabled BOOLEAN,
    		   created_at TIMESTAMP WITHOUT TIME ZONE,
    		   last_updated_at  TIMESTAMP WITHOUT TIME ZONE,
    		   created_by VARCHAR(50),
    		   updated_by VARCHAR(50),
    		   version BIGINT
			  )
LANGUAGE SQL
AS
$$
	  SELECT
	    id,
        username,
        email,
        first_name,
        middle_name,
        last_name,
        mobile_no,
        picture_url,
		password,
        confirmed,
        locked,
        lock_expired_at,
        enabled,
        created_at,
        last_updated_at,
        created_by,
        updated_by,
        version
    FROM bp.users WHERE id=user_id;
$$;


DROP FUNCTION IF EXISTS bp.SearchUsers;
CREATE FUNCTION bp.SearchUsers(SearchText Text, PageNumber INT, PageSize INT)
RETURNS TABLE (
			   id BIGINT,
			   username VARCHAR(50),
			   email VARCHAR(75),
			   first_name VARCHAR(50),
			   middle_name VARCHAR(50),
    		   last_name VARCHAR(50),
			   mobile_no VARCHAR(20),
			   picture_url VARCHAR(500),
			   confirmed BOOLEAN,
			   locked BOOLEAN,
    		   lock_expired_at TIMESTAMP WITHOUT TIME ZONE,
    		   enabled BOOLEAN,
    		   created_at TIMESTAMP WITHOUT TIME ZONE,
    		   last_updated_at  TIMESTAMP WITHOUT TIME ZONE,
    		   created_by VARCHAR(50),
    		   updated_by VARCHAR(50),
    		   version BIGINT,
			   rank REAL,
			   total_pages BIGINT
			  )
LANGUAGE SQL
AS
$$
	  SELECT
	    id,
        username,
        email,
        first_name,
        middle_name,
        last_name,
        mobile_no,
        picture_url,
        confirmed,
        locked,
        lock_expired_at,
        enabled,
        created_at,
        last_updated_at,
        created_by,
        updated_by,
        version,
        ts_rank_cd(search_field, query) AS rank,
		 count(*) OVER() AS total_pages
    FROM
        bp.users u,
        to_tsquery(SearchText) query
    WHERE
        query @@ search_field
    ORDER BY rank DESC
    LIMIT PageSize OFFSET PageNumber;
$$;

