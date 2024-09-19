package com.quemistry.user_ms.service.impl;

import com.quemistry.user_ms.mapper.ClassInvitationMapper;
import com.quemistry.user_ms.mapper.ClassMapper;
import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.ClassInvitationDto;
import com.quemistry.user_ms.model.SaveClassRequest;
import com.quemistry.user_ms.model.response.ClassResponseDto;
import com.quemistry.user_ms.repository.ClassInvitationRepository;
import com.quemistry.user_ms.repository.ClassRepository;
import com.quemistry.user_ms.repository.TutorRepository;
import com.quemistry.user_ms.repository.entity.Class;
import com.quemistry.user_ms.repository.entity.ClassInvitation;
import com.quemistry.user_ms.repository.entity.Tutor;
import com.quemistry.user_ms.repository.entity.User;
import com.quemistry.user_ms.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    public ClassServiceImpl(ClassRepository classRepository, ClassInvitationRepository classInvitationRepository, TutorRepository tutorRepository) {
        this.classRepository = classRepository;
        this.classInvitationRepository = classInvitationRepository;
        this.tutorRepository = tutorRepository;
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
        classEntity.setCode(request.getCode());
        classEntity.setDescription(request.getDescription());
        classEntity.setEducationLevel(request.getEducationLevel());
        classEntity.setSubject(request.getSubject());
        classEntity.setCreationAndModifiedDetails(now, request.getUserId());
        classEntity.setTutors(tutors.stream().toList());
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
        existingClass.setCode(input.getCode());
        existingClass.setDescription(input.getDescription());
        existingClass.setEducationLevel(input.getEducationLevel());
        existingClass.setSubject(input.getSubject());
        existingClass.setModified(input.getUserId());
        existingClass.setStatus(input.getStatus());
        if(input.getTutorEmails().isEmpty()) {
            HashSet<Tutor> tutors = new HashSet<>();
            tutors.addAll(getTutorsByEmails(input.getTutorEmails()));
            existingClass.getTutors().clear();
            existingClass.getTutors().addAll(tutors);
        }
        Class clazz = classRepository.save(existingClass);
        ClassDto classDto = ClassMapper.INSTANCE.classToClassDto(clazz);
        return classDto;
    }

    @Override
    public List<ClassDto> getAllClasses(String userAccountId) {
        return  ClassMapper.INSTANCE.classesToClassesDto(this.classRepository.findAllByTutorId(userAccountId));
    }

    @Override
    public ClassDto getClassWithInvitations(Long classId, String tutorAccountId) {
//        Optional<Class> classOptional = classRepository.findById(classId);
        Optional<Class> classOptional = classRepository.findByClassIdAndTutorAccountId(classId, tutorAccountId);
        if (!classOptional.isPresent()) {
            String message = String.format("class not found for classId=%s and tutorAccountId=%s", classId, tutorAccountId);
            log.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        Class clazz = classOptional.get();
        ClassDto classDto =  ClassMapper.INSTANCE.classToClassDto(clazz);
        List<ClassInvitation> classInvitations = classInvitationRepository.findByClassId(classId);
        List<ClassInvitationDto> classInvitationDtos = ClassInvitationMapper.INSTANCE.classInvitationsToClassInvitationDto(classInvitations);
        classDto.setClassInvitations(classInvitationDtos);
        classDto.setTutorEmails(clazz.getTutors().stream().map(Tutor::getUserEntity).map(User::getEmail).collect(Collectors.toList()));
        return classDto;
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
