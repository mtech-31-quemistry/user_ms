package com.quemistry.user_ms.mapper;


import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.repository.entity.Class;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClassesMapper {
    ClassesMapper INSTANCE = Mappers.getMapper(ClassesMapper.class);

    List<ClassDto> classesToClassesDto(List<Class> classes);

    ClassDto classToClassDto(Class clazz);
}
