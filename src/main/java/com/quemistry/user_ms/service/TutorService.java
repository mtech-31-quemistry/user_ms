package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.TutorDto;


public interface TutorService {

    TutorDto saveProfile(TutorDto tutorDto);

    TutorDto getProfile(String email);
}
