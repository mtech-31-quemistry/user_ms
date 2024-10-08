package com.quemistry.user_ms.service.impl;

import com.quemistry.user_ms.mapper.ClassInvitationMapper;
import com.quemistry.user_ms.mapper.ClassMapper;
import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.ClassInvitationDto;
import com.quemistry.user_ms.model.RemoveStudentRequest;
import com.quemistry.user_ms.model.SaveClassRequest;
import com.quemistry.user_ms.model.response.ClassResponseDto;
import com.quemistry.user_ms.repository.ClassInvitationRepository;
import com.quemistry.user_ms.repository.ClassRepository;
import com.quemistry.user_ms.repository.StudentRepository;
import com.quemistry.user_ms.repository.TutorRepository;
import com.quemistry.user_ms.repository.entity.Class;
import com.quemistry.user_ms.repository.entity.ClassInvitation;
import com.quemistry.user_ms.repository.entity.Student;
import com.quemistry.user_ms.repository.entity.Tutor;
import com.quemistry.user_ms.repository.entity.User;
import com.quemistry.user_ms.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final ClassInvitationRepository classInvitationRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;

    public ClassServiceImpl(ClassRepository classRepository, ClassInvitationRepository classInvitationRepository, TutorRepository tutorRepository, StudentRepository studentRepository) {
        this.classRepository = classRepository;
        this.classInvitationRepository = classInvitationRepository;
        this.tutorRepository = tutorRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public ClassResponseDto saveClass(SaveClassRequest request) {
        log.info("save class started");
        log.info("save class -> user id: {}", request.getUserId());
        HashSet<Tutor> tutors = new HashSet<>();
        Optional<Tutor> tutorOptional = tutorRepository.findTutorByUserEntityAccountId(request.getUserId());

        if (tutorOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "tutor not found");
        }
        tutors.add(tutorOptional.get());
        tutors.addAll(getTutorsByEmails(request.getTutorEmails()));
        var classResponseDto = new ClassResponseDto();

        var classEntity = new Class();
        var now = OffsetDateTime.now();
        classEntity.setStatus("active");
        classEntity.setDescription(request.getDescription());
        classEntity.setEducationLevel(request.getEducationLevel());
        classEntity.setSubject(request.getSubject());
        classEntity.setCreationAndModifiedDetails(now, request.getUserId());
        classEntity.setTutors(tutors.stream().toList());
        classEntity.setStartDate(request.getStartDate());
        classEntity.setEndDate(request.getEndDate());
        this.classRepository.save(classEntity);

        classResponseDto.setSuccess(true);

        log.info("save class ended");

        return classResponseDto;
    }

    @Override
    public ClassDto updateClass(ClassDto input) {
        log.info("update class started");
        log.info("update class -> user id: {} and class id: {}", input.getUserId(), input.getId());

        var classOptional = this.classRepository.findById(input.getId());

        if (classOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("class with id=%s not found", input.getId()));

        Class existingClass = classOptional.get();
        existingClass.setDescription(input.getDescription());
        existingClass.setEducationLevel(input.getEducationLevel());
        existingClass.setSubject(input.getSubject());
        existingClass.setModified(input.getUserId());
        existingClass.setStatus(input.getStatus());
        existingClass.setStartDate(input.getStartDate());
        existingClass.setEndDate(input.getEndDate());
        if (!input.getTutorEmails().isEmpty()) {
            HashSet<Tutor> tutors = new HashSet<>(getTutorsByEmails(input.getTutorEmails()));
            existingClass.getTutors().clear();
            existingClass.getTutors().addAll(tutors);
        }
        Class clazz = classRepository.save(existingClass);
        return ClassMapper.INSTANCE.classToClassDto(clazz);
    }

    @Override
    public List<ClassDto> getAllClasses(String userAccountId) {
        return ClassMapper.INSTANCE.classesToClassesDto(this.classRepository.findAllByTutorId(userAccountId));
    }

    @Override
    public ClassDto getClassWithInvitations(Long classId, String tutorAccountId) {
        Class clazz = getClassByClassIdAndTutorAccountId(classId, tutorAccountId);
        ClassDto classDto = ClassMapper.INSTANCE.classToClassDto(clazz);
        classDto.setTutorEmails(clazz.getTutors().stream().map(Tutor::getUserEntity).map(User::getEmail).collect(Collectors.toList()));
        return classDto;
    }

    @Override
    public ClassDto removeStudentFromClass(Long classId, Long studentId, String tutorAccountId) {
        Class clazz = getClassByClassIdAndTutorAccountId(classId, tutorAccountId);
        Student student = clazz.getStudents().stream()
                .filter( s -> s.getId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> {
                    String message = String.format("student not found for studentId=%s and classId=%s", studentId, classId);
                    log.error(message);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                });
        clazz.getStudents().remove(student);
        student.getClasses().remove(clazz);
        classInvitationRepository.deleteByUserEmailAndClassEntityId(student.getUserEntity().getEmail(), classId);
        studentRepository.save(student);
        classRepository.save(clazz);
        return ClassMapper.INSTANCE.classToClassDto(clazz);
    }

    @Override
    public ClassDto removeStudents(String tutorAccountId, RemoveStudentRequest removeStudentRequest) {
        List<String> emails = removeStudentRequest.getEmails();
        Class clazz = getClassByClassIdAndTutorAccountId(removeStudentRequest.getClassId(), tutorAccountId);
        int count = classInvitationRepository.deleteByUserEmailInAndClassEntityId(emails, removeStudentRequest.getClassId());
        log.info("remove students -> deleted invitation count: {}", count);
        List<Student> students = clazz.getStudents().stream()
                .filter(s -> emails.contains(s.getUserEntity().getEmail()))
                .collect(Collectors.toList());

        if (!students.isEmpty()){
            clazz.getStudents().removeAll(students);
            students.forEach(s -> s.getClasses().remove(clazz));
            studentRepository.saveAll(students);
            classRepository.save(clazz);
        }
        return ClassMapper.INSTANCE.classToClassDto(clazz);
    }


    private Class getClassByClassIdAndTutorAccountId(Long classId, String tutorAccountId) {
        Optional<Class> optionalClass = classRepository.findByClassIdAndTutorAccountId(classId, tutorAccountId);
        if (!optionalClass.isPresent()) {
            String message = String.format("class not found for classId=%s and tutorAccountId=%s", classId, tutorAccountId);
            log.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        return optionalClass.get();
    }


    public List<Tutor> getTutorsByEmails(List<String> emails) {
        List<Tutor> tutors = new ArrayList<>();
        for (String email : emails) {
            Optional<Tutor> tutorOptional = tutorRepository.findTutorByUserEntityEmail(email);
            if (tutorOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("tutor with email=%s not found", email));
            }
            tutors.add(tutorOptional.get());
        }
        return tutors;
    }

}
