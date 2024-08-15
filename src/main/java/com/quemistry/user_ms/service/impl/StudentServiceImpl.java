package com.quemistry.user_ms.service.impl;

import com.quemistry.user_ms.constant.EmailConstant;
import com.quemistry.user_ms.constant.UserConstant;
import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.StudentInvitationDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;
import com.quemistry.user_ms.repository.*;
import com.quemistry.user_ms.repository.entity.ClassInvitation;
import com.quemistry.user_ms.repository.entity.Student;
import com.quemistry.user_ms.repository.entity.User;
import com.quemistry.user_ms.service.CryptoService;
import com.quemistry.user_ms.service.NotificationService;
import com.quemistry.user_ms.service.StudentService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.HashMap;

import static com.quemistry.user_ms.constant.EmailConstant.STUDENT_INVITATION_TEMPLATE;

@Slf4j
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final TutorRepository tutorRepository;

    private final ClassRepository classRepository;

    private final StudentClassRepository studentClassRepository;

    private final ClassInvitationRepository classInvitationRepository;

    private final NotificationService notificationService;

    private final CryptoService cryptoService;

    private final String frontendURL;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            UserRepository userRepository,
            TutorRepository tutorRepository,
            ClassRepository classRepository,
            NotificationService notificationService,
            StudentClassRepository studentClassRepository,
            ClassInvitationRepository classInvitationRepository,
            CryptoService cryptoService,
            @Value("${quemistry.user.front-end.url}") String frontendURL) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
        this.classRepository = classRepository;
        this.notificationService = notificationService;
        this.studentClassRepository = studentClassRepository;
        this.classInvitationRepository = classInvitationRepository;
        this.cryptoService = cryptoService;
        this.frontendURL = frontendURL;
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
    public boolean sendInvitation(StudentInvitationDto input, String tutorAccountId) throws Exception {

        log.info("Send invitation started");

        var tutorOptional = this.tutorRepository.findTutorByUserEntityAccountId(tutorAccountId);

        if (tutorOptional.isEmpty()) {
            log.error("tutor id (%s) not found".formatted(tutorAccountId));
            return false;
        }

        var classOptional = this.classRepository.findAllByCode(input.classCode());

        if (classOptional.isEmpty()) {
            log.error("class code (%s) not found".formatted(input.classCode()));
            return false;
        }

        var tutor = tutorOptional.get();
        var classEntity = classOptional.get();
        String encryptedClassCode = URLEncoder.encode(this.cryptoService.encrypt(classEntity.getCode()), StandardCharsets.UTF_8);

        var templateItems = new HashMap<String, String>();
        templateItems.put("%%student_name%%", input.studentFullName());
        templateItems.put("%%class_room%%", classEntity.getDescription());
        templateItems.put("%%tuition_center%%", tutor.getTuitionCentre());
        templateItems.put("%%invitation_url%%", "%s/student/accept-invitation?code=%s".formatted(this.frontendURL, encryptedClassCode));
        templateItems.put("%%tutor_name%%", tutor.getUserEntity().getFullName());

        boolean canSend = this.notificationService.sendEmailNotification(
                input.studentEmail(),
                STUDENT_INVITATION_TEMPLATE,
                templateItems);


        var classInvitation = new ClassInvitation();
        classInvitation.setClassEntity(classEntity);
        classInvitation.setUserEmail(input.studentEmail());
        classInvitation.setStatus(EmailConstant.STATUS_SENT);
        classInvitation.setUserType(UserConstant.USER_TYPE_STUDENT);
        this.classInvitationRepository.save(classInvitation);


        log.info("Send invitation ended");

        return canSend;
    }
}
