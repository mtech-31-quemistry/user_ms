package com.quemistry.user_ms.mapper;


import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.TutorDto;
import com.quemistry.user_ms.repository.entity.Student;
import com.quemistry.user_ms.repository.entity.Tutor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "userEntity.firstName", target = "firstName")
    @Mapping(source = "userEntity.lastName", target = "lastName")
    @Mapping(source = "userEntity.email", target = "email")
    @Mapping(source = "userEntity.accountId", target = "accountId")
    TutorDto tutorToTutorDto(Tutor tutor);

    @Mapping(source = "userEntity.firstName", target = "firstName")
    @Mapping(source = "userEntity.lastName", target = "lastName")
    @Mapping(source = "userEntity.email", target = "email")
    @Mapping(source = "userEntity.accountId", target = "accountId")
    StudentDto studentToStudentDto(Student student);

    List<StudentDto> studentsToStudentDtos(List<Student> students);
}
