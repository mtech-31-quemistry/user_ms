package com.quemistry.user_ms.service.impl;

import com.quemistry.user_ms.mapper.ClassInvitationMapper;
import com.quemistry.user_ms.mapper.ClassesMapper;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClassServiceImpl implements ClassService {

    private static final ClassesMapper CLASSES_MAPPER = ClassesMapper.INSTANCE;
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
        List<Tutor> tutors = new ArrayList<>();
        Optional<Tutor> tutorOptional = tutorRepository.findTutorByUserEntityAccountId(request.getUserId());

        if (!tutorOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "tutor not found" );
        }
        tutors.add(tutorOptional.get());
//        request.getTutorAccountIds().forEach(
//                i -> {
//                    Optional<Tutor> tutorOptional = tutorRepository.findTutorByUserEntityAccountId(i);
//                    if (!tutorOptional.isPresent()){
//                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "tutor not found" );
//                    }
//                    tutors.add(tutorOptional.get());
//
//                }
//        );

        var classResponseDto = new ClassResponseDto();

        var classEntity = new Class();
        var now = OffsetDateTime.now();
        classEntity.setCode(request.getCode());
        classEntity.setDescription(request.getDescription());
        classEntity.setEducationLevel(request.getEducationLevel());
        classEntity.setSubject(request.getSubject());
        classEntity.setCreationAndModifiedDetails(now, request.getUserId());
        classEntity.setTutors(tutors);
        this.classRepository.save(classEntity);

        classResponseDto.setSuccess(true);

        log.info("save class ended");

        return classResponseDto;
    }

    @Override
    public ClassResponseDto updateClass(ClassDto input) {
        log.info("update class started");
        log.info("update class -> user id: {}", input.getUserId());

        var classOptional = this.classRepository.findById(input.getId());

        if (classOptional.isEmpty())
            return null;

        var classResponseDto = new ClassResponseDto();
        var existingClass = classOptional.get();
        existingClass.setCode(input.getCode());
        existingClass.setDescription(input.getDescription());
        existingClass.setEducationLevel(input.getEducationLevel());
        existingClass.setSubject(input.getSubject());
        existingClass.setModified(input.getUserId());
        this.classRepository.save(existingClass);

        classResponseDto.setSuccess(true);

        log.info("update class ended");

        return classResponseDto;
    }

    @Override
    public List<ClassDto> getAllClasses() {
        return CLASSES_MAPPER.classesToClassesDto(this.classRepository.findAll());
    }

    @Override
    public ClassDto getClassWithInvitations(Long classId, String tutorAccountId) {
//        Optional<Class> classOptional = classRepository.findById(classId);
        Optional<Class> classOptional = classRepository.findByClassIdAndTutorAccountId(classId, tutorAccountId);
        if (!classOptional.isPresent()){
            log.info("class not found for classId={}", classId);
            return null;
        }
        Class clazz = classOptional.get();
        ClassDto classDto = CLASSES_MAPPER.classToClassDto(clazz);
        List<ClassInvitation> classInvitations = classInvitationRepository.findByClassId(classId);
        List<ClassInvitationDto> classInvitationDtos = ClassInvitationMapper.INSTANCE.classInvitationsToClassInvitationDto(classInvitations);
        classDto.setClassInvitations(classInvitationDtos);
        classDto.setTutorEmails(clazz.getTutors().stream().map(Tutor::getUserEntity).map(User::getEmail).collect(Collectors.toList()));
        return classDto;
    }
}
