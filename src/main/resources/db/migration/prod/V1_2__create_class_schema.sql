CREATE TABLE IF NOT EXISTS qms_user.class
(
    id              serial PRIMARY KEY,
    code            varchar(50)           NOT NULL unique,
    description     varchar(100)          NULL,
    subject         varchar(50)           NULL,
    education_level varchar(50)           NULL,
    created_on      timestamptz           NOT NULL,
    created_by      character varying(36) NOT NULL,
    modified_on     timestamptz           NOT NULL,
    modified_by     character varying(36) NOT NULL
);
