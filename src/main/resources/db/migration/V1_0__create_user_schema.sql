CREATE TABLE IF NOT EXISTS member.user
(
    id serial PRIMARY KEY,
    account_id VARCHAR(255) UNIQUE,
    email VARCHAR(255),
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    created_on timestamptz NOT NULL,
    created_by character varying(36) NOT NULL,
    modified_on timestamptz NOT NULL,
    modified_by character varying(36) NOT NULL
);

