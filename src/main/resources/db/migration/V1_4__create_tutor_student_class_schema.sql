CREATE TABLE IF NOT EXISTS qms_user.student_class
(
    student_id INT REFERENCES qms_user.student (id),
    class_id   INT         NULL,
    status     varchar(25) NOT NULL,
    PRIMARY KEY (student_id, class_id),
    CONSTRAINT fk_student_class_student FOREIGN KEY (student_id) REFERENCES qms_user.student (id),
    CONSTRAINT fk_student_class_class FOREIGN KEY (class_id) REFERENCES qms_user.class (id)
);

CREATE TABLE IF NOT EXISTS qms_user.tutor_class
(
    tutor_id INT REFERENCES qms_user.tutor (id),
    class_id INT REFERENCES qms_user.class (id),
    status   varchar(25) NOT NULL,
    PRIMARY KEY (tutor_id, class_id),
    CONSTRAINT fk_tutor_class_tutor FOREIGN KEY (tutor_id) REFERENCES qms_user.tutor (id),
    CONSTRAINT fk_tutor_class_class FOREIGN KEY (class_id) REFERENCES qms_user.class (id)
);

CREATE TABLE IF NOT EXISTS qms_user.class_invitation
(
    id         serial PRIMARY KEY,
    user_email VARCHAR,
    class_id   int         NOT NULL,
    status     varchar(25) NOT NULL,
    user_type  INT         NOT NULL,
    UNIQUE (user_email, class_id),
    CONSTRAINT fk_student_class_class FOREIGN KEY (class_id) REFERENCES qms_user.class (id)
);