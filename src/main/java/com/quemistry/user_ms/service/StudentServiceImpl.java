package com.quemistry.user_ms.service;

import com.quemistry.user_ms.constant.UserConstant;
import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;
import com.quemistry.user_ms.repository.StudentRepository;
import com.quemistry.user_ms.repository.UserRepository;
import com.quemistry.user_ms.repository.entity.Student;
import com.quemistry.user_ms.repository.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseDto<StudentResponseDto> updateStudentProfile(StudentDto student) {
        log.info("update student profile started");
        log.info("update student profile -> user id: {}", student.getUserId());

        var responseDto = new ResponseDto<StudentResponseDto>();
        var studentResponseDto = new StudentResponseDto();

        var userOptional = this.userRepository.findUserEntityByAccountId(student.getUserId());

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            Student existingStudent = this.studentRepository.findStudentEntityByUserEntityId(existingUser.getId());

            existingUser.setFirstName(student.getFirstName());
            existingUser.setLastName(student.getLastName());
            existingUser.setModifiedBy(student.getUserId());
            existingUser.setModifiedOn(OffsetDateTime.now());
            this.userRepository.save(existingUser);

            existingStudent.setEducationLevel(student.getEducationLevel());
            existingStudent.setModifiedBy(student.getUserId());
            existingStudent.setModifiedOn(OffsetDateTime.now());
            this.studentRepository.save(existingStudent);

            studentResponseDto.setSuccess(true);

            responseDto.setStatusCode(UserConstant.STATUS_CODE_SUCCESS);
            responseDto.setStatusMessage("Your profile has been updated");
            responseDto.setPayload(studentResponseDto);

            log.info("update student profile finished");

            return responseDto;
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
        responseDto.setStatusCode(UserConstant.STATUS_CODE_SUCCESS);
        responseDto.setStatusMessage("Your profile has been created");
        responseDto.setPayload(studentResponseDto);

        log.info("create student profile finished");
        return responseDto;
    }
}
