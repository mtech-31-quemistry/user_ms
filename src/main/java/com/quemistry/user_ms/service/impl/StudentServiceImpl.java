package com.quemistry.user_ms.service.impl;

import com.quemistry.user_ms.constant.ClassInvitationStatus;
import com.quemistry.user_ms.constant.UserConstant;
import com.quemistry.user_ms.helper.StringHelper;
import com.quemistry.user_ms.mapper.UserMapper;
import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.StudentInvitationDto;
import com.quemistry.user_ms.model.StudentProfileRequest;
import com.quemistry.user_ms.repository.*;
import com.quemistry.user_ms.repository.entity.Class;
import com.quemistry.user_ms.repository.entity.*;
import com.quemistry.user_ms.service.CryptoService;
import com.quemistry.user_ms.service.NotificationService;
import com.quemistry.user_ms.service.StudentService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.quemistry.user_ms.constant.EmailConstant.STUDENT_INVITATION_TEMPLATE;
import static com.quemistry.user_ms.constant.UserConstant.STATUS_ACTIVE;

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
    public StudentDto updateStudentProfile(StudentProfileRequest studentProfileRequest) {
        log.info("update student profile started");
        log.info("update student profile -> user id: {}", studentProfileRequest.getUserId());

        var userOptional = this.userRepository.findUserEntityByAccountId(studentProfileRequest.getUserId());

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            Student existingStudent = this.studentRepository.findStudentEntityByUserEntityId(existingUser.getId());

            existingUser.setFirstName(studentProfileRequest.getFirstName());
            existingUser.setLastName(studentProfileRequest.getLastName());
            existingUser.setEmail(studentProfileRequest.getEmail());
            existingUser.setModifiedBy(studentProfileRequest.getUserId());
            existingUser.setModifiedOn(OffsetDateTime.now());
            this.userRepository.save(existingUser);

            existingStudent.setEducationLevel(studentProfileRequest.getEducationLevel());
            existingStudent.setModifiedBy(studentProfileRequest.getUserId());
            existingStudent.setModifiedOn(OffsetDateTime.now());
            Student student = this.studentRepository.save(existingStudent);

            log.info("update student profile finished");

            return UserMapper.INSTANCE.studentToStudentDto(student);
        }

        var userEntity = new User();
        var now = OffsetDateTime.now();
        userEntity.setAccountId(studentProfileRequest.getUserId());
        userEntity.setFirstName(studentProfileRequest.getFirstName());
        userEntity.setLastName(studentProfileRequest.getLastName());
        userEntity.setEmail(studentProfileRequest.getEmail());
        userEntity.setCreationAndModifiedDetails(now, studentProfileRequest.getUserId());
        this.userRepository.save(userEntity);

        var studentEntity = new Student();
        studentEntity.setUserEntity(userEntity);
        studentEntity.setEducationLevel(studentProfileRequest.getEducationLevel());
        studentEntity.setCreationAndModifiedDetails(now, studentProfileRequest.getUserId());
        Student student = this.studentRepository.save(studentEntity);

