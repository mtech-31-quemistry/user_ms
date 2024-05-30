CREATE TABLE IF NOT EXISTS member.student
(
    id serial PRIMARY KEY,
    user_id serial NOT NULL,
    education_level character varying(20) NOT NULL,
    created_on timestamp NOT NULL,
    created_by character varying(20) NOT NULL,
    modified_on timestamp NOT NULL,
    modified_by character varying(20) NOT NULL
);
