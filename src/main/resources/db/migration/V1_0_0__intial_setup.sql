CREATE SCHEMA IF NOT EXISTS bp;

CREATE SEQUENCE IF NOT EXISTS bp.users_id_seq AS BIGINT;
CREATE TABLE IF NOT EXISTS bp.users(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('bp.users_id_seq'::regclass),
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(75) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50) NULL,
    last_name VARCHAR(50) NOT NULL,
    picture_url VARCHAR(500) NULL,
    password TEXT NOT NULL,
    is_confirmed BOOLEAN NOT NULL DEFAULT FALSE,
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    lock_expiry_date TIMESTAMP WITHOUT TIME ZONE,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    last_updated  TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(50) NOT NULL,
    search_field TSVECTOR NOT NULL,
    version BIGINT NULL
);

CREATE SEQUENCE IF NOT EXISTS bp.user_verification_id_seq AS BIGINT;
CREATE TABLE IF NOT EXISTS bp.user_verification(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('bp.user_verification_id_seq'::regclass),
    user_id BIGINT REFERENCES bp.users(id),
    token TEXT NOT NULL UNIQUE,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS bp.refresh_token_id_seq AS BIGINT;
CREATE TABLE IF NOT EXISTS bp.refresh_token(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('bp.refresh_token_id_seq'::regclass),
    user_id BIGINT REFERENCES bp.users(id),
    token TEXT NOT NULL UNIQUE,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);


CREATE SEQUENCE IF NOT EXISTS bp.roles_id_seq AS BIGINT;
CREATE TABLE IF NOT EXISTS bp.roles
(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('bp.roles_id_seq'::regclass),
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(100),
    created_by VARCHAR(50) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    version BIGINT NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS bp.permissions_id_seq AS BIGINT;
CREATE TABLE IF NOT EXISTS bp.permissions
(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('bp.permissions_id_seq'::regclass),
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(100),
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(50) NOT NULL,
    version BIGINT NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS bp.role_permissions_id_seq AS BIGINT;
CREATE TABLE IF NOT EXISTS bp.role_permissions(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('bp.role_permissions_id_seq'::regclass),
    role_id BIGINT REFERENCES bp.roles(id),
    permission_id BIGINT REFERENCES bp.permissions(id),
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(50) NOT NULL,
    version BIGINT NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_role_permissions ON bp.role_permissions(role_id, permission_id);

CREATE SEQUENCE IF NOT EXISTS bp.user_roles_id_seq AS BIGINT;
CREATE TABLE IF NOT EXISTS bp.user_roles(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('bp.user_roles_id_seq'::regclass),
    role_id BIGINT REFERENCES bp.roles(id),
    user_id BIGINT REFERENCES bp.users(id),
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(50) NOT NULL,
    version BIGINT NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_roles ON bp.user_roles(role_id, user_id);
