ALTER TABLE qms_user.tutor
ALTER COLUMN user_id TYPE INTEGER;

ALTER TABLE qms_user.tutor
DROP CONSTRAINT IF EXISTS fk_user_id;

ALTER TABLE qms_user.tutor
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id) REFERENCES "user"(id);


ALTER TABLE qms_user.student
ALTER COLUMN user_id TYPE INTEGER;

ALTER TABLE qms_user.student
DROP CONSTRAINT IF EXISTS fk_user_id;

ALTER TABLE qms_user.student
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id) REFERENCES "user"(id);