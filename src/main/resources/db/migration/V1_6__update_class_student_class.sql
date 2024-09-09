ALTER TABLE qms_user.student_class
    ADD COLUMN created_on timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    ADD COLUMN created_by character varying(36) DEFAULT 'system',
    ADD COLUMN modified_on timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN modified_by character varying(36) DEFAULT 'system';

ALTER TABLE qms_user.class
    ADD COLUMN status VARCHAR,
    ADD COLUMN class_start_ts timestamptz
    ADD COLUMN class_end_ts timestamptz;

ALTER TABLE qms_user.tutor_class
    DROP COLUMN status,
    ADD COLUMN created_on timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN created_by character varying(36) DEFAULT 'system',
    ADD COLUMN modified_on timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN modified_by character varying(36) DEFAULT 'system';

ALTER TABLE qms_user."user"
    ALTER COLUMN first_name DROP NOT NULL,
    ALTER COLUMN last_name DROP NOT NULL,
    ALTER COLUMN created_on SET DEFAULT NOW(),
    ALTER COLUMN created_by SET DEFAULT 'system',
    ALTER COLUMN modified_on SET DEFAULT NOW(),
    ALTER COLUMN modified_by SET DEFAULT 'system';

ALTER TABLE qms_user.student
    ALTER COLUMN education_level DROP NOT NULL,
    ALTER COLUMN created_on SET DEFAULT NOW(),
    ALTER COLUMN created_by SET DEFAULT 'system',
    ALTER COLUMN modified_on SET DEFAULT NOW(),
    ALTER COLUMN modified_by SET DEFAULT 'system';


ALTER TABLE qms_user.tutor
    ALTER COLUMN created_on SET DEFAULT NOW(),
    ALTER COLUMN created_by SET DEFAULT 'system',
    ALTER COLUMN modified_on SET DEFAULT NOW(),
    ALTER COLUMN modified_by SET DEFAULT 'system';