//        studentResponseDto.setSuccess(true);

        log.info("create student profile finished");
        return UserMapper.INSTANCE.studentToStudentDto(student);
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

        String message;
        if (tutorOptional.isEmpty()) {
            message = "tutor id=%s not found".formatted(tutorAccountId);
            log.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
//            return false;
        }
        Optional<Class> classOptional = classRepository.findById(input.classId());

        if (classOptional.isEmpty()) {
            message = "class id=%s not found".formatted(input.classId());
            log.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
//            return false;
        }


        var tutor = tutorOptional.get();
        var classEntity = classOptional.get();


        var classInvitation = new ClassInvitation();
        classInvitation.setClassEntity(classEntity);
        classInvitation.setUserEmail(input.studentEmail().trim());
        classInvitation.setStatus(ClassInvitationStatus.INVITED);
        classInvitation.setUserType(UserConstant.USER_TYPE_STUDENT);
        classInvitation.setCode(StringHelper.getRandomString(10));
        this.classInvitationRepository.save(classInvitation);

        String encryptedClassCode = URLEncoder.encode(this.cryptoService.encrypt(classInvitation.getCode()), StandardCharsets.UTF_8);

        var templateItems = new HashMap<String, String>();
        templateItems.put("%%student_name%%", input.studentFullName().trim());
        templateItems.put("%%class_room%%", classEntity.getDescription().trim());
        templateItems.put("%%tuition_center%%", tutor.getTuitionCentre().trim());
        templateItems.put("%%invitation_url%%", "%s/students/invitation/accept?code=%s".formatted(this.frontendURL, encryptedClassCode));
        templateItems.put("%%tutor_name%%", tutor.getUserEntity().getFullName().trim());

        boolean canSend = this.notificationService.sendEmailNotification(
                input.studentEmail(),
                STUDENT_INVITATION_TEMPLATE,
                templateItems);


        log.info("Send invitation ended");

        return canSend;
    }

    @Override
    public boolean acceptInvitation(String studentEmail, String accountId, String code) throws Exception {

        log.info("Accept invitation started");
        log.debug("studentEmail: {}, accountId: {}, code: {}", studentEmail, accountId, code);
        String decoded = URLDecoder.decode(code, StandardCharsets.UTF_8);

        log.debug("decoded: {}, {}, {}", studentEmail, accountId, code);
        String invitationCode = this.cryptoService.decrypt(decoded);
        log.debug("decrypted invitationCode: {} ", invitationCode);
        Optional<ClassInvitation> classInvitationOptional = this.classInvitationRepository.findByCode(invitationCode);
        if (classInvitationOptional.isEmpty()) {
            String message = String.format("Class Invitation with code=%s not found", invitationCode);
            log.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        ClassInvitation classInvitation = classInvitationOptional.get();

        if (!classInvitation.getUserEmail().equals(studentEmail)) {
            String message = String.format("invited user (%s) and request user (%s) is not same", classInvitation.getUserEmail(), studentEmail);
            log.error(message);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
        }

        classInvitation.setStatus(ClassInvitationStatus.ACCEPTED);
        classInvitationRepository.save(classInvitation);

        Optional<User> userOptional = userRepository.findUserEntityByAccountId(accountId);
        Student student;
        if (userOptional.isEmpty()) {
            log.info("user with accountId={} not found", accountId);
            student = new Student(accountId, studentEmail);
            student = studentRepository.save(student);
        } else {
            Optional<Student> studentOptional = studentRepository.findStudentByUserEntityAccountId(accountId);
            if (studentOptional.isEmpty()) {
                String message = String.format("user with accountId=%s found, but not in student table", accountId);
                log.info(message);
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                student = new Student();
                student.setUserEntity(userOptional.get());
                student.setCreatedAndModifiedUser(accountId);
                student = studentRepository.save(student);
            } else {
                log.info("student with accountId={} found", accountId);
                student = studentOptional.get();
            }
        }
        var studentClassKey = new StudentClass.StudentClassKey();
        var studentClass = new StudentClass(studentClassKey, student, classInvitation.getClassEntity(), STATUS_ACTIVE);
        this.studentClassRepository.save(studentClass);

        log.info("Accept invitation ended");
        return true;
    }

    @Override
    public StudentDto getStudentProfile(String studentEmail) {
        StudentDto dto = new StudentDto();
        Optional<Student> optionalStudent = this.studentRepository.findStudentByUserEntityEmail(studentEmail);

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            dto.setEducationLevel(student.getEducationLevel());
            dto.setEmail(student.getUserEntity().getEmail());
            dto.setFirstName(student.getUserEntity().getFirstName());
            dto.setLastName(student.getUserEntity().getLastName());
            dto.setAccountId(student.getUserEntity().getAccountId());
        }

        return dto;
    }

    @Override
    public List<StudentDto> searchStudentProfile(List<String> studentEmails, List<String> studentAccountIds, String tutorEmail) {
        List<Student> students = studentRepository.findStudentByEmailOrAccountId(studentEmails, studentAccountIds, tutorEmail);
        if (students.isEmpty()) {
            String message = String.format("student not found for emails=%s, accountIds=%s, tutorEmail=%s", studentEmails, studentAccountIds, tutorEmail);
            log.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        return UserMapper.INSTANCE.studentsToStudentDtos(students);
    }
}
