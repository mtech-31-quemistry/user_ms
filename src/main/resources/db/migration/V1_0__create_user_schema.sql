CREATE TABLE IF NOT EXISTS qms_user.user
(
    id serial PRIMARY KEY,
    account_id VARCHAR(255) UNIQUE,
    email VARCHAR,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    created_on timestamptz NOT NULL,
    created_by character varying(36) NOT NULL,
    modified_on timestamptz NOT NULL,
    modified_by character varying(36) NOT NULL
);

