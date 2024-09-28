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
       'M2IyZTFhMWU5YzFiNGIzYc+dGDUgqT+h+a+mem0hs6G7v3eMxkVGndvKftG0Ug==',
       'M2IyZTFhMWU5YzFiNGIzYe+dGDUgnRSp4rD8V2MhZNDRHCtladfGIrV38SxRsmE=',
       'M2IyZTFhMWU5YzFiNGIzYe+dGDUgpTOz5I3pdGeMd36xHlke7DIa+P+SfxAA',
       now(), 'mock', now(), 'mock');

INSERT INTO qms_user.tutor
(id, user_id, education_level, tuition_centre, created_on, created_by, modified_on, modified_by)
VALUES(9999, 9999, 'J1', 'tuitionCentre', now(), 'mock', now(), 'mock');



-- STUDENT
INSERT INTO qms_user."user"
(id, account_id, email, first_name, last_name, created_on, created_by, modified_on, modified_by)
VALUES(8888, '81904eaf-f42d-4bf1-98a4-0bb99df06c2b',
       'M2IyZTFhMWU5YzFiNGIzYcicGT43hyaA/aLhdSwvbg49a6euVi94/J7LKoLdBVp2',
       'M2IyZTFhMWU5YzFiNGIzYeicGT43hyaG+bH7bUwtbAbYjLLTqWI7vsyJUlu6OmD/',
       'M2IyZTFhMWU5YzFiNGIzYeicGT43hyaM8bD8V2MhZIOPS28oXoJPjUhIMuXvdWA=',
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
VALUES(7777, 'M2IyZTFhMWU5YzFiNGIzYcicGT43hyaA/aLhdSwvbg49a6euVi94/J7LKoLdBVp2', 7777, 'accepted', 2, 'TGQHPNORA8');
