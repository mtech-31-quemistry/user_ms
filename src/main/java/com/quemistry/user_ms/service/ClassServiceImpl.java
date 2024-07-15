package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.response.ClassResponseDto;
import com.quemistry.user_ms.repository.ClassRepository;
import com.quemistry.user_ms.repository.entity.Class;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;

    public ClassServiceImpl(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    @Override
    public ClassResponseDto saveClass(ClassDto input) {
        log.info("save class started");
        log.info("save class -> user id: {}", input.getUserId());

        var classResponseDto = new ClassResponseDto();

        var classEntity = new Class();
        var now = OffsetDateTime.now();
        classEntity.setName(input.getName());
        classEntity.setDescription(input.getDescription());
        classEntity.setEducationLevel(input.getEducationLevel());
        classEntity.setSubject(input.getSubject());
        classEntity.setCreationAndModificationDetails(now, input.getUserId());
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
        existingClass.setName(input.getName());
        existingClass.setDescription(input.getDescription());
        existingClass.setEducationLevel(input.getEducationLevel());
        existingClass.setSubject(input.getSubject());
        existingClass.setModified(input.getUserId());
        this.classRepository.save(existingClass);

        classResponseDto.setSuccess(true);

        log.info("update class ended");

        return classResponseDto;
    }
}
