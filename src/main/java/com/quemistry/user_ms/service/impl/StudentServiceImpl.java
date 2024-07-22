package com.quemistry.user_ms.service.impl;

import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.StudentInvitationDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;
import com.quemistry.user_ms.repository.ClassRepository;
import com.quemistry.user_ms.repository.StudentRepository;
import com.quemistry.user_ms.repository.TutorRepository;
import com.quemistry.user_ms.repository.UserRepository;
import com.quemistry.user_ms.repository.entity.Student;
import com.quemistry.user_ms.repository.entity.User;
import com.quemistry.user_ms.service.NotificationService;
import com.quemistry.user_ms.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;

import static com.quemistry.user_ms.constant.EmailConstant.STUDENT_INVITATION_TEMPLATE;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final TutorRepository tutorRepository;

    private final ClassRepository classRepository;

    private final NotificationService notificationService;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            UserRepository userRepository,
            TutorRepository tutorRepository,
            ClassRepository classRepository,
            NotificationService notificationService) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
        this.classRepository = classRepository;
        this.notificationService = notificationService;
    }

    @Override
    public StudentResponseDto updateStudentProfile(StudentDto student) {
        log.info("update student profile started");
        log.info("update student profile -> user id: {}", student.getUserId());

        var studentResponseDto = new StudentResponseDto();

        var userOptional = this.userRepository.findUserEntityByAccountId(student.getUserId());

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            Student existingStudent = this.studentRepository.findStudentEntityByUserEntityId(existingUser.getId());

            existingUser.setFirstName(student.getFirstName());
            existingUser.setLastName(student.getLastName());
            existingUser.setEmail(student.getEmail());
            existingUser.setModifiedBy(student.getUserId());
            existingUser.setModifiedOn(OffsetDateTime.now());
            this.userRepository.save(existingUser);

            existingStudent.setEducationLevel(student.getEducationLevel());
            existingStudent.setModifiedBy(student.getUserId());
            existingStudent.setModifiedOn(OffsetDateTime.now());
            this.studentRepository.save(existingStudent);

            studentResponseDto.setSuccess(true);
            log.info("update student profile finished");

            return studentResponseDto;
        }

        var userEntity = new User();
        var now = OffsetDateTime.now();
        userEntity.setAccountId(student.getUserId());
        userEntity.setFirstName(student.getFirstName());
        userEntity.setLastName(student.getLastName());
        userEntity.setEmail(student.getEmail());
        userEntity.setCreationAndModificationDetails(now, student.getUserId());
        this.userRepository.save(userEntity);

        var studentEntity = new Student();
        studentEntity.setUserEntity(userEntity);
        studentEntity.setEducationLevel(student.getEducationLevel());
        studentEntity.setCreationAndModificationDetails(now, student.getUserId());
        this.studentRepository.save(studentEntity);

        studentResponseDto.setSuccess(true);

        log.info("create student profile finished");
        return studentResponseDto;
    }

    /**
     * @param input          Student Invitation Object
     * @param tutorAccountId Tutor Account ID
     * @return flag
     */
    @Override
    public boolean sendInvitation(StudentInvitationDto input, String tutorAccountId) {

        log.info("Send invitation started");

        var tutorOptional = this.tutorRepository.findTutorByUserEntityAccountId(tutorAccountId);

        if (tutorOptional.isEmpty()) {
            log.error("tutor id (%s) not found".formatted(tutorAccountId));
            return false;
        }

        var classOptional = this.classRepository.findClassByCode(input.classCode());

        if (classOptional.isEmpty()) {
            log.error("class code (%s) not found".formatted(input.classCode()));
            return false;
        }

        var tutor = tutorOptional.get();
        var templateItems = new HashMap<String, String>();
        templateItems.put("%%student_name%%", input.studentFullName());
        templateItems.put("%%class_room%%", classOptional.get().getDescription());
        templateItems.put("%%tuition_center%%", tutor.getTuitionCentre());
        templateItems.put("%%invitation_url%%", "https://%s/student/accept-invitation");
        templateItems.put("%%tutor_name%%", tutor.getUserEntity().getFullName());


        boolean canSend = this.notificationService.sendEmailNotification(
                input.studentEmail(),
                STUDENT_INVITATION_TEMPLATE,
                templateItems);

        log.info("Send invitation ended");

        return canSend;
    }
}
