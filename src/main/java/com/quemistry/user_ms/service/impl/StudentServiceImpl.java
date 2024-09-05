package com.quemistry.user_ms.service.impl;

import com.quemistry.user_ms.constant.ClassInvitationStatus;
import com.quemistry.user_ms.constant.UserConstant;
import com.quemistry.user_ms.helper.StringHelper;
import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.StudentInvitationDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;
import com.quemistry.user_ms.repository.ClassInvitationRepository;
import com.quemistry.user_ms.repository.ClassRepository;
import com.quemistry.user_ms.repository.StudentClassRepository;
import com.quemistry.user_ms.repository.StudentRepository;
import com.quemistry.user_ms.repository.TutorRepository;
import com.quemistry.user_ms.repository.UserRepository;
import com.quemistry.user_ms.repository.entity.Class;
import com.quemistry.user_ms.repository.entity.ClassInvitation;
import com.quemistry.user_ms.repository.entity.Student;
import com.quemistry.user_ms.repository.entity.StudentClass;
import com.quemistry.user_ms.repository.entity.User;
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
        userEntity.setCreationAndModifiedDetails(now, student.getUserId());
        this.userRepository.save(userEntity);

        var studentEntity = new Student();
        studentEntity.setUserEntity(userEntity);
        studentEntity.setEducationLevel(student.getEducationLevel());
        studentEntity.setCreationAndModifiedDetails(now, student.getUserId());
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

        String message;
        if (tutorOptional.isEmpty()) {
            message = "tutor id (%s) not found".formatted(tutorAccountId);
            log.error("tutor id (%s) not found".formatted(tutorAccountId));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,message );
//            return false;
        }
        Optional<Class> classOptional = classRepository.findByCode(input.classCode());

        if (classOptional.isEmpty()) {
            message = "class code (%s) not found".formatted(input.classCode());
            log.error("class code (%s) not found".formatted(input.classCode()));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,message );
//            return false;
        }


        var tutor = tutorOptional.get();
        var classEntity = classOptional.get();


        var classInvitation = new ClassInvitation();
        classInvitation.setClassEntity(classEntity);
        classInvitation.setUserEmail(input.studentEmail());
        classInvitation.setStatus(ClassInvitationStatus.INVITED);
        classInvitation.setUserType(UserConstant.USER_TYPE_STUDENT);
        classInvitation.setCode(StringHelper.getRandomString(10));
        this.classInvitationRepository.save(classInvitation);

        String encryptedClassCode = URLEncoder.encode(this.cryptoService.encrypt(classInvitation.getCode()), StandardCharsets.UTF_8);

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


        log.info("Send invitation ended");

        return canSend;
    }

    @Override
    public boolean acceptInvitation(String studentEmail, String accountId, String code) throws Exception {

        log.info("Accept invitation started");
        log.debug("studentEmail: {}, accountId: {}, code: {}", studentEmail, accountId, code);
        String decoded = URLDecoder.decode(code, StandardCharsets.UTF_8);

        log.debug("decoded: {}", studentEmail, accountId, code);
        String invitationCode = this.cryptoService.decrypt(decoded);
        log.debug("decrypted invitationCode: {} ", invitationCode);
        ClassInvitation classInvitation = this.classInvitationRepository.findByCode(invitationCode).orElseThrow();

        if (!classInvitation.getUserEmail().equals(studentEmail)) {
            String message = String.format("invited user (%s) and request user (%s) is not same",classInvitation.getUserEmail(), studentEmail);
            log.error(message);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
        }

        classInvitation.setStatus(ClassInvitationStatus.ACCEPTED);
        classInvitationRepository.save(classInvitation);

        Optional<User> userOptional = userRepository.findUserEntityByAccountId(accountId);
        Student student;
        if (userOptional.isEmpty()){
            log.info("user with accountId={} not found", accountId);
            student = new Student(accountId,studentEmail);
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
}
