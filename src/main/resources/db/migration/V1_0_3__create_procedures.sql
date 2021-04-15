DROP PROCEDURE IF EXISTS bp.CreateUser;
CREATE PROCEDURE bp.CreateUser
(
	id INOUT BIGINT,
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
    created_by VARCHAR(50)
)
LANGUAGE plpgsql AS
$$
BEGIN
   INSERT INTO bp.Users (
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
    created_by
   ) VALUES  (
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
    created_by
    ) RETURNING bp.Users.id INTO id;
END
$$;


DROP PROCEDURE IF EXISTS bp.UpdateUser;
CREATE PROCEDURE bp.UpdateUser
(
	user_id BIGINT,
    user_username VARCHAR(50),
    user_email VARCHAR(75),
    user_first_name VARCHAR(50),
    user_middle_name VARCHAR(50),
    user_last_name VARCHAR(50),
    user_mobile_no VARCHAR(20),
    user_picture_url VARCHAR(500),
    user_confirmed BOOLEAN,
    user_locked BOOLEAN,
    user_lock_expired_at TIMESTAMP WITHOUT TIME ZONE,
    user_enabled BOOLEAN,
    user_updated_by VARCHAR(50),
	user_version BIGINT,
	RETURN_VALUE INOUT INT
)
LANGUAGE plpgsql AS
$$
DECLARE
existing_version bp.Users.version%type;
BEGIN

	SELECT bp.Users.version INTO existing_version FROM bp.Users WHERE id= user_id;

  IF NOT FOUND THEN
	  RAISE 'user with id % not found', user_id;
   ELSEIF existing_version != user_version THEN
	  RAISE 'user details had been changed,re-fetch to see the new changes';
   END IF;

WITH rows AS (
    UPDATE bp.Users SET
	username=user_username,
    email=user_email,
    first_name=user_first_name,
    middle_name=user_middle_name,
    last_name=user_last_name,
    mobile_no=user_mobile_no,
    picture_url=user_picture_url,
    confirmed=user_confirmed,
    locked=user_locked,
    lock_expired_at=user_lock_expired_at,
    enabled=user_enabled,
    last_updated_at = NOW(),
    updated_by = user_updated_by,
	version = version + 1
	WHERE id=user_id AND version = user_version
	RETURNING 1
)
SELECT count(*) INTO RETURN_VALUE FROM rows;
END
$$;


DROP PROCEDURE IF EXISTS bp.deleteUser;
CREATE PROCEDURE bp.deleteUser(user_id BIGINT,	user_version BIGINT, RETURN_VALUE INOUT INT)
LANGUAGE plpgsql AS
$$
DECLARE
existing_version bp.Users.version%type;
BEGIN

	SELECT bp.Users.version INTO existing_version FROM bp.Users WHERE id= user_id;
	  IF NOT FOUND THEN
		  RAISE 'user with id % not found', user_id;
	   ELSEIF existing_version != user_version THEN
		  RAISE 'user details had been changed,refetch to see new changes';
	   END IF;

WITH rows AS (
    DELETE FROM bp.Users
	WHERE id=user_id AND version = user_version
	RETURNING 1
)
SELECT count(*) INTO RETURN_VALUE FROM rows;
END
$$;


DROP FUNCTION IF EXISTS bp.deleteUserReturningUser;
CREATE FUNCTION bp.deleteUserReturningUser(user_id BIGINT,	user_version BIGINT)
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
LANGUAGE SQL AS
$$
   --DECLARE existing_version bp.Users.version%type;

	--SELECT bp.Users.version INTO existing_version FROM bp.Users WHERE id= user_id;
	--  IF NOT FOUND THEN
	--	  RAISE 'user with id % not found', user_id;
	--   ELSEIF existing_version != user_version THEN
	--	  RAISE 'user details had been changed,refetch to see new changes';
	 --  END IF;

    WITH deleted as (DELETE FROM bp.Users
	WHERE id=user_id AND version = user_version
	RETURNING *)
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
    FROM deleted;
$$;