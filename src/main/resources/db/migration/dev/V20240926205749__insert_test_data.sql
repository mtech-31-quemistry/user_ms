DELETE FROM qms_user.student_class where created_by = 'mock';
DELETE FROM qms_user.tutor_class where created_by = 'mock';
DELETE FROM qms_user.class_invitation where id = 7777;
DELETE FROM qms_user.class where created_by = 'mock';
DELETE FROM qms_user.student where created_by = 'mock';
DELETE FROM qms_user.tutor where created_by = 'mock';
DELETE FROM qms_user.user where created_by = 'mock';

-- TUTOR
INSERT INTO qms_user."user"
(id, account_id, email, first_name, last_name, created_on, created_by, modified_on, modified_by)
VALUES(9999, 'be2876df-6b26-463f-9f66-54ba5b776011',
       'dXMzciFtaWNyMHNlcnZpYzPQwcLzaBnUPyay5pWA2J+Eg5yXiHpdqagFKjCVk6U=',
       'dXMzciFtaWNyMHNlcnZpYzPMwNPobjHMLC6kqYGv0osVt/LMh3KocL3D7xLa8mVcxNWxzl7EtQ==',
       'dXMzciFtaWNyMHNlcnZpYzP21c/xdTfdOiu6am5V3h5XXj0G9FlCl5bLyg==',
       now(), 'mock', now(), 'mock');

INSERT INTO qms_user.tutor
(id, user_id, education_level, tuition_centre, created_on, created_by, modified_on, modified_by)
VALUES(9999, 9999, 'J1', 'tuitionCentre', now(), 'mock', now(), 'mock');



-- STUDENT
INSERT INTO qms_user."user"
(id, account_id, email, first_name, last_name, created_on, created_by, modified_on, modified_by)
VALUES(8888, '81904eaf-f42d-4bf1-98a4-0bb99df06c2b',
       'dXMzciFtaWNyMHNlcnZpYzPXwMP4fzfNHiK/oZrB1okZRc2cmifZ3TjmGFwnx6RZxw==',
       'dXMzciFtaWNyMHNlcnZpYzP3wMP4fzfNGCasu4Kh1IsREKd436Iq+Fmv/vil+62cXA==',
       'dXMzciFtaWNyMHNlcnZpYzP3wMP4fzfNEi6tvLiO2IPcDZ1dqOot4rQPU90vEDpu',
       now(), 'mock', now(), 'mock');

INSERT INTO qms_user.student
(id, user_id, education_level, created_on, created_by, modified_on, modified_by)
VALUES(8888, 8888, 'J1', now(), 'mock', now(), 'mock');


-- CLASS
INSERT INTO qms_user."class"
(id, description, subject, education_level, created_on, created_by, modified_on, modified_by, status, start_date, end_date)
VALUES(7777,  'classDescription', 'Chemistry', 'J1', now(), 'mock', now(), 'mock', 'active',  now(),  now());

INSERT INTO qms_user.student_class
(student_id, class_id, status, created_on, created_by, modified_on, modified_by)
VALUES(8888, 7777, 'active', now(), 'mock', now(), 'mock');

INSERT INTO qms_user.tutor_class
(tutor_id, class_id, created_on, created_by, modified_on, modified_by)
VALUES(9999, 7777, now(), 'mock', now(), 'mock');


INSERT INTO qms_user.class_invitation
(id, user_email, class_id, status, user_type, code)
VALUES(7777, 'dXMzciFtaWNyMHNlcnZpYzPXwMP4fzfNHiK/oZrB1okZRc2cmifZ3TjmGFwnx6RZxw==', 7777, 'accepted', 2, 'TGQHPNORA8');
