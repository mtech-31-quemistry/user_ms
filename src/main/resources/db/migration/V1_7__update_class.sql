

ALTER TABLE qms_user.class
    RENAME COLUMN class_start_ts TO start_date;

ALTER TABLE qms_user.class
    RENAME COLUMN class_end_ts TO end_date;

