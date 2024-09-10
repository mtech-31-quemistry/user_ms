package com.quemistry.user_ms.service.impl;

import com.quemistry.user_ms.mapper.UserMapper;
import com.quemistry.user_ms.model.TutorDto;
import com.quemistry.user_ms.repository.TutorRepository;
import com.quemistry.user_ms.repository.UserRepository;
import com.quemistry.user_ms.repository.entity.Tutor;
import com.quemistry.user_ms.repository.entity.User;
import com.quemistry.user_ms.service.TutorService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class TutorServiceImpl implements TutorService {

    private final UserRepository userRepository;

    private final TutorRepository tutorRepository;

    public TutorServiceImpl(
            UserRepository userRepository,
            TutorRepository tutorRepository) {
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
    }

    @Override
    public TutorDto saveProfile(TutorDto tutorDto) {
        log.info("save profile -> tutorDto: {}", tutorDto);

        User user = new User();
        user.setAccountId(tutorDto.getAccountId());
        user.setEmail(tutorDto.getEmail());
        user.setFirstName(tutorDto.getFirstName());
        user.setLastName(tutorDto.getLastName());
        user.setCreationAndModifiedDetails(OffsetDateTime.now(), tutorDto.getAccountId());
        user = userRepository.saveAndFlush(user);
        log.info("saved user: {}", user);
        Tutor tutor = new Tutor();
        tutor.setUserEntity(user);
        tutor.setEducationLevel(tutorDto.getEducationLevel());
        tutor.setTuitionCentre(tutorDto.getTuitionCentre());
        tutor.setCreationAndModifiedDetails(OffsetDateTime.now(), tutorDto.getAccountId());
        tutor = tutorRepository.saveAndFlush(tutor);
        log.info("saved tutor: {}", tutor);
        return UserMapper.INSTANCE.tutorToTutorDto(tutor);
    }

    @Override
    public TutorDto getProfile(String email) {
        Optional<Tutor> optionalTutor = tutorRepository.findTutorByUserEntityEmail(email);

        if (optionalTutor.isEmpty()) {
            String message = String.format("tutor not found for email=%s",email);
            log.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        return UserMapper.INSTANCE.tutorToTutorDto(optionalTutor.get());
    }
}
