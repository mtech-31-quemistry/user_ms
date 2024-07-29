CREATE TABLE IF NOT EXISTS member.student_class
(
    student_id INT REFERENCES member.student (id),
    class_id   INT REFERENCES member.class (id),
    status     INT,
    PRIMARY KEY (student_id, class_id),
    CONSTRAINT fk_student_class_student FOREIGN KEY (student_id) REFERENCES member.student (id),
    CONSTRAINT fk_student_class_class FOREIGN KEY (class_id) REFERENCES member.class (id)
);

CREATE TABLE IF NOT EXISTS member.tutor_class
(
    tutor_id INT REFERENCES member.tutor (id),
    class_id INT REFERENCES member.class (id),
    status   INT,
    PRIMARY KEY (tutor_id, class_id),
    CONSTRAINT fk_tutor_class_tutor FOREIGN KEY (tutor_id) REFERENCES member.tutor (id),
    CONSTRAINT fk_tutor_class_class FOREIGN KEY (class_id) REFERENCES member.class (id)
);
