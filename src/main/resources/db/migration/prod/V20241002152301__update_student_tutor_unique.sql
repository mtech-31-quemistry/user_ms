ALTER TABLE qms_user.student
DROP CONSTRAINT IF EXISTS unique_user_id_student;

ALTER TABLE qms_user.student
    ADD CONSTRAINT unique_user_id_student
        UNIQUE (user_id);

ALTER TABLE qms_user.tutor
DROP CONSTRAINT IF EXISTS unique_user_id_tutor;

ALTER TABLE qms_user.tutor
    ADD CONSTRAINT unique_user_id_tutor
        UNIQUE (user_id);
