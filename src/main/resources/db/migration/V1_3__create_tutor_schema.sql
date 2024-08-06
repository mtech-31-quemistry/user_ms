CREATE TABLE IF NOT EXISTS qms_user.tutor
(
    id              serial PRIMARY KEY,
    user_id         serial                 NOT NULL,
    education_level character varying(20)  NULL,
    tuition_centre  character varying(100) NOT NULL,
    created_on      timestamptz            NOT NULL,
    created_by      character varying(36)  NOT NULL,
    modified_on     timestamptz            NOT NULL,
    modified_by     character varying(36)  NOT NULL
);

INSERT INTO qms_user.user (account_id, email, first_name, last_name, created_on, created_by, modified_on, modified_by)
values ('8da3cf6e-2822-4ea9-8cfc-4a83f3c4b7a2',
        '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',
        now(), 'SYSTEM', now(),
        'SYSTEM');

INSERT INTO qms_user.tutor
(user_id, education_level, tuition_centre, created_on, created_by, modified_on, modified_by)
values ((select id from qms_user.user where account_id = '8da3cf6e-2822-4ea9-8cfc-4a83f3c4b7a2'), 'J1', 'Centre 1', now(),
        'SYSTEM', now(), 'SYSTEM');
