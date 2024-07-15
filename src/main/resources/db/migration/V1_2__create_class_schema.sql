CREATE TABLE IF NOT EXISTS member.class
(
    id              serial PRIMARY KEY,
    name            varchar(50)           NOT NULL,
    description     varchar(100)          NULL,
    subject         varchar(50)           NULL,
    education_level varchar(50)           NULL,
    created_on      timestamptz           NOT NULL,
    created_by      character varying(36) NOT NULL,
    modified_on     timestamptz           NOT NULL,
    modified_by     character varying(36) NOT NULL
);
