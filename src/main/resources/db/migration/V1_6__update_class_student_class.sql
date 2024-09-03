ALTER TABLE qms_user.student_class
    ADD COLUMN created_on timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    ADD COLUMN created_by character varying(36) DEFAULT 'SYSTEM',
    ADD COLUMN modified_on timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN modified_by character varying(36) DEFAULT 'SYSTEM';

ALTER TABLE qms_user.class
    ADD COLUMN status VARCHAR;

ALTER TABLE qms_user.tutor_class
    DROP COLUMN status,
    ADD COLUMN created_on timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN created_by character varying(36) DEFAULT 'SYSTEM',
    ADD COLUMN modified_on timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN modified_by character varying(36) DEFAULT 'SYSTEM';