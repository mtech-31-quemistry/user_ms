package com.quemistry.user_ms.mapper;


import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.repository.entity.Class;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {UserMapper.class} )
public interface ClassMapper {
    ClassMapper INSTANCE = Mappers.getMapper(ClassMapper.class);

    List<ClassDto> classesToClassesDto(List<Class> classes);

    @Mapping(target = "userId", ignore = true)
    ClassDto classToClassDto(Class clazz);
}
