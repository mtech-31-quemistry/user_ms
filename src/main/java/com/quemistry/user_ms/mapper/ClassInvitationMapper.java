package com.quemistry.user_ms.mapper;


import com.quemistry.user_ms.model.ClassInvitationDto;
import com.quemistry.user_ms.repository.entity.ClassInvitation;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {ClassMapper.class})
public interface ClassInvitationMapper {
    ClassInvitationMapper INSTANCE = Mappers.getMapper(ClassInvitationMapper.class);

    List<ClassInvitationDto> classInvitationsToClassInvitationDto(List<ClassInvitation> classInvitations);
}
